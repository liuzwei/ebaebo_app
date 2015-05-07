package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.PictureAdapter;
import com.app.ebaebo.entity.Photos;
import com.app.ebaebo.entity.Pictures;
import com.app.ebaebo.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/16
 * Time: 23:06
 * 类的功能、说明写在此处.
 */
public class PictureActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView title;
    private TextView name;
    private PictureAdapter adapter;
    private GridView clv;

    List<Pictures> list = new ArrayList<Pictures>();

    private ImageView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictures);
        initView();
        setTheme(R.style.index_theme);
        Photos photos = (Photos) getIntent().getExtras().get("photo");

        if(photos!=null){
            list = photos.getList();
            title.setText(photos.getName());
        }
        adapter = new PictureAdapter(list, this);
        clv.setAdapter(adapter);
        String time =  TimeUtils.zhuanhuanTime(Long.parseLong(photos.getDateline()));
        name.setText(photos.getPublisher()+"创建于"+time);
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.picturesback);
        title  = (TextView) this.findViewById(R.id.title);
        name  = (TextView) this.findViewById(R.id.name);
        clv = (GridView) this.findViewById(R.id.lstv);
        add = (ImageView) this.findViewById(R.id.add);

        add.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.picturesback:
                finish();
                break;
            case R.id.add:
                break;
        }
    }

}
