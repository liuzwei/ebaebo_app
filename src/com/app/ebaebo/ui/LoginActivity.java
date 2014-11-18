package com.app.ebaebo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ebaebo.R;
import com.app.ebaebo.data.AccountDATA;
import com.app.ebaebo.data.ErrorDATA;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.util.InternetURL;
import com.app.ebaebo.util.StringUtil;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText username;//用户名
    private EditText password;//密码
    private TextView loginBtn;//登陆按钮
    private TextView forgetPass;//忘记密码
    private ProgressDialog progressDialog;

    private RequestQueue mRequestQueue;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initView();

        mRequestQueue = Volley.newRequestQueue(this);
    }

    private void initView(){
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        loginBtn = (TextView) findViewById(R.id.login_btn);
        forgetPass = (TextView) findViewById(R.id.login_forgetpass);

        loginBtn.setOnClickListener(this);
        forgetPass.setOnClickListener(this);

        username.setText(sp.getString("username", ""));
        password.setText(sp.getString("password", ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn://登陆按钮
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle(R.string.login_progress_dialog);
                progressDialog.show();
                final String name = username.getText().toString();
                final String pass = password.getText().toString();

                if (name.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //组装请求url
                String uri = String.format(InternetURL.LOGIN_API + "?user_name=%s&password=%s", name, pass);
                StringRequest request = new StringRequest(
                        Request.Method.GET,
                        uri,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Gson gson = new Gson();
                                try {
                                    AccountDATA data = gson.fromJson(s, AccountDATA.class);
                                    Account account = data.getData();
                                    saveAccount(name, pass, account);
                                    if ("1".equals(account.getIs_student())){//如果是家长登陆，选择身份
                                        Intent setIdentity = new Intent(LoginActivity.this, SelectIdentityActivity.class);
                                        setIdentity.putExtra("account", account);
                                        startActivity(setIdentity);
                                    }else {//老师登陆直接跳转到主页面
                                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(main);
                                    }
                                }catch (Exception e){
                                    ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                                    if (errorDATA.getMsg().equals("failed")){
                                        Toast.makeText(mContext, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                progressDialog.dismiss();
                            }
                        },new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                });

                mRequestQueue.add(request);
                break;
            case R.id.login_forgetpass://忘记密码
                Intent forgetPass = new Intent(this, ForgetPassOneActivity.class);
                startActivity(forgetPass);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRequestQueue.cancelAll(this);
    }

    private void saveAccount(String username, String password, Account account){
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("account", gson.toJson(account));
        editor.commit();
    }
}
