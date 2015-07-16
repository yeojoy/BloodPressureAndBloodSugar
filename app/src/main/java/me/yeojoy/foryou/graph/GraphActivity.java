package me.yeojoy.foryou.graph;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.yeojoy.foryou.BaseActivity;
import me.yeojoy.foryou.R;
import me.yeojoy.foryou.config.Consts;

public class GraphActivity extends BaseActivity implements Consts {

    private static final String TAG = GraphActivity.class.getSimpleName();

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        if (i != null) {
            FragmentManager manager = getFragmentManager();

            switch (i.getIntExtra(KEY_GRAPH_TYPE, 0)) {
                case GRAPH_TYPE_BLOOD_PRESSURE:
                    Fragment pressureFragment = new GraphPressureFragment();
                    pressureFragment.setArguments(i.getExtras());
                    manager.beginTransaction()
                            .add(R.id.container, pressureFragment)
                            .commit();
                    getSupportActionBar().setTitle("혈압 그래프");
                    break;
                case GRAPH_TYPE_BLOOD_SUGAR:
                    Fragment sugarFragment = new GraphSugarFragment();
                    sugarFragment.setArguments(i.getExtras());

                    manager.beginTransaction()
                            .add(R.id.container, sugarFragment)
                            .commit();
                    getSupportActionBar().setTitle("혈당 그래프");
                    break;
                default:
                    finish();
                    return;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
