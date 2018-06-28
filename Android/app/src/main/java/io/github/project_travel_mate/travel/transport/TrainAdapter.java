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
        View vi = convertView;
        if (vi == null)
            vi = mInflater.inflate(R.layout.train_listitem, parent, false);

        TextView title = vi.findViewById(R.id.bus_name);
        TextView description = vi.findViewById(R.id.bustype);
        TextView atime = vi.findViewById(R.id.arr);
        TextView dtime = vi.findViewById(R.id.dep);
        Button more = vi.findViewById(R.id.more);
        Button book = vi.findViewById(R.id.book);
        TextView d0, d1, d2, d3, d4, d5, d6;
        d0 = vi.findViewById(R.id.d0);
        d1 = vi.findViewById(R.id.d1);
        d2 = vi.findViewById(R.id.d2);
        d3 = vi.findViewById(R.id.d3);
        d4 = vi.findViewById(R.id.d4);
        d5 = vi.findViewById(R.id.d5);
        d6 = vi.findViewById(R.id.d6);

        try {
            title.setText(mFeedItems.getJSONObject(position).getString("name"));
            String descriptionText = "Train Number : " +
                    mFeedItems.getJSONObject(position).getString("train_number");
            String aTimeText = "Arrival Time : " + mFeedItems.getJSONObject(position).getString("arrival_time");
            String dTimeText = "Departure Time : " + mFeedItems.getJSONObject(position).getString("departure_time");
            description.setText(descriptionText);
            atime.setText(aTimeText);
            dtime.setText(dTimeText);

            JSONArray ar = mFeedItems.getJSONObject(position).getJSONArray("days");
            for (int i = 0; i < ar.length(); i++) {
                int m = ar.getInt(i);
                if (m == 1)
                    continue;
                switch (i) {

                    case 0:
                        d0.setText("N");
                        d0.setBackgroundResource(R.color.red);
                        break;
                    case 1:
                        d1.setText("N");
                        d1.setBackgroundResource(R.color.red);
                        break;
                    case 2:
                        d2.setText("N");
                        d2.setBackgroundResource(R.color.red);
                        break;
                    case 3:
                        d3.setText("N");
                        d3.setBackgroundResource(R.color.red);
                        break;
                    case 4:
                        d4.setText("N");
                        d4.setBackgroundResource(R.color.red);
                        break;
                    case 5:
                        d5.setText("N");
                        d5.setBackgroundResource(R.color.red);
                        break;
                    case 6:
                        d6.setText("N");
                        d6.setBackgroundResource(R.color.red);
                        break;
                }

            }

            more.setOnClickListener(view -> {
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
            book.setOnClickListener(view -> {
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

}