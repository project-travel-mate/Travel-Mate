package io.github.project_travel_mate.travel.swipefragmentrealtime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.MapItem;
import utils.TravelmateSnackbars;

/**
 * Adapter to display a list of all fetched nearby places
 */
public class MapListItemAdapter extends BaseAdapter {


    final Context mContext;
    final List<MapItem> mMapItems;
    private final LayoutInflater mInflater;

    public MapListItemAdapter(Context context, List<MapItem> mapItems) {
        mContext = context;
        mMapItems = mapItems;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        MapListItemAdapter.ViewHolder holder;
        if (convertView != null) {
            holder = (MapListItemAdapter.ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.map_list_item, parent, false);
            holder = new MapListItemAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.title.setText(android.text.Html.fromHtml(mMapItems.get(position).getName()).toString());
        holder.description.setText(android.text.Html.fromHtml(mMapItems.get(position).getAddress()).toString());
        //when Call is clicked
        holder.call.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mMapItems.get(position).getNumber()));
            mContext.startActivity(intent);
        });
        //when Book is clicked
        holder.book.setOnClickListener(v -> {
            Intent browserIntent;
            try {
                browserIntent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse(mMapItems.get(position).getAddress()));
                mContext.startActivity(browserIntent);
            } catch (Exception e) {
                Activity activity = (Activity) mContext;
                TravelmateSnackbars.createSnackBar(activity.findViewById(R.id.list_view_realtime),
                        R.string.no_activity_for_browser, Snackbar.LENGTH_LONG).show();

            }
        });
        return convertView;

    }

    @Override
    public int getCount() {
        return mMapItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMapItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        @BindView(R.id.item_title)
        TextView title;
        @BindView(R.id.item_description)
        TextView description;
        @BindView(R.id.call)
        Button call;
        @BindView(R.id.book)
        Button book;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
