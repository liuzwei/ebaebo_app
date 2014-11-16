package com.app.ebaebo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.OnClickContentItemListener;
import com.app.ebaebo.adapter.PhotoAdapter;
import com.app.ebaebo.adapter.YuyingAdapter;
import com.app.ebaebo.entity.Photos;
import com.app.ebaebo.entity.Pictures;
import com.app.ebaebo.entity.Yuying;
import com.app.ebaebo.util.HttpUtils;
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
 * Date: 2014/11/14
 * Time: 8:13
 * 类的功能、说明写在此处.
 */
public class YuyingMessageActivity extends BaseActivity implements OnClickContentItemListener, View.OnClickListener,ContentListView.OnRefreshListener, ContentListView.OnLoadListener {
    private ImageView yuyingback;//返回按钮
    private YuyingAdapter adapter;
    private ContentListView clv;
    private int index = 1;
    private Map<String,String> map = new HashMap<String,String>();
    List<Yuying> list = new ArrayList<Yuying>();
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ContentListView.REFRESH:
                    clv.onRefreshComplete();
                    NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
                    if (networkInfo != null) {
                        list.clear();
                    }else {
                        Toast.makeText(mContext, "当前网络不可用", Toast.LENGTH_SHORT).show();
                    }
                    list.addAll(datalist);
                    clv.setResultSize(datalist.size());
                    adapter.notifyDataSetChanged();
                    break;
                case ContentListView.LOAD:
                    clv.onLoadComplete();
                    list.addAll(datalist);
                    clv.setResultSize(datalist.size());
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuyingmessages);
        initView();
        setTheme(R.style.index_theme);
        registerBoradcastReceiver();
        adapter = new YuyingAdapter(list, this);
        clv.setAdapter(adapter);
        clv.setOnRefreshListener(this);
        clv.setOnLoadListener(this);
        adapter.setOnClickContentItemListener(this);
        loadData(ContentListView.LOAD);
    }

    private void initView() {
        yuyingback = (ImageView) this.findViewById(R.id.yuyingback);
        yuyingback.setOnClickListener(this);
        clv = (ContentListView) this.findViewById(R.id.lstv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.yuyingback:
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
            loadData(ContentListView.REFRESH);
        }

    };

    /**
     * 加载数据监听实现
     */
    @Override
    public void onLoad() {
        index++;
        loadData(ContentListView.LOAD);
    }

    /**
     * 刷新数据监听实现
     */
    @Override
    public void onRefresh() {
        index = 1;
        loadData(ContentListView.REFRESH);
    }
    private void loadData(final int what) {
            Message msg = mHandler.obtainMessage();
            msg.what = what;
            getData();
    }
    List<Yuying> datalist = null;
    public void getData()
    {
        getAppThread().execute(new Runnable() {
            @Override
            public void run() {
                datalist =  new ArrayList<Yuying>();
                map.put("school_id", "1");
                map.put("pageSize", "20");
                map.put("pageIndex", String.valueOf(index));
                String result  = HttpUtils.postRequest("http://yey.xqb668.com/index/ServiceJson/news", map);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if(code.equals("200")){//如果成功的话
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if(jsonArray!=null && jsonArray.length()>0){
                            String id = "";
                            String title = "";
                            String pic = "";
                            String summary = "";
                            String content = "";
                            String publish_uid = "";
                            String publisher = "";
                            String school_id = "";
                            String dateline = "";
                            Yuying yuying = null;
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = (JSONObject)jsonArray.opt(i);
                                id = jsonObject1.getString("id");
                                title = jsonObject1.getString("title");
                                pic = jsonObject1.getString("pic");
                                summary = jsonObject1.getString("summary");
                                content = jsonObject1.getString("content");
                                publish_uid = jsonObject1.getString("publish_uid");
                                publisher = jsonObject1.getString("publisher");
                                school_id = jsonObject1.getString("school_id");
                                dateline = jsonObject1.getString("dateline");
                                yuying = new Yuying(id, title, pic, summary, content, publish_uid, publisher, school_id, dateline);
                                datalist.add(yuying);
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

    @Override
    public void onClickContentItem(int position, int flag, Object object) {

    }
}
