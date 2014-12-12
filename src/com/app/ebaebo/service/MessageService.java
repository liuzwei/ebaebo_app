package com.app.ebaebo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.logging.LogRecord;

/**
 * Created by liuzwei on 2014/12/12.
 */
public class MessageService extends Service {
    private static final String TAG = MessageService.class.getSimpleName();
    private Handler handler = new Handler() ;

    private MessageBinder messageBinder = new MessageBinder();

    private String uid;
    private String friend_id;
    private String user_type;

    @Override
    public IBinder onBind(Intent intent) {
        ArrayList<String> data = intent.getStringArrayListExtra("getData");
        uid = data.get(0);
        friend_id = data.get(1);
        user_type = data.get(2);
        Log.i(TAG, "Start bind...");
        return messageBinder;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "start onCreate~~~");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e(TAG, "start onStart~~~");
        super.onStart(intent, startId);

        //1000毫秒执行一次
        handler.postDelayed(getMessage, 1000);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "start onDestroy~~~");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "start onUnbind~~~");
        return super.onUnbind(intent);
    }

    public class MessageBinder extends Binder{
         public MessageService getService(){
             return MessageService.this;
         }
    }

    private Runnable getMessage = new Runnable() {
        @Override
        public void run() {

        }
    };
}
