package me.yeojoy.foryou;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import me.yeojoy.foryou.config.Consts;
import me.yeojoy.foryou.input.InputActivity;

public class MainActivity extends AppCompatActivity implements Consts {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_input) {
            Intent intent = new Intent(mContext, InputActivity.class);
            intent.putExtra(KEY_INPUT_TYPE, INPUT_TYPE_BLOOD_PRESSURE);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
