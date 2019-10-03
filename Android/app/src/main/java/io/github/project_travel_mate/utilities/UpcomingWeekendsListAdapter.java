package io.github.project_travel_mate.utilities;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.UpcomingWeekends;

public class UpcomingWeekendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<UpcomingWeekends> mUpcomingWeekends;

    UpcomingWeekendsListAdapter(ArrayList<UpcomingWeekends> upcomingWeekends) {
        initData(upcomingWeekends);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.upcoming_weekends_item, viewGroup, false);
        return new UpcomingWeekendsViewHolder(view, i);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        UpcomingWeekendsViewHolder holder = (UpcomingWeekendsViewHolder) viewHolder;
        UpcomingWeekends uw = mUpcomingWeekends.get(i);

        holder.name.setText(uw.getmName());
        holder.timelineView.setMarker(VectorDrawableCompat
                .create(mContext.getResources(), R.drawable.ic_marker_inactive, mContext.getTheme()));
        holder.date.setText(uw.getmDate() + "-" + uw.getmMonth());
    }

    @Override
    public int getItemCount() {
        return mUpcomingWeekends.size();
    }

    public void initData(ArrayList<UpcomingWeekends> upcomingWeekends) {
        mUpcomingWeekends = upcomingWeekends;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    class UpcomingWeekendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.upcoming_weekends_item_name)
        TextView name;
        @BindView(R.id.upcoming_weekends_item_date)
        TextView date;
        @BindView(R.id.timeline_view)
        TimelineView timelineView;

        UpcomingWeekendsViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);

            timelineView.initLine(viewType);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
