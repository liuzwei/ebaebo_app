package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.app.ebaebo.R;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/14
 * Time: 8:53
 * 类的功能、说明写在此处.
 */
public class JiaohuActivity extends BaseActivity implements View.OnClickListener {
    private ImageView jiaohuback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaohu);
        initView();
    }

    private void initView() {
        jiaohuback = (ImageView) this.findViewById(R.id.jiaohuback);
        jiaohuback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.jiaohuback:
                finish();
                break;
        }
    }
}
