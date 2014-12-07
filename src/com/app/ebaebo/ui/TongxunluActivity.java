package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
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
import com.app.ebaebo.adapter.TongxunluAdapter;
import com.app.ebaebo.data.ErrorDATA;
import com.app.ebaebo.data.TongxunluDATA;
import com.app.ebaebo.entity.Tongxunlu;
import com.app.ebaebo.util.InternetURL;
import com.app.ebaebo.widget.ContentListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/19
 * Time: 21:49
 * 类的功能、说明写在此处.
 */
public class TongxunluActivity extends BaseActivity implements OnClickContentItemListener, View.OnClickListener,ContentListView.OnRefreshListener, ContentListView.OnLoadListener {
    private ImageView back;//返回按钮
    private TongxunluAdapter adapter;
    private ContentListView clv;
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private List<Tongxunlu> list = new ArrayList<Tongxunlu>();
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tongxunlu);
        initView();
        setTheme(R.style.index_theme);
        adapter = new TongxunluAdapter(list, this);
        mRequestQueue = Volley.newRequestQueue(this);
        clv.setAdapter(adapter);
        getData();
        adapter.setOnClickContentItemListener(this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.back:
                    finish();
                    break;
            }
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {

    }

    private void getData(){
        String uri = String.format(InternetURL.GET_TONGXUNLU_URL+"?school_id=%s&uid=%s&class_id=%s","1", "73", "1" );
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            TongxunluDATA data = gson.fromJson(s, TongxunluDATA.class);
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
    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        clv = (ContentListView) this.findViewById(R.id.lstv);
        clv.setOnRefreshListener(this);
        clv.setOnLoadListener(this);
    }
}
