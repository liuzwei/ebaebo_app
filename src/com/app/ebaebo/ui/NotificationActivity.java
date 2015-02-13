package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ebaebo.R;
import com.app.ebaebo.entity.NotifMessage;

public class NotificationActivity extends BaseActivity implements View.OnClickListener{
    private NotifMessage message;

    private ImageView notiyBack;//退出按钮
    private TextView notiyTitle;//标题
    private TextView notiyContent;//内容
    private TextView notiyTime;//时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        message = (NotifMessage) getIntent().getSerializableExtra(Constants.UMENG_MESSAGE);

        initView();
        initData();

    }

    private void initData(){
        notiyTitle.setText(message.getTitle());
        notiyContent.setText(message.getContent());
        notiyTime.setText(message.getTime());
    }

    private void initView(){
        notiyTitle = (TextView) findViewById(R.id.notification_title);
        notiyContent = (TextView) findViewById(R.id.notification_content);
        notiyBack = (ImageView) findViewById(R.id.notification_back);
        notiyTime = (TextView) findViewById(R.id.notification_time);
        notiyBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

    }
}
