package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.app.ebaebo.R;

/**
 * Created by liuzwei on 2014/11/14.
 */
public class BabySettingActivity extends BaseActivity implements View.OnClickListener{
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_setting);
        initView();
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.baby_setting_back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.baby_setting_back:
                finish();
                break;
        }
    }
}
