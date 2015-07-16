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
import me.yeojoy.foryou.model.BloodPressure;
import me.yeojoy.foryou.utils.CommonUtils;

/**
 * Created by yeojoy on 15. 7. 7..
 */
public class BloodPressureAdapter
        extends RecyclerView.Adapter<BloodPressureAdapter.ItemViewHolder>
        implements Consts {

    private static final String TAG = BloodPressureAdapter.class.getSimpleName();

    private Context mContext;
    private List<BloodPressure> mBloodPressureList;

    private int mPosition;

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
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        BloodPressure bp = mBloodPressureList.get(position);

        if (bp == null) return;

        mPosition = position;

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

        holder.tvMax.setOnClickListener(mListener);
        holder.tvMin.setOnClickListener(mListener);
        holder.tvPulse.setOnClickListener(mListener);

    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, GraphActivity.class);
            intent.putExtra(KEY_GRAPH_ITEM_POSITON, mPosition);
            intent.putExtra(KEY_GRAPH_TYPE, GRAPH_TYPE_BLOOD_PRESSURE);
            mContext.startActivity(intent);
        }
    };

    @Override
    public int getItemCount() {
        return mBloodPressureList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvDate, tvTime, tvMax, tvMin, tvPulse;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvMax = (TextView) itemView.findViewById(R.id.tv_blood_pressure_max);
            tvMin = (TextView) itemView.findViewById(R.id.tv_blood_pressure_min);
            tvPulse = (TextView) itemView.findViewById(R.id.tv_blood_pulse);
        }
    }

    public void setBloodPressureList(List<BloodPressure> list) {
        mBloodPressureList.clear();
        mBloodPressureList.addAll(list);

        notifyDataSetChanged();
    }
}
