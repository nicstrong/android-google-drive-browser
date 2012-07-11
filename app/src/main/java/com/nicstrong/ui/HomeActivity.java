package com.nicstrong.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ArrayAdapter;
import com.actionbarsherlock.app.ActionBar;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.nicstrong.drive.DriveAccount;
import com.nicstrong.drive.DriveAccountManager;
import com.nicstrong.googledrivebrowser.R;

import java.util.List;

import static com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST;

public class HomeActivity extends RoboSherlockFragmentActivity implements LoaderManager.LoaderCallbacks<List<DriveAccount >>,ActionBar.OnNavigationListener {
	@Inject
	private DriveAccountManager accountManager;

	private List<DriveAccount> accounts;
	private ArrayAdapter<DriveAccount> accountsAdapter;

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
		configureActionBar();
	}

	@Override
	public void onLoaderReset(Loader<List<DriveAccount>> loader       ) {
	}

	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(NAVIGATION_MODE_LIST);

		accountsAdapter = new ArrayAdapter<DriveAccount>(this, R.layout.sherlock_spinner_item, accounts);
		accountsAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		getSupportActionBar().setListNavigationCallbacks(accountsAdapter, this);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}
}

