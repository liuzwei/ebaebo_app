package com.app.ebaebo.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.GridImageAdapter;
import com.app.ebaebo.util.CommonDefine;
import com.app.ebaebo.util.FileUtils;
import com.app.ebaebo.util.ImageUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by liuzwei on 2014/11/26.
 */
public class PublishPictureActivity extends BaseActivity implements View.OnClickListener{
    private final static int SELECT_LOCAL_PHOTO = 110;

    private GridView gridView;
    private ImageView back;
    private TextView publish;
    private EditText content;
    private GridImageAdapter gridImageAdapter;
    private ArrayList<String> dataList = new ArrayList<String>();
    private ArrayList<String> tDataList = new ArrayList<String>();

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_picture_layout);

        initView();
        openPhotoAlbum();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_picture_back://返回

                break;
            case R.id.publish_picture_run://发布

                break;
        }
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.publish_picture_back);
        publish = (TextView) findViewById(R.id.publish_picture_run);

        content = (EditText) findViewById(R.id.publish_picture_content);
        back.setOnClickListener(this);
        publish.setOnClickListener(this);

        gridView = (GridView) findViewById(R.id.gridview_image);
        gridImageAdapter = new GridImageAdapter(mContext, dataList);
        gridView.setAdapter(gridImageAdapter);

        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String path = dataList.get(position);
                if (path.contains("default") && position == dataList.size() -1 && dataList.size() -1 != 9) {
                    showSelectImageDialog();
                } else {
                    Intent intent = new Intent(PublishPictureActivity.this, ImageDelActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("path", dataList.get(position));
                    startActivityForResult(intent, CommonDefine.DELETE_IMAGE);
                }
            }
        });
    }

    // 选择相册，相机
    private void showSelectImageDialog() {
        final Dialog picAddDialog = new Dialog(PublishPictureActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(PublishPictureActivity.this, R.layout.item_dialog_camera, null);
        TextView camera = (TextView) picAddInflate.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {// 选择相机
                Intent cameraIntent = new Intent();
                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
                // 根据文件地址创建文件
                File file = new File(CommonDefine.FILE_PATH);
                if (file.exists()) {
                    file.delete();
                }
                uri = Uri.fromFile(file);
                // 设置系统相机拍摄照片完成后图片文件的存放地址
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                // 开启系统拍照的Activity
                startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
                picAddDialog.dismiss();
            }
        });
        TextView mapStroge = (TextView) picAddInflate.findViewById(R.id.mapstorage);
        mapStroge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {// 选择图库
                Intent intent = new Intent(PublishPictureActivity.this, AlbumActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("dataList", getIntentArrayList(dataList));
                bundle.putString("editContent", content.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, CommonDefine.TAKE_PICTURE_FROM_GALLERY);
                picAddDialog.dismiss();
//                AlbumEditActivity.this.finish();
            }
        });
        TextView cancel = (TextView) picAddInflate.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    /**
     * 打开相册
     */
    private void openPhotoAlbum() {
        startActivityForResult(new Intent(PublishPictureActivity.this, AlbumActivity.class), SELECT_LOCAL_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case SELECT_LOCAL_PHOTO:
                    tDataList =data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                        }
                        if (dataList.size() < 9) {
                            dataList.add("camera_default");
                        }
                        gridImageAdapter.notifyDataSetChanged();
                    }
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        return;
                    }
                    Bitmap bitmap = ImageUtils.getUriBitmap(this, uri, 400, 400);
                    String cameraImagePath = FileUtils.saveBitToSD(bitmap, System.currentTimeMillis() + "");

//				Bundle bundle = data.getExtras();
//				Bitmap bitmap = (Bitmap) bundle.get("data");
//				String cameraImagePath = ImageUtils.setCameraImage(bitmap);

                    for (int i = 0; i < dataList.size(); i++) {
                        String path = dataList.get(i);
                        if(path.contains("default")) {
                            dataList.remove(dataList.size() - 1);
                        }
                    }
                    dataList.add(cameraImagePath);
                    if(dataList.size() < 9) {
                        dataList.add("camera_default");
                    }
                    gridImageAdapter.notifyDataSetChanged();
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_GALLERY:
//                    Bundle bundle2 = data.getExtras();
                    tDataList = data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        dataList.clear();
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                        }
                        if (dataList.size() < 9) {
                            dataList.add("camera_default");
                        }
                        gridImageAdapter.notifyDataSetChanged();
                    }

                    break;
                case CommonDefine.DELETE_IMAGE:
                    int position = data.getIntExtra("position", -1);
                    dataList.remove(position);
                    if(dataList.size() < 9 ) {
                        dataList.add(dataList.size(), "camera_default");
                        for (int i = 0; i < dataList.size(); i++) {
                            String path = dataList.get(i);
                            if(path.contains("default")) {
                                dataList.remove(dataList.size() - 2);
                            }
                        }
                    }
                    gridImageAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

        ArrayList<String> tDataList = new ArrayList<String>();

        for (String s : dataList) {
            if (!s.contains("default")) {
                tDataList.add(s);
            }
        }
        return tDataList;
    }
}
