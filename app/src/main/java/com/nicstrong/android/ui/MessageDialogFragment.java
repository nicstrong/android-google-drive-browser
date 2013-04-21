
package com.nicstrong.android.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import static android.app.Activity.RESULT_OK;

public class MessageDialogFragment extends BaseDialogFragment implements OnClickListener {

    private static final String TAG = "message_dialog";

    public static <T extends android.support.v4.app.FragmentActivity> void show(final T activity, final String title, final String message) {
        show(activity, Integer.MIN_VALUE, title, message, Integer.MIN_VALUE, null);
    }

	public static <T extends android.support.v4.app.FragmentActivity> void show(final T activity,
	                                                                            int requestCode, final String title, final String message) {
		show(activity, requestCode, title, message, Integer.MIN_VALUE, null);
	}

	public static <T extends android.support.v4.app.FragmentActivity> void show(final T activity,
	                                                                            int requestCode, final String title, final String message, int iconAttribute) {
		show(activity, requestCode, title, message, iconAttribute, null);
	}

    public static <T extends android.support.v4.app.FragmentActivity> void
    show(final T activity,
         final int requestCode, final String title, final String message, final int iconAttribute,
            final Bundle bundle) {
        Bundle arguments = createArguments(title, message, requestCode, iconAttribute);
        if (bundle != null)
            arguments.putAll(bundle);
        show(activity, new MessageDialogFragment(), arguments, TAG);
    }

    public Dialog onCreateDialog(final Bundle savedInstanceState) {
	    AlertDialog.Builder builder = createDefaultBuilder();
	    builder.setPositiveButton(android.R.string.ok, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.dismiss();
	    if (getActivity() instanceof DialogResultListener) {
	        ((DialogResultListener) getActivity()).onDialogResult(getArguments().getInt(ARG_REQUEST_CODE), RESULT_OK, getArguments());
	    }
    }
}
