package com.nicstrong.android.ui;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.inject.Inject;
import com.madgag.android.listviews.ViewHolder;
import com.madgag.android.listviews.ViewHolderFactory;
import com.madgag.android.listviews.ViewHoldingListAdapter;
import com.nicstrong.android.drive.DriveAccount;
import com.nicstrong.android.drive.DriveAccountManager;
import com.nicstrong.googledrivebrowser.R;
import com.nicstrong.android.util.InjectableAsyncLoader;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST;
import static com.madgag.android.listviews.ViewInflator.viewInflatorFor;

public class HomeActivity extends RoboSherlockFragmentActivity implements LoaderManager.LoaderCallbacks<List<DriveAccount>>, ActionBar.OnNavigationListener {
    private static final Logger logger = Logger.getLogger(HomeActivity.class.getName());

    private static final int REQUEST_CODE_GOOGLE_AUTH = 1;
    private static final int REQUEST_CODE_PLAY_SERVICES_ERR = 2;

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

        accountManager.setCurrentAccount(currentAccount);

        tryAuthenticate();

        return false;
    }

    private void tryAuthenticate() {
        GoogleAuthAsyncTask task = new GoogleAuthAsyncTask(this);
        task.execute(accountManager.getCurrentAccount());
    }

    public class GoogleAuthAsyncTask extends AsyncTask<DriveAccount, Void, Exception> {
        private Context context;

        public GoogleAuthAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Exception doInBackground(DriveAccount... params) {
            DriveAccount account = params[0];

            if (account != null && account.hasAccessToken()) {
                GoogleAuthUtil.invalidateToken(context, account.getAccessToken());
            }

            try {
                account.setAccessToken(GoogleAuthUtil.getToken(context, account.getName(), account.getScope()));
            } catch (IOException e) {
                return e;
            } catch (GoogleAuthException e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception ex) {
            if (ex != null) {
                if (ex instanceof GooglePlayServicesAvailabilityException) {
                    GooglePlayServicesAvailabilityException playEx = (GooglePlayServicesAvailabilityException) ex;
                    PlayServicesErrorDialogFragment.show(HomeActivity.this, playEx.getConnectionStatusCode(),
                            REQUEST_CODE_PLAY_SERVICES_ERR);
                    return;
                }

                if (ex instanceof UserRecoverableAuthException) {
                    UserRecoverableAuthException userAuthEx = (UserRecoverableAuthException) ex;
                    HomeActivity.this.startActivityForResult(userAuthEx.getIntent(),
                            REQUEST_CODE_GOOGLE_AUTH);
                    return;
                }

                logger.log(Level.SEVERE, "Play Services Error", ex);

                if (ex instanceof IOException) {
                    final Resources resources = HomeActivity.this.getResources();
                    MessageDialogFragment.show(HomeActivity.this, resources.getString(R.string.network_error_title),
                            resources.getString(R.string.network_error_message));
                    return;
                }

                if (ex instanceof IOException) {
                    final Resources resources = HomeActivity.this.getResources();
                    MessageDialogFragment.show(HomeActivity.this, resources.getString(R.string.network_error_title),
                            resources.getString(R.string.network_error_message));
                    return;
                }

                if (ex instanceof GoogleAuthException) {
                    final Resources resources = HomeActivity.this.getResources();
                    MessageDialogFragment.show(HomeActivity.this, resources.getString(R.string.unknown_play_services_error_title),
                            resources.getString(R.string.unknown_play_services_error_title));
                    return;
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new DriveFolderFragment(), "loading")
                        .commit();
            }
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

