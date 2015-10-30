package com.cgt.android.form.framework.application;

import android.app.Application;

import com.cgt.android.form.framework.R;
import com.parse.Parse;

/**
 * Created by kst-android on 28/10/15.
 */
public class CgtApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(getApplicationContext(), getString(R.string.parseApplicationId), getString(R.string.parseClientKey));
    }
}
