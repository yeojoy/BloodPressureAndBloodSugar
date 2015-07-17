package me.yeojoy.foryou;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
import me.yeojoy.library.log.MyLog;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> implements ApiKey, Consts {

    private static final String TAG = ApplicationTest.class.getSimpleName();

    private Context mContext;

    private String mObjectId = null;

    private Date mDate;

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

        mDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse("2015-07-14 13:35");
    }

    public void testDateButton() {
        MyLog.i(TAG);
        Button btn = new Button(mContext);
        btn.setText("2015-04-28");

        Calendar c = CommonUtils.getCalendarByString(btn);
        assertNotNull(c);

        assertEquals(2015, c.get(Calendar.YEAR));
        assertEquals(3, c.get(Calendar.MONTH));
        assertEquals(28, c.get(Calendar.DAY_OF_MONTH));
        MyLog.i(TAG, "ends.");
    }

    public void testInsertData() {
        MyLog.i(TAG);
        BloodPressure pressure = ParseObject.create(BloodPressure.class);
        pressure.setBloodPressureMax(128);
        pressure.setBloodPressureMin(80);
        pressure.setBloodPulse(66);

        try {
            mDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse("2015-07-14 13:35");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        MyLog.d(TAG, new SimpleDateFormat(DATE_TIME_FORMAT).format(mDate));

        assertNotNull(mDate);
        pressure.setRegisteredDate(mDate);

        Calendar c = Calendar.getInstance();
        c.setTime(mDate);
        assertEquals(2015, c.get(Calendar.YEAR));
        // Month 는 현재 달 -1 이다. 7월이면 6임
        assertEquals(6, c.get(Calendar.MONTH));
        assertEquals(14, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(13, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(35, c.get(Calendar.MINUTE));

        final CountDownLatch signal = new CountDownLatch(1);

        pressure.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                MyLog.i(TAG);
                assertNull(e);

                signal.countDown();
            }
        });

        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MyLog.i(TAG, "ends");
    }

    public void testQueryBloodPressureData() {
        MyLog.i(TAG);
        final CountDownLatch signal = new CountDownLatch(1);

        ParseQuery<BloodPressure> query = ParseQuery.getQuery(BloodPressure.class);
        query.findInBackground(new FindCallback<BloodPressure>() {
            @Override
            public void done(List<BloodPressure> list, ParseException e) {
                MyLog.i(TAG);

                assertNotNull(list);

                for (BloodPressure b : list) {
                    MyLog.d(TAG, b.toString());
                    if (b.getRegisteredDate().getTime() == mDate.getTime()) {

                        mObjectId = b.getObjectId();
                        MyLog.d(TAG, "Object ID >>>>>>>>>>>> " + mObjectId);

                    }
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

        MyLog.i(TAG, "ends");
    }

    public void testUpdateData() throws InterruptedException {
        MyLog.i(TAG);
        if (mObjectId == null || mObjectId.isEmpty()) {
            MyLog.i(TAG, "mObjectId is null or not exists.");
            return;
        }

        final CountDownLatch signal = new CountDownLatch(2);

        ParseQuery<BloodPressure> query = ParseQuery.getQuery(BloodPressure.class);

        query.getInBackground(mObjectId, new GetCallback<BloodPressure>() {

            @Override
            public void done(BloodPressure bloodPressure, ParseException e) {
                MyLog.i(TAG);

                bloodPressure.setBloodPressureMax(111);
                bloodPressure.setBloodPressureMin(55);
                bloodPressure.setBloodPulse(44);
                bloodPressure.saveInBackground();

                signal.countDown();
            }
        });

        query.findInBackground(new FindCallback<BloodPressure>() {
            @Override
            public void done(List<BloodPressure> list, ParseException e) {

                MyLog.i(TAG);

                assertNotNull(list);
                for (BloodPressure b : list) {
                    MyLog.d(TAG, b.toString());
                }
                assertTrue(list.size() > 0);
                signal.countDown();
            }
        });

        signal.await();
    }

    public void testDeleteData() throws InterruptedException {
        MyLog.i(TAG);

        if (mObjectId == null || mObjectId.isEmpty()) {
            MyLog.i(TAG, "mObjectId is null or not exists.");
            return;
        }

        final CountDownLatch signal = new CountDownLatch(1);

        ParseQuery<BloodPressure> query = ParseQuery.getQuery(BloodPressure.class);

        query.getInBackground(mObjectId, new GetCallback<BloodPressure>() {

            @Override
            public void done(BloodPressure bloodPressure, ParseException e) {
                MyLog.i(TAG);
                if (bloodPressure != null)
                    bloodPressure.deleteInBackground();

                signal.countDown();
            }
        });

        signal.await();
    }
}