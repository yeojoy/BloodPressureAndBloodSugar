package me.yeojoy.foryou;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import me.yeojoy.foryou.config.ApiKey;
import me.yeojoy.foryou.config.Consts;
import me.yeojoy.foryou.model.BloodPressure;
import me.yeojoy.foryou.model.BloodSugar;
import me.yeojoy.foryou.utils.CommonUtils;
import my.lib.MyLog;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> implements ApiKey, Consts {

    private static final String TAG = ApplicationTest.class.getSimpleName();

    private Context mContext;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();
        mContext = getApplication();

        // when you create subclass of PasreObject,
        // You should register subclass, if you want to use!
        ParseObject.registerSubclass(BloodPressure.class);
        ParseObject.registerSubclass(BloodSugar.class);

        // Add your initialization code here
        Parse.initialize(mContext, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

    public void testDateButton() {
        Button btn = new Button(mContext);
        btn.setText("2015-04-28");

        Calendar c = CommonUtils.getCalendarByString(btn);
        assertNotNull(c);

        assertEquals(2015, c.get(Calendar.YEAR));
        assertEquals(3, c.get(Calendar.MONTH));
        assertEquals(28, c.get(Calendar.DAY_OF_MONTH));
    }

    public void testInsertData() {
        MyLog.i(TAG, "testInsertData()");
        BloodPressure pressure = ParseObject.create(BloodPressure.class);
        pressure.setBloodPressureMax(128);
        pressure.setBloodPressureMin(80);
        pressure.setBloodPulse(66);

        Date date = null;

        try {
            date = new SimpleDateFormat(DATE_TIME_FORMAT).parse("2015-07-14 13:35");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        MyLog.d(TAG, new SimpleDateFormat(DATE_TIME_FORMAT).format(date));

        assertNotNull(date);
        pressure.setRegisteredDate(date);
        MyLog.i(TAG, "testInsertData() ends");
    }

    public void testQueryBloodPressureData() {
        MyLog.i(TAG, "testQueryBloodPressureData()");
        final CountDownLatch signal = new CountDownLatch(1);

        ParseQuery<BloodPressure> query = ParseQuery.getQuery(BloodPressure.class);
        query.findInBackground(new FindCallback<BloodPressure>() {
            @Override
            public void done(List<BloodPressure> list, ParseException e) {
                MyLog.i(TAG, "done()");

                assertNotNull(list);
                for (BloodPressure b : list) {
                    MyLog.d(TAG, b.toString());
                }
                assertTrue(list.size() > 0);
                signal.countDown();
            }
        });


        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MyLog.i(TAG, "testQueryBloodPressureData() ends");
    }
}