package me.yeojoy.foryou;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import me.yeojoy.foryou.config.Consts;

/**
 * A placeholder fragment containing a simple view.
 */
public class BloodPressureFragment extends Fragment implements Consts {

    private static final String TAG = BloodPressureFragment.class.getSimpleName();

    public static final String VIEW_TAG = "혈압";

    private Context mContext;

    private ListView mLvBloodPressure;

    private TextView mTvEmptyData;

    private List<ParseObject> mBloodPressureDataList;

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

        mLvBloodPressure = (ListView) view.findViewById(R.id.lv_blood_pressure);
        mTvEmptyData = (TextView) view.findViewById(R.id.tv_empty_data);
    }

    @Override
    public void onResume() {
        super.onResume();
        SelectAsyncTask task = new SelectAsyncTask();
        task.execute();
    }

    private class SelectAsyncTask extends AsyncTask<Void, Void, List<ParseObject>> {

        @Override
        protected List<ParseObject> doInBackground(Void... params) {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PARSE_BLOOD_PRESSURE_TABLE);
            query.orderByDescending(PARSE_COMMON_COLUMN_CREATE_AT);

            try {
                return query.find();
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<ParseObject> data) {
            super.onPostExecute(data);

            if (data == null || data.size() < 1) {
                Log.e(TAG, "no data.");

                mTvEmptyData.setVisibility(View.VISIBLE);
                return;
            }

            mBloodPressureDataList = data;
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                    R.layout.todo_row);
            for (ParseObject todo : mBloodPressureDataList) {
                adapter.add(String.valueOf(todo.get(PARSE_BP_COLUMN_MAX)));
            }
            mTvEmptyData.setVisibility(View.GONE);
            mLvBloodPressure.setAdapter(adapter);
        }
    }


}
