package com.app.ebaebo.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.app.ebaebo.R;
import com.app.ebaebo.util.HttpUtils;


/**
 * author: liuzwei
 * Date: 2014/8/19
 * Time: 15:39
 * 类的功能、说明写在此处.
 */
public class EmailDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private EditText mobile;
    private Button sure;
    private Button cancle;
    private String mobileNum;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

            }
            super.handleMessage(msg);
        }
    };

    public EmailDialog(Context context, int them) {
        super(context,them);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_dialog);
        initView();
    }

    private void initView(){
        mobile = (EditText) findViewById(R.id.mobile);
        sure = (Button) this.findViewById(R.id.sure);
        sure.setOnClickListener(this);
        cancle = (Button) this.findViewById(R.id.cancle);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean isMobileNet, isWifiNet;
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(context);
            isWifiNet = HttpUtils.isWifiDataEnable(context);
            if (!isMobileNet && !isWifiNet){
                Toast.makeText(context, "当前网络连接不可用", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("网络连接不可用", e.getMessage());
        }
        switch (v.getId()){
            case R.id.sure:
                mobileNum = mobile.getText().toString();//手机号
                break;
            case R.id.cancle:
                EmailDialog.this.dismiss();
                break;
        }
    }

}