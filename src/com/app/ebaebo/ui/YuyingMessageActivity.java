package com.app.ebaebo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.OnClickContentItemListener;
import com.app.ebaebo.adapter.YuyingAdapter;
import com.app.ebaebo.data.ErrorDATA;
import com.app.ebaebo.data.YuyingDATA;
import com.app.ebaebo.entity.*;
import com.app.ebaebo.util.InternetURL;
import com.app.ebaebo.widget.ContentListView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

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
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private List<Yuying> list = new ArrayList<Yuying>();
    private RequestQueue mRequestQueue;
    private Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuyingmessages);
        initView();
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        setTheme(R.style.index_theme);
        adapter = new YuyingAdapter(list, this);
        mRequestQueue = Volley.newRequestQueue(this);
        clv.setAdapter(adapter);
        getData();
        adapter.setOnClickContentItemListener(this);
        clv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Yuying yy = list.get(position);
                Intent detailYY =  new Intent(YuyingMessageActivity.this, YuYiingDetailActivity.class);
                detailYY.putExtra("yy", yy.getId());
                startActivity(detailYY);
            }
        });
    }

    private void initView() {
        yuyingback = (ImageView) this.findViewById(R.id.yuyingback);
        yuyingback.setOnClickListener(this);
        clv = (ContentListView) this.findViewById(R.id.lstv);
        clv.setOnRefreshListener(this);
        clv.setOnLoadListener(this);
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

    private void getData(){
        String uri = String.format(InternetURL.GET_YUYING_MESSAGE+"?school_id=%s&pageIndex=%d&pageSize=%d",account.getSchool_id(), pageIndex, 20 );
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            YuyingDATA data = gson.fromJson(s, YuyingDATA.class);
                            if (IS_REFRESH){
                                list.clear();
                            }
                            list.addAll(data.getData());
                            if (data.getData().size() < 10){
                                clv.setResultSize(0);
                            }
                            clv.onRefreshComplete();
                            clv.onLoadComplete();
                            adapter.notifyDataSetChanged();

                        }catch (Exception e){
                            ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                            if (errorDATA.getMsg().equals("failed")){
                                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mRequestQueue.add(request);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        IS_REFRESH = true;
        pageIndex = 1;
        getData();
    }

    //上拉加载
    @Override
    public void onLoad() {
        IS_REFRESH = false;
        pageIndex++;
        getData();
    }
    Yuying yy = null;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag)
        {
            case 1:
                yy = list.get(position);
                Intent detailYY =  new Intent(this, YuYiingDetailActivity.class);
                detailYY.putExtra("yy", yy.getId());
                startActivity(detailYY);
                break;
        }
    }

}
