package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.app.ebaebo.R;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/14
 * Time: 8:13
 * 类的功能、说明写在此处.
 */
public class YuyingMessageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView yuyingback;//返回按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuyingmessages);
        initView();
    }

    private void initView() {
        yuyingback = (ImageView) this.findViewById(R.id.yuyingback);
        yuyingback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.yuyingback:
                finish();
                break;
        }
    }
}
