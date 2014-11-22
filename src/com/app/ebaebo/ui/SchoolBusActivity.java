package com.app.ebaebo.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.ebaebo.R;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * author: ${zhanghailong}
 * Date: 2014/11/14
 * Time: 8:33
 * 类的功能、说明写在此处.
 */
public class SchoolBusActivity extends BaseActivity implements View.OnClickListener {
    private Toast mToast;
    private BMapManager mBMapManager;
    private MapView mMapView = null;
    private MapController mMapController = null;
    private LocationClient mLocClient;
    private LocationData mLocData;
    //定位图层
    private	LocationOverlay myLocationOverlay = null;
    private boolean isRequest = false;//是否手动触发请求定位
    private boolean isFirstLoc = true;//是否首次定位
    private PopupOverlay mPopupOverlay  = null;//弹出泡泡图层，浏览节点时使用
    private View viewCache;
    private BDLocation location;

    private ImageView schoolbusback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBMapManager = new BMapManager(this);
        mBMapManager.init("87DDC64449E5E1DC2AE735D9A28EA1311C15B2B1", new MKGeneralListenerImpl());
//        setContentView(R.layout.map_main);
        setContentView(R.layout.shoolbus);
        initView();

        //点击按钮手动请求定位
        ((Button) findViewById(R.id.request)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocation();
            }
        });

        mMapView = (MapView) findViewById(R.id.bmapView); //获取百度地图控件实例
        mMapController = mMapView.getController(); //获取地图控制器
        mMapController.enableClick(true);   //设置地图是否响应点击事件
        mMapController.setZoom(20);   //设置地图缩放级别
        mMapView.setBuiltInZoomControls(true);   //显示内置缩放控件

        viewCache = LayoutInflater.from(this).inflate(R.layout.pop_layout, null);
        mPopupOverlay = new PopupOverlay(mMapView ,new PopupClickListener() {

            @Override
            public void onClickedPopup(int arg0) {
                mPopupOverlay.hidePop();
            }
        });

        mLocData = new LocationData();

        //实例化定位服务，LocationClient类必须在主线程中声明
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(new BDLocationListenerImpl());//注册定位监听接口

        /**
         * 设置定位参数
         */
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //打开GPRS
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000); //设置发起定位请求的间隔时间为5000ms
        option.disableCache(false);//禁止启用缓存定位
//		option.setPoiNumber(5);    //最多返回POI个数
//		option.setPoiDistance(1000); //poi查询距离
//		option.setPoiExtraInfo(true);  //是否需要POI的电话和地址等详细信息

        mLocClient.setLocOption(option);
        mLocClient.start();  //	调用此方法开始定位

        //定位图层初始化
        myLocationOverlay = new LocationOverlay(mMapView);
        //设置定位数据
        myLocationOverlay.setData(mLocData);

        myLocationOverlay.setMarker(getResources().getDrawable(R.drawable.location_arrows));

        //添加定位图层
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();

        //修改定位数据后刷新图层生效
        mMapView.refresh();


    }

    private void initView() {
        schoolbusback = (ImageView) this.findViewById(R.id.schoolbusback);
        schoolbusback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.schoolbusback:
                finish();
                break;
        }
    }
    /**
     * 定位接口，需要实现两个方法
     * @author xiaanming
     *
     */
    public class BDLocationListenerImpl implements BDLocationListener {
        /**
         * 接收异步返回的定位结果，参数是BDLocation类型参数
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }

            SchoolBusActivity.this.location = location;

            mLocData.latitude = location.getLatitude();
            mLocData.longitude = location.getLongitude();
            //如果不显示定位精度圈，将accuracy赋值为0即可
            mLocData.accuracy = location.getRadius();
            mLocData.direction = location.getDerect();

            //将定位数据设置到定位图层里
            myLocationOverlay.setData(mLocData);
            //更新图层数据执行刷新后生效
            mMapView.refresh();



            if(isFirstLoc || isRequest){
                mMapController.animateTo(new GeoPoint(
                        (int) (location.getLatitude() * 1e6), (int) (location
                        .getLongitude() * 1e6)));

                showPopupOverlay(location);

                isRequest = false;
            }

            isFirstLoc = false;
        }

        /**
         * 接收异步返回的POI查询结果，参数是BDLocation类型参数
         */
        @Override
        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    /**
     * 常用事件监听，用来处理通常的网络错误，授权验证错误等
     * @author xiaanming
     *
     */
    public class MKGeneralListenerImpl implements MKGeneralListener{

        /**
         * 一些网络状态的错误处理回调函数
         */
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                showToast("您的网络出错啦！");
            }
        }

        /**
         * 授权错误的时候调用的回调函数
         */
        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                showToast("API KEY错误, 请检查！");
            }
        }

    }

    //
    private class LocationOverlay extends MyLocationOverlay{

        public LocationOverlay(MapView arg0) {
            super(arg0);
        }

        @Override
        protected boolean dispatchTap() {
            showPopupOverlay(location);
            return super.dispatchTap();
        }

        @Override
        public void setMarker(Drawable arg0) {
            super.setMarker(arg0);
        }

    }


    private void showPopupOverlay(BDLocation location){
        TextView popText = ((TextView)viewCache.findViewById(R.id.location_tips));
        popText.setText("[我的位置]\n" + location.getAddrStr());
        mPopupOverlay.showPopup(getBitmapFromView(popText),
                new GeoPoint((int)(location.getLatitude()*1e6), (int)(location.getLongitude()*1e6)),
                10);
    }

    /**
     * 手动请求定位的方法
     */
    public void requestLocation() {
        isRequest = true;

        if(mLocClient != null && mLocClient.isStarted()){
            showToast("正在定位......");
            mLocClient.requestLocation();
        }else{
            Log.d("LocSDK3", "locClient is null or not started");
        }
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.destroy();
        if(mBMapManager != null){
            mBMapManager.destroy();
            mBMapManager = null;
        }
        //退出时销毁定位
        if (mLocClient != null){
            mLocClient.stop();
        }

        super.onDestroy();
    }

    private void showToast(String msg){
        if(mToast == null){
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }else{
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
