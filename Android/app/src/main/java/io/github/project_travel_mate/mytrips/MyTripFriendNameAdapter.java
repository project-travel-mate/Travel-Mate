package io.github.project_travel_mate.mytrips;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Trip;
import objects.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.EXTRA_MESSAGE_TRIP_OBJECT;
import static utils.Constants.USER_TOKEN;

class MyTripFriendNameAdapter extends ArrayAdapter<User> {
    private final Activity mContext;
    private final List<User> mUsers;
    private Trip mTrip;
    private String mFriendId;
    private Handler mHandler;

    MyTripFriendNameAdapter(Activity context, List<User> users, Trip trip, String id) {
        super(context, R.layout.home_city_listitem, users);
        this.mContext = context;
        this.mUsers = users;
        this.mFriendId = id;
        this.mTrip = trip;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = Objects.requireNonNull(mInflater).inflate(R.layout.trip_friend_listitem, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        holder.name.setText(mUsers.get(position).getFirstName());
        Picasso.with(mContext).load(mUsers.get(position).getImage()).placeholder(R.drawable.icon_profile)
                .error(R.drawable.icon_profile).into(holder.imageView);
        holder.removeFriend.setOnClickListener(v -> showFriendRemoveDialog(mUsers.get(position).getFirstName()));
        return view;
    }

    /**
     * @param friendFirstName first name of friend to be removed
     *                        to display in alert dialog's message
     */
    private void showFriendRemoveDialog(String friendFirstName) {
        ContextThemeWrapper crt = new ContextThemeWrapper(mContext, R.style.AlertDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(crt);
        builder.setMessage(String.format(mContext.getString(R.string.remove_friend_message), friendFirstName))
                .setPositiveButton(R.string.positive_button,
                        (dialog, which) -> removeFriend())
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> {

                        });
        builder.create().show();
    }

    class ViewHolder {

        @BindView(R.id.friend_name)
        TextView name;
        @BindView(R.id.friend_profile_picture)
        ImageView imageView;
        @BindView(R.id.remove_friend)
        ImageView removeFriend;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    /**
     * removes friend from a trip
     */
    private void removeFriend() {

        mHandler = new Handler(Looper.getMainLooper());

        //getting user token for authorization
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String token =  preferences.getString(USER_TOKEN, "null");

        String uri = API_LINK_V2 + "remove-friend-from-trip/" + mTrip.getId() + "/" + mFriendId;
        Log.v("EXECUTING", uri);
        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + token)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                mHandler.post(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(mContext, R.string.removed_friend_message,
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, MyTripInfoActivity.class);
                        intent.putExtra(EXTRA_MESSAGE_TRIP_OBJECT, mTrip);
                        mContext.finish();
                        mContext.startActivity(intent);
                    } else
                        Toast.makeText(mContext, res, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}

