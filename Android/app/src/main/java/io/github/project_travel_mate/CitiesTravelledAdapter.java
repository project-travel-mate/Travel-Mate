package io.github.project_travel_mate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.destinations.description.FinalCityInfoActivity;
import objects.City;

public class CitiesTravelledAdapter extends RecyclerView.Adapter<CitiesTravelledAdapter.ViewHolder> {

    private Context mContext;
    private List<City> mCities;

    public CitiesTravelledAdapter(Context context, List<City> cities) {
        mContext = context;
        mCities = cities;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.city_image)
        ImageView cityImage;
        @BindView(R.id.city_name)
        TextView cityName;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_city_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.with(mContext).load(mCities.get(position).getAvatar()).placeholder(R.drawable.placeholder_image)
                .into(holder.cityImage);
        holder.cityName.setText(mCities.get(position).getNickname());
        holder.cityImage.setOnClickListener(v -> {
            Intent intent = FinalCityInfoActivity.getStartIntent(mContext, mCities.get(position));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }
}
