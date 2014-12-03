package com.app.ebaebo.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ebaebo.R;
import com.app.ebaebo.data.BabyDATA;
import com.app.ebaebo.data.ErrorDATA;
import com.app.ebaebo.data.UploadDATA;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Baby;
import com.app.ebaebo.util.CommonUtil;
import com.app.ebaebo.util.InternetURL;
import com.google.gson.Gson;
import com.qd.recorder.FFmpegRecorderActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzwei on 2014/11/24.
 *
 * 发布视频
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class PublishVideoActivity extends BaseActivity implements View.OnClickListener,MediaPlayer.OnCompletionListener {
    private ImageView back;
    private TextView publish;
    private EditText content;
    private TextView filePath;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private String babyId;//要发布的宝宝ID
    private ProgressDialog progressDialog;

    private String path;

    private Account account;
    private List<Baby> babies = new ArrayList<Baby>();//下拉列表宝宝

    public static final int VIDEO_CODE = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_video_layout);
        initView();
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        getBabyList();
        openVideo();

    }

    private void initView(){
        back  = (ImageView) findViewById(R.id.publish_video_back);
        publish = (TextView) findViewById(R.id.publish_video_run);
        content = (EditText) findViewById(R.id.publish_video_content);
        filePath = (TextView) findViewById(R.id.publish_video_filepath);
        spinner = (Spinner) findViewById(R.id.publish_video_spinner);

        back.setOnClickListener(this);
        publish.setOnClickListener(this);
    }

    private void openVideo(){
        Intent intent = new Intent(PublishVideoActivity.this, FFmpegRecorderActivity.class);
        startActivityForResult(intent, VIDEO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_CODE){
            ArrayList<String> aryList = data.getStringArrayListExtra("list");
            if (aryList.size() > 0){
                path = aryList.get(0);
                String fileName = path.substring(path.lastIndexOf("/")+1);
                filePath.setText("视频文件:" + fileName);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish_video_back://返回
                finish();
                break;
            case R.id.publish_video_run://发布
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("正在发布，请稍后");
                progressDialog.show();
                uploadFile();
                break;
        }
    }

    private void uploadFile(){
        Map<String, File> files = new HashMap<String, File>();
        File file = new File(path);
        files.put("file", file);
        Map<String, String> params = new HashMap<String, String>();
        addPutUploadFileRequest(
                InternetURL.UPLOAD_FILE,
                files,
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)) {
                            UploadDATA data = getGson().fromJson(s, UploadDATA.class);
                            if (data.getCode() == 200){
                                publishRun(data.getUrl());
                            }
                        }else {
                            Toast.makeText(mContext, "数据有误 ", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                },
                null);
    }

    private void publishRun(final String videoPath){
        final String user_type = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GROWING_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)){
                             ErrorDATA data  = getGson().fromJson(s, ErrorDATA.class);
                            if (data.getCode() == 200){
                                if (progressDialog != null){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(mContext, "发布失败，请稍后重试 ", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid",account.getUid());
                params.put("user_type", user_type);
                params.put("type","2");
                params.put("child_id", babyId);
                params.put("file", videoPath);
                params.put("content", content.getText().toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    private void getBabyList(){
        String uri = String.format(InternetURL.GET_BABY_URL +"?uid=%s", account.getUid());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try{
                            BabyDATA data = gson.fromJson(s, BabyDATA.class);
                            babies.addAll(data.getData());
                            List<String> names = new ArrayList<String>();
                            for (int i=0; i<babies.size(); i++){
                                names.add(babies.get(i).getName());
                            }
                            spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, names);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            spinner.setAdapter(spinnerAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Baby baby = babies.get(position);
                                    babyId = baby.getId();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }catch (Exception e){
                            Toast.makeText(mContext, "获取宝宝列表出错", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
        getRequestQueue().add(request);
    }
}
