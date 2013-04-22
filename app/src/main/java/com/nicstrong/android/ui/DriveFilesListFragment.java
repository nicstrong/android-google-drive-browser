package com.nicstrong.android.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.nicstrong.googledrivebrowser.R;

public class DriveFilesListFragment extends SherlockFragment {
    public static final String ARG_PARENT_FILE_ID = "parent_file_id";

    public DriveFilesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_drive_files_list, container, false);

        return rootView;
    }


}

