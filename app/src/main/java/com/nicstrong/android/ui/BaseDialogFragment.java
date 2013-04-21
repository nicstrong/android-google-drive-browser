package com.nicstrong.android.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockDialogFragment;

public abstract class BaseDialogFragment extends RoboSherlockDialogFragment implements DialogInterface.OnClickListener {
    protected static final String ARG_TITLE = "title";
    protected static final String ARG_MESSAGE = "message";
    protected static final String ARG_REQUEST_CODE = "requestCode";
    protected static final String ARG_ICON_ATTRIBUTE = "iconAttribute";

    protected static <T extends android.support.v4.app.FragmentActivity>
    void show(T activity, BaseDialogFragment fragment, Bundle arguments, String tag) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment current = manager.findFragmentByTag(tag);
        if (current != null) {
            transaction.remove(current);
        }
        transaction.addToBackStack(null);
        fragment.setArguments(arguments);
        fragment.show(manager, tag);
    }

    protected static Bundle createArguments(final String title, final String message, final int requestCode, final int iconAttribute) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_REQUEST_CODE, requestCode);
        arguments.putString(ARG_TITLE, title);
        arguments.putString(ARG_MESSAGE, message);
        if (iconAttribute != Integer.MIN_VALUE) {
            arguments.putInt(ARG_ICON_ATTRIBUTE, iconAttribute);
        }
        return arguments;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

        ((DialogResultListener) getActivity()).onDialogResult(getArguments().getInt(ARG_REQUEST_CODE), Activity.RESULT_CANCELED, getArguments());
    }

    protected AlertDialog.Builder createDefaultBuilder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        if (getArguments().containsKey(ARG_ICON_ATTRIBUTE)) {
            builder.setIconAttribute(getArguments().getInt(ARG_ICON_ATTRIBUTE));
        }
        builder.setTitle(getArguments().getString(ARG_TITLE));
        builder.setMessage(getArguments().getString(ARG_MESSAGE));
        builder.setCancelable(true);
        builder.setOnCancelListener(this);
        return builder;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}

