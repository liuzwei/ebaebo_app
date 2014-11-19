package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ebaebo.R;

/**
 * Created by liuzwei on 2014/11/14.
 */
public class MumSettingActivity extends BaseActivity implements View.OnClickListener{
    private ImageView back;//返回
    private ImageView tx;//头像
    private EditText zhanghao;//账号
    private EditText mobile;//手机
    private EditText password;//密码
    private TextView set;//设置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mm_setting);
        initView();
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        zhanghao = (EditText) this.findViewById(R.id.zhanghao);
        mobile = (EditText) this.findViewById(R.id.mobile);
        password = (EditText) this.findViewById(R.id.password);
        set = (TextView) this.findViewById(R.id.set);
        set.setOnClickListener(this);
        tx = (ImageView) this.findViewById(R.id.tx);
        tx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.set:
                //设置妈妈
                break;
            case R.id.tx:
                //设置头像
                break;
        }
    }
}
