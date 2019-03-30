package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.ZoneName;
import utils.CurrencyConverterGlobal;

public class TimezoneConverterAdapter extends RecyclerView.Adapter<TimezoneConverterAdapter.ViewHolder>
        implements Filterable {

    private List<ZoneName> mListCurrencyNames, mListCurrencyNamesFiltered;
    public Activity activity;

    TimezoneConverterAdapter(Activity activity, List<ZoneName> list) {
        this.activity = activity;
        this.mListCurrencyNames = list;
        this.mListCurrencyNamesFiltered = list;
        Log.d("my list size", "" + getCount());
    }

    public int getCount() {
        return mListCurrencyNamesFiltered.size();
    }

    public Object getItem(int position) {
        return mListCurrencyNamesFiltered.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.row_curency_converter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (android.os.Build.VERSION.SDK_INT >= 5.0) {
            holder.mShortTitle.setText(mListCurrencyNamesFiltered.get(position).shortName);
        } else {
            holder.mShortTitle.setText(mListCurrencyNamesFiltered.get(position).abbreviation);
        }

        int mResId;
        if (android.os.Build.VERSION.SDK_INT >= 5.0) {
            mResId = activity.getResources().getIdentifier(
                    mListCurrencyNamesFiltered.get(position).abbreviation
                      .toLowerCase(),
                    "drawable", activity.getPackageName());
        } else {
            mResId = activity.getResources().getIdentifier(
                    mListCurrencyNamesFiltered.get(position).shortName.toLowerCase(),
                    "drawable", activity.getPackageName());
        }
        Log.d("Resouce resouce id", "" + mListCurrencyNamesFiltered.get(position).shortName);

        if (mResId == 0) {
            mResId = activity.getResources().getIdentifier("tryk", "drawable", activity.getPackageName());
            holder.mImageView.setImageResource(mResId);
        }
        holder.mImageView.setImageResource(mResId);

        holder.itemView.setOnClickListener(view -> {
            if (android.os.Build.VERSION.SDK_INT >= 5.0) {
                CurrencyConverterGlobal.global_image_id =
                        activity.getResources().getIdentifier(
                                mListCurrencyNamesFiltered.get(position).abbreviation
                                  .toLowerCase(),
                                "drawable", activity.getPackageName());
                CurrencyConverterGlobal.global_country_name = mListCurrencyNamesFiltered.get(position).shortName;
                CurrencyConverterGlobal.country_id = mListCurrencyNamesFiltered.get(position).abbreviation;

                if (CurrencyConverterGlobal.country_id.contains("TRY")) {
                    CurrencyConverterGlobal.global_image_id = activity.getResources().getIdentifier("tryk",
                            "drawable", activity.getPackageName());

                }
                if (CurrencyConverterGlobal.country_id.contains("TMM")) {
                    CurrencyConverterGlobal.global_image_id = activity.getResources().getIdentifier("tryk",
                            "drawable", activity.getPackageName());
                }
            } else {
                CurrencyConverterGlobal.global_image_id =
                        activity.getResources().getIdentifier(
                                mListCurrencyNamesFiltered.get(position).shortName.toLowerCase(),
                                "drawable", activity.getPackageName());
                CurrencyConverterGlobal.global_country_name = mListCurrencyNamesFiltered.get(position).abbreviation;
                CurrencyConverterGlobal.country_id = mListCurrencyNamesFiltered.get(position).shortName;
                if (CurrencyConverterGlobal.country_id.contains("TRY")) {
                    CurrencyConverterGlobal.global_image_id = activity.getResources().getIdentifier("tryk",
                            "drawable", activity.getPackageName());
                }
                if (CurrencyConverterGlobal.country_id.contains("TMM")) {
                    CurrencyConverterGlobal.global_image_id = activity.getResources().getIdentifier("tryk",
                            "drawable", activity.getPackageName());
                }
            }
            activity.finish();
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mListCurrencyNamesFiltered = mListCurrencyNames;
                } else {
                    List<ZoneName> filteredList = new ArrayList<>();
                    for (ZoneName row : mListCurrencyNames) {
                        if (row.getShortName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getAbbreviation().toLowerCase().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    mListCurrencyNamesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListCurrencyNamesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListCurrencyNamesFiltered = (ArrayList<ZoneName>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mListCurrencyNamesFiltered.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView mShortTitle;
        @BindView(R.id.imageView)
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
