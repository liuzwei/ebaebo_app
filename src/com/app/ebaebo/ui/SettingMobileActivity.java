package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ebaebo.R;
import com.app.ebaebo.entity.Account;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/18
 * Time: 15:58
 * 类的功能、说明写在此处.
 */
public class SettingMobileActivity extends BaseActivity implements View.OnClickListener{
    private ImageView back;
    private TextView mobile;
    private String mobilenum;
    Account account ;
//    private TextView set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setmibole);
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        mobile = (TextView) this.findViewById(R.id.mobile);
        mobile.setText(account.getMobile());
//        set = (TextView) this.findViewById(R.id.set);
//        set.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
//            case R.id.set:
//                //设置手机事件
//                mobilenum = mobile.getText().toString();//邮箱
//                break;
        }
    }
}
