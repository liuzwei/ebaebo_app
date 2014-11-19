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
public class BabySettingActivity extends BaseActivity implements View.OnClickListener{
    ImageView back;//返回按钮
    private ImageView tx;//头像
    private EditText nameText;//name
    private EditText guanxiText;//关系
    private TextView set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baobao_setting);
        initView();
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        set = (TextView) this.findViewById(R.id.set);
        set.setOnClickListener(this);
        tx = (ImageView) this.findViewById(R.id.tx);
        tx.setOnClickListener(this);
        nameText = (EditText) this.findViewById(R.id.name);
        guanxiText = (EditText) this.findViewById(R.id.guanxi);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.set:
                //设置
                break;
            case R.id.tx:
                //头像设置
                break;
        }
    }
}
