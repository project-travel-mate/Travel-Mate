package io.github.project_travel_mate.friend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.User;

class MyFriendsAdapter extends ArrayAdapter<User> {

    private final Context mContext;
    private final List<User> mFriends;
    private LayoutInflater mInflater;

    public MyFriendsAdapter(Context context,
                   List<User> friends) {
        super(context, R.layout.friend_listitem, friends);
        this.mContext = context;
        this.mFriends = friends;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        MyFriendsAdapter.ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.friend_listitem, parent, false);
            holder = new MyFriendsAdapter.ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (MyFriendsAdapter.ViewHolder) view.getTag();
        }

        Picasso.with(mContext).load(mFriends.get(position).getImage()).placeholder(R.drawable.default_user_icon)
                .error(R.drawable.default_user_icon)
                .into(holder.friendImage);
        String fullName = mFriends.get(position).getFirstName() + " " + mFriends.get(position).getLastName();
        holder.friendDisplayName.setText(fullName);

        view.setOnClickListener(v -> {
            Intent intent = FriendsProfileActivity.getStartIntent(mContext, mFriends.get(position).getId());
            mContext.startActivity(intent);
        });

        return view;
    }

    class ViewHolder {

        @BindView(R.id.profile_image)
        ImageView friendImage;
        @BindView(R.id.friend_display_name)
        TextView friendDisplayName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
