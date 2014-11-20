package com.app.ebaebo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.DianmingAdapter;
import com.app.ebaebo.data.DianmingDATA;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Child;
import com.app.ebaebo.util.CommonUtil;
import com.app.ebaebo.util.InternetURL;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/14
 * Time: 23:01
 * 类的功能、说明写在此处.
 */
public class DianmingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView dianmingback;
    private List<Child> list = new ArrayList<Child>();
    private DianmingAdapter adapter;
    private ListView listView;
    private TextView className;//班级名称
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dianming);
        initView();

        adapter = new DianmingAdapter(list, mContext);
        listView.setAdapter(adapter);

        getData();
    }

    private void initView() {
        dianmingback = (ImageView) this.findViewById(R.id.dianmingback);
        dianmingback.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.dianming_lstv);
        className = (TextView) findViewById(R.id.dianming_classname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dianmingback:
                finish();
                break;
        }
    }

    private void getData(){
        Account account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        if (account != null) {
            String uri = String.format(InternetURL.DIANMING_URL + "?uid=%s&class_id=%s", account.getUid(), account.getClass_id());
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (CommonUtil.isJson(s)){
                                DianmingDATA data = getGson().fromJson(s, DianmingDATA.class);
                                className.setText(data.getData().getSclass().getName());
                                list.addAll(data.getData().getChild());
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(mContext, "数据错误，请稍后重试", Toast.LENGTH_SHORT).show();
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
}
