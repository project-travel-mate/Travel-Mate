package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.CityHistoryListItem;
import utils.ExpandableTextView;

public class CityHistoryAdapter extends ArrayAdapter<CityHistoryListItem> {

    private Context mContext;
    private List<CityHistoryListItem> mCityHistory;
    private LayoutInflater mInflater;
    private boolean mIsClicked = false;

    public  CityHistoryAdapter(Context context, List<CityHistoryListItem> cityHistory) {
        super(context, R.layout.city_history_listitem);
        mContext = context;
        mCityHistory = cityHistory;
    }

    @Override
    public int getCount() {
        return mCityHistory.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.city_history_listitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.heading.setText(mCityHistory.get(position).getHeading());
        holder.text.setText(mCityHistory.get(position).getText());
        //expand text when it is clicked and collapse
        //when clicked again
        holder.text.setOnClickListener(v -> {
            holder.text.handleExpansion(mIsClicked);
            mIsClicked = !mIsClicked;
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.text)
        ExpandableTextView text;
        @BindView(R.id.heading)
        TextView heading;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
