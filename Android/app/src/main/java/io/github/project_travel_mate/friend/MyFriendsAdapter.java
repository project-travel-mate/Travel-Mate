package io.github.project_travel_mate.friend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.User;


class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.MyFriendsViewHolder> {

    private final Context mContext;
    private final List<User> mFriends;
    private LayoutInflater mInflater;

    MyFriendsAdapter(Context context, List<User> friends) {
        this.mContext = context;
        this.mFriends = friends;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.friend_listitem, parent, false);
        return new MyFriendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFriendsViewHolder holder, int position) {

        Picasso.with(mContext).load(mFriends.get(position).getImage()).placeholder(R.drawable.default_user_icon)
                .error(R.drawable.default_user_icon)
                .into(holder.friendImage);
        String fullName = mFriends.get(position).getFirstName() + " " + mFriends.get(position).getLastName();
        holder.friendDisplayName.setText(fullName);

        holder.my_friends_linear_layout.setOnClickListener(v -> {
            Intent intent = FriendsProfileActivity.getStartIntent(mContext, mFriends.get(position).getId());
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    class MyFriendsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_image)
        ImageView friendImage;
        @BindView(R.id.friend_display_name)
        TextView friendDisplayName;
        @BindView(R.id.my_friends_linear_layout)
        LinearLayout my_friends_linear_layout;

        MyFriendsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
