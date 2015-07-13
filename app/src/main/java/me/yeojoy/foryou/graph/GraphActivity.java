package me.yeojoy.foryou.graph;

import android.content.Context;
import android.os.Bundle;

import me.yeojoy.foryou.BaseActivity;
import me.yeojoy.foryou.R;

public class GraphActivity extends BaseActivity {

    private static final String TAG = GraphActivity.class.getSimpleName();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        mContext = this;
    }

}
