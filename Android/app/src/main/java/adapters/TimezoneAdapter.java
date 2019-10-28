package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.utilities.helper.FlagHelper;

public class TimezoneAdapter extends ArrayAdapter<String> {

    private List<String> mTimezones;
    private final Filter mFilter;

    public TimezoneAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0, new ArrayList<>(objects));
        this.mTimezones = new ArrayList<>(objects);
        this.mFilter = new TimezoneFilter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.item_timezone, parent, false);
        }

        String timezone = this.getItem(position);
        if (timezone == null) {
            return view;
        }

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTimezoneLabel.setText(timezone);

        String escapedTimezone = timezone.contains("/") ? timezone.split("/")[1] : timezone;
        viewHolder.ivTimezoneFlag.setImageResource(FlagHelper.Companion.retrieveFlagDrawable(escapedTimezone));

        return view;
    }

    @Override
    public Filter getFilter() {
        return this.mFilter;
    }

    class TimezoneFilter extends android.widget.Filter {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            List<String> matchingTimezones = new ArrayList<>();
            if (TextUtils.isEmpty(constraint)) {
                matchingTimezones.addAll(TimezoneAdapter.this.mTimezones);
            } else {
                String pattern = constraint.toString().toLowerCase().trim();
                for (String timezone : TimezoneAdapter.this.mTimezones) {
                    if (timezone.toLowerCase().contains(pattern)) {
                        matchingTimezones.add(timezone);
                    }
                }
            }

            Filter.FilterResults filterResults = new Filter.FilterResults();
            filterResults.values = matchingTimezones;
            filterResults.count = matchingTimezones.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            TimezoneAdapter.this.clear();
            TimezoneAdapter.this.addAll((List) results.values);
            TimezoneAdapter.this.notifyDataSetChanged();
        }
    }
    class ViewHolder {
        @BindView(R.id.tvTimezoneLabel)
        TextView tvTimezoneLabel;

        @BindView(R.id.ivTimezoneFlag)
        ImageView ivTimezoneFlag;

        public ViewHolder(@NonNull View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
