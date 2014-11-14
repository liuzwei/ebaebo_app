package com.app.ebaebo.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.app.ebaebo.R;
import com.app.ebaebo.adapter.OnClickContentItemListener;
import com.app.ebaebo.adapter.PhotoAdapter;
import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Photos;
import com.app.ebaebo.entity.Pictures;
import com.app.ebaebo.util.HttpUtils;
import com.app.ebaebo.util.ShellContext;
import com.app.ebaebo.widget.ContentListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 21:24
 * 类的功能、说明写在此处.
 */
public class PhotosActivity extends BaseActivity implements OnClickContentItemListener, View.OnClickListener {
    private ListView clv;
    private PhotoAdapter adapter;
    private List<Photos> list = new ArrayList<Photos>();
    private ProgressDialog pd = null;
    private Map<String,String> map = new HashMap<String,String>();
    Account account = (Account) ShellContext.getAttribute(ShellContext.ACCOUNT);
    private ImageView photosback;//返回按钮
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pd.dismiss();
            switch (msg.what) {
                case Constants.SUCCESS:
                    adapter.notifyDataSetChanged();
                    break;
                case Constants.FAIL:
                    alert(R.string.error1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);
        initView();
        setTheme(R.style.index_theme);
        adapter = new PhotoAdapter(list, this);
        clv.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
        if(account != null){
//            map.put("uid", account.getUid());
            map.put("uid", "41");
            loadData();
        }

    }

    private void initView() {
        clv = (ListView) this.findViewById(R.id.lstv);
        photosback = (ImageView) this.findViewById(R.id.photosback);
        photosback.setOnClickListener(this);
    }

    public void loadData()
    {
        getAppThread().execute(new Runnable() {
            @Override
            public void run() {
                String result  = HttpUtils.postRequest("http://yey.xqb668.com/json.php/sclass.api-albumlist/", map);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if(code.equals("200")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if(jsonArray!=null && jsonArray.length()>0){
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
                                String id = jsonObject2.getString("id");
                                String name = jsonObject2.getString("name");
                                List<Pictures> pictures = new ArrayList<Pictures>();
//                                pictures.add()
                                String pic = jsonObject2.getString("pic");
                                list.add(new Photos(id, name, pictures ));
//                                jsonObject2.get
//                                infos.add(new Info(jsonObject2.getString("property_id"),jsonObject2.getString("property")));
                            }
                            mHandler.sendMessage(mHandler.obtainMessage(Constants.SUCCESS));
                        }
                        mHandler.sendMessage(mHandler.obtainMessage(Constants.FAIL));
                    }else{
                        mHandler.sendMessage(mHandler.obtainMessage(Constants.FAIL));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(Constants.FAIL));
                }
            }
        });
    }

    private int pos = 0;
    private Photos photo;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        pos = position;
        photo = list.get(position);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.photosback:
                finish();
                break;
        }
    }
}
