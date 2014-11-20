package com.app.ebaebo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.app.ebaebo.util.ToastUtil;
import com.google.gson.Gson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class BaseActivity extends Activity {
    public Context mContext;
    public SharedPreferences sp;
    public LayoutInflater inflater;

    private ExecutorService appThread = Executors.newSingleThreadExecutor();

    private Gson gson = new Gson();

    ConnectivityManager connectMgr ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        sp = getSharedPreferences("ebaebo", Context.MODE_PRIVATE);
        inflater = LayoutInflater.from(mContext);
        connectMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 获得线程池
     * @return
     */
    public ExecutorService getAppThread() {
        return appThread;
    }

    public Gson getGson(){
        return gson;
    }

    protected Context getContext() {
        return mContext;
    }
    /**
     * 根据资源ID
     * @param resId
     */
    public void alert(int resId){
        ToastUtil.show(getApplicationContext(), resId);
    }

    //存储sharepreference
    public void save(String key, Object value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, gson.toJson(value)).commit();
    }
}
