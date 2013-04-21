package com.nicstrong.android.ui;

import android.os.Bundle;

public interface DialogResultListener {
	void onDialogResult(int requestCode, int resultCode, Bundle arguments);
}