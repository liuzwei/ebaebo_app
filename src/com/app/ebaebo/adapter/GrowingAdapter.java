package com.app.ebaebo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.ebaebo.EbaeboApplication;
import com.app.ebaebo.R;
import com.app.ebaebo.entity.Growing;
import com.app.ebaebo.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by liuzwei on 2014/11/16.
 */
public class GrowingAdapter extends BaseAdapter {
    private List<Growing> list;
    private Context context;
    private ViewHolder viewHolder;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public GrowingAdapter(List<Growing> list, Context context){
        this.list = list;
        this.context = context;
    }

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
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.growing_item,null);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.growing_item_photo);
            viewHolder.publisher = (TextView) convertView.findViewById(R.id.growing_item_name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.growing_item_time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.growing_item_content);
            viewHolder.favours = (LinearLayout) convertView.findViewById(R.id.growing_item_favours);
            viewHolder.comment = (LinearLayout) convertView.findViewById(R.id.growing_item_comment);
            viewHolder.share = (ImageView) convertView.findViewById(R.id.growing_item_share);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.growing_item_picture);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Growing growing = list.get(position);
        viewHolder.publisher.setText(growing.getPublisher());
        viewHolder.time.setText(growing.getTime());
        imageLoader.displayImage(growing.getUrl(), viewHolder.photo, EbaeboApplication.txOptions, animateFirstListener);
        if (!StringUtil.isNullOrEmpty(growing.getUrl())) {
            viewHolder.picture.setVisibility(View.VISIBLE);
            imageLoader.displayImage(growing.getUrl(), viewHolder.picture, EbaeboApplication.txOptions, animateFirstListener);
        }else {
            viewHolder.picture.setVisibility(View.GONE);
        }
        //todo   type类型返回的为空
//        switch (Integer.parseInt(growing.getType()) ){
        switch (0){
            case 0://文字
                viewHolder.content.setText(growing.getDept());
                break;
            case 1://照片

                break;
            case 2://视频

                break;
        }

        viewHolder.favours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, null);
            }
        });
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 2, null);
            }
        });
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 3, null);
            }
        });

        return convertView;
    }

    class ViewHolder{
        ImageView photo;
        TextView publisher;
        TextView time;
        TextView content;
        LinearLayout favours;//收藏
        LinearLayout comment;//评论
        ImageView share;//分享
        ImageView picture;
    }
}
