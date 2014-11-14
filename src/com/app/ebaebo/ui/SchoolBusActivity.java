package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.app.ebaebo.R;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/14
 * Time: 8:33
 * 类的功能、说明写在此处.
 */
public class SchoolBusActivity extends BaseActivity implements View.OnClickListener {
    private ImageView schoolbusback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoolbus);
        initView();
    }

    private void initView() {
        schoolbusback = (ImageView) this.findViewById(R.id.schoolbusback);
        schoolbusback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.schoolbusback:
                finish();
                break;
        }
    }
}
