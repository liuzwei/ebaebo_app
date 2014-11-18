package com.app.ebaebo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.GrowingAdapter;
import com.app.ebaebo.adapter.OnClickContentItemListener;
import com.app.ebaebo.data.BabyDATA;
import com.app.ebaebo.data.ErrorDATA;
import com.app.ebaebo.data.GrowingDATA;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Baby;
import com.app.ebaebo.entity.Growing;
import com.app.ebaebo.util.InternetURL;
import com.app.ebaebo.widget.ContentListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements
        View.OnClickListener,OnClickContentItemListener,ContentListView.OnRefreshListener, ContentListView.OnLoadListener {
//    public List<Fragment> fragments = new ArrayList<Fragment>();
//    RadioGroup radioGroups;
    private ImageView leftbutton;
    private SlideMenu slideMenu;
    private TextView user;//用户
    private TextView growup;//成长记录
    private TextView message;//交互信息
    private TextView photoAlbum;//班级相册
    private TextView schoolCar;//校车通知
    private TextView addressBook;//通讯录
    private TextView yuyingInfo;//育英信息
    private TextView callName;//点名
    private TextView setting;//设置
    private Spinner growingManager;//成长管理下拉
    private ArrayAdapter<String> spinnerAdapter;

    private ContentListView listView;
    private GrowingAdapter adapter;

    private String uid;
    private int pageIndex;
    private int pageSize;
    private int child_id;
    private Account account;

    private List<Growing> growingList = new ArrayList<Growing>();
    private List<Baby> babies = new ArrayList<Baby>();//下拉列表宝宝
    private RequestQueue mRequestQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        String accountStr = sp.getString("account", "");
        if (!accountStr.isEmpty()){
            try{
                account =getGson().fromJson(accountStr, Account.class);
                if (account != null){
                    uid = account.getUid();
                    pageIndex = 1;
                    pageSize = 20;
                }
            }catch (Exception e){
                Log.i("Account Gson Exception", "Account转换异常");
            }
        }

        mRequestQueue = Volley.newRequestQueue(this);
        adapter = new GrowingAdapter(growingList, mContext);
        listView.setAdapter(adapter);
        getData(ContentListView.REFRESH);
        getBaby();
//        radioGroups = (RadioGroup) findViewById(R.id.main_radiogroups);
//
//        fragments.add(new MessageFragment());
//        fragments.add(new PhotoFragment());
//        fragments.add(new VideoFragment());
//        fragments.add(new RecordFragment());
//        fragments.add(new PictureFragment());
//
//
//        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content,  radioGroups);
//        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
//            @Override
//            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
//                super.OnRgsExtraCheckedChanged(radioGroup, checkedId, index);
//
//            }
//        });


    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);
        slideMenu = (SlideMenu) findViewById(R.id.slide_menu);

        user = (TextView) slideMenu.findViewById(R.id.leftmenu_user);
        growup = (TextView) slideMenu.findViewById(R.id.leftmenu_growup);
        message = (TextView) slideMenu.findViewById(R.id.leftmenu_message);
        photoAlbum = (TextView) slideMenu.findViewById(R.id.leftmenu_photo_album);
        schoolCar = (TextView) slideMenu.findViewById(R.id.leftmenu_school_car);
        addressBook = (TextView) slideMenu.findViewById(R.id.leftmenu_address_book);
        yuyingInfo = (TextView) slideMenu.findViewById(R.id.leftmenu_info);
        callName = (TextView) slideMenu.findViewById(R.id.leftmenu_callname);
        setting = (TextView) slideMenu.findViewById(R.id.leftmenu_setting);
        growingManager = (Spinner) slideMenu.findViewById(R.id.growing_manager_spinner);

        listView = (ContentListView) slideMenu.findViewById(R.id.index_pull_refresh_lsv);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);

        user.setOnClickListener(this);
        growup.setOnClickListener(this);
        message.setOnClickListener(this);
        photoAlbum.setOnClickListener(this);
        schoolCar.setOnClickListener(this);
        addressBook.setOnClickListener(this);
        yuyingInfo.setOnClickListener(this);
        callName.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftbutton:
                if (slideMenu.isMainScreenShowing()) {
                    slideMenu.openMenu();
                } else {
                    slideMenu.closeMenu();
                }
                break;
            case R.id.leftmenu_user://用户

                break;
            case R.id.leftmenu_growup://成长记录
                if (slideMenu.isMainScreenShowing()) {
                    slideMenu.openMenu();
                } else {
                    slideMenu.closeMenu();
                }
                break;
            case R.id.leftmenu_message://交互信息
                Intent jiaohu = new Intent(MainActivity.this, JiaohuActivity.class);
                startActivity(jiaohu);
                break;
            case R.id.leftmenu_photo_album://班级相册
                Intent photo = new Intent(this, PhotosActivity.class);
                photo.putExtra("uid", uid);
                startActivity(photo);
                break;
            case R.id.leftmenu_school_car://校车通知
                Intent schoolbus = new Intent(MainActivity.this, SchoolBusActivity.class);
                startActivity(schoolbus);
                break;
            case R.id.leftmenu_address_book://通讯簿

                break;
            case R.id.leftmenu_info://育英信息
                Intent yuying = new Intent(MainActivity.this, YuyingMessageActivity.class);
                startActivity(yuying);
                break;
            case R.id.leftmenu_callname://点名
                Intent dianming = new Intent(this, DianmingActivity.class);
                startActivity(dianming);
                break;

            case R.id.leftmenu_setting://设置
                Intent setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(setting);
                break;
        }
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {

    }

    private void getData(final int tag){
        String uri = String.format(InternetURL.GROWING_MANAGER_API+"?uid=%s&pageIndex=%d&pageSize=%d&child_id=%d",uid, pageIndex, pageSize, child_id);
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            GrowingDATA data = gson.fromJson(s, GrowingDATA.class);
                            if (data.getData().size() < 10){
                                listView.setResultSize(0);
                            }
                            switch (tag){
                                case ContentListView.REFRESH://刷新
                                    listView.onRefreshComplete();
                                    growingList.clear();
                                    break;
                                case ContentListView.LOAD://加载更多
                                    listView.onLoadComplete();
                                    break;
                            }
                            growingList.addAll(data.getData());
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

    private void getBaby(){
        String uid = account.getUid();
        String uri = String.format(InternetURL.GET_BABY_URL +"?uid=%s", uid);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try{
//                            BabyDATA data = gson.fromJson(s, BabyDATA.class);
//                            babies.addAll(data.getData());
                            List<String> names = new ArrayList<String>();
                            for (int i=0; i<babies.size()+1; i++){
                                if (i==0){
                                    names.add("成长管理");
                                }else {
                                    names.add(babies.get(i-1).getName());
                                }
                            }
                            spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, names);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            growingManager.setAdapter(spinnerAdapter);
                            growingManager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        mRequestQueue.add(request);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        pageIndex = 1;
        getData(ContentListView.REFRESH);
    }

    //上拉加载
    @Override
    public void onLoad() {
        pageIndex++;
        getData(ContentListView.LOAD);
    }
}
