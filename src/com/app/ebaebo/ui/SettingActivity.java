package com.app.ebaebo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.app.ebaebo.ActivityTack;
import com.app.ebaebo.R;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.util.SexRadioGroup;
import com.app.ebaebo.util.StringUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * Created by liuzwei on 2014/11/13.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    LinearLayout setPassword;//修改密码
    LinearLayout myPhone;//我的手机号
    LinearLayout babySet;//宝宝设置
    LinearLayout mumSet;//妈妈设置
    LinearLayout boundEmail;//绑定邮箱
    LinearLayout settingwifiliner;//设置wifi
    LinearLayout about;//
    ImageView back;//返回
    TextView zhanghao;
    Account account ;
    TextView setting_exit;//退出当前账号
    private String identity;
    private TextView setting_question;
    private LinearLayout setting_baobao;
    private LinearLayout setting_mama;
    private LinearLayout setting_teacherliner;//老师设置
    private LinearLayout setting_checkversion;//检查更新
    private ProgressDialog progressDialog;

    private SexRadioGroup profile_personal_sex;
    private RadioButton button_one;
    private RadioButton button_two;
    private String sex_selected = "";
    private String iswifi = "0";//0是开  1是关

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        iswifi = getGson().fromJson(sp.getString(Constants.ISWIFI, ""), String.class);
        if (StringUtil.isNullOrEmpty(iswifi)){
            //如果没设置过
            iswifi = "1";
        }
        initView();
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        zhanghao.setText(account.getUser_name());
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        if(identity.equals("0")){
            setting_question.setText("爸爸设置");
        }
        if(identity.equals("1")){
            setting_question.setText("妈妈设置");
        }
        if(account.getIs_teacher().equals("1")){
            setting_baobao.setVisibility(View.GONE);
            setting_mama.setVisibility(View.GONE);
            setting_teacherliner.setVisibility(View.VISIBLE);
        }
    }

    private void initView(){
        profile_personal_sex = (SexRadioGroup) this.findViewById(R.id.segment_text);
        profile_personal_sex.setOnCheckedChangeListener(this);
        button_one = (RadioButton) this.findViewById(R.id.button_one);
        button_two = (RadioButton) this.findViewById(R.id.button_two);

        if (!StringUtil.isNullOrEmpty(iswifi)) {
            if ("1".equals(iswifi)) {
                button_two.setChecked(true);
                button_one.setChecked(false);
            }
            if ("0".equals(iswifi)) {
                button_two.setChecked(false);
                button_one.setChecked(true);
            }
        }

        setPassword = (LinearLayout) findViewById(R.id.setting_password);
        myPhone = (LinearLayout) findViewById(R.id.setting_phonenumber);
        babySet = (LinearLayout) findViewById(R.id.setting_baobao);
        mumSet = (LinearLayout) findViewById(R.id.setting_mama);
        boundEmail = (LinearLayout) findViewById(R.id.setting_bound_email);
        about = (LinearLayout) findViewById(R.id.setting_about);
        back = (ImageView) findViewById(R.id.setting_back);
        zhanghao = (TextView) this.findViewById(R.id.zhanghao);
        setting_exit = (TextView) this.findViewById(R.id.setting_exit);
        setting_exit.setOnClickListener(this);
        setPassword.setOnClickListener(this);
        myPhone.setOnClickListener(this);
        babySet.setOnClickListener(this);
        mumSet.setOnClickListener(this);
        boundEmail.setOnClickListener(this);
        about.setOnClickListener(this);
        back.setOnClickListener(this);
        setting_question = (TextView) this.findViewById(R.id.setting_question);
        setting_baobao = (LinearLayout) this.findViewById(R.id.setting_baobao);
        setting_mama = (LinearLayout) this.findViewById(R.id.setting_mama);
        setting_teacherliner = (LinearLayout) this.findViewById(R.id.setting_teacherliner);
        setting_checkversion = (LinearLayout) this.findViewById(R.id.setting_checkversion);
        setting_teacherliner.setOnClickListener(this);
        setting_checkversion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_password://设置密码
                Intent pass = new Intent(this, SettingPassword.class);
                startActivity(pass);
                break;
            case R.id.setting_phonenumber://我的手机号
                Intent mobile = new Intent(this, SettingMobileActivity.class);
                startActivity(mobile);
                break;
            case R.id.setting_baobao://宝宝设置
                Intent babySet = new Intent(SettingActivity.this, BabySettingActivity.class);
                startActivity(babySet);
                break;
            case R.id.setting_mama://家长设置
                Intent mumSet = new Intent(SettingActivity.this, MumSettingActivity.class);
                startActivity(mumSet);
                break;
            case R.id.setting_bound_email://绑定邮箱
                Intent email = new Intent(this, SettingEmailActivity.class);
                startActivity(email);
                break;
            case R.id.setting_about://关于
                Intent about = new Intent(this, SettingAboutActivity.class);
                startActivity(about);
                break;
            case R.id.setting_back://返回按钮
                finish();
                break;
            case R.id.setting_exit:
                ActivityTack.getInstanse().popUntilActivity(LoginActivity.class);
                break;
            case R.id.setting_teacherliner:
                Intent teacher = new Intent(this,TeacherSettingActivity.class);
                startActivity(teacher);
                break;
            case R.id.setting_checkversion:
            {
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.check_new_version).toString();
                progressDialog = new ProgressDialog(SettingActivity.this);
                progressDialog.setMessage(message);
                progressDialog.show();

                UmengUpdateAgent.forceUpdate(this);

                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                        progressDialog.dismiss();
                        switch (i) {
                            case UpdateStatus.Yes:
//                                Toast.makeText(mContext, "有新版本发现", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.No:
                                Toast.makeText(SettingActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.Timeout:
                                Toast.makeText(SettingActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
                break;
        }
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == profile_personal_sex) {
            if (checkedId == R.id.button_one) {
                //开
                sex_selected = "0";
                save(Constants.ISWIFI, sex_selected);
            } else if (checkedId == R.id.button_two) {
                //关
                sex_selected = "1";
                save(Constants.ISWIFI, sex_selected);
            }
        }

    }
}
