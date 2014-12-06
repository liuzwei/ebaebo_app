package com.app.ebaebo;


import android.app.Application;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import com.app.ebaebo.db.DBManager;
import com.app.ebaebo.entity.Message;
import com.app.ebaebo.entity.NotifMessage;
import com.app.ebaebo.ui.Constants;
import com.app.ebaebo.ui.NotificationActivity;
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.MKGeneralListener;
//import com.baidu.mapapi.map.MKEvent;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 21:54
 * 类的功能、说明写在此处.
 */
public class EbaeboApplication extends Application {
    //----------------百度地图------------------
    public LocationClient mLocationClient;
    public GeofenceClient mGeofenceClient;
    public MyLocationListener mMyLocationListener;
    public TextView mLocationResult,logMsg;
    public TextView trigger,exit;
    public Vibrator mVibrator;
    public static String dwlocation;
    //-----------------------------------
    public static DisplayImageOptions options;
    public static DisplayImageOptions txOptions;//头像图片
    public static DisplayImageOptions tpOptions;//详情页图片
    public static DisplayImageOptions adOptions;
    public static DisplayImageOptions txDetailOptions;//详情页头像图片
    public static ExecutorService lxThread = Executors.newFixedThreadPool(20);
    private DBManager dbManager;
    public static JSONArray jsonArray;//好友列表
    public static JSONArray recent_chatters;//最近联系人


    private static final String TAG = EbaeboApplication.class.getName();

    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        initImageLoader(getApplicationContext());
        MobclickAgent.updateOnlineConfig(getApplicationContext());
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        dbManager = new DBManager(getApplicationContext());

        //------百度地图---
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mGeofenceClient = new GeofenceClient(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        //------
        /**
         * 该Handler是在IntentService中被调用，故
         * 1. 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 2. IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
         * 	      或者可以直接启动Service    自定义消息
         * */
        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public Notification getNotification(Context context, UMessage msg) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time = sdf.format(new Date());
                NotifMessage message = new NotifMessage(msg.title, msg.text, msg.custom, time);
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        Notification mNotification = builder.build();
//                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        dbManager.add(message);//将消息保存到数据库
                        return mNotification;
                    default:
                        dbManager.add(message);//将消息保存到数据库
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
//        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 自定义行为内容
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Intent intent = new Intent(context, NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time = sdf.format(new Date());
                NotifMessage message = new NotifMessage(msg.title, msg.text, msg.custom, time);

                Bundle mBundle = new Bundle();
                mBundle.putSerializable(Constants.UMENG_MESSAGE, message);
                intent.putExtras(mBundle);

                startActivity(intent);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    public EbaeboApplication() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.hctp)
                .showImageForEmptyUri(R.drawable.hctp)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.hctp)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型
//                .displayer(new RoundedBitmapDisplayer(5))
                .build();									   // 创建配置过得DisplayImageOption对象

        txOptions = new DisplayImageOptions.Builder()//头像
                .showImageOnLoading(R.drawable.txhc)
                .showImageForEmptyUri(R.drawable.txhc)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.txhc)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型头像
                .build();

        adOptions = new DisplayImageOptions.Builder()//广告
                .showImageOnLoading(R.drawable.hctp1)
                .showImageForEmptyUri(R.drawable.hctp1)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.hctp1)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型广告位
                .build();

        tpOptions = new DisplayImageOptions.Builder()//图片
                .showImageOnLoading(R.drawable.hctp)
                .showImageForEmptyUri(R.drawable.hctp)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.hctp)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型图片
                .build();

    }

    /**
     * 初始化图片加载组件ImageLoader
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
            sb.append("latitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation){
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\ndirection : ");
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append(location.getDirection());
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }
    }

    /**
     * 显示请求字符串
     * @param str
     */
    public void logMsg(String str) {
        dwlocation = str;
        try {
            if (mLocationResult != null)
                mLocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 高精度地理围栏回调
     * @author jpren
     *
     */

}
