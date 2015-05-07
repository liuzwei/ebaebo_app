package com.app.ebaebo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.app.ebaebo.EbaeboApplication;
import com.app.ebaebo.R;
import com.app.ebaebo.entity.Favours;
import com.app.ebaebo.entity.FavoursObj;
import com.app.ebaebo.entity.Growing;
import com.app.ebaebo.ui.Constants;
import com.app.ebaebo.ui.GalleryUrlActivity;
import com.app.ebaebo.ui.VideoViewActivity;
import com.app.ebaebo.util.StringUtil;
import com.app.ebaebo.widget.PictureGridview;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/16.
 */
public class GrowingAdapter extends BaseAdapter {
    private List<Growing> list;
    private Context context;
    private ViewHolder viewHolder;
//    private RoundImagePhoto roundImagePhoto;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public GrowingAdapter(List<Growing> list, Context context){
        this.list = list;
        this.context = context;
//        roundImagePhoto = new RoundImagePhoto(context);
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
            viewHolder.redHeart = (ImageView) convertView.findViewById(R.id.red_heart);
            viewHolder.gridView = (PictureGridview) convertView.findViewById(R.id.growing_item_gridview);
            viewHolder.iv2 = (ImageView) convertView.findViewById(R.id.iv2);
            viewHolder.home_photo_item_photo_video = (ImageView) convertView.findViewById(R.id.home_photo_item_photo_video);
            viewHolder.playVideo = (ImageView) convertView.findViewById(R.id.growing_item_play_video);
            viewHolder.favoursLayout = (LinearLayout) convertView.findViewById(R.id.growing_item_favours_detail);
            viewHolder.favoursNum = (TextView) convertView.findViewById(R.id.growing_item_favours_num);
            viewHolder.commentLayout = (LinearLayout) convertView.findViewById(R.id.growing_item_comment_list);
            viewHolder.liner_record = (LinearLayout) convertView.findViewById(R.id.liner_record);
            viewHolder.video = (RelativeLayout) convertView.findViewById(R.id.video);
            viewHolder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.video.setVisibility(View.GONE);
        viewHolder.liner_record.setVisibility(View.GONE);
        viewHolder.gridView.setVisibility(View.GONE);

        final Growing growing = list.get(position);
        viewHolder.publisher.setText(growing.getPublisher());
        viewHolder.time.setText(growing.getTime());
        imageLoader.displayImage(growing.getPublisher_cover(), viewHolder.photo, EbaeboApplication.txOptions, animateFirstListener);
        if ("1".equals(growing.getIs_favoured())){
            viewHolder.redHeart.setImageDrawable(context.getResources().getDrawable(R.drawable.red_favours));
        }else {
            viewHolder.redHeart.setImageDrawable(context.getResources().getDrawable(R.drawable.favours));
        }
        if(!StringUtil.isNullOrEmpty(growing.getDept())){
            viewHolder.content.setVisibility(View.VISIBLE);
        }
        viewHolder.content.setText(growing.getDept());

        //判断有没有收藏过
        if ("1".equals(growing.getIs_favoured())){
            FavoursObj favoursObj = growing.getFavours();
            viewHolder.favoursLayout.setVisibility(View.VISIBLE);
            viewHolder.favoursNum.setText(favoursObj.getCount() + "");
            List<Favours> favoursList = favoursObj.getList();
            for (Favours favours : favoursList){
                if (viewHolder.favoursLayout.getChildCount()>=2) {
                    viewHolder.favoursLayout.removeViewAt(2);
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setHorizontalGravity(LinearLayout.HORIZONTAL);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ImageView imageView = new ImageView(context);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
                    imageLoader.displayImage(favours.getCover(), imageView, EbaeboApplication.txOptions, animateFirstListener);
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setTextSize(12);
                    textView.setText(favours.getName());
                    linearLayout.addView(imageView);
                    linearLayout.addView(textView);
                    viewHolder.favoursLayout.setGravity(Gravity.CENTER_VERTICAL);
                    viewHolder.favoursLayout.setPadding(10, 0,0,0);
                    viewHolder.favoursLayout.addView(linearLayout);
                }
            }
        }else {
            viewHolder.favoursLayout.setVisibility(View.GONE);
        }

        //添加评论
        if (growing.getComments().size()>0){
            viewHolder.commentLayout.removeAllViews();
            List<Favours> comments = growing.getComments();
            for (Favours favours : comments){
                View view = LayoutInflater.from(context).inflate(R.layout.comment_list_item,null);
                ImageView imageView = (ImageView) view.findViewById(R.id.comment_list_item_photo);
                TextView name = (TextView) view.findViewById(R.id.comment_list_item_name);
                TextView content = (TextView) view.findViewById(R.id.comment_list_item_content);
                TextView time = (TextView) view.findViewById(R.id.comment_list_item_time);
                imageLoader.displayImage(favours.getCover(), imageView, EbaeboApplication.txOptions, animateFirstListener);
                name.setText(favours.getName());
                content.setText(":"+favours.getContent());
                time.setText(favours.getTime());
                viewHolder.commentLayout.addView(view);
            }
        }else {
            viewHolder.commentLayout.removeAllViews();
        }
        switch (Integer.parseInt(growing.getType()) ){
            case 0://文字
                viewHolder.gridView.setVisibility(View.GONE);
                viewHolder.content.setText(growing.getDept());
                break;
            case 1://照片
                if (!StringUtil.isNullOrEmpty(growing.getUrl())) {
                    final String[] picUrls = growing.getUrl().split(",");
                    viewHolder.gridView.setVisibility(View.VISIBLE);
                    viewHolder.gridView.setAdapter(new ImageGridViewAdapter(picUrls, context));
                    viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(context, GalleryUrlActivity.class);
                            intent.putExtra(Constants.IMAGE_URLS, picUrls);
                            intent.putExtra(Constants.IMAGE_POSITION, position);
                            context.startActivity(intent);
                        }
                    });
                }
                break;
            case 2://视频
                viewHolder.gridView.setVisibility(View.GONE);
                viewHolder.video.setVisibility(View.VISIBLE);
                viewHolder.playVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!StringUtil.isNullOrEmpty(growing.getUrl())) {
                            Intent flvIntent = new Intent(context, VideoViewActivity.class);
                            flvIntent.putExtra(Constants.VIDEO_URL, growing.getUrl());
                            context.startActivity(flvIntent);
                        }
                    }
                });
                break;
            case 3://录音
                viewHolder.liner_record.setVisibility(View.VISIBLE);
                viewHolder.gridView.setVisibility(View.GONE);
                AnimationDrawable ad2 =  (AnimationDrawable) viewHolder.iv2.getBackground();
                if (growing.isPlay()) {
                    ad2.start();
                }else {
                    ad2.stop();
                }
                viewHolder.liner_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickContentItemListener.onClickContentItem(position, 4, growing.getUrl());
                    }
                });
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
        ImageView redHeart;

//        ImageView playRecord;
        PictureGridview gridView;
        ImageView playVideo;//播放视频按钮

        LinearLayout commentLayout;
        LinearLayout liner_record;
        LinearLayout favoursLayout;
        TextView favoursNum;//喜爱的数量
        RelativeLayout video;
        ImageView home_photo_item_photo_video;
        ImageView iv2;
        TextView tv2;

    }
}
