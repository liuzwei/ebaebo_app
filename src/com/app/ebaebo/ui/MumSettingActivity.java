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
    private EditText name;
    private EditText guanxi;
    private TextView set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mum_setting);
        initView();
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        name = (EditText) this.findViewById(R.id.name);
        guanxi = (EditText) this.findViewById(R.id.guanxi);
        set = (TextView) this.findViewById(R.id.set);
        set.setOnClickListener(this);
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
        }
    }
}
