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
 * Time: 15:58
 * 类的功能、说明写在此处.
 */
public class SettingEmailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView back;
    private EditText email;
    private String emailText;
    private TextView set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setemail);
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        email = (EditText) this.findViewById(R.id.email);
        set = (TextView) this.findViewById(R.id.set);
        set.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.set:
                //设置邮箱事件
                emailText = email.getText().toString();//邮箱
                break;
        }
    }
}
