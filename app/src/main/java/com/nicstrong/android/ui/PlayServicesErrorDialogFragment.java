package com.nicstrong.android.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class PlayServicesErrorDialogFragment extends RoboSherlockDialogFragment {
    private static final String TAG = "play_services_error_dialog";

    private static final String ARG_CONNECTION_STATUS_CODE = "connection_status_code";
    private static final String ARG_REQUEST_CODE = "request_code";

    public static void show(FragmentActivity activity, int connectionStatusCode, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CONNECTION_STATUS_CODE, connectionStatusCode);
        bundle.putInt(ARG_REQUEST_CODE, requestCode);

        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment current = manager.findFragmentByTag(TAG);
        if (current != null) {
            transaction.remove(current);
        }
        RoboSherlockDialogFragment fragment = new PlayServicesErrorDialogFragment();
        transaction.addToBackStack(null);
        fragment.setArguments(bundle);
        fragment.show(manager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int statusCode = getArguments().getInt(ARG_CONNECTION_STATUS_CODE);
        int resultCode = getArguments().getInt(ARG_REQUEST_CODE);
        return GooglePlayServicesUtil.getErrorDialog(statusCode, getSherlockActivity(), resultCode);
    }
}
