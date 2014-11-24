package com.app.ebaebo.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.ebaebo.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by liuzwei on 2014/11/24.
 */
public class PublishPhotoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText content;
    private ImageView photoImage;
    private TextView publish;

    private static int REQUEST_CODE = 1;
    private File file;
    private Bitmap photo;
    private String pictureDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_photo_layout);

        initView();

        openCamera();
    }

    /**
     * 销毁图片文件
     */
    private void destoryBimap() {
        if (photo != null && !photo.isRecycled()) {
            photo.recycle();
            photo = null;
        }
    }

    private void openCamera(){
        destoryBimap();
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            String saveDir = Environment.getExternalStorageDirectory()
                    + "/ebaebo/photoCache";
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            file = new File(saveDir, "photo.jpg");
            file.delete();
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PublishPhotoActivity.this,
                            "文件为空",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Toast.makeText(PublishPhotoActivity.this,
                    "检查有无内存卡 ", Toast.LENGTH_LONG)
                    .show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                return;
            }
            if (file != null && file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                photo = BitmapFactory.decodeFile(file.getPath(), options);
                photoImage.setBackgroundDrawable(new BitmapDrawable(photo));
                pictureDir = file.getPath();
            } else {
                Toast.makeText(this, "图片文件为空",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.publish_photo_back);
        photoImage = (ImageView) findViewById(R.id.publish_photo_imv);
        content = (EditText) findViewById(R.id.publish_photo_content);
        publish = (TextView) findViewById(R.id.publish_photo_run);

        back.setOnClickListener(this);
        publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_photo_back://返回
                finish();
                break;

            case R.id.publish_photo_run://发布

                break;
        }
    }
}
