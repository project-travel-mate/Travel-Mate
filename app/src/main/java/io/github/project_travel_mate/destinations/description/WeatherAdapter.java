package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Weather;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private Context mContext;
    private ArrayList<Weather> mWeatherForecast;
    private int mLastPosition = -1;

    WeatherAdapter(Context context, ArrayList<Weather> weatherForecast) {
        this.mContext = context;
        this.mWeatherForecast = weatherForecast;
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherHolder(LayoutInflater.from(mContext).inflate(R.layout.weather_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {
        Weather weather = mWeatherForecast.get(position);

        holder.dayOfWeek.setText(weather.getDayOfWeek());
        holder.minTemp.setText(String.valueOf(weather.getMinTemp()));
        holder.maxTemp.setText(String.valueOf(weather.getMaxTemp()));
        holder.date.setText(weather.getDate());
        holder.icon.setImageResource(weather.getImageId());
        DrawableCompat.setTint(holder.icon.getDrawable(), ContextCompat.getColor(mContext, android.R.color.white));

        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return mWeatherForecast.size();
    }

    private void setAnimation(View view, int position) {
        if (position > mLastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in);
            view.startAnimation(animation);
            mLastPosition = position;
        }
    }


    static class WeatherHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.weather_icon)
        ImageView icon;
        @BindView(R.id.today)
        TextView dayOfWeek;
        @BindView(R.id.min_temp)
        TextView minTemp;
        @BindView(R.id.max_temp)
        TextView maxTemp;
        @BindView(R.id.date)
        TextView date;

        WeatherHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
