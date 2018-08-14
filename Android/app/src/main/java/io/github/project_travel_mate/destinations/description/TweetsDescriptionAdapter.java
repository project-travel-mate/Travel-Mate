package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.TweetDescription;
import utils.CircleImageView;

public class TweetsDescriptionAdapter extends RecyclerView.Adapter<TweetsDescriptionAdapter.ViewHolder> {

    private Context mContext;
    private List<TweetDescription> mTweets;

    public TweetsDescriptionAdapter(Context context, List<TweetDescription> tweets) {
        mContext = context;
        mTweets = tweets;
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.tweets_description_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TweetDescription tweet = mTweets.get(position);
        //Convert date to dd-mm-yy format
        final Calendar cal = Calendar.getInstance();
        try {
            cal.setTimeInMillis(Long.parseLong(tweet.getCreatedAt()) * 1000);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        final String timeString =
                new SimpleDateFormat("dd-MMM-YY", Locale.US).format(cal.getTime());

        Picasso.with(mContext).load(tweet.getAvatar()).placeholder(R.drawable.default_user_icon)
                .error(R.drawable.default_user_icon)
                .into(holder.profileImage);
        holder.retweetsCount.setText(tweet.getRetweetsCount());
        holder.favCount.setText(tweet.getFavCount());
        holder.createdAt.setText(timeString);
        holder.tweetText.setText(tweet.getTweetText());
        holder.username.setText(tweet.getUserScreenName());
        holder.userScreenName.setText(String.format(mContext.getString(R.string.username), tweet.getUsername()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_image)
        CircleImageView profileImage;
        @BindView(R.id.user_full_name)
        TextView username;
        @BindView(R.id.user_screen_name)
        TextView userScreenName;
        @BindView(R.id.created_at)
        TextView createdAt;
        @BindView(R.id.retweets_count)
        TextView retweetsCount;
        @BindView(R.id.fav_count)
        TextView favCount;
        @BindView(R.id.tweet_text)
        TextView tweetText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
