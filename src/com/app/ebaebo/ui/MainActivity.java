package com.app.ebaebo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioGroup;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.FragmentTabAdapter;
import com.app.ebaebo.fragment.MessageFragment;
import com.app.ebaebo.fragment.PhotoFragment;
import com.app.ebaebo.fragment.PictureFragment;
import com.app.ebaebo.fragment.RecordFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    public List<Fragment> fragments = new ArrayList<Fragment>();
    RadioGroup radioGroups;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        radioGroups = (RadioGroup) findViewById(R.id.main_radiogroups);

        fragments.add(new MessageFragment());
        fragments.add(new PhotoFragment());
        fragments.add(new RecordFragment());
        fragments.add(new PictureFragment());

        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content,  radioGroups);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                super.OnRgsExtraCheckedChanged(radioGroup, checkedId, index);

            }
        });
    }
}
