package io.github.project_travel_mate.searchcitydialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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

    private static final String ARG_TITLE = "TITLE";
    private static final String ARG_HINT = "HINT";

    @BindView(R.id.rv_cities)
    RecyclerView rvCities;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.et_query)
    EditText etQuery;

    private Listener mListener;
    private List<CitySearchModel> mCitySearchModels = new ArrayList<>();
    private CitySearchModelAdapter mCitySearchModelAdapter;

    public static CitySearchBottomSheetDialogFragment newInstance(@StringRes int title, @StringRes int hint) {
        final CitySearchBottomSheetDialogFragment fragment = new CitySearchBottomSheetDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_TITLE, title);
        args.putInt(ARG_HINT, hint);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialog1 -> {
            View bottomSheet = ((BottomSheetDialog) dialog1)
                    .findViewById(android.support.design.R.id.design_bottom_sheet);

            BottomSheetBehavior.from(bottomSheet)
                    .setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        });

        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }

        int title = arguments.getInt(ARG_TITLE, R.string.search_title);
        int hint = arguments.getInt(ARG_HINT, R.string.search_hint);

        tvTitle.setText(title);
        etQuery.setHint(hint);

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
        private String mQueryString = "";

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

            String name = citySearchModel.getName();

            final SpannableStringBuilder sb = new SpannableStringBuilder(name);

            // Span to set text color to some RGB value
            final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);

            // Span to make text bold
            final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

            int start = name.toLowerCase().indexOf(mQueryString.toLowerCase());
            int end = start + mQueryString.length();

            if (start == -1) {
                start = 0;
            }

            if (end == -1) {
                end = 0;
            }

            sb.setSpan(fcs, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            holder.tvCityName.setText(sb);
            Picasso.with(context).load(citySearchModel.getImageUrl())
                    .into(holder.civCityImage);

            if (mListener != null)
                holder.itemView.setOnClickListener(
                        view -> mListener.onCitySearchModelClicked(position));

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
                        mQueryString = "";
                        mFilteredCitySearchModels.addAll(mCitySearchModels);
                        filterResults.values = mFilteredCitySearchModels;
                        filterResults.count = mFilteredCitySearchModels.size();
                        return filterResults;
                    }

                    mQueryString = constraint.toString();
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
