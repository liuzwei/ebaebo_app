package com.app.ebaebo.ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.app.ebaebo.R;
import com.app.ebaebo.util.PicUtil;
import com.app.ebaebo.util.imageBrowser.GalleryWidget.BasePagerAdapter;
import com.app.ebaebo.util.imageBrowser.GalleryWidget.GalleryViewPager;
import com.app.ebaebo.util.imageBrowser.GalleryWidget.UrlPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author: liuzwei
 * Date: 2014/8/27
 * Time: 17:53
 * 类的功能、说明写在此处.
 */
public class GalleryUrlActivity extends BaseActivity{
    private GalleryViewPager mViewPager;
    private String[] imageUrls;
    private TextView picSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_url);
        picSum = (TextView) findViewById(R.id.gallery_pic_sum);
        Bundle bundle = getIntent().getExtras();
        imageUrls = bundle.getStringArray(Constants.IMAGE_URLS);
        int pagerPosition = bundle.getInt(Constants.IMAGE_POSITION, 0);
        List<String> items = new ArrayList<String>();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Collections.addAll(items, imageUrls);
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items, dm.widthPixels, dm.heightPixels);
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener()
        {
            @Override
            public void onItemChange(int currentPosition)
            {
                picSum.setText(currentPosition + 1 + "/" + imageUrls.length);
            }
        });

        mViewPager = (GalleryViewPager)findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(pagerPosition);
    }

    public void download(View view){
        int i = mViewPager.getCurrentItem();
        getAppThread().execute(new PicUtil(imageUrls[i]));
        String fileName = PicUtil.getImagePath(imageUrls[i]);
        Toast.makeText(mContext, "已保存至" + fileName, Toast.LENGTH_SHORT).show();
    }
}
