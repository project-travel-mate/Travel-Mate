package io.github.project_travel_mate.searchcitydialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import utils.CircleImageView;

public class CitySearchBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.rv_cities)
    RecyclerView rvCities;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.et_query)
    EditText etQuery;

    private Listener mListener;
    private List<CitySearchModel> mCitySearchModels = new ArrayList<>();
    private CitySearchModelAdapter mCitySearchModelAdapter;

    public static CitySearchBottomSheetDialogFragment newInstance() {
        final CitySearchBottomSheetDialogFragment fragment = new CitySearchBottomSheetDialogFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_citysearchmodel_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mCitySearchModelAdapter = new CitySearchModelAdapter(mCitySearchModels);
        rvCities.setAdapter(mCitySearchModelAdapter);
        etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mCitySearchModelAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public interface Listener {
        void onCitySearchModelClicked(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.hotel_city_image)
        CircleImageView civCityImage;

        @BindView(R.id.hotel_city_name)
        TextView tvCityName;

        @BindView(R.id.hotel_city_id)
        TextView tvCityId;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    public void setmCitySearchModels(List<CitySearchModel> mCitySearchModels) {
        this.mCitySearchModels = mCitySearchModels;
        if (mCitySearchModelAdapter != null) {
            mCitySearchModelAdapter.notifyDataSetChanged();
        }
    }

    private class CitySearchModelAdapter extends RecyclerView.Adapter<ViewHolder> implements Filterable {

        private List<CitySearchModel> mCitySearchModels;
        private List<CitySearchModel> mFilteredCitySearchModels = new ArrayList<>();

        CitySearchModelAdapter(@NonNull List<CitySearchModel> mCitySearchModels) {
            this.mCitySearchModels = mCitySearchModels;
            mFilteredCitySearchModels.addAll(mCitySearchModels);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CitySearchModel citySearchModel = mFilteredCitySearchModels.get(position);
            Context context = holder.itemView.getContext();
            if (context == null) {
                return;
            }

            holder.tvCityName.setText(citySearchModel.getName());
            Picasso.with(context).load(citySearchModel.getImageUrl())
                    .into(holder.civCityImage);

            //highlight the letter(s) user has searched for
//            if (mSearchTag != null)
//                text.setText(StringsHelper.highlightLCS(object.getTitle(), getSearchTag(),
//                        Color.RED ));
//            else text.setText(object.getTitle());

            if (mListener != null)
                holder.itemView.setOnClickListener(
                        view -> mListener.onCitySearchModelClicked(position));


//            holder.text.setText(String.valueOf(position));
//            holder.itemView.setOnClickListener(v -> {
//                if (mListener != null) {
//                    mListener.onCitySearchModelClicked(getAdapterPosition());
//                    dismiss();
//                }
//            });

        }

        @Override
        public int getItemCount() {
            return mFilteredCitySearchModels.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    mFilteredCitySearchModels.clear();

                    if (constraint == null || constraint.length() == 0) {
                        mFilteredCitySearchModels.addAll(mCitySearchModels);
                        filterResults.values = mCitySearchModels;
                        filterResults.count = mCitySearchModels.size();
                        return filterResults;
                    }

                    for (CitySearchModel citySearchModel :
                            mCitySearchModels) {
                        if (citySearchModel.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            mFilteredCitySearchModels.add(citySearchModel);
                        }
                    }

                    filterResults.count = mFilteredCitySearchModels.size();
                    filterResults.values = mFilteredCitySearchModels;

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
        }
    }

}
