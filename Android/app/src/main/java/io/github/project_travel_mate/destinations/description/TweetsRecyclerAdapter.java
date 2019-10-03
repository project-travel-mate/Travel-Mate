package io.github.project_travel_mate.destinations.description;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Tweet;

/**
 * @author nitinnatural@gmail.com
 * */

class TweetsRecyclerAdapter extends RecyclerView.Adapter<TweetsRecyclerAdapter.ViewHolder> {
    private final List<Tweet> mTweetsList;
    TweetsAdapterListener listener;

    TweetsRecyclerAdapter(List<Tweet> tweets, TweetsAdapterListener listener) {
        this.mTweetsList = tweets;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending_tweet, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Tweet tweet = mTweetsList.get(position);
        h.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return mTweetsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tagmain)
        TextView name;

        @BindView(R.id.iv_type)
        ImageView ivType;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onTrendingTweetsClicked(mTweetsList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Tweet t) {

            name.setSelected(true); // to make sure marquee works

            if (t.getName().toLowerCase().contains("#")) {
                ivType.setImageResource(R.drawable.ic_hash);
                StringBuilder sb = new StringBuilder(t.getName());
                sb.deleteCharAt(0);
                name.setText(sb.toString());

            } else {
                ivType.setImageResource(R.drawable.ic_dot);
                name.setText(t.getName());
            }


        }
    }



    public interface TweetsAdapterListener {
        void onTrendingTweetsClicked(Tweet tweet);
    }
}