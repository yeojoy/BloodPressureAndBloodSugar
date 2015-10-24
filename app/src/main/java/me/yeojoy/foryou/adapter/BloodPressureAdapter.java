package me.yeojoy.foryou.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.yeojoy.foryou.R;
import me.yeojoy.foryou.config.Consts;
import me.yeojoy.foryou.graph.GraphActivity;
import me.yeojoy.foryou.input.ModifyActivity;
import me.yeojoy.foryou.model.BloodPressure;
import me.yeojoy.foryou.utils.CommonUtils;
import me.yeojoy.library.log.MyLog;

/**
 * Created by yeojoy on 15. 7. 7..
 */
public class BloodPressureAdapter
        extends RecyclerView.Adapter<BloodPressureAdapter.ItemViewHolder>
        implements Consts {

    private static final String TAG = BloodPressureAdapter.class.getSimpleName();

    private Context mContext;
    private List<BloodPressure> mBloodPressureList;

    private ItemViewHolder viewHolder;

    public BloodPressureAdapter(Context context, List<BloodPressure> list) {
        mContext = context;
        if (list != null)
            mBloodPressureList = list;
        else
            mBloodPressureList = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_blood_pressure, parent, false);
        viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        BloodPressure bp = mBloodPressureList.get(position);

        if (bp == null) return;

        int[] colors = CommonUtils.getTextColorOfBloodPressure(mContext,
                bp.getBloodPressureMax(), bp.getBloodPressureMin());

        holder.tvDate.setText(new SimpleDateFormat(DATE_FORMAT)
                .format(bp.getRegisteredDate()));
        holder.tvTime.setText(new SimpleDateFormat(TIME_FORMAT)
                .format(bp.getRegisteredDate()));

        SpannableString spannableString
                = new SpannableString(String.valueOf(bp.getBloodPressureMax()));
        spannableString.setSpan(new ForegroundColorSpan(colors[0]),
                0, spannableString.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        holder.tvMax.setText(spannableString);

        spannableString
                = new SpannableString(String.valueOf(bp.getBloodPressureMin()));
        spannableString.setSpan(new ForegroundColorSpan(colors[1]),
                0, spannableString.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        holder.tvMin.setText(spannableString);


        holder.tvPulse.setText(String.valueOf(bp.getBloodPulse()));


        holder.rootView.setOnClickListener(new PressureClickListener
                (position));
        holder.rootView.setOnLongClickListener(new PressureLongClickListener
                (position));

    }

    @Override
    public int getItemCount() {
        return mBloodPressureList.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView tvDate, tvTime, tvMax, tvMin, tvPulse;


        public ItemViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvMax = (TextView) itemView.findViewById(R.id.tv_blood_pressure_max);
            tvMin = (TextView) itemView.findViewById(R.id.tv_blood_pressure_min);
            tvPulse = (TextView) itemView.findViewById(R.id.tv_blood_pulse);
        }

    }

    private class PressureClickListener implements View.OnClickListener {

        private int mPosition;

        public PressureClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            MyLog.i(TAG);
            MyLog.d(TAG, "Postion >>>>> " + mPosition);
            Intent intent = new Intent(mContext, GraphActivity.class);
            intent.putExtra(KEY_GRAPH_ITEM_POSITON, mPosition);
            intent.putExtra(KEY_GRAPH_TYPE, GRAPH_TYPE_BLOOD_PRESSURE);
            mContext.startActivity(intent);
        }
    }

    private class PressureLongClickListener implements View.OnLongClickListener {

        private int mPosition;

        public PressureLongClickListener(int position) {
            mPosition = position;
        }

        @Override
        public boolean onLongClick(View v) {
            MyLog.i(TAG);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            BloodPressure bp = mBloodPressureList.get(mPosition);

            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append(new SimpleDateFormat(DATE_TIME_FORMAT).format(bp.getRegisteredDate()));
            sb.append("에 측정한").append("\n");
            sb.append("최대 : ").append(bp.getBloodPressureMax());
            sb.append(", 최소 : ").append(bp.getBloodPressureMin());
            sb.append(", 심박 : ").append(bp.getBloodPulse());
            sb.append("\n\n").append("데이터를 수정 혹은 삭제 하시겠습니까?");
            builder.setMessage(sb);

            builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyLog.i(TAG);
                    MyLog.d(TAG, "Position >>> " + mPosition);

                    BloodPressure bp = mBloodPressureList.get(mPosition);
                    bp.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(mContext, "삭제 했습니다.", Toast.LENGTH_SHORT).show();
                            mBloodPressureList.remove(mPosition);

                            notifyDataSetChanged();
                        }
                    });
                }
            });

            builder.setNeutralButton("수정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyLog.i(TAG);
                    BloodPressure bp = mBloodPressureList.get(mPosition);
                    Intent intent = new Intent(mContext, ModifyActivity.class);
                    intent.putExtra(KEY_INPUT_TYPE, INPUT_TYPE_BLOOD_PRESSURE);

                    Bundle b = new Bundle();
                    b.putString(KEY_OBJECT_ID, bp.getObjectId());
                    b.putInt(KEY_PRESSURE_MAX, bp.getBloodPressureMax());
                    b.putInt(KEY_PRESSURE_MIN, bp.getBloodPressureMin());
                    b.putInt(KEY_PRESSURE_PULSE, bp.getBloodPulse());
                    b.putLong(KEY_DATE_TIME, bp.getRegisteredDate().getTime());
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();

            return true;
        }
    }

    public void setBloodPressureList(List<BloodPressure> list) {
        mBloodPressureList.clear();
        mBloodPressureList.addAll(list);

        notifyDataSetChanged();
    }
}
