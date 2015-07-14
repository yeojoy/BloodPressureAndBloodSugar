package me.yeojoy.foryou;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.urqa.clientinterface.URQAController;

import me.yeojoy.foryou.config.ApiKey;
import me.yeojoy.foryou.model.BloodPressure;
import me.yeojoy.foryou.model.BloodSugar;

/**
 * Created by yeojoy on 15. 7. 3..
 *
 * You need an ApiKey interface, it has api keys of Parse and URQA.
 */
public class ForYouApplication extends Application implements ApiKey {

    private static final String TAG = ForYouApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // when you create subclass of PasreObject,
        // You should register subclass, if you want to use!
        ParseObject.registerSubclass(BloodPressure.class);
        ParseObject.registerSubclass(BloodSugar.class);

        // Add your initialization code here
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        URQAController.InitializeAndStartSession(this, URQA_API_KEY);
    }
}
