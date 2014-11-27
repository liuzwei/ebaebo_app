package com.app.ebaebo.ui;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
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
import com.app.ebaebo.util.StringUtil;
import com.kubility.demo.MP3Recorder;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by liuzwei on 2014/11/24.
 */
public class PublishRecordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView publish;
    private ImageView play;
    private ImageView cancel;
    private Button voice;
    private EditText content;

    private Spinner spinner;
    private GridView gridView;
    private ArrayAdapter<String> spinnerAdapter;
    private String babyId;//要发布的宝宝ID
    private ProgressDialog progressDialog;
    private Account account;
    private List<Baby> babies = new ArrayList<Baby>();//下拉列表宝宝

    /** log标记 */
    private static final String LOG_TAG = PublishRecordActivity.class.getSimpleName();
    /** 用于语音播放 */
    private MediaPlayer mPlayer = new MediaPlayer();
    /** 录音存储路径 */
    private static final String PATH = "/Recorder.mp3";
    private MP3Recorder recorder = new MP3Recorder(Environment.getExternalStorageDirectory()
            + PATH , 8000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_record_layout);
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        getBabyList();
        initView();
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.publish_record_back);
        publish = (TextView) findViewById(R.id.publish_record_run);
        play = (ImageView) findViewById(R.id.publish_record_play);
        voice = (Button) findViewById(R.id.publish_record_voice);
        spinner = (Spinner) findViewById(R.id.publish_record_spinner);
        content = (EditText) findViewById(R.id.publish_record_content);
        cancel = (ImageView) findViewById(R.id.publish_record_cancel);

        play.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        cancel.setOnClickListener(this);
        back.setOnClickListener(this);
        publish.setOnClickListener(this);
        play.setOnClickListener(this);
        voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        recorder.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        play.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                        recorder.stop();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 获得宝宝列表
     */
    private void  getBabyList(){
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

//    private void startVoice(){
//// 设置录音保存路径
//        mFileName = PATH + UUID.randomUUID().toString() + ".amr";
//        String state = android.os.Environment.getExternalStorageState();
//        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
//            Log.i(LOG_TAG, "SD Card is not mounted,It is  " + state + ".");
//        }
//        File directory = new File(mFileName).getParentFile();
//        if (!directory.exists() && !directory.mkdirs()) {
//            Log.i(LOG_TAG, "Path to file could not be created");
//        }
//        mRecorder = new MediaRecorder();
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//        mRecorder.setOutputFile(mFileName);
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//        try {
//            mRecorder.prepare();
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "prepare() failed");
//        }
//        mRecorder.start();
//    }

//    private void stopVoice(){
//        try {
//            mRecorder.stop();
//            play.setVisibility(View.VISIBLE);
//            cancel.setVisibility(View.VISIBLE);
//        }catch (Exception e){
//            Log.i("PublishRecordActivity", "录音时间太短，为0， 无法保存");
//        }finally {
//            mRecorder.release();
//            mRecorder = null;
//        }
////        Toast.makeText(getApplicationContext(), "保存录音" + mFileName, 0).show();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_record_back://返回
                finish();
                break;
            case R.id.publish_record_play://播放
                try {
                    mPlayer.reset();
                    mPlayer.setDataSource(Environment.getExternalStorageDirectory() + PATH);
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "播放失败");
                }
                break;
            case R.id.publish_record_run://发布
                progressDialog = new ProgressDialog(PublishRecordActivity.this);
                progressDialog.setMessage("正在发布，请稍后");
                progressDialog.show();
                uploadRecord();
                break;
            case R.id.publish_record_cancel:
                File file = new File(Environment.getExternalStorageDirectory() + PATH);
                if (file.isFile() && file.exists()){
                    file.delete();
                    play.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    Toast.makeText(mContext, "删除文件成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void uploadRecord(){
        if (StringUtil.isNullOrEmpty(babyId)){
            progressDialog.dismiss();
            Toast.makeText(mContext, "请选择宝宝", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isNullOrEmpty(Environment.getExternalStorageDirectory() + PATH)){
            progressDialog.dismiss();
            Toast.makeText(mContext, "请录制音频，然后发布", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory() + PATH);
        if (!file.exists() || !file.isFile()){
            progressDialog.dismiss();
            Toast.makeText(mContext, "请录制音频，然后发布", Toast.LENGTH_SHORT).show();
            return;
        }
        if (CommonUtil.getMp3TrackLength(new File(Environment.getExternalStorageDirectory() + PATH)) < 1){
            progressDialog.dismiss();
            Toast.makeText(mContext, "时长小于一秒，请重新录制", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, File> files = new HashMap<String, File>();
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

    private void publishRun(final String recordPath){

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
                params.put("file", recordPath);
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
    protected void onDestroy() {
        super.onDestroy();
        recorder.stop();
    }
}
