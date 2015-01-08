package com.app.ebaebo.ui;

import android.app.DownloadManager;
import android.content.*;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.ChatAdapter;
import com.app.ebaebo.data.ErrorDATA;
import com.app.ebaebo.data.MessageDATA;
import com.app.ebaebo.data.UploadDATA;
import com.app.ebaebo.entity.*;
import com.app.ebaebo.service.MessageService;
import com.app.ebaebo.util.CommonUtil;
import com.app.ebaebo.util.DownloadUtil;
import com.app.ebaebo.util.InternetURL;
import com.app.ebaebo.util.StringUtil;
import com.app.ebaebo.widget.SoundMeter;

import java.io.File;
import java.util.*;

/**
 * Created by liuzwei on 2014/11/21.
 *
 * 双人聊天交互页面
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private MessageService messageService;
    private ImageView back;
    private ListView listView;
    private EditText sendMessage;
    private Button send;
    private TextView mBtnRcd;//按住说话按钮
    private RelativeLayout mBottom;
    private AccountMessage accountMessage;
    private TextView chatTitle;
    String identity;
    private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding, voice_rcd_hint_tooshort;
    private ImageView img1, sc_img1;
    private SoundMeter mSensor;
    private View rcChat_popup;
    private LinearLayout del_re;
    private ImageView chatting_mode_btn, volume;
    private LinearLayout layoutBottom;//底部引入的框
    private View faceView;//表情框

    private boolean btn_vocie = false;
    private boolean isShosrt = false;
    private String voiceName;
    private long startVoiceT, endVoiceT;
    private int flag = 1;
    private Account account;
    private ChatAdapter adapter;
    private Handler mHandler = new Handler();
    private List<Message> list = new ArrayList<Message>();
    private static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private MessageReceiver messageReceiver;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messageService = ((MessageService.MessageBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        accountMessage = (AccountMessage) getIntent().getSerializableExtra(Constants.ACCOUNT_MESSAGE);
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        initView();
        chatTitle.setText(String.format("与 %s 聊天中", accountMessage.getName()));

        getData();
        bindMessageService();
        messageReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.app.ebaebo.ui.RECEIVER");
        registerReceiver(messageReceiver, intentFilter);
//        adapter = new ChatAdapter(list, mContext,sp, )
    }

    private void bindMessageService(){
        Intent intent = new Intent();
        intent.setClass(ChatActivity.this, MessageService.class);
        ArrayList<String> data = new ArrayList<String>();
        data.add(account.getUid());
        data.add(accountMessage.getUid());
        data.add(identity);
        intent.putStringArrayListExtra("getData", data);
        ChatActivity.this.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        ChatActivity.this.startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i  = new Intent();
        i.setClass(ChatActivity.this, MessageService.class);
        ChatActivity.this.unbindService(serviceConnection);
        mContext.stopService(i);

        unregisterReceiver(messageReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_sendbtn://发送按钮
                if (!StringUtil.isNullOrEmpty(sendMessage.getText().toString())) {
                    Message message = new Message();
                    message.setUid(account.getUid());
                    message.setTo_uids(accountMessage.getUid());
                    message.setType("0");//文字消息
                    message.setContent(sendMessage.getText().toString());
                    sendMsg(message);
                }else {
                    sendMessage.setHint("请输入消息");
                }
                break;
            case R.id.chat_back://返回按钮
                finish();
                break;
        }
    }

    private void initView(){
        layoutBottom = (LinearLayout) this.findViewById(R.id.rl_bottom);

        mBtnRcd = (TextView) layoutBottom.findViewById(R.id.btn_rcd);
        mBottom = (RelativeLayout) layoutBottom.findViewById(R.id.btn_bottom);
        chatting_mode_btn = (ImageView) layoutBottom.findViewById(R.id.ivPopUp);
        faceView = layoutBottom.findViewById(R.id.ll_facechoose);
        volume = (ImageView) this.findViewById(R.id.volume);
        rcChat_popup = this.findViewById(R.id.rcChat_popup);
        img1 = (ImageView) this.findViewById(R.id.img1);
        sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
        del_re = (LinearLayout) this.findViewById(R.id.del_re);
        voice_rcd_hint_rcding = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_rcding);
        voice_rcd_hint_loading = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_loading);
        voice_rcd_hint_tooshort = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_tooshort);
        mSensor = new SoundMeter();

        back = (ImageView) this.findViewById(R.id.chat_back);
        listView = (ListView) this.findViewById(R.id.chat_listview);
        sendMessage = (EditText) layoutBottom.findViewById(R.id.chat_sendmessage);
        send = (Button) layoutBottom.findViewById(R.id.chat_sendbtn);
        chatTitle = (TextView) this.findViewById(R.id.chat_title);

        back.setOnClickListener(this);
        send.setOnClickListener(this);

        //语音文字切换按钮
        chatting_mode_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (btn_vocie) {
                    mBtnRcd.setVisibility(View.GONE);
                    mBottom.setVisibility(View.VISIBLE);

                    btn_vocie = false;
                    chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_msg_btn);

                } else {
                    mBtnRcd.setVisibility(View.VISIBLE);
                    mBottom.setVisibility(View.GONE);
                    chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_voice_btn);
                    btn_vocie = true;
                    if (faceView.getVisibility() == View.VISIBLE) {
                        faceView.setVisibility(View.GONE);
                    }
                }
            }
        });
        mBtnRcd.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                //按下语音录制按钮时返回false执行父类OnTouch
                return false;
            }
        });
    }

    private void getData(){
        String uri = String.format(InternetURL.MESSAGE_DETAIL_LIST+"?uid=%s&friend_id=%s&user_type=%s&new=1", account.getUid(), accountMessage.getUid(),identity);
//        String uri = String.format(InternetURL.MESSAGE_DETAIL_LIST+"?uid=%s&friend_id=%s&user_type=%s&new=0&pageSize=2", 91, 75,identity);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)){
                            MessageDATA data = getGson().fromJson(s, MessageDATA.class);
                            list.addAll(data.getData().getList());
                            adapter = new ChatAdapter(list, mContext,sp, data.getData().getUser());
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            //todo
                        }else {
                            Toast.makeText(mContext, "数据错误，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(mContext, "服务器异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        getRequestQueue().add(request);
    }

    /**
     * 发送消息
     */
    private void sendMsg(final Message message){
//        String uri = String.format(InternetURL.MESSAGE_SEND_URL + "?content=%s&uid=%s&type=0&to_uids=%s&url=%s&user_type=%s",message.getContent(), message.getUid(),message.getTo_uids(),message.getUrl(), identity);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MESSAGE_SEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)) {
                            ErrorDATA data = getGson().fromJson(s, ErrorDATA.class);
                            if (data.getCode() == 200) {
                                if ("0".equals(message.getType())) {
//                                    Message message = new Message(account.getUid(), accountMessage.getUid(), System.currentTimeMillis(), "0", sendMessage.getText().toString());
                                    message.setDateline(System.currentTimeMillis());
                                    list.add(message);
                                    listView.setSelection(list.size() - 1);
                                    adapter.notifyDataSetChanged();
                                    sendMessage.setText("");
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("ChatActivity", volleyError.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid", message.getUid());
                if (!StringUtil.isNullOrEmpty(message.getContent())) {
                    params.put("content", message.getContent());
                }
                params.put("type",message.getType());
                params.put("to_uids",message.getTo_uids());
                if (!StringUtil.isNullOrEmpty(message.getUrl())) {
                    params.put("url", message.getUrl());
                }
                params.put("user_type",identity);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    //按下语音录制按钮时
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!Environment.getExternalStorageDirectory().exists()) {
            Toast.makeText(this, "请检查内存卡", Toast.LENGTH_LONG).show();
            return false;
        }

        if (btn_vocie) {
            System.out.println("1");
            int[] location = new int[2];
            mBtnRcd.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
            int btn_rc_Y = location[1];
            int btn_rc_X = location[0];
            int[] del_location = new int[2];
            del_re.getLocationInWindow(del_location);
            int del_Y = del_location[1];
            int del_x = del_location[0];
            if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
                if (!Environment.getExternalStorageDirectory().exists()) {
                    Toast.makeText(this, "请检查内存卡", Toast.LENGTH_LONG).show();
                    return false;
                }
                System.out.println("2");
                if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {//判断手势按下的位置是否是语音录制按钮的范围内
                    System.out.println("3");
                    mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
                    rcChat_popup.setVisibility(View.VISIBLE);
                    voice_rcd_hint_loading.setVisibility(View.VISIBLE);
                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    voice_rcd_hint_tooshort.setVisibility(View.GONE);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (!isShosrt) {
                                voice_rcd_hint_loading.setVisibility(View.GONE);
                                voice_rcd_hint_rcding
                                        .setVisibility(View.VISIBLE);
                            }
                        }
                    }, 300);
                    img1.setVisibility(View.VISIBLE);
                    del_re.setVisibility(View.GONE);
                    startVoiceT = System.currentTimeMillis();
                    voiceName = PATH +"/ebaebo/"+startVoiceT + ".mp3";
                    start(voiceName);
                    flag = 2;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {//松开手势时执行录制完成
                System.out.println("4");
                mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
                if (event.getY() >= del_Y
                        && event.getY() <= del_Y + del_re.getHeight()
                        && event.getX() >= del_x
                        && event.getX() <= del_x + del_re.getWidth()) {
                    rcChat_popup.setVisibility(View.GONE);
                    img1.setVisibility(View.VISIBLE);
                    del_re.setVisibility(View.GONE);
                    stop();
                    flag = 1;
                    File file = new File(voiceName);
                    if (file.exists()) {
                        file.delete();
                    }
                } else {

                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    stop();
                    endVoiceT = System.currentTimeMillis();
                    flag = 1;
                    int time = (int) ((endVoiceT - startVoiceT) / 1000);
                    if (time < 1) {
                        isShosrt = true;
                        voice_rcd_hint_loading.setVisibility(View.GONE);
                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                voice_rcd_hint_tooshort
                                        .setVisibility(View.GONE);
                                rcChat_popup.setVisibility(View.GONE);
                                isShosrt = false;
                            }
                        }, 500);
                        return false;
                    }

                    final Message message = new Message(account.getUid(),
                            accountMessage.getUid(),
                            System.currentTimeMillis(),
                            "3",
                            voiceName);
                    message.setUrl(voiceName);

                    //上传文件
                    File file = new File(voiceName);
                    Map<String, File> files = new HashMap<String, File>();
                    files.put("file", file);
                    Map<String, String> params = new HashMap<String, String>();
                    addPutUploadFileRequest(
                            InternetURL.UPLOAD_FILE,
                            files,
                            params,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    if (CommonUtil.isJson(s)) {
                                        UploadDATA data = getGson().fromJson(s, UploadDATA.class);
                                        //上传文件成功后进行发送消息
                                        if (data.getCode() == 200){
                                            message.setUrl(data.getUrl());
                                            sendMsg(message);
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            },
                            null);
                    list.add(message);
                    listView.setSelection(list.size()-1);
                    adapter.notifyDataSetChanged();
                    rcChat_popup.setVisibility(View.GONE);

                }
            }
            if (event.getY() < btn_rc_Y) {//手势按下的位置不在语音录制按钮的范围内
                System.out.println("5");
                Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
                        R.anim.cancel_rc);
                Animation mBigAnimation = AnimationUtils.loadAnimation(this,
                        R.anim.cancel_rc2);
                img1.setVisibility(View.GONE);
                del_re.setVisibility(View.VISIBLE);
                del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
                if (event.getY() >= del_Y
                        && event.getY() <= del_Y + del_re.getHeight()
                        && event.getX() >= del_x
                        && event.getX() <= del_x + del_re.getWidth()) {
                    del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
                    sc_img1.startAnimation(mLitteAnimation);
                    sc_img1.startAnimation(mBigAnimation);
                }
            } else {

                img1.setVisibility(View.VISIBLE);
                del_re.setVisibility(View.GONE);
                del_re.setBackgroundResource(0);
            }
        }
        return super.onTouchEvent(event);
    }

    private static final int POLL_INTERVAL = 300;

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            stop();
        }
    };
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };

    private void start(String name) {
        mSensor.start(name);
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        volume.setImageResource(R.drawable.amp1);
    }

    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
            case 1:
                volume.setImageResource(R.drawable.amp1);
                break;
            case 2:
            case 3:
                volume.setImageResource(R.drawable.amp2);

                break;
            case 4:
            case 5:
                volume.setImageResource(R.drawable.amp3);
                break;
            case 6:
            case 7:
                volume.setImageResource(R.drawable.amp4);
                break;
            case 8:
            case 9:
                volume.setImageResource(R.drawable.amp5);
                break;
            case 10:
            case 11:
                volume.setImageResource(R.drawable.amp6);
                break;
            default:
                volume.setImageResource(R.drawable.amp7);
                break;
        }
    }

    public class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String messages = intent.getStringExtra("messages");
            MessageDATA messageDATA = getGson().fromJson(messages, MessageDATA.class);

            List<Message> listMessage = messageDATA.getData().getList();
            for (Message message : listMessage){
                if (!StringUtil.isNullOrEmpty(message.getUrl())){
                    new Thread( new DownloadUtil(message.getUrl())).start();
                    message.setUrl(DownloadUtil.getFilePath(message.getUrl()));
                    list.add(message);
                }else {
                    list.add(message);
                }
            }
//            list.addAll(messageDATA.getData().getList());
            adapter.notifyDataSetChanged();
        }
    }
}
