package com.nicstrong.android.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.nicstrong.googledrivebrowser.R;

public class DriveFolderFragment extends SherlockFragment {
    private static final int TRY_AGAIN_DELAY_MILLIS = 7 * 1000; // 7 seconds

    private final Handler mHandler = new Handler();

    public DriveFolderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_login_loading, container, false);

        return rootView;
    }


}

