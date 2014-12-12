package com.app.ebaebo.ui;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import com.app.ebaebo.R;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/12
 * Time: 11:26
 * 类的功能、说明写在此处.
 */
public class SetWifiActivity extends BaseActivity implements View.OnClickListener {
    private RadioGroup group_temo;
    private RadioButton checkRadioButton;
    WifiManager WifiManager=null;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setwifi);
        group_temo = (RadioGroup) findViewById(R.id.switchwifi);

        // 对WIFI网卡进行操作需要通过WifiManager对象来进行，获取该对象的方法如下：
        WifiManager=(WifiManager)SetWifiActivity.this.getSystemService(Context.WIFI_SERVICE);
        //改变默认选项
       // group_temo.check(R.id.open);
        //获取默认被被选中值
        //获取网卡当前的状态
        int state = WifiManager.getWifiState();
        if(state == 1 || state == 2){//open
            group_temo.check(R.id.open);
        }else{
            //close
            group_temo.check(R.id.close);
        }
       // checkRadioButton = (RadioButton) group_temo.findViewById(group_temo.getCheckedRadioButtonId());
//        Toast.makeText(this, "默认的选项的值是:" + WifiManager.getWifiState(), Toast.LENGTH_LONG).show();
        //注册事件
        group_temo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //点击事件获取的选择对象
                checkRadioButton = (RadioButton) group_temo.findViewById(checkedId);
                if(checkRadioButton.getText().equals("open") ){
                    //打开wifi
                    //打开WIFI网卡
                    WifiManager.setWifiEnabled(true);
                    Toast.makeText(SetWifiActivity.this, "打开了WIFI" , Toast.LENGTH_LONG).show();
//                    Toast.makeText(SetWifiActivity.this, "open:" + WifiManager.getWifiState(), Toast.LENGTH_LONG).show();
                }else {
                    //关掉wifi
                    // 关闭WIFI网卡
                    WifiManager.setWifiEnabled(false);
                    Toast.makeText(SetWifiActivity.this, "关闭了WIFI" , Toast.LENGTH_LONG).show();
//                    Toast.makeText(SetWifiActivity.this, "close:" + WifiManager.getWifiState(), Toast.LENGTH_LONG).show();
                }
            }
        });

        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
        }
    }
}
