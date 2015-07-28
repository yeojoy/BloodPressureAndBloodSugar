package me.yeojoy.foryou.graph;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
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
import me.yeojoy.foryou.model.BloodSugar;
import me.yeojoy.library.log.MyLog;

/**
 * Created by yeojoy on 15. 7. 13..
 */
public class GraphSugarFragment extends Fragment implements Consts, ParseConsts {

    private static final String TAG = GraphSugarFragment.class.getSimpleName();

    private LineChart mLcBlood;

    private int mPosition = -1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        mLcBlood.setDrawGridBackground(false);
        YAxis rightAxis = mLcBlood.getAxisRight();
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = mLcBlood.getAxisLeft();
        leftAxis.setDrawGridLines(false);

        XAxis xAxis = mLcBlood.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryBloodSugarData();

        if (getArguments() != null) {
            mPosition = getArguments().getInt(KEY_GRAPH_ITEM_POSITON);
            MyLog.d(TAG, "Position >>>>>>>> " + mPosition);
        }
    }

    private void queryBloodSugarData() {
        MyLog.i(TAG);

        ParseQuery<BloodSugar> query = ParseQuery.getQuery(BloodSugar.class);
//        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.orderByAscending(PARSE_COMMON_COLUMN_REGISTERED_AT);
        query.findInBackground(new FindCallback<BloodSugar>() {
            @Override
            public void done(List<BloodSugar> list, ParseException e) {
                if (list == null || list.size() < 1) {
                    MyLog.d(TAG, e);
                    return;
                }

                displayBloodSugarData(list);
            }
        });

    }

    private void displayBloodSugarData(List<BloodSugar> list) {
        MyLog.i(TAG);
        List<Entry> sugarList = new ArrayList<>();
        List<Entry> weightList = new ArrayList<>();
        // X 좌표 이름
        List<String> xVals = new ArrayList<>();
        List<LineDataSet> dataSets = new ArrayList<>();

        for (int i = 0, j = list.size(); i < j; i++) {
            BloodSugar bs = list.get(i);
            Entry max = new Entry(bs.getBloodSugar(), i);
            Entry min = new Entry(bs.getWeight(), i);
            String date = new SimpleDateFormat(DATE_TIME_FORMAT).format(bs.getRegisteredDate());

            sugarList.add(max);
            weightList.add(min);
            xVals.add(date);
        }

        LineDataSet lineDataSet1 = new LineDataSet(sugarList, BLOOD_PRESSURE_MAX_X_LABEL);
        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet1.setColor(Color.BLUE);
        LineDataSet lineDataSet2 = new LineDataSet(weightList, BLOOD_PRESSURE_MIN_X_LABEL);
        lineDataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet2.setColor(Color.RED);

        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);

        LineData lineData = new LineData(xVals, dataSets);
        mLcBlood.setData(lineData);
        mLcBlood.setHighlightEnabled(true);
        if (mPosition > -1) mLcBlood.highlightValue(mPosition, mPosition);
        mLcBlood.invalidate();
    }
}
