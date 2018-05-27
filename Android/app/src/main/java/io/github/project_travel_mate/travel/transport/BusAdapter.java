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

    final Context context;
    final JSONArray FeedItems;
    private final LayoutInflater inflater;

    BusAdapter(Context context, JSONArray feedItems) {
        this.context = context;
        this.FeedItems = feedItems;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return FeedItems.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return FeedItems.getJSONObject(position);
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
            vi = inflater.inflate(R.layout.bus_listitem, (ViewGroup) null);

        TextView title = vi.findViewById(R.id.bus_name);
        TextView description = vi.findViewById(R.id.bustype);
        TextView add = vi.findViewById(R.id.add);
        Button contact = vi.findViewById(R.id.call);
        Button url = vi.findViewById(R.id.book);
        TextView fair = vi.findViewById(R.id.fair);


        try {
            title.setText(FeedItems.getJSONObject(position).getString("name"));
            description.setText(FeedItems.getJSONObject(position).getString("type"));
            add.setText(FeedItems.getJSONObject(position).getString("dep_add"));

            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    try {
                        intent.setData(Uri.parse("tel:" + FeedItems.getJSONObject(position).getString("contact")));
                        context.startActivity(intent);
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

                    context.startActivity(browserIntent);
                }
            });
            String fairText = FeedItems.getJSONObject(position).getString("fair") + " Rs";
            fair.setText(fairText);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR : ", e.getMessage() + " ");
        }
        return vi;
    }
}
