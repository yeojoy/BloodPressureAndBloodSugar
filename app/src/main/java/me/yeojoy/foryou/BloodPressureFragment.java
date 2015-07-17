package me.yeojoy.foryou;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import me.yeojoy.foryou.adapter.BloodPressureAdapter;
import me.yeojoy.foryou.config.ParseConsts;
import me.yeojoy.foryou.model.BloodPressure;
import me.yeojoy.foryou.view.DividerItemDecoration;
import me.yeojoy.library.log.MyLog;

/**
 * A placeholder fragment containing a simple view.
 */
public class BloodPressureFragment extends Fragment implements ParseConsts {

    private static final String TAG = BloodPressureFragment.class.getSimpleName();

    public static final String VIEW_TAG = "혈압";

    private Context mContext;

    private RecyclerView mRvBloodPressure;

    private TextView mTvEmptyData;

    private List<BloodPressure> mBloodPressureDataList;

    private BloodPressureAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blood_pressure, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout llHeader = (LinearLayout) view.findViewById(R.id.ll_header);

        mRvBloodPressure = (RecyclerView) view.findViewById(R.id.rv_blood_pressure);
        mRvBloodPressure.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        mTvEmptyData = (TextView) view.findViewById(R.id.tv_empty_data);

        mAdapter = new BloodPressureAdapter(mContext, null);
        mRvBloodPressure.setAdapter(mAdapter);
        mRvBloodPressure.setLayoutManager(new LinearLayoutManager(mContext));

        setHeader(llHeader);
    }

    @Override
    public void onResume() {
        super.onResume();

//        showBloodPressureData();

        SelectAsyncTask task = new SelectAsyncTask();
        task.execute();
    }

    public void setHeader(LinearLayout header) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.row_blood_pressure, null);
        ((TextView) layout.findViewById(R.id.tv_date)).setText(R.string.label_blood_date);
        ((TextView) layout.findViewById(R.id.tv_time)).setText(R.string.label_blood_time);
        ((TextView) layout.findViewById(R.id.tv_blood_pressure_max)).setText(R.string.label_blood_pressure_max);
        ((TextView) layout.findViewById(R.id.tv_blood_pressure_min)).setText(R.string.label_blood_pressure_min);
        ((TextView) layout.findViewById(R.id.tv_blood_pulse)).setText(R.string.label_blood_pulse);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        header.addView(layout, params);
    }

    private void showBloodPressureData() {
        ParseQuery<BloodPressure> query = ParseQuery.getQuery(BloodPressure.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<BloodPressure>() {
            @Override
            public void done(List<BloodPressure> list, ParseException e) {
                if (list == null || list.size() < 1) {
                    MyLog.d(TAG, e.getMessage());
                    return;
                }

                displayData(list);
            }
        });
    }

    private void displayData(final List<BloodPressure> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (list == null || list.size() < 1) {
                    MyLog.e(TAG, "no list.");

                    mTvEmptyData.setVisibility(View.VISIBLE);
                    return;
                }

                mBloodPressureDataList = list;
                mAdapter.setBloodPressureList(list);

                mTvEmptyData.setVisibility(View.GONE);
            }
        });
    }

    private class SelectAsyncTask extends AsyncTask<Void, Void, List<BloodPressure>> {

        @Override
        protected List<BloodPressure> doInBackground(Void... params) {
            ParseQuery<BloodPressure> query = ParseQuery.getQuery(BloodPressure.class);
            query.orderByDescending(PARSE_COMMON_COLUMN_REGISTERED_AT);

            List<BloodPressure> list = null;
            try {
                list = query.find();
                if (list != null)
                    ParseObject.pinAll(PARSE_BLOOD_PRESSURE_TABLE, list);
            } catch (ParseException e) {
                MyLog.d(TAG, e.getMessage());
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<BloodPressure> data) {
            super.onPostExecute(data);

            if (data == null || data.size() < 1) {
                MyLog.e(TAG, "no data.");

                mTvEmptyData.setVisibility(View.VISIBLE);
                return;
            }

            mBloodPressureDataList = data;
            mAdapter.setBloodPressureList(data);

            mTvEmptyData.setVisibility(View.GONE);
        }
    }


}
