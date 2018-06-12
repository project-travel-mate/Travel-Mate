package io.github.project_travel_mate.travel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import io.github.project_travel_mate.R;

class ShoppingAdapter extends BaseAdapter {

    private final Context context;
    private final JSONArray feedItems;
    private final LayoutInflater inflater;

    ShoppingAdapter(Context context, JSONArray feedItems) {
        this.context = context;
        this.feedItems = feedItems;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return feedItems.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return feedItems.getJSONObject(position);
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
            vi = inflater.inflate(R.layout.shop_listitem, parent, false);

        TextView title = vi.findViewById(R.id.VideoTitle);
        TextView description = vi.findViewById(R.id.VideoDescription);
        ImageView iv = vi.findViewById(R.id.VideoThumbnail);

        try {
            String name = feedItems.getJSONObject(position).getString("name");
            name = Html.fromHtml(name).toString();
            title.setText(name);

            String descriptionText = feedItems.getJSONObject(position).getString("value");

            descriptionText = Html.fromHtml(descriptionText).toString() + " "
                + feedItems.getJSONObject(position).getString("currency");
            description.setText(descriptionText);

            Picasso.with(context).load(feedItems.getJSONObject(position).getString("image")).into(iv);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = null;
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(feedItems.getJSONObject(position).getString("url")));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                context.startActivity(browserIntent);
            }
        });
        return vi;
    }
}