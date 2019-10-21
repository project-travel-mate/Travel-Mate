package io.github.project_travel_mate.notifications;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.mytrips.MyTripInfoActivity;
import objects.Notification;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

class NotificationsAdapter extends ArrayAdapter<Notification> {

    private final Activity mContext;
    private final List<Notification> mNotifications;
    private String mToken;
    private Handler mHandler;

    NotificationsAdapter(Context context, List<Notification> items) {
        super(context, R.layout.notification_listitem, items);
        mNotifications = items;
        this.mContext = (Activity) context;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        NotificationsAdapter.ViewHolder holder;
        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = Objects.requireNonNull(mInflater).inflate(R.layout.notification_listitem, parent, false);
            holder = new NotificationsAdapter.ViewHolder(view);
            view.setTag(holder);
        } else
            holder = (NotificationsAdapter.ViewHolder) view.getTag();

        holder.name.setText(mNotifications.get(position).getText());
        holder.notificationTime
                .setText(mNotifications.get(position).getCreatedAt());
        if (mNotifications.get(position).isRead()) {
            holder.readStatus.setVisibility(View.INVISIBLE);
        } else {
            holder.readStatus.setVisibility(View.VISIBLE);
        }
        view.setFocusable(false);
        view.setOnClickListener(view12 -> {
            if (!mNotifications.get(position).isRead()) {
                markAsRead(position);
            }
            openTripPage(position);
        });
        return view;
    }

    public void markAsRead(final int position) {
        String uri;
        uri = API_LINK_V2 + "mark-notification/" + mNotifications.get(position).getId();
        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
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
                });
            }
        });
    }

    public void openTripPage(int position) {
        Intent intent = MyTripInfoActivity.getStartIntent(mContext, mNotifications.get(position).getTrip());
        mContext.startActivity(intent);
    }

    class ViewHolder {
        @BindView(R.id.text)
        TextView name;
        @BindView(R.id.read_status)
        View readStatus;
        @BindView(R.id.notification_time)
        TextView notificationTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}