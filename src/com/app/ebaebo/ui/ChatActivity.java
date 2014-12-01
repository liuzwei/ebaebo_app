package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.ChatAdapter;
import com.app.ebaebo.data.ErrorDATA;
import com.app.ebaebo.data.MessageDATA;
import com.app.ebaebo.entity.*;
import com.app.ebaebo.util.CommonUtil;
import com.app.ebaebo.util.InternetURL;
import com.app.ebaebo.util.StringUtil;
import com.app.ebaebo.util.ToastUtil;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuzwei on 2014/11/21.
 *
 * 双人聊天交互页面
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ListView listView;
    private EditText sendMessage;
    private Button send;
    private AccountMessage accountMessage;
    private TextView chatTitle;
    String identity;

    private Account account;
    private ChatAdapter adapter;
    private List<Message> list = new ArrayList<Message>();

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
//        adapter = new ChatAdapter(list, mContext,sp, )
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_sendbtn://发送按钮
                sendMsg();
                break;
            case R.id.chat_back://返回按钮
                finish();
                break;
        }
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.chat_back);
        listView = (ListView) findViewById(R.id.chat_listview);
        sendMessage = (EditText) findViewById(R.id.chat_sendmessage);
        send = (Button) findViewById(R.id.chat_sendbtn);
        chatTitle = (TextView) findViewById(R.id.chat_title);

        back.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    private void getData(){
        String uri = String.format(InternetURL.MESSAGE_DETAIL_LIST+"?uid=%s&friend_id=%s&user_type=%s&new=0", account.getUid(), accountMessage.getUid(),identity);
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
    private void sendMsg(){
        String cont = sendMessage.getText().toString();
        String uri = String.format(InternetURL.MESSAGE_SEND_URL + "?content=%s&uid=%s&type=0&to_uids=%s&user_type=%s",cont, account.getUid(),accountMessage.getUid(), identity);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)) {
                            ErrorDATA data = getGson().fromJson(s, ErrorDATA.class);
                            if (data.getCode() == 200) {
                                Message message = new Message(account.getUid(), accountMessage.getUid(), System.currentTimeMillis(), "0", sendMessage.getText().toString());
                                list.add(message);
                                listView.setSelection(list.size()-1);
                                adapter.notifyDataSetChanged();
                                sendMessage.setText("");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
        getRequestQueue().add(request);
    }
}
