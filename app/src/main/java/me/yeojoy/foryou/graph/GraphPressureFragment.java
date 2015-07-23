package me.yeojoy.foryou.graph;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.yeojoy.foryou.R;
import me.yeojoy.foryou.config.Consts;
import me.yeojoy.foryou.config.ParseConsts;
import me.yeojoy.foryou.model.BloodPressure;
import me.yeojoy.library.log.MyLog;

/**
 * Created by yeojoy on 15. 7. 13..
 */
public class GraphPressureFragment extends Fragment implements Consts, ParseConsts {

    private static final String TAG = GraphPressureFragment.class.getSimpleName();

    private Context mContext;

    private LineChart mLcBlood;

    private int mPosition = -1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mLcBlood = (LineChart) view.findViewById(R.id.lc_blood);
        mLcBlood.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryBloodPressureData();

        if (getArguments() != null) {
            mPosition = getArguments().getInt(KEY_GRAPH_ITEM_POSITON);
            MyLog.d(TAG, "Position >>>>>>>> " + mPosition);
        }
    }

    private void queryBloodPressureData() {
        MyLog.i(TAG);

        ParseQuery<BloodPressure> query = ParseQuery.getQuery(BloodPressure.class);
//        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.orderByAscending(PARSE_COMMON_COLUMN_REGISTERED_AT);
        query.findInBackground(new FindCallback<BloodPressure>() {
            @Override
            public void done(List<BloodPressure> list, ParseException e) {
                if (list == null || list.size() < 1) {
                    MyLog.d(TAG, e);
                    return;
                }

                displayBloodPressureData(list);
            }
        });

    }

    private void displayBloodPressureData(List<BloodPressure> list) {
        MyLog.i(TAG);
        List<Entry> maxList = new ArrayList<>();
        List<Entry> minList = new ArrayList<>();
        List<Entry> pulseList = new ArrayList<>();
        // X 좌표 이름
        List<String> xVals = new ArrayList<>();
        List<LineDataSet> dataSets = new ArrayList<>();

        for (int i = 0, j = list.size(); i < j; i++) {
            BloodPressure bp = list.get(i);
            Entry max = new Entry(bp.getBloodPressureMax(), i);
            Entry min = new Entry(bp.getBloodPressureMin(), i);
            Entry pulse = new Entry(bp.getBloodPulse(), i);
            String date = new SimpleDateFormat(DATE_TIME_FORMAT).format(bp.getRegisteredDate());

            maxList.add(max);
            minList.add(min);
            pulseList.add(pulse);
            xVals.add(date);
        }

        LineDataSet lineDataSet1 = new LineDataSet(maxList, BLOOD_PRESSURE_MAX_X_LABEL);
        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet1.setColor(Color.BLUE);
        LineDataSet lineDataSet2 = new LineDataSet(minList, BLOOD_PRESSURE_MIN_X_LABEL);
        lineDataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet2.setColor(Color.RED);
        LineDataSet lineDataSet3 = new LineDataSet(pulseList, BLOOD_PRESSURE_PULSE_X_LABEL);
        lineDataSet3.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet3.setColor(Color.GREEN);

        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        dataSets.add(lineDataSet3);

        LineData lineData = new LineData(xVals, dataSets);
        mLcBlood.setData(lineData);
        mLcBlood.setHighlightEnabled(true);
        if (mPosition > -1) mLcBlood.highlightValue(mPosition, mPosition);
        mLcBlood.invalidate();
    }
}
