package com.app.ebaebo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
        }
    }
}
