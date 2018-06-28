package io.github.project_travel_mate.destinations;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import flipviewpager.adapter.BaseFlipAdapter;
import flipviewpager.utils.FlipSettings;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.destinations.description.FinalCityInfoActivity;
import io.github.project_travel_mate.destinations.description.TweetsActivity;
import io.github.project_travel_mate.destinations.funfacts.FunFactsActivity;
import objects.City;
import views.FontTextView;

import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;

class CityAdapter extends BaseFlipAdapter<City> {

    private final Activity mContext;
    private final Typeface mTypefaceTex;
    private final int[] mIdsInterest = {R.id.interest_1, R.id.interest_2, R.id.interest_3, R.id.interest_4};


    CityAdapter(Context context, List<City> items, FlipSettings settings) {
        super(context, items, settings);
        this.mContext = (Activity) context;
        mTypefaceTex = Typeface.createFromAsset(context.getAssets(), "fonts/texgyreadventor-bold.otf");
    }

    @Override
    public View getPage(int position, View convertView, ViewGroup parent, final City city1, final City city2) {
        final CitiesHolder holder;

        if (convertView == null) {
            holder = new CitiesHolder();
            convertView = mContext.getLayoutInflater().inflate(R.layout.home_city_merge_page, parent, false);
            holder.leftAvatar = convertView.findViewById(R.id.first);
            holder.rightAvatar = convertView.findViewById(R.id.second);
            holder.left = convertView.findViewById(R.id.name1);
            holder.right = convertView.findViewById(R.id.name2);
            holder.infoPage = mContext.getLayoutInflater().inflate(R.layout.home_city_info, parent, false);
            holder.nickName = holder.infoPage.findViewById(R.id.nickname);
            holder.fv1 = (FontTextView) holder.infoPage.findViewById(R.id.interest_1);
            holder.fv2 = (FontTextView) holder.infoPage.findViewById(R.id.interest_2);
            holder.fv3 = (FontTextView) holder.infoPage.findViewById(R.id.interest_3);
            holder.fv4 = (FontTextView) holder.infoPage.findViewById(R.id.interest_4);

            for (int id : mIdsInterest)
                holder.interests.add(holder.infoPage.findViewById(id));

            convertView.setTag(holder);
        } else {
            holder = (CitiesHolder) convertView.getTag();
        }

        switch (position) {
            case 1:
                Picasso.with(mContext).
                        load(city1.getAvatar()).
                        placeholder(R.drawable.delhi).
                        into(holder.leftAvatar);
                holder.left.setTypeface(mTypefaceTex);
                holder.left.setText(city1.getNickname());

                if (city2 != null) {
                    holder.right.setText(city2.getNickname());
                    holder.right.setTypeface(mTypefaceTex);
                    Picasso.with(mContext).
                            load(city2.getAvatar()).
                            placeholder(R.drawable.delhi).
                            into(holder.rightAvatar);
                }
                break;
            default:
                fillHolder(holder, position == 0 ? city1 : city2);
                holder.infoPage.setTag(holder);
                return holder.infoPage;
        }
        return convertView;
    }

    @Override
    public int getPagesCount() {
        return 3;
    }

    private void fillHolder(CitiesHolder holder, final City city) {
        if (city == null)
            return;
        Iterator<TextView> iViews = holder.interests.iterator();
        Iterator<String> iInterests = city.getInterests().iterator();
        while (iViews.hasNext() && iInterests.hasNext())
            iViews.next().setText(iInterests.next());
        holder.infoPage.setBackgroundColor(mContext.getResources().getColor(getRandomColor()));
        holder.nickName.setText(city.getNickname());

        holder.nickName.setOnClickListener(v -> {
        });

        holder.fv1.setOnClickListener(v -> {
            Intent intent = FinalCityInfoActivity.getStartIntent(mContext, city);
            mContext.startActivity(intent);
        });

        if (city.getFunFactsCount() < 1) {
            holder.fv3.setVisibility(View.GONE);
        } else {
            holder.fv3.setVisibility(View.VISIBLE);
        }

        holder.fv3.setOnClickListener(v -> {
            Intent intent = FunFactsActivity.getStartIntent(mContext, city);
            mContext.startActivity(intent);
        });

        holder.fv2.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/?ie=UTF8&hq=&ll=" +
                    city.getLatitude() + "," + city.getLongitude() +
                    "&z=13")); // zoom level
            mContext.startActivity(browserIntent);
        });

        holder.fv4.setOnClickListener(v -> {
            Intent intent = TweetsActivity.getStartIntent(mContext, city);
            mContext.startActivity(intent);
        });
    }

    class CitiesHolder {
        final List<TextView> interests = new ArrayList<>();
        ImageView leftAvatar;
        ImageView rightAvatar;
        View infoPage;
        TextView fv1, fv2, fv3, fv4;
        TextView left, right;
        TextView nickName;
    }

    private int getRandomColor() {
        double random = Math.random();
        int randomNum8 = (int) (random * 100) % 8;
        int color;
        switch (randomNum8) {
            case 0:
                color = R.color.sienna;
                break;
            case 1:
                color = R.color.saffron;
                break;
            case 2:
                color = R.color.green;
                break;
            case 3:
                color = R.color.pink;
                break;
            case 4:
                color = R.color.orange;
                break;
            case 5:
                color = R.color.saffron;
                break;
            case 6:
                color = R.color.purple;
                break;
            case 7:
                color = R.color.blue;
                break;
            default:
                color = R.color.blue;
                break;
        }
        return color;
    }
}