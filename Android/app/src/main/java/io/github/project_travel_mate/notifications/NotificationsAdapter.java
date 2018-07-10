package io.github.project_travel_mate.notifications;


import android.app.Activity;
import android.content.Context;
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
import objects.Notification;

class NotificationsAdapter extends ArrayAdapter<Notification> {

    private final Activity mContext;
    private final List<Notification> mNotifications;

    NotificationsAdapter(Context context, List<Notification> items) {
        super(context, R.layout.notification_listitem, items);
        mNotifications = items;
        this.mContext = (Activity) context;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        NotificationsAdapter.ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = Objects.requireNonNull(mInflater).inflate(R.layout.notification_listitem, parent, false);
            holder = new NotificationsAdapter.ViewHolder(view);
            view.setTag(holder);
        } else
            holder = (NotificationsAdapter.ViewHolder) view.getTag();

        holder.name.setText(mNotifications.get(position).getText());
        if (mNotifications.get(position).isRead()) {
            holder.readStatus.setVisibility(View.INVISIBLE);
        }
        view.setOnClickListener(view1 -> mNotifications.get(position).setRead(true));
        view.setFocusable(false);
        return view;
    }

    class ViewHolder {
        @BindView(R.id.text)
        TextView name;
        @BindView(R.id.read_status)
        View readStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}