package me.yeojoy.foryou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.yeojoy.foryou.R;
import me.yeojoy.foryou.config.Consts;
import me.yeojoy.foryou.graph.GraphActivity;
import me.yeojoy.foryou.model.BloodSugar;
import me.yeojoy.foryou.utils.CommonUtils;

/**
 * Created by yeojoy on 15. 7. 7..
 */
public class BloodSugarAdapter extends RecyclerView.Adapter<BloodSugarAdapter.ItemViewHolder>
        implements Consts {

    private static final String TAG = BloodSugarAdapter.class.getSimpleName();

    private Context mContext;
    private List<BloodSugar> mBloodSugarList;

    private int mPosition;

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

        mPosition = position;

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

        holder.tvSugar.setOnClickListener(mListener);
        holder.tvMeasureTime.setOnClickListener(mListener);
        holder.tvWeight.setOnClickListener(mListener);
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, GraphActivity.class);
            intent.putExtra(KEY_GRAPH_ITEM_POSITON, mPosition);
            intent.putExtra(KEY_GRAPH_TYPE, GRAPH_TYPE_BLOOD_SUGAR);
            mContext.startActivity(intent);
        }
    };

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
