package me.yeojoy.foryou.info;

import android.os.Bundle;

import me.yeojoy.foryou.BaseActivity;
import me.yeojoy.foryou.R;

public class InfoActivity extends BaseActivity {

    private static final String TAG = InfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /*
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
        if (id == R.id.action_input_blood_pressure) {
            Intent intent = new Intent(mContext, InputActivity.class);
            intent.putExtra(KEY_INPUT_TYPE, INPUT_TYPE_BLOOD_PRESSURE);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_input_blood_sugar) {
            Intent intent = new Intent(mContext, InputActivity.class);
            intent.putExtra(KEY_INPUT_TYPE, INPUT_TYPE_BLOOD_SUGAR);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
