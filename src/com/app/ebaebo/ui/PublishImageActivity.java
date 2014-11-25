package com.app.ebaebo.ui;

import android.net.Uri;
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
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Baby;
import com.app.ebaebo.util.InternetURL;
import com.app.ebaebo.util.StringUtil;
import com.app.ebaebo.widget.ContentListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzwei on 2014/11/22.
 */
public class PublishImageActivity extends BaseActivity implements View.OnClickListener{
    private ImageView back;
    private TextView publish;
    private Spinner spinner;
    private EditText content;
    private GridView gridView;
    private ArrayAdapter<String> spinnerAdapter;

    private String babyId;//要发布的宝宝ID
    private Account account;
    private int res[] = new int[]{R.drawable.abaose};
    private List<Baby> babies = new ArrayList<Baby>();//下拉列表宝宝
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_image_layout);
        initView();
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        getBabyList();

        List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
        for(int i=0;i<res.length;i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("imageView",res[i]);
            data.add(map);
        }
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.image_cell_layout , new String[]{"imageView"}, new int[]{R.id.imageView1});
//        gridView.setAdapter(simpleAdapter);
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.publish_image_back);
        publish = (TextView) findViewById(R.id.publish_image_run);
        spinner = (Spinner) findViewById(R.id.publish_image_spinner);
        content = (EditText) findViewById(R.id.publish_image_content);
        gridView = (GridView) findViewById(R.id.publish_image_gv);

        back.setOnClickListener(this);
        publish.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_image_back://后退按钮
                finish();
                break;
            case R.id.publish_image_run://发布按钮
                push();
                break;
        }
    }

    private void  getBabyList(){
        String uri = String.format(InternetURL.GET_BABY_URL +"?uid=%s", account.getUid());
//        String uri = "http://yey.xqb668.com/json.php/growing.api-childrens/?uid=102";
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
//                            ErrorDATA data = gson.fromJson(s, ErrorDATA.class);
//                            if (data.getCode() == 500){
//                                Log.i("ErrorData", "获取baby信息数据错误");
//                            }
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

    private void push(){
        final String pushContent = content.getText().toString();
        if (StringUtil.isNullOrEmpty(pushContent)){
            Toast.makeText(mContext, "文字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isNullOrEmpty(babyId)){
            Toast.makeText(mContext, "请选择宝宝", Toast.LENGTH_SHORT).show();
            return;
        }
        final String identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GROWING_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        ErrorDATA data = getGson().fromJson(s, ErrorDATA.class);
                        if (data.getCode() == 200){
                            Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("content",pushContent);
                params.put("uid",account.getUid());
                params.put("user_type", identity);
                params.put("type","0");
                params.put("child_id", babyId);

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
}
