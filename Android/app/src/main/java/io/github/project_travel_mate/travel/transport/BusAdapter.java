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

// Sets adapter for bus list
class BusAdapter extends BaseAdapter {

    private final Context mContext;
    private final JSONArray mFeedItems;
    private final LayoutInflater mInflater;

    BusAdapter(Context context, JSONArray feedItems) {
        this.mContext = context;
        this.mFeedItems = feedItems;

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFeedItems.length();
    }

    @Override
    public Object getItem(int position) {
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
            vi = mInflater.inflate(R.layout.bus_listitem, parent, false);

        TextView title = vi.findViewById(R.id.bus_name);
        TextView description = vi.findViewById(R.id.bustype);
        TextView add = vi.findViewById(R.id.add);
        Button contact = vi.findViewById(R.id.call);
        Button url = vi.findViewById(R.id.book);
        TextView fair = vi.findViewById(R.id.fair);


        try {
            title.setText(mFeedItems.getJSONObject(position).getString("name"));
            description.setText(mFeedItems.getJSONObject(position).getString("type"));
            add.setText(mFeedItems.getJSONObject(position).getString("dep_add"));

            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    try {
                        intent.setData(Uri.parse("tel:" + mFeedItems.getJSONObject(position).getString("contact")));
                        mContext.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent;
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://redbus.in"));

                    mContext.startActivity(browserIntent);
                }
            });
            String fairText = mFeedItems.getJSONObject(position).getString("fair") + " Rs";
            fair.setText(fairText);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR : ", "Message : " + e.getMessage());
        }
        return vi;
    }
}
