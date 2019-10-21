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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;

class ShoppingAdapter extends BaseAdapter {

    private final Context mContext;
    private final JSONArray mFeedItems;
    private final LayoutInflater mInflater;

    ShoppingAdapter(Context context, JSONArray feedItems) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.shop_listitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            String name = mFeedItems.getJSONObject(position).getString("name");
            name = Html.fromHtml(name).toString();
            holder.title.setText(name);

            String descriptionText = mFeedItems.getJSONObject(position).getString("value");

            descriptionText = Html.fromHtml(descriptionText).toString() + " "
                    + mFeedItems.getJSONObject(position).getString("currency");
            holder.description.setText(descriptionText);

            Picasso.with(mContext).load(mFeedItems.getJSONObject(position).getString("image")).into(holder.iv);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        convertView.setOnClickListener(view -> {
            Intent browserIntent = null;
            try {
                browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(mFeedItems.getJSONObject(position).getString("url")));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            mContext.startActivity(browserIntent);
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.VideoTitle)
        TextView title;
        @BindView(R.id.VideoDescription)
        TextView description;
        @BindView(R.id.VideoThumbnail)
        ImageView iv;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}