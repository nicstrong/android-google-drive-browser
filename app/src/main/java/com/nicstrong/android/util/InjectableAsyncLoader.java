package com.nicstrong.android.util;

import android.content.Context;
import com.google.inject.Inject;
import roboguice.RoboGuice;
import roboguice.inject.ContextScope;

public abstract class InjectableAsyncLoader<T> extends AsyncLoader<T> {

	@Inject
	private ContextScope contextScope;

	public InjectableAsyncLoader(final Context context) {
		super(context);

		RoboGuice.injectMembers(context, this);
	}


	@Override
	public T loadInBackground() {
		contextScope.enter(getContext());
		try {
			return load();
		} finally {
			contextScope.exit(getContext());
		}
	}

	public abstract T load();
}
