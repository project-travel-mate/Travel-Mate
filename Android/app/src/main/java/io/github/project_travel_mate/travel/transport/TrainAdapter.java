package io.github.project_travel_mate.travel.transport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;

class TrainAdapter extends BaseAdapter {

    private final Context mContext;
    private final JSONArray mFeedItems;
    private final LayoutInflater mInflater;

    TrainAdapter(Context context, JSONArray feedItems) {
        this.mContext = context;
        this.mFeedItems = feedItems;

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mFeedItems.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        try {
            return mFeedItems.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View vi = convertView;
        if (vi == null) {
            vi = mInflater.inflate(R.layout.train_listitem, parent, false);
            holder = new ViewHolder(vi);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        try {
            holder.title.setText(mFeedItems.getJSONObject(position).getString("name"));
            String descriptionText = "Train Number : " +
                    mFeedItems.getJSONObject(position).getString("train_number");
            String aTimeText = "Arrival Time : " + mFeedItems.getJSONObject(position).getString("arrival_time");
            String dTimeText = "Departure Time : " + mFeedItems.getJSONObject(position).getString("departure_time");
            holder.description.setText(descriptionText);
            holder.atime.setText(aTimeText);
            holder.dtime.setText(dTimeText);

            JSONArray ar = mFeedItems.getJSONObject(position).getJSONArray("days");
            for (int i = 0; i < ar.length(); i++) {
                int m = ar.getInt(i);
                if (m == 1)
                    continue;
                switch (i) {

                    case 0:
                        holder.d0.setText("N");
                        holder.d0.setBackgroundResource(R.color.red);
                        break;
                    case 1:
                        holder.d1.setText("N");
                        holder.d1.setBackgroundResource(R.color.red);
                        break;
                    case 2:
                        holder.d2.setText("N");
                        holder.d2.setBackgroundResource(R.color.red);
                        break;
                    case 3:
                        holder.d3.setText("N");
                        holder.d3.setBackgroundResource(R.color.red);
                        break;
                    case 4:
                        holder.d4.setText("N");
                        holder.d4.setBackgroundResource(R.color.red);
                        break;
                    case 5:
                        holder.d5.setText("N");
                        holder.d5.setBackgroundResource(R.color.red);
                        break;
                    case 6:
                        holder.d6.setText("N");
                        holder.d6.setBackgroundResource(R.color.red);
                        break;
                }

            }

            holder.more.setOnClickListener(view -> {
                Intent browserIntent = null;
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.cleartrip.com/trains/" +
                                    mFeedItems.getJSONObject(position).getString("train_number")));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                mContext.startActivity(browserIntent);

            });
            holder.book.setOnClickListener(view -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.cleartrip.com/trains/" +
                                    mFeedItems.getJSONObject(position).getString("train_number")));
                    mContext.startActivity(intent);
                } catch (JSONException e12) {
                    e12.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR : ", "Message : " + e.getMessage());
        }
        return vi;
    }

    class ViewHolder {
        @BindView(R.id.train_name)
        TextView title;
        @BindView(R.id.traintype)
        TextView description;
        @BindView(R.id.arr)
        TextView atime;
        @BindView(R.id.dep)
        TextView dtime;
        @BindView(R.id.more)
        Button more;
        @BindView(R.id.book)
        Button book;
        @BindView(R.id.d0)
        TextView d0;
        @BindView(R.id.d1)
        TextView d1;
        @BindView(R.id.d2)
        TextView d2;
        @BindView(R.id.d3)
        TextView d3;
        @BindView(R.id.d4)
        TextView d4;
        @BindView(R.id.d5)
        TextView d5;
        @BindView(R.id.d6)
        TextView d6;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}