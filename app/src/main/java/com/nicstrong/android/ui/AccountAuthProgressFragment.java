package com.nicstrong.android.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.nicstrong.googledrivebrowser.R;

public class AccountAuthProgressFragment extends SherlockFragment {
    private static final int TRY_AGAIN_DELAY_MILLIS = 7 * 1000; // 7 seconds

    private final Handler mHandler = new Handler();

    public AccountAuthProgressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_login_loading, container, false);

        final View takingAWhilePanel = rootView.findViewById(R.id.taking_a_while_panel);
        final View tryAgainButton = rootView.findViewById(R.id.retry_button);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isAdded()) {
                    return;
                }

                takingAWhilePanel.setVisibility(View.VISIBLE);
            }
        }, TRY_AGAIN_DELAY_MILLIS);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((HomeActivity) getActivity()).cancelAuth = true;
    }
}

