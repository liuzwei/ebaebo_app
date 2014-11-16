package com.app.ebaebo.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.OnClickContentItemListener;
import com.app.ebaebo.adapter.PhotoAdapter;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Photos;
import com.app.ebaebo.entity.Pictures;
import com.app.ebaebo.util.HttpUtils;
import com.app.ebaebo.util.ShellContext;
import com.app.ebaebo.widget.ContentListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 21:24
 * 类的功能、说明写在此处.
 */
public class PhotosActivity extends BaseActivity implements OnClickContentItemListener, View.OnClickListener {
    private ListView clv;
    private PhotoAdapter adapter;
    //定义一个集合用来存放相册集合
    private List<Photos> list = new ArrayList<Photos>();
    private ProgressDialog pd = null;
    private Map<String,String> map = new HashMap<String,String>();
    Account account = (Account) ShellContext.getAttribute(ShellContext.ACCOUNT);
    private ImageView photosback;//返回按钮
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            pd.dismiss();
            switch (msg.what) {
                case Constants.SUCCESS:
                    adapter.notifyDataSetChanged();
                    break;
                case Constants.FAIL:
                    alert(R.string.error1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);
        initView();
        setTheme(R.style.index_theme);
        registerBoradcastReceiver();

        adapter = new PhotoAdapter(list, this);
        clv.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
//        if(account != null){
//            map.put("uid", account.getUid());
            map.put("uid", "102");
            loadData();
//        }

    }

    private void initView() {
        clv = (ListView) this.findViewById(R.id.lstv);
        photosback = (ImageView) this.findViewById(R.id.photosback);
        photosback.setOnClickListener(this);
    }

    public void loadData()
    {
        getAppThread().execute(new Runnable() {
            @Override
            public void run() {
                String result  = HttpUtils.postRequest("http://yey.xqb668.com/json.php/sclass.api-albumlist/", map);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if(code.equals("200")){//如果成功的话
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if(jsonArray!=null && jsonArray.length()>0){
                            //相册信息
                            String id = "";
                            String name = "";
                            String publisher = "";
                            String publish_uid = "";
                            String class_id = "";
                            String number = "";
                            String cover ="";
                            String school_id = "";
                            String dateline = "";
                            JSONArray jsonArray2 = null;

                            //照片信息
                            String idd = "";
                            String album_id = "";
                            String pic = "";
                            String datelinee = "";
                            Pictures pictures = null;
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
                                id = jsonObject2.getString("id");
                                name = jsonObject2.getString("name");
                                publisher = jsonObject2.getString("publisher");
                                publish_uid = jsonObject2.getString("publish_uid");
                                class_id = jsonObject2.getString("class_id");
                                number = jsonObject2.getString("number");
                                cover = jsonObject2.getString("cover");
                                school_id = jsonObject2.getString("school_id");
                                dateline = jsonObject2.getString("dateline");
                                jsonArray2 = jsonObject2.getJSONArray("list");
                                //定义一个集合用来存放照片
                                List<Pictures> listpics =  new ArrayList<Pictures>();
                                if(jsonArray2!=null && jsonArray2.length() > 0){
                                    for(int j =0; j<jsonArray2.length();j++){
                                        JSONObject jsonObject3 = (JSONObject)jsonArray2.opt(i);
                                        idd = jsonObject3.getString("id");
                                        album_id = jsonObject3.getString("album_id");
                                        pic = jsonObject3.getString("pic");
                                        datelinee = jsonObject3.getString("dateline");
                                        pictures =  new Pictures(idd, album_id, pic, datelinee);
                                        listpics.add(pictures);//照片集合
                                    }
                                }
                                //相册对象photo
                                Photos photo = new Photos(id, name, publisher, publish_uid, class_id, number, cover, school_id, dateline, listpics);
                                //相册集合添加相册对象
                                list.add(photo);
                            }
                            mHandler.sendMessage(mHandler.obtainMessage(Constants.SUCCESS));
                        }else{
                            //相册为空
                            mHandler.sendMessage(mHandler.obtainMessage(Constants.SUCCESS));
                        }
                    }else{
                        mHandler.sendMessage(mHandler.obtainMessage(Constants.FAIL));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(Constants.FAIL));
                }
            }
        });
    }

    private int pos = 0;
    private Photos photo;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        pos = position;
        photo = list.get(position);
        switch (flag)
        {
            case 1:
                Intent pic = new Intent(this, PictureActivity.class);
                pic.putExtra("photo", photo);
                startActivity(pic);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.photosback:
                finish();
                break;
        }
    }
    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.SEND_SUCCESS);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            loadData();
        }

    };

}
