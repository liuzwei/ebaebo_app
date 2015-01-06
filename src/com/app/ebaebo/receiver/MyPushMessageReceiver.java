package com.app.ebaebo.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ebaebo.data.SuccessDATA;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.ui.Constants;
import com.app.ebaebo.ui.MainActivity;
import com.app.ebaebo.util.InternetURL;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/6.
 */
public class MyPushMessageReceiver extends FrontiaPushMessageReceiver {
    @Override
    public void onBind(Context context, int errorCode, String appid,String userId, String channelId, String requestId) {
        updateChanelId(context, channelId);
    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> strings, List<String> strings2, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> strings, List<String> strings2, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> strings, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s2) {

        Log.e("Message",s);
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s2, String s3) {
        Log.e("Message",s);
    }

    public void updateChanelId(final Context context, String chanelId){
        Account account = new Gson().fromJson(context.getSharedPreferences("ebaebo", Context.MODE_PRIVATE).getString(Constants.ACCOUNT_KEY, ""), Account.class);
        RequestQueue queue = Volley.newRequestQueue(context);
        String uri = String.format(InternetURL.CHANEL_ID+"?uid=%s&type=android&channel_id=%s&op=add", account.getUid(), chanelId);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            SuccessDATA successDATA = new Gson().fromJson(s, SuccessDATA.class);
                            if (successDATA.getCode() == 200){
                                Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
        queue.add(request);
    }
}
