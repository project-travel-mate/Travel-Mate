package io.github.project_travel_mate.destinations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import flipviewpager.adapter.BaseFlipAdapter;
import flipviewpager.utils.FlipSettings;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.destinations.description.FinalCityInfoActivity;
import io.github.project_travel_mate.destinations.description.TweetsActivity;
import io.github.project_travel_mate.destinations.description.WeatherActivity;
import io.github.project_travel_mate.destinations.funfacts.FunFactsActivity;
import objects.City;

class CityAdapter extends BaseFlipAdapter<City> {

    private final Activity mContext;
    private final int[] mIdsInterest = {R.id.interest_1, R.id.interest_2, R.id.interest_3, R.id.interest_4};

    CityAdapter(Context context, List<City> items, FlipSettings settings) {
        super(context, items, settings);
        this.mContext = (Activity) context;
    }

    @Override
    public View getPage(int position, View convertView, ViewGroup parent, final City city1, final City city2) {
        CitiesHolder holder;
        CitiesInfoHolder infoHolder;
        if (convertView == null) {
            convertView = mContext.getLayoutInflater().inflate(R.layout.home_city_merge_page, parent, false);
            holder = new CitiesHolder(convertView);
            holder.infoPage = mContext.getLayoutInflater().inflate(R.layout.home_city_info, parent, false);

            for (int id : mIdsInterest)
                holder.interests.add(holder.infoPage.findViewById(id));

            convertView.setTag(holder);
        } else {
            holder = (CitiesHolder) convertView.getTag();
        }
        infoHolder = new CitiesInfoHolder(holder.infoPage);

        switch (position) {
            case 1:
                Picasso.with(mContext).
                        load(city1.getAvatar()).
                        placeholder(R.drawable.placeholder_image).
                        into(holder.leftAvatar);
                holder.left.setText(city1.getNickname());

                if (city2 != null) {
                    holder.right.setText(city2.getNickname());
                    Picasso.with(mContext).
                            load(city2.getAvatar()).
                            placeholder(R.drawable.placeholder_image).
                            into(holder.rightAvatar);
                }
                break;
            default:
                fillHolder(holder, infoHolder, position == 0 ? city1 : city2);
                holder.infoPage.setTag(holder);
                return holder.infoPage;
        }
        return convertView;
    }

    @Override
    public int getPagesCount() {
        return 5;
    }

    private void fillHolder(CitiesHolder holder, CitiesInfoHolder infoHolder, final City city) {
        if (city == null)
            return;
        Iterator<TextView> iViews = holder.interests.iterator();
        Iterator<String> iInterests = city.getInterests().iterator();
        while (iViews.hasNext() && iInterests.hasNext())
            iViews.next().setText(iInterests.next());
        holder.infoPage.setBackgroundColor(mContext.getResources().getColor(city.getBackgroundColor()));
        infoHolder.nickName.setText(city.getNickname());

        infoHolder.nickName.setOnClickListener(v -> {
        });

        infoHolder.fv1.setOnClickListener(v -> {
            Intent intent = FinalCityInfoActivity.getStartIntent(mContext, city);
            mContext.startActivity(intent);
        });

        if (city.getFunFactsCount() < 1) {
            infoHolder.fv3.setVisibility(View.GONE);
        } else {
            infoHolder.fv3.setVisibility(View.VISIBLE);
        }

        infoHolder.fv3.setOnClickListener(v -> {
            Intent intent = FunFactsActivity.getStartIntent(mContext, city);
            mContext.startActivity(intent);
        });

        infoHolder.fv2.setOnClickListener(v -> {
            Intent intent = WeatherActivity.getStartIntent(mContext, city, null);
            mContext.startActivity(intent);
        });

        infoHolder.fv4.setOnClickListener(v -> {
            Intent intent = TweetsActivity.getStartIntent(mContext, city);
            mContext.startActivity(intent);
        });
    }

    class CitiesHolder {
        final List<TextView> interests = new ArrayList<>();
        @BindView(R.id.first)
        ImageView leftAvatar;
        @BindView(R.id.second)
        ImageView rightAvatar;
        @BindView(R.id.name1)
        TextView left;
        @BindView(R.id.name2)
        TextView right;
        View infoPage;

        CitiesHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    class CitiesInfoHolder {
        @BindView(R.id.nickname)
        TextView nickName;
        @BindView(R.id.interest_1)
        TextView fv1;
        @BindView(R.id.interest_2)
        TextView fv2;
        @BindView(R.id.interest_3)
        TextView fv3;
        @BindView(R.id.interest_4)
        TextView fv4;

        CitiesInfoHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}