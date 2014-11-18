package com.app.ebaebo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.ebaebo.EbaeboApplication;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.AnimateFirstDisplayListener;
import com.app.ebaebo.entity.Account;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by liuzwei on 2014/11/18.
 */
public class SelectIdentityActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout father;
    private LinearLayout mother;

    private ImageView fatherPhoto;
    private ImageView motherPhoto;
    private TextView fatherName;
    private TextView motherName;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_identity_layout);
        initView();
        Account account = (Account) getIntent().getSerializableExtra("account");
        if (account != null){
            imageLoader.displayImage(account.getF_cover(), fatherPhoto, EbaeboApplication.txOptions, animateFirstListener);
            imageLoader.displayImage(account.getM_cover(), motherPhoto, EbaeboApplication.txOptions, animateFirstListener);
            fatherName.setText(account.getF_name());
            motherName.setText(account.getM_name());
        }
    }

    private void initView(){
        father = (LinearLayout) findViewById(R.id.father_linear);
        mother = (LinearLayout) findViewById(R.id.mother_linear);
        fatherPhoto = (ImageView) findViewById(R.id.identity_father);
        fatherName = (TextView) findViewById(R.id.identity_father_name);
        motherPhoto = (ImageView) findViewById(R.id.identity_mother);
        motherName = (TextView) findViewById(R.id.identity_mother_name);
        father.setOnClickListener(this);
        mother.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.father_linear:
            case R.id.mother_linear:
                Intent intent = new Intent(SelectIdentityActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
