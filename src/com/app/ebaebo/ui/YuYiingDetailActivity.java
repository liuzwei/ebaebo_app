package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ebaebo.R;
import com.app.ebaebo.entity.Yuying;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/18
 * Time: 14:25
 * 类的功能、说明写在此处.
 */
public class YuYiingDetailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView yyback;
    private TextView cont;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuyingdetail);
        Yuying yy =(Yuying) getIntent().getExtras().get("yy");
        initView();
        cont.setText(yy.getContent());
        title.setText(yy.getTitle());
    }

    private void initView() {
        yyback = (ImageView) this.findViewById(R.id.yydetailback);
        yyback.setOnClickListener(this);
        cont = (TextView) this.findViewById(R.id.content);
        title = (TextView) this.findViewById(R.id.title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.yydetailback:
                finish();
                break;
        }
    }
}
