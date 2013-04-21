package com.nicstrong.android.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class ConfirmDialogFragment extends BaseDialogFragment implements OnClickListener {

    private static final String TAG = "confirm_dialog";

    public static <T extends android.support.v4.app.FragmentActivity & DialogResultListener> void show(final T activity,
            final int requestCode, final String title, final String message) {
        show(activity, requestCode, title, message, null);
    }

    public static <T extends android.support.v4.app.FragmentActivity & DialogResultListener> void show(final T activity,
            final int requestCode, final String title, final String message,
            final Bundle bundle) {
        Bundle arguments = createArguments(title, message, requestCode, Integer.MIN_VALUE);
        if (bundle != null)
            arguments.putAll(bundle);
        show(activity, new ConfirmDialogFragment(), arguments, TAG);
    }

    public Dialog onCreateDialog(final Bundle savedInstanceState) {
	    AlertDialog.Builder builder = createDefaultBuilder();

	    builder.setPositiveButton(android.R.string.yes, this);
	    builder.setNegativeButton(android.R.string.no, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        switch (which) {
        case BUTTON_POSITIVE:

	        ((DialogResultListener) getActivity()).onDialogResult(getArguments().getInt(ARG_REQUEST_CODE), RESULT_OK, getArguments());
	        break;
        case BUTTON_NEGATIVE:

	        ((DialogResultListener) getActivity()).onDialogResult(getArguments().getInt(ARG_REQUEST_CODE), RESULT_CANCELED, getArguments());
	        break;
        }
    }
}
