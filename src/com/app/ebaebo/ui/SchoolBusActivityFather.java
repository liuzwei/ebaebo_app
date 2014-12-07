package com.app.ebaebo.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ebaebo.EbaeboApplication;
import com.app.ebaebo.R;
import com.app.ebaebo.data.DianmingDATA;
import com.app.ebaebo.data.TraceDATA;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Trace;
import com.app.ebaebo.util.CommonUtil;
import com.app.ebaebo.util.InternetURL;
import com.app.ebaebo.util.StringUtil;
import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.map.BaiduMap.OnMapDrawFrameCallback;
import com.baidu.mapapi.model.LatLng;
import com.baidu.nplatform.comapi.map.MapController;
import com.baidu.pplatform.comapi.basestruct.GeoPoint;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/14
 * Time: 8:33
 * 类的功能、说明写在此处.
 */
public class SchoolBusActivityFather extends BaseActivity  implements OnMapDrawFrameCallback, View.OnClickListener{
    private ImageView schoolbusback;
    private TextView shoolbusinstance;//车辆距离
    private static final String LTAG = SchoolBusActivityFather.class.getSimpleName();
    // 地图相关
    MapView mMapView;
    BaiduMap mBaiduMap;
    private List<LatLng> latLngPolygon  = new ArrayList<LatLng>();
    private List<Trace> listDw  = new ArrayList<Trace>();
    private float[] vertexs;
    private FloatBuffer vertexBuffer;
    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d(LTAG, "action: " + s);

            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Toast.makeText(mContext, "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置", Toast.LENGTH_SHORT).show();
            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Toast.makeText(mContext, "网络出错", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private SDKReceiver mReceiver;
    //定位
    private LocationClient mLocationClient;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor="gcj02";
    private BMapManager mBMapManager;
    private MapController mMapController = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
        setContentView(R.layout.shoolbusfather);
        initView();
        getData();
    }

    public void MydrawPointStart(Double lat, Double lng){
        //定义Maker坐标点
        LatLng point = new LatLng(lat,lng);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.startbutton);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }
    public void MydrawPointEnd(Double lat, Double lng){
        //定义Maker坐标点
        LatLng point = new LatLng(lat,lng);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.stopbutton);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

    }
    private void initView() {
        schoolbusback = (ImageView) findViewById(R.id.schoolbusbackfather);
        schoolbusback.setOnClickListener(this);
        shoolbusinstance = (TextView) findViewById(R.id.shoolbusinstance);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapDrawFrameCallback(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.schoolbusbackfather:
                finish();
                break;
        }
    }
    private void getData(){
        Account account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        if (account != null) {
            String uri = String.format(InternetURL.GET_LOCATION_URL + "?uid=%s&class_id=%s", account.getUid(), account.getClass_id());
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (CommonUtil.isJson(s)){
                                TraceDATA data = getGson().fromJson(s, TraceDATA.class);
                                listDw = data.getData();
                                if(listDw!=null){
                                    for(int i =0;i<listDw.size();i++){
                                        Trace trace=listDw.get(i);
//                                        LatLng latlng = new LatLng(Double.parseDouble(trace.getLat()) , Double.parseDouble(trace.getLng()));
                                        LatLng latlng = new LatLng( 24.956247+i,121.514817+i);
                                        latLngPolygon.add(latlng);
                                    }
                                }
                                //开始处理数据
                                if(latLngPolygon!=null &&latLngPolygon.size()>0){
                                    //绘制起点
                                    MydrawPointStart(latLngPolygon.get(0).latitude, latLngPolygon.get(0).longitude);
                                    //绘制终点
                                    MydrawPointEnd(latLngPolygon.get(latLngPolygon.size() - 1).latitude, latLngPolygon.get(latLngPolygon.size() - 1).longitude);
                                    //获得距离
                                    Double instant = getInstant(latLngPolygon);
                                    String strLong = String.valueOf(instant/1000);
                                    if(strLong.length()>6){
                                        strLong = strLong.substring(0,6);
                                    }
                                    shoolbusinstance.setText("距离  "+ strLong +"公里");
                                }
                                //定位到当期位置
                                LatLng ll = new LatLng(latLngPolygon.get(0).latitude, latLngPolygon.get(0).longitude);
                                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                                mBaiduMap.setMapStatus( u );

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

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        // onResume 纹理失效
        textureId = -1;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void onMapDrawFrame(GL10 gl, MapStatus drawingMapStatus) {
        if (mBaiduMap.getProjection() != null) {
            calPolylinePoint(drawingMapStatus);
            drawPolyline(gl, Color.argb(255, 255, 0, 0), vertexBuffer, 10, 3,
                    drawingMapStatus);
        }
    }

    public void calPolylinePoint(MapStatus mspStatus) {
        PointF[] polyPoints = new PointF[latLngPolygon.size()];
        vertexs = new float[3 * latLngPolygon.size()];
        int i = 0;
        for (LatLng xy : latLngPolygon) {
            polyPoints[i] = mBaiduMap.getProjection().toOpenGLLocation(xy,
                    mspStatus);
            vertexs[i * 3] = polyPoints[i].x;
            vertexs[i * 3 + 1] = polyPoints[i].y;
            vertexs[i * 3 + 2] = 0.0f;
            i++;
        }
        for (int j = 0; j < vertexs.length; j++) {
            Log.d(LTAG, "vertexs[" + j + "]: " + vertexs[j]);
        }
        vertexBuffer = makeFloatBuffer(vertexs);
    }

    private FloatBuffer makeFloatBuffer(float[] fs) {
        ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(fs);
        fb.position(0);
        return fb;
    }

    private void drawPolyline(GL10 gl, int color, FloatBuffer lineVertexBuffer,
                              float lineWidth, int pointSize, MapStatus drawingMapStatus) {

        gl.glEnable(GL10.GL_BLEND);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        float colorA = Color.alpha(color) / 255f;
        float colorR = Color.red(color) / 255f;
        float colorG = Color.green(color) / 255f;
        float colorB = Color.blue(color) / 255f;

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVertexBuffer);
        gl.glColor4f(colorR, colorG, colorB, colorA);
        gl.glLineWidth(lineWidth);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, pointSize);

        gl.glDisable(GL10.GL_BLEND);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
    int textureId = -1;
//    double mLat1 = 39.90923; // point1纬度
//    double mLon1 = 116.357428; // point1经度
//    double mLat2 = 39.90923;// point2纬度
//    double mLon2 = 116.397428;// point2经度
//    double distance = GetShortDistance(mLon1, mLat1, mLon2, mLat2);
    //获得距离
    public Double getInstant(List<LatLng> lists){
        Double countDistant=0.0;
        if(lists!=null && lists.size()>0){
            for(int i=0;i<lists.size();i++){
                if(i!=0){//只要不是第一个
                    LatLng latLng1 =  lists.get(i - 1);//前一个
                    LatLng latLng2 =  lists.get(i);//后一个
                    countDistant = countDistant + StringUtil.GetShortDistance(latLng2.longitude, latLng2.latitude, latLng1.longitude, latLng1.latitude);
                }
            }
        }
        return countDistant;
    }
}