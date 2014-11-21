package com.app.ebaebo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ebaebo.EbaeboApplication;
import com.app.ebaebo.R;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Message;
import com.app.ebaebo.entity.MessageData;
import com.app.ebaebo.entity.UserData;
import com.app.ebaebo.ui.Constants;
import com.app.ebaebo.util.CommonUtil;
import com.app.ebaebo.util.InternetURL;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/21.
 */
public class ChatAdapter extends BaseAdapter {
    private List<Message> list;
    private Context context;
    private ViewHolder viewHolder;
    private SharedPreferences sp;
    private static Gson gson = new Gson();
    private UserData userData;
    private static boolean isMe = true;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ChatAdapter(List<Message> list, Context context, SharedPreferences sp, UserData userData){
        this.list = list;
        this.context = context;
        this.sp = sp;
        this.userData = userData;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = list.get(position);
        Account account = gson.fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);

        if (convertView == null){
            if(message.getTo_uids().equals(account.getUid())){
                convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_text_left, null);
                isMe = true;
            }else {
                convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_text_right, null);
                isMe = false;
            }
            viewHolder = new ViewHolder();
            viewHolder.sendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.iv_userhead);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_chatcontent);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (isMe){
            viewHolder.name.setText(userData.getTo().getName());
            imageLoader.displayImage(String.format("%s%s", Constants.API_HEAD, userData.getTo().getCover()), viewHolder.photo, EbaeboApplication.txOptions, animateFirstListener);
        }else {
            viewHolder.name.setText(userData.getFrom().getName());
            imageLoader.displayImage(String.format("%s%s", Constants.API_HEAD, userData.getFrom().getCover()), viewHolder.photo, EbaeboApplication.txOptions, animateFirstListener);
        }
        viewHolder.sendTime.setText(CommonUtil.longToString(message.getDateline()));
        viewHolder.content.setText(message.getContent());
        return convertView;
    }

    class ViewHolder{
        TextView sendTime;
        ImageView photo;
        TextView name;
        TextView content;
    }
}
