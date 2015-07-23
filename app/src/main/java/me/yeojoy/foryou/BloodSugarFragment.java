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

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import me.yeojoy.foryou.adapter.BloodSugarAdapter;
import me.yeojoy.foryou.config.ParseConsts;
import me.yeojoy.foryou.model.BloodSugar;
import me.yeojoy.foryou.view.DividerItemDecoration;
import me.yeojoy.library.log.MyLog;

/**
 * A placeholder fragment containing a simple view.
 */
public class BloodSugarFragment extends Fragment implements ParseConsts {

    private static final String TAG = BloodSugarFragment.class.getSimpleName();

    public static final String VIEW_TAG = "혈당";

    private Context mContext;

    private RecyclerView mRvBloodPressure;

    private TextView mTvEmptyData;

    private List<BloodSugar> mBloodSugarDataList;

    private BloodSugarAdapter mAdapter;

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

        mAdapter = new BloodSugarAdapter(mContext, null);
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
        LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.row_blood_sugar, null);
        ((TextView) layout.findViewById(R.id.tv_date)).setText(R.string.label_blood_date);
        ((TextView) layout.findViewById(R.id.tv_time)).setText(R.string.label_blood_time);
        ((TextView) layout.findViewById(R.id.tv_blood_sugar)).setText(R.string.label_blood_sugar);
        ((TextView) layout.findViewById(R.id.tv_measure_time)).setText(R.string.label_measure_time);
        ((TextView) layout.findViewById(R.id.tv_weight)).setText(R.string.label_weight);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        header.addView(layout, params);
    }

    private class SelectAsyncTask extends AsyncTask<Void, Void, List<BloodSugar>> {

        @Override
        protected List<BloodSugar> doInBackground(Void... params) {
            ParseQuery<BloodSugar> query = ParseQuery.getQuery(BloodSugar.class);
            query.orderByDescending(PARSE_COMMON_COLUMN_REGISTERED_AT);

            try {
                return query.find();
            } catch (ParseException e) {
                MyLog.e(TAG, e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<BloodSugar> data) {
            super.onPostExecute(data);

            if (data == null || data.size() < 1) {
                MyLog.e(TAG, "no data.");

                mTvEmptyData.setVisibility(View.VISIBLE);
                return;
            }

            mBloodSugarDataList = data;
            mAdapter.setBloodSugarList(data);

            mTvEmptyData.setVisibility(View.GONE);
//            mRvBloodPressure.setAdapter(adapter);
        }
    }


}
