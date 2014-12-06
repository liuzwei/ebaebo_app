package com.app.ebaebo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ebaebo.ActivityTack;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.GrowingAdapter;
import com.app.ebaebo.adapter.OnClickContentItemListener;
import com.app.ebaebo.data.BabyDATA;
import com.app.ebaebo.data.ErrorDATA;
import com.app.ebaebo.data.GrowingDATA;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Baby;
import com.app.ebaebo.entity.Growing;
import com.app.ebaebo.util.CommonUtil;
import com.app.ebaebo.util.InternetURL;
import com.app.ebaebo.util.PhoneEnvUtil;
import com.app.ebaebo.util.Player;
import com.app.ebaebo.widget.ContentListView;
import com.google.gson.Gson;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.ArrayList;
import java.util.LinkedList;
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
    private RelativeLayout footLayout;
    private RelativeLayout text;//文字
    private RelativeLayout photo;//拍照
    private RelativeLayout video;//摄像
    private RelativeLayout record;//录音
    private RelativeLayout picture;// 图库

    private ContentListView listView;
    private GrowingAdapter adapter;

    private String uid;
    private int pageIndex;
    private int pageSize;
    private String child_id;
    private Account account;
    private String identity;
    private Growing qGrowing;

    private List<Player> players = new LinkedList<Player>();
    private long waitTime = 2000;
    private long touchTime = 0;

    private List<Growing> growingList = new ArrayList<Growing>();
    private List<Baby> babies = new ArrayList<Baby>();//下拉列表宝宝
    private RequestQueue mRequestQueue;
    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

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
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);

        mRequestQueue = Volley.newRequestQueue(this);
        adapter = new GrowingAdapter(growingList, mContext);
        adapter.setOnClickContentItemListener(this);
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
        if(identity.equals("0")){
            user.setText("        "+account.getF_name());
        }
        if(identity.equals("1")){
            user.setText("        "+account.getM_name());
        }
        if(account.getIs_teacher().equals("1")){
            user.setText("        "+account.getNick_name());
        }

        //youmeng

        // 设置分享内容
                mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
        // 设置分享图片, 参数2为图片的url地址
                mController.setShareMedia(new UMImage(this,
                        "http://www.umeng.com/images/pic/banner_module_social.png"));
        // 设置分享图片，参数2为本地图片的资源引用
        //mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        //mController.setShareMedia(new UMImage(getActivity(),
        //                                BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

        // 设置分享音乐
        //UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
        //uMusic.setAuthor("GuGu");
        //uMusic.setTitle("天籁之音");
        // 设置音乐缩略图
        //uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
        //mController.setShareMedia(uMusic);

        // 设置分享视频
        //UMVideo umVideo = new UMVideo(
        //          "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
        // 设置视频缩略图
        //umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
        //umVideo.setTitle("友盟社会化分享!");
        //mController.setShareMedia(umVideo);

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
        footLayout = (RelativeLayout) slideMenu.findViewById(R.id.index_foot_layout);
        text = (RelativeLayout) footLayout.findViewById(R.id.foot_content);
        photo = (RelativeLayout) footLayout.findViewById(R.id.foot_photo);
        video = (RelativeLayout) footLayout.findViewById(R.id.foot_video);
        record = (RelativeLayout) footLayout.findViewById(R.id.foot_record);
        picture = (RelativeLayout) footLayout.findViewById(R.id.foot_picture);

        text.setOnClickListener(this);
        photo.setOnClickListener(this);
        video.setOnClickListener(this);
        record.setOnClickListener(this);
        picture.setOnClickListener(this);

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
                if(account.getIs_teacher().equals("1")){//是教师登陆的话
                    Intent schoolbus = new Intent(MainActivity.this, OpenglDemo.class);
                    startActivity(schoolbus);
                }else{
                    //家长登陆的话
                    Intent schoolbus = new Intent(MainActivity.this, SchoolBusActivityFather.class);
                    startActivity(schoolbus);
                }
                break;
            case R.id.leftmenu_address_book://通讯簿
                Intent txl = new Intent(MainActivity.this, TongxunluActivity.class);
                startActivity(txl);
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
            case R.id.foot_content://文字
                Intent textIntent = new Intent(MainActivity.this, PublishImageActivity.class);
                startActivity(textIntent);
                break;
            case R.id.foot_photo://拍照
                Intent takePhoto = new Intent(MainActivity.this, PublishPhotoActivity.class);
                startActivity(takePhoto);
                break;
            case R.id.foot_video://摄像
                Intent video = new Intent(MainActivity.this, PublishVideoActivity.class);
                startActivity(video);
                break;
            case R.id.foot_record://录音
                Intent record = new Intent(MainActivity.this, PublishRecordActivity.class);
                startActivity(record);
                break;
            case R.id.foot_picture://图库
                startActivity(new Intent(MainActivity.this, PublishPictureActivity.class));
                break;
        }
    }

    @Override
    public void onClickContentItem(final int position, int flag, final Object object) {
        qGrowing = growingList.get(position);

        switch (flag){
            case 1://收藏
                final Growing growing = growingList.get(position);
                String cancel;
                if ("1".equals(growing.getIs_favoured())){
                    cancel = "1";
                }else {
                    cancel = "";
                }
                String uri = String.format(InternetURL.FAVOURS_URL+"?growing_id=%s&uid=%s&user_type=%s&cancel=%s",growing.getId(), account.getUid(), identity ,cancel);
                StringRequest request = new StringRequest(
                        Request.Method.GET,
                        uri,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (CommonUtil.isJson(s)){
                                    ErrorDATA data = getGson().fromJson(s, ErrorDATA.class);
                                    if (data.getCode() == 200){
                                        if (!"1".equals(growing.getIs_favoured())){
                                            growingList.get(position).setIs_favoured("1");
                                            Toast.makeText(mContext, "已收藏", Toast.LENGTH_SHORT).show();
                                        }else {
                                            growingList.get(position).setIs_favoured("0");
                                            Toast.makeText(mContext, "已取消收藏", Toast.LENGTH_SHORT).show();
                                        }
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(mContext, "收藏失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                    }
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
                break;
            case 2://评论
                Intent comment = new Intent(MainActivity.this, CommentActivity.class);
                comment.putExtra(Constants.GROWING_KEY, growingList.get(position));
                startActivity(comment);
                break;
            case 3://分享
                final Growing grow = growingList.get(position);
//                private String dept;//文字描述
//                private String type;//0 文字 1 照片 2 视频
                 if(grow.getType().equals("0")){
                     //文字
                 }
                if(grow.getType().equals("1")){
                    //照片
                }
                if(grow.getDept().equals("2")){
                    //视频
                    UMVideo umVideo = new UMVideo(grow.getDept());
                    umVideo.setMediaUrl(grow.getUrl());
                    umVideo.setThumb(grow.getUrl());
//                    umVideo.setTitle(shareTitle+info.getJieshao());
//                    umVideo.setTargetUrl();
                    mController.setShareMedia(umVideo);
                }
                String appID = "wxd97dd1adcce2b199";
                String appSecret = "7955104817ad2d367ff337ca56e12756";
                //1.添加QQ空间分享
                QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "100582055","f1a53f9becde189f56362e8b98e22f60");
//                qZoneSsoHandler.setTargetUrl(shareUrl+shareParams);
                qZoneSsoHandler.addToSocialSDK();
                //2.添加QQ好友分享
                UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100582055","f1a53f9becde189f56362e8b98e22f60");
//                qqSsoHandler.setTargetUrl(shareUrl+shareParams);
                qqSsoHandler.addToSocialSDK();
                // 添加微信平台
                UMWXHandler wxHandler = new UMWXHandler(this,appID);
                wxHandler.addToSocialSDK();
                //支持微信朋友圈
                UMWXHandler wxCircleHandler = new UMWXHandler(this,appID);
                wxCircleHandler.setToCircle(true);
                wxCircleHandler.addToSocialSDK();
                //单独设置微信分享
                WeiXinShareContent xinShareContent = new WeiXinShareContent();
//                if(!"".equals(grow.getDept())){
//                    xinShareContent.setShareContent(grow.getDept());
//                    xinShareContent.setTitle(grow.getDept());
//                }
//                xinShareContent.setShareImage(new UMImage(this, sharePic));
//                xinShareContent.setTargetUrl(shareUrl+shareParams);
                mController.setShareMedia(xinShareContent);
                CircleShareContent circleMedia = new CircleShareContent();
//                circleMedia.setShareContent(shareCont);
//                circleMedia.setTitle(shareTitle+shareCont);
//                circleMedia.setShareImage(new UMImage(this, sharePic));
//                circleMedia.setTargetUrl(shareUrl+shareParams);
                mController.setShareMedia(circleMedia);
                mController.openShare(this, false);
                break;
            case 4://播放音乐
                final Player player = new Player();
                qGrowing.setPlay(qGrowing.isPlay() ? false : true);
                for (Growing mGrowing : growingList){
                    if (mGrowing.isPlay() && !mGrowing.getId().equals(qGrowing.getId())){
                        if (players.size() > 0){
                            players.remove(0).stop();
                        }
                    }
                    if (!mGrowing.getId().equals(qGrowing.getId())){
                        mGrowing.setPlay(false);
                    }
                }
                if (players.size() > 0){
                    players.remove(0).stop();
                    adapter.notifyDataSetChanged();
                    return;
                }

                adapter.notifyDataSetChanged();
                players.add(player);
                getAppThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        player.playUrl((String) object);
                    }
                });
                break;
        }
    }

    private void getData(final int tag){
        if (!PhoneEnvUtil.isNetworkConnected(mContext)){
            Toast.makeText(mContext, R.string.check_network_isuse, Toast.LENGTH_SHORT).show();
            return;
        }
        String uri = String.format(InternetURL.GROWING_MANAGER_API+"?uid=%s&pageIndex=%d&pageSize=%d&child_id=%s",uid, pageIndex, pageSize, child_id);
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
                            BabyDATA data = gson.fromJson(s, BabyDATA.class);
                            babies.addAll(data.getData());
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
                            growingManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {
                                        child_id = "";
                                    } else{
                                        Baby baby = babies.get(position-1);
                                        child_id = baby.getId();
                                    }
                                    getData(ContentListView.REFRESH);

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

    /**
     * 再摁退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode){
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime){
                Toast.makeText(mContext, "再摁退出登录", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            }else {
                ActivityTack.getInstanse().exit(mContext);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
