package com.app.ebaebo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ebaebo.EbaeboApplication;
import com.app.ebaebo.R;
import com.app.ebaebo.entity.Child;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/20.
 */
public class DianmingAdapter extends BaseAdapter {
    private List<Child> list;
    private Context context;
    private ViewHolder holder;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public DianmingAdapter(List<Child> list, Context context){
        this.list = list;
        this.context = context;
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
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.dianming_item, null);
            holder.photo = (ImageView) convertView.findViewById(R.id.dianming_photo);
            holder.name = (TextView) convertView.findViewById(R.id.dianming_name);
            holder.time = (TextView) convertView.findViewById(R.id.dianming_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Child child = list.get(position);
        imageLoader.displayImage(child.getCover(), holder.photo, EbaeboApplication.txOptions, animateFirstListener);
        holder.name.setText(child.getName());
        holder.time.setText(child.getDateline()+"");
        return convertView;
    }

    class ViewHolder{
        ImageView photo;
        TextView name;
        TextView time;
    }
}
