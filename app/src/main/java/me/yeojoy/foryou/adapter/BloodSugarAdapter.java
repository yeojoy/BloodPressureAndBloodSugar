package me.yeojoy.foryou.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import me.yeojoy.foryou.model.BloodSugar;
import me.yeojoy.foryou.utils.CommonUtils;
import me.yeojoy.library.log.MyLog;

/**
 * Created by yeojoy on 15. 7. 7..
 */
public class BloodSugarAdapter extends RecyclerView.Adapter<BloodSugarAdapter.ItemViewHolder>
        implements Consts {

    private static final String TAG = BloodSugarAdapter.class.getSimpleName();

    private Context mContext;
    private List<BloodSugar> mBloodSugarList;

    public BloodSugarAdapter(Context context, List<BloodSugar> list) {
        mContext = context;
        if (list != null)
            mBloodSugarList = list;
        else
            mBloodSugarList = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_blood_sugar, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        BloodSugar bs = mBloodSugarList.get(position);

        if (bs == null) return;

        holder.tvDate.setText(new SimpleDateFormat(DATE_FORMAT).format(bs.getRegisteredDate()));
        holder.tvTime.setText(new SimpleDateFormat(TIME_FORMAT).format(bs.getRegisteredDate()));

        int color = CommonUtils.getTextColorOfBloodSugar(mContext, bs.getBloodSugar(), bs.getMeasureTime());
        SpannableString spannableString = new SpannableString(String.valueOf(bs.getBloodSugar()));
        spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        holder.tvSugar.setText(spannableString);

        String measureTime;
        switch (bs.getMeasureTime()) {
            case 0:
                measureTime = IMMEDIATELY;
                break;

            case 2:
                measureTime = AFTER_TWO_HOURS;
                break;

            case 3:
                measureTime = EMPTY;
                break;

            default:
                measureTime = AFTER_ONE_HOUR;
                break;
        }

        holder.tvMeasureTime.setText(measureTime);

        if (bs.getWeight() > 0f)
            holder.tvWeight.setText(String.valueOf(bs.getWeight()));
        else
            holder.tvWeight.setText("-");

        holder.tvSugar.setOnClickListener(new PressureClickListener(position));
        holder.tvMeasureTime.setOnClickListener(new PressureClickListener(position));
        holder.tvWeight.setOnClickListener(new PressureClickListener(position));

        holder.tvDate.setOnLongClickListener(new PressureLongClickListener(position));
        holder.tvTime.setOnLongClickListener(new PressureLongClickListener(position));
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
            intent.putExtra(KEY_GRAPH_TYPE, GRAPH_TYPE_BLOOD_SUGAR);
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
            MyLog.d(TAG, "Postion >>>>> " + mPosition);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            BloodSugar bs = mBloodSugarList.get(mPosition);

            StringBuilder sb = new StringBuilder();
            sb.append(new SimpleDateFormat(DATE_TIME_FORMAT).format(bs.getRegisteredDate()));
            sb.append("에 측정한").append("\n");
            sb.append("Sugar : ").append(bs.getBloodSugar());
            sb.append(", Weight : ").append(bs.getWeight());
            sb.append("\n\n").append("데이터를 삭제 하시겠습니까?");
            builder.setMessage(sb);
            builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyLog.i(TAG);
                    MyLog.d(TAG, "Postion >>> " + mPosition);

                    BloodSugar bp = mBloodSugarList.get(mPosition);
                    bp.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(mContext, "삭제 했습니다.", Toast.LENGTH_SHORT).show();

                            notifyDataSetChanged();
                        }
                    });
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

    @Override
    public int getItemCount() {
        return mBloodSugarList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvDate, tvTime, tvSugar, tvMeasureTime, tvWeight;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvSugar = (TextView) itemView.findViewById(R.id.tv_blood_sugar);
            tvMeasureTime = (TextView) itemView.findViewById(R.id.tv_measure_time);
            tvWeight = (TextView) itemView.findViewById(R.id.tv_weight);
        }
    }

    public void setBloodSugarList(List<BloodSugar> list) {
        mBloodSugarList.clear();
        mBloodSugarList.addAll(list);

        notifyDataSetChanged();
    }
}
