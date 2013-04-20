package com.nicstrong.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.madgag.android.listviews.ViewHolder;
import com.madgag.android.listviews.ViewHolderFactory;
import com.madgag.android.listviews.ViewHoldingListAdapter;
import com.nicstrong.drive.DriveAccount;
import com.nicstrong.drive.DriveAccountManager;
import com.nicstrong.googledrivebrowser.R;
import com.nicstrong.util.InjectableAsyncLoader;

import java.util.List;

import static com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST;
import static com.madgag.android.listviews.ViewInflator.viewInflatorFor;

public class HomeActivity extends RoboSherlockFragmentActivity implements LoaderManager.LoaderCallbacks<List<DriveAccount>>, ActionBar.OnNavigationListener {
    @Inject
    private DriveAccountManager accountManager;

    private List<DriveAccount> accounts;
    private ViewHoldingListAdapter<DriveAccount> accountsAdapter;
    public boolean cancelAuth = false;
    private DriveAccount currentAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<DriveAccount>> onCreateLoader(int id, Bundle bundle) {
        return new DriveAccountLoader(this, accountManager);
    }

    @Override
    public void onLoadFinished(Loader<List<DriveAccount>> loader, List<DriveAccount> accounts) {
        this.accounts = accounts;
        if (accountsAdapter != null) {
            accountsAdapter.setList(accounts);
        } else {
            configureActionBar();
        }

        ActionBar actionBar = getSupportActionBar();
        for (int i = 0; i < accounts.size(); i++) {
            if (accountManager.getCurrentAccount() != null &&
                    accounts.get(i).getName().equals(accountManager.getCurrentAccount().getName())) {
                actionBar.setSelectedNavigationItem(i);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<DriveAccount>> loader) {
    }

    private void configureActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(NAVIGATION_MODE_LIST);
        accountsAdapter = new ViewHoldingListAdapter<DriveAccount>(accounts, viewInflatorFor(this, android.R.layout.simple_list_item_1),
                new ViewHolderFactory<DriveAccount>() {
                    @Override
                    public ViewHolder<DriveAccount> createViewHolderFor(View view) {
                        return new DriveAccountViewHolder(view);
                    }
                    @Override
                    public Class<? extends ViewHolder<DriveAccount>> getHolderClass() {
                        return DriveAccountViewHolder.class;
                    }
                });
        getSupportActionBar().setListNavigationCallbacks(accountsAdapter, this);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            Toast.makeText(this, R.string.no_connection_cant_login, Toast.LENGTH_SHORT).show();
            return false;
        }

        cancelAuth = false;
        currentAccount = accountsAdapter.getItem(itemPosition);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, new AccountAuthProgressFragment(), "loading")
            .commit();

        tryAuthenticate();

        return false;
    }

    private void tryAuthenticate() {
        if (!currentAccount.hasAccessToken()) {

        }
    }

    public static class DriveAccountLoader extends InjectableAsyncLoader<List<DriveAccount>> {
        private final DriveAccountManager accountManager;

        public DriveAccountLoader(Context context, DriveAccountManager accountManager) {
            super(context);
            this.accountManager = accountManager;
        }

        @Override
        public List<DriveAccount> load() {
            return accountManager.getAccounts();
        }
    }

    public static class DriveAccountViewHolder implements ViewHolder<DriveAccount> {
        private final TextView name;

        public DriveAccountViewHolder(View v) {
            name = (TextView) v.findViewById(android.R.id.text1);
        }

        @Override
        public void updateViewFor(DriveAccount driveAccount) {
            name.setText(driveAccount.getName());
        }
    }
}

