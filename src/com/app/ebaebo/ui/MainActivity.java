package com.app.ebaebo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.*;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.FragmentTabAdapter;
import com.app.ebaebo.fragment.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
//    public List<Fragment> fragments = new ArrayList<Fragment>();
//    RadioGroup radioGroups;
    private ImageView leftbutton;
    private SlideMenu slideMenu;
    private TextView user;//用户
    private TextView growup;//成长记录
    private TextView message;//交互信息
    private TextView photoAlbum;//班级相册
    private TextView schoolCar;//校车通知
    private TextView addressBook;//通讯录
    private TextView yuyingInfo;//育英信息
    private TextView callName;//点名
    private TextView setting;//设置
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        radioGroups = (RadioGroup) findViewById(R.id.main_radiogroups);
//
//        fragments.add(new MessageFragment());
//        fragments.add(new PhotoFragment());
//        fragments.add(new VideoFragment());
//        fragments.add(new RecordFragment());
//        fragments.add(new PictureFragment());
//
//
//        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content,  radioGroups);
//        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
//            @Override
//            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
//                super.OnRgsExtraCheckedChanged(radioGroup, checkedId, index);
//
//            }
//        });
    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);
        slideMenu = (SlideMenu) findViewById(R.id.slide_menu);

        user = (TextView) slideMenu.findViewById(R.id.leftmenu_user);
        growup = (TextView) slideMenu.findViewById(R.id.leftmenu_growup);
        message = (TextView) slideMenu.findViewById(R.id.leftmenu_message);
        photoAlbum = (TextView) slideMenu.findViewById(R.id.leftmenu_photo_album);
        schoolCar = (TextView) slideMenu.findViewById(R.id.leftmenu_school_car);
        addressBook = (TextView) slideMenu.findViewById(R.id.leftmenu_address_book);
        yuyingInfo = (TextView) slideMenu.findViewById(R.id.leftmenu_info);
        callName = (TextView) slideMenu.findViewById(R.id.leftmenu_callname);
        setting = (TextView) slideMenu.findViewById(R.id.leftmenu_setting);

        user.setOnClickListener(this);
        growup.setOnClickListener(this);
        message.setOnClickListener(this);
        photoAlbum.setOnClickListener(this);
        schoolCar.setOnClickListener(this);
        addressBook.setOnClickListener(this);
        yuyingInfo.setOnClickListener(this);
        callName.setOnClickListener(this);
        setting.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftbutton:
                if (slideMenu.isMainScreenShowing()) {
                    slideMenu.openMenu();
                } else {
                    slideMenu.closeMenu();
                }
                break;
            case R.id.leftmenu_user://用户

                break;
            case R.id.leftmenu_growup://成长记录

                break;
            case R.id.leftmenu_message://交互信息
                Intent jiaohu = new Intent(MainActivity.this, JiaohuActivity.class);
                startActivity(jiaohu);
                break;
            case R.id.leftmenu_photo_album://班级相册
                Intent photo = new Intent(this, PhotosActivity.class);
                startActivity(photo);
                break;
            case R.id.leftmenu_school_car://校车通知
                Intent schoolbus = new Intent(MainActivity.this, SchoolBusActivity.class);
                startActivity(schoolbus);
                break;
            case R.id.leftmenu_address_book://通讯簿

                break;
            case R.id.leftmenu_info://育英信息
                Intent yuying = new Intent(MainActivity.this, YuyingMessageActivity.class);
                startActivity(yuying);
                break;
            case R.id.leftmenu_callname://点名

                break;

            case R.id.leftmenu_setting://设置
                Intent setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(setting);
                break;
        }
    }
}
