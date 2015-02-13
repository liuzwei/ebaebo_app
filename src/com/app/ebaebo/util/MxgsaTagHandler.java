package com.app.ebaebo.util;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * author: ${zhanghailong}
 * Date: 2014/11/20
 * Time: 21:50
 * 类的功能、说明写在此处.
 */
public class MxgsaTagHandler implements TagHandler {
    private int sIndex = 0;
    private  int eIndex=0;
    private final Context mContext;

    public MxgsaTagHandler(Context context){
        mContext=context;
    }

    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.toLowerCase().equals("mxgsa")) {
            if (opening) {
                sIndex=output.length();
            }else {
                eIndex=output.length();
                output.setSpan(new MxgsaSpan(), sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
    private class MxgsaSpan extends ClickableSpan implements OnClickListener{
        @Override
        public void onClick(View widget) {
        }
    }
}
