package io.github.project_travel_mate.destinations.description;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.CityHistoryListItem;
import utils.ExpandableTextView;

public class CityHistoryAdapter extends RecyclerView.Adapter<CityHistoryAdapter.ViewHolder> {

    private List<CityHistoryListItem> mCityHistory;

    public CityHistoryAdapter(List<CityHistoryListItem> mCityHistory) {
        this.mCityHistory = mCityHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.city_history_listitem, viewGroup, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityHistoryListItem cityHistoryListItem = mCityHistory.get(position);

        holder.heading.setText(cityHistoryListItem.getHeading());
        holder.text.setText(cityHistoryListItem.getText());
        holder.text.handleExpansion(cityHistoryListItem.getExpanded());
        //expand text when it is clicked and collapse
        //when clicked again
        holder.text.setOnClickListener(v -> {
            cityHistoryListItem.setExpanded(!cityHistoryListItem.getExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return mCityHistory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        ExpandableTextView text;
        @BindView(R.id.heading)
        TextView heading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}