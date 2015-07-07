package me.yeojoy.foryou;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import me.yeojoy.foryou.adapter.BloodPressureAdapter;
import me.yeojoy.foryou.config.Consts;
import me.yeojoy.foryou.model.BloodPressure;

/**
 * A placeholder fragment containing a simple view.
 */
public class BloodPressureFragment extends Fragment implements Consts {

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
        mTvEmptyData = (TextView) view.findViewById(R.id.tv_empty_data);

        mAdapter = new BloodPressureAdapter(mContext, null);
        mRvBloodPressure.setAdapter(mAdapter);
        mRvBloodPressure.setLayoutManager(new LinearLayoutManager(mContext));

        setHeader(llHeader);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private class SelectAsyncTask extends AsyncTask<Void, Void, List<BloodPressure>> {

        @Override
        protected List<BloodPressure> doInBackground(Void... params) {
            ParseQuery<BloodPressure> query = ParseQuery.getQuery(BloodPressure.class);
            query.orderByDescending(PARSE_COMMON_COLUMN_REGISTERED_AT);

            try {
                return query.find();
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<BloodPressure> data) {
            super.onPostExecute(data);

            if (data == null || data.size() < 1) {
                Log.e(TAG, "no data.");

                mTvEmptyData.setVisibility(View.VISIBLE);
                return;
            }

            mBloodPressureDataList = data;
            mAdapter.setBloodPressureList(data);

            mTvEmptyData.setVisibility(View.GONE);
//            mRvBloodPressure.setAdapter(adapter);
        }
    }


}
