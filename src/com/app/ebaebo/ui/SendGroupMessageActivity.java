package com.app.ebaebo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ebaebo.R;
import com.app.ebaebo.data.ErrorDATA;
import com.app.ebaebo.data.TongxunluDATA;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Message;
import com.app.ebaebo.entity.Tongxunlu;
import com.app.ebaebo.util.CommonUtil;
import com.app.ebaebo.util.InternetURL;
import com.app.ebaebo.util.StringUtil;
import com.google.gson.Gson;

import java.util.*;

/**
 * Created by liuzwei on 2014/12/13.
 */
public class SendGroupMessageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView send;//发送
    private RadioGroup radioGroup;
    private EditText content;
    private Map<String, String> uidMap = new HashMap<String, String>();
    private String identity;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_group_message_layout);

        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);

        initView();
        getData();
    }

    private void initView(){
        back = (ImageView) this.findViewById(R.id.send_group_message_back);
        send = (TextView) this.findViewById(R.id.send_group_message_send);
        radioGroup = (RadioGroup) this.findViewById(R.id.send_group_message_radio);
        content = (EditText) this.findViewById(R.id.send_group_message_content);

        back.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    private void getData(){
        String uri = String.format(InternetURL.GET_TONGXUNLU_URL+"?school_id=%s&uid=%s&class_id=%s","1", "73", "1" );
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        if (CommonUtil.isJson(s)){
                            TongxunluDATA data = gson.fromJson(s, TongxunluDATA.class);
                            final List<Tongxunlu> list = data.getData();
                            for (int i=0; i<list.size(); i++){
                                CheckBox checkBox = new CheckBox(mContext);
                                checkBox.setText(list.get(i).getName());
                                final int finalI = i;
                                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked){
                                            uidMap.put(list.get(finalI).getUid(), list.get(finalI).getUid());
                                        }else {
                                            uidMap.remove(list.get(finalI).getUid());
                                        }
                                    }
                                });
                                radioGroup.addView(checkBox,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            }
                        }else {
                            Toast.makeText(mContext, "数据错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        getRequestQueue().add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_group_message_back://返回

                break;
            case R.id.send_group_message_send://发送
                if (uidMap.size() == 0){
                    Toast.makeText(mContext, "请选择接受人 ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isNullOrEmpty(content.getText().toString())){
                    Toast.makeText(mContext, "请输入信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                String to_uids = "";
                Iterator iterator = uidMap.keySet().iterator();
                while (iterator.hasNext()){
                    to_uids += iterator.next() + ",";
                }
                Log.e("SendGroup", to_uids);
                Message message = new Message();
                message.setUid(account.getUid());
                message.setTo_uids(to_uids);
                message.setContent(content.getText().toString());
                sendMsg(message);
                break;
        }
    }

    /**
     * 发送消息
     */
    private void sendMsg(final Message message){
        String uri = String.format(InternetURL.MESSAGE_SEND_URL + "?content=%s&uid=%s&type=0&to_uids=%s&file=%s&user_type=%s",message.getContent(), message.getUid(),message.getTo_uids(),message.getUrl(), identity);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)) {
                            ErrorDATA data = getGson().fromJson(s, ErrorDATA.class);
                            if (data.getCode() == 200) {
                                Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
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
