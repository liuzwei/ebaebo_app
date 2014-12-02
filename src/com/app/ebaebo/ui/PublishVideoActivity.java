package com.app.ebaebo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.app.ebaebo.R;
import com.qd.recorder.FFmpegRecorderActivity;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/24.
 *
 * 发布视频
 */
public class PublishVideoActivity extends BaseActivity {
    public static final int VIDEO_CODE = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_video_layout);

        openVideo();
    }

    private void openVideo(){
        Intent video = new Intent(this, FFmpegRecorderActivity.class);
        startActivityForResult(video, VIDEO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_CODE){
            List<String> tDataList =data.getStringArrayListExtra("data");
            String videoPath = tDataList.get(0);
            String picPath = tDataList.get(1);
        }
    }
}
