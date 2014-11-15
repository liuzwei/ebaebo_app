package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.app.ebaebo.R;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/14
 * Time: 23:01
 * 类的功能、说明写在此处.
 */
public class DianmingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView dianmingback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dianming);
        initView();
    }

    private void initView() {
        dianmingback = (ImageView) this.findViewById(R.id.dianmingback);
        dianmingback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dianmingback:
                finish();
                break;
        }
    }
}
