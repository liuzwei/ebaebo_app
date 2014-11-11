package com.app.ebaebo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.ebaebo.R;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class PhotoFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.photo_fragment, container, false);

        return view;
    }
}
