package com.app.ebaebo.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ebaebo.EbaeboApplication;
import com.app.ebaebo.R;
import com.app.ebaebo.entity.Pictures;
import com.app.ebaebo.entity.Yuying;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 21:33
 * 类的功能、说明写在此处.
 */
public class YuyingAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Yuying> list;
    private Context context;

    public YuyingAdapter(List<Yuying> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.yuying_item,null);
            holder.pic = (ImageView)convertView.findViewById(R.id.pic);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.detail = (ImageView) convertView.findViewById(R.id.detail);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final Yuying cell = list.get(position);
        String title  = cell.getTitle();
        if(cell.getTitle().length() > 25){
            title = cell.getTitle().substring(0,25);
        }
        holder.title.setText(title) ;
        String cont = cell.getContent();
        if(cell.getContent().length() > 40){
            cont = cell.getContent().substring(0,40);
        }
        holder.content.setText(cont);
        holder.time.setText(cell.getDateline());

        try {
            imageLoader.displayImage(cell.getPic(), holder.pic, EbaeboApplication.options, animateFirstListener);
        }catch (Exception e){
            Log.d("没有网络图片", e.getMessage());
        }
        holder.detail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, null);
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView pic;
        ImageView detail;
        TextView title;
        TextView content;
        TextView time;
    }
}
