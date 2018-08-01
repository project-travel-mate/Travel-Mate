package io.github.project_travel_mate.destinations.description;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Tweet;

class TweetsAdapter extends ArrayAdapter<Tweet> {
    private final Activity mContext;
    private final List<Tweet> mTweetsList;

    TweetsAdapter(Activity context, List<Tweet> tweets) {
        super(context, R.layout.trip_listitem, tweets);
        this.mContext = context;
        this.mTweetsList = tweets;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = Objects.requireNonNull(mInflater).inflate(R.layout.tweet_listitem, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        holder.name.setText(mTweetsList.get(position).getName());

        view.setOnClickListener(v -> {
            Intent intent = TweetsDescriptionActivity.getStartIntent(mContext, mTweetsList.get(position).getName());
            mContext.startActivity(intent);
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.tagmain)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}