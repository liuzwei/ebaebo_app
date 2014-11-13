package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.app.ebaebo.R;

/**
 * Created by liuzwei on 2014/11/13.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    TextView account;//账号
    TableRow setPassword;//修改密码
    TableRow myPhone;//我的手机号
    TableRow babySet;//宝宝设置
    TableRow mumSet;//妈妈设置
    TableRow boundEmail;//绑定邮箱
    TableRow about;//
    ImageView back;//返回
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        initView();
    }

    private void initView(){
        account = (TextView) findViewById(R.id.setting_account);
        setPassword = (TableRow) findViewById(R.id.setting_password);
        myPhone = (TableRow) findViewById(R.id.setting_phonenumber);
        babySet = (TableRow) findViewById(R.id.setting_baobao);
        mumSet = (TableRow) findViewById(R.id.setting_mama);
        boundEmail = (TableRow) findViewById(R.id.setting_bound_email);
        about = (TableRow) findViewById(R.id.setting_about);
        back = (ImageView) findViewById(R.id.setting_back);

        setPassword.setOnClickListener(this);
        myPhone.setOnClickListener(this);
        babySet.setOnClickListener(this);
        mumSet.setOnClickListener(this);
        boundEmail.setOnClickListener(this);
        about.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_password://设置密码

                break;
            case R.id.setting_phonenumber://我的手机号

                break;
            case R.id.setting_baobao://宝宝设置

                break;
            case R.id.setting_mama://妈妈设置

                break;
            case R.id.setting_bound_email://绑定邮箱

                break;
            case R.id.setting_about://关于

                break;
            case R.id.setting_back://返回按钮
                finish();
                break;
        }
    }
}
