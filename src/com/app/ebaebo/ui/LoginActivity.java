package com.app.ebaebo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.ebaebo.R;
import com.app.ebaebo.util.InternetURL;
import org.json.JSONException;
import org.json.JSONObject;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn://登陆按钮
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle(R.string.login_progress_dialog);
                progressDialog.show();
                String name = username.getText().toString();
                String pass = password.getText().toString();

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
                JSONObject params = new JSONObject();
                try {
                    params.put("user_name", name);
                    params.put("password", pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, InternetURL.LOGIN_API,params,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("jsonObject：" + response);
                        progressDialog.dismiss();
                        //登陆成功后跳转到主页面
                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(main);
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                mRequestQueue.add(jr);
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
}
