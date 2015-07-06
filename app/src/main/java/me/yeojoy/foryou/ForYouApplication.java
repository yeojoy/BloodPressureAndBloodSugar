package me.yeojoy.foryou;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;

import me.yeojoy.foryou.config.Consts;

/**
 * Created by yeojoy on 15. 7. 3..
 */
public class ForYouApplication extends Application implements Consts {

    private static final String TAG = ForYouApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);
        // Enable Local Datastore.
//        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
