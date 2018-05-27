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
import io.github.project_travel_mate.destinations.description.FinalCityInfo;
import io.github.project_travel_mate.destinations.funfacts.FunFacts;
import objects.City;
import views.FontTextView;

class CityAdapter extends BaseFlipAdapter<City> {

    private Activity context;
    private final int pages = 3;
    private Typeface tex;
    private final int[] idsInterest = {R.id.interest_1, R.id.interest_2, R.id.interest_3, R.id.interest_4};


    CityAdapter(Context context, List<City> items, FlipSettings settings) {
        super(context, items, settings);
        this.context = (Activity) context;
        tex = Typeface.createFromAsset(context.getAssets(), "fonts/texgyreadventor-bold.otf");
    }

    @Override
    public View getPage(int position, View convertView, ViewGroup parent, final City friend1, final City friend2) {
        final CitiesHolder holder;

        if (convertView == null) {
            holder = new CitiesHolder();
            convertView = context.getLayoutInflater().inflate(R.layout.home_city_merge_page, parent, false);
            holder.leftAvatar = convertView.findViewById(R.id.first);
            holder.rightAvatar = convertView.findViewById(R.id.second);
            holder.left = convertView.findViewById(R.id.name1);
            holder.right = convertView.findViewById(R.id.name2);
            holder.infoPage = context.getLayoutInflater().inflate(R.layout.home_city_info, parent, false);
            holder.nickName = holder.infoPage.findViewById(R.id.nickname);
            holder.fv1 = (FontTextView) holder.infoPage.findViewById(R.id.interest_1);
            holder.fv2 = (FontTextView) holder.infoPage.findViewById(R.id.interest_2);
            holder.fv3 = (FontTextView) holder.infoPage.findViewById(R.id.interest_3);
            holder.fv4 = (FontTextView) holder.infoPage.findViewById(R.id.interest_4);

            for (int id : idsInterest)
                holder.interests.add((TextView) holder.infoPage.findViewById(id));

            convertView.setTag(holder);
        } else {
            holder = (CitiesHolder) convertView.getTag();
        }

        switch (position) {
            case 1:
                Picasso.with(context).
                        load(friend1.getAvatar()).
                        placeholder(R.drawable.delhi).
                        into(holder.leftAvatar);
                holder.left.setTypeface(tex);
                holder.left.setText(friend1.getNickname());

                if (friend2 != null) {
                    holder.right.setText(friend2.getNickname());
                    holder.right.setTypeface(tex);
                    Picasso.with(context).
                            load(friend2.getAvatar()).
                            placeholder(R.drawable.delhi).
                            into(holder.rightAvatar);
                }
                break;
            default:
                fillHolder(holder, position == 0 ? friend1 : friend2);
                holder.infoPage.setTag(holder);
                return holder.infoPage;
        }
        return convertView;
    }

    @Override
    public int getPagesCount() {
        return pages;
    }

    private void fillHolder(CitiesHolder holder, final City friend) {
        if (friend == null)
            return;
        Iterator<TextView> iViews = holder.interests.iterator();
        Iterator<String> iInterests = friend.getInterests().iterator();
        while (iViews.hasNext() && iInterests.hasNext())
            iViews.next().setText(iInterests.next());
        holder.infoPage.setBackgroundColor(context.getResources().getColor(friend.getBackground()));
        holder.nickName.setText(friend.getNickname());

        holder.nickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.fv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FinalCityInfo.class);
                i.putExtra("id_", friend.getId());
                i.putExtra("name_", friend.getNickname());
                i.putExtra("image_", friend.getAvatar());
                context.startActivity(i);
            }
        });

        holder.fv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FunFacts.class);
                i.putExtra("id_", friend.getId());
                i.putExtra("name_", friend.getNickname());
                context.startActivity(i);
            }
        });

        holder.fv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/?ie=UTF8&hq=&ll=" +
                        friend.getLa() +
                        "," +
                        friend.getLo() +
                        "&z=13")); // zoom level
                context.startActivity(browserIntent);
            }
        });

        holder.fv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                context.startActivity(browserIntent);
            }
        });
    }

    class CitiesHolder {
        ImageView leftAvatar;
        ImageView rightAvatar;
        View infoPage;
        TextView fv1, fv2, fv3, fv4;
        TextView left, right;
        final List<TextView> interests = new ArrayList<>();
        TextView nickName;
    }
}