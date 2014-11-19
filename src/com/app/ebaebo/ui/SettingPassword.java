package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ebaebo.R;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/18
 * Time: 22:25
 * 类的功能、说明写在此处.
 */
public class SettingPassword extends BaseActivity implements View.OnClickListener {
    private ImageView back;//返回
    private EditText password;
    private EditText newpass;
    private EditText surepass;
    private TextView set;//设置

    private String pass ;
    private String newpassword;
    private String surepassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingpass);
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        set = (TextView) this.findViewById(R.id.set);
        set.setOnClickListener(this);
        password = (EditText) this.findViewById(R.id.password);
        newpass = (EditText) this.findViewById(R.id.newpass);
        surepass = (EditText) this.findViewById(R.id.sure);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.set:
                //设置密码
                break;
        }
    }
}
