package com.app.ebaebo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.app.ebaebo.R;

/**
 * Created by liuzwei on 2014/11/13.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    TextView account;//账号
    LinearLayout setPassword;//修改密码
    LinearLayout myPhone;//我的手机号
    LinearLayout babySet;//宝宝设置
    LinearLayout mumSet;//妈妈设置
    LinearLayout boundEmail;//绑定邮箱
    LinearLayout about;//
    ImageView back;//返回
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initView();
    }

    private void initView(){
        account = (TextView) findViewById(R.id.setting_account);
        setPassword = (LinearLayout) findViewById(R.id.setting_password);
        myPhone = (LinearLayout) findViewById(R.id.setting_phonenumber);
        babySet = (LinearLayout) findViewById(R.id.setting_baobao);
        mumSet = (LinearLayout) findViewById(R.id.setting_mama);
        boundEmail = (LinearLayout) findViewById(R.id.setting_bound_email);
        about = (LinearLayout) findViewById(R.id.setting_about);
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
                Intent babySet = new Intent(SettingActivity.this, BabySettingActivity.class);
                startActivity(babySet);
                break;
            case R.id.setting_mama://妈妈设置
                Intent mumSet = new Intent(SettingActivity.this, MumSettingActivity.class);
                startActivity(mumSet);
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
