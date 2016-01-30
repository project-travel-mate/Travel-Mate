package adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import tie.hackathon.travelguide.R;

/**
 * Created by swati on 9/10/15.
 */


public class Shop_adapter extends BaseAdapter {

    Context context;
    JSONArray FeedItems;
    private static LayoutInflater inflater = null;

    public Shop_adapter(Context context, JSONArray FeedItems) {
        this.context = context;
        this.FeedItems = FeedItems;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return FeedItems.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
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
            vi = inflater.inflate(R.layout.shop_listitem, null);

        TextView Title = (TextView) vi.findViewById(R.id.VideoTitle);
        TextView Description = (TextView) vi.findViewById(R.id.VideoDescription);
        ImageView iv = (ImageView) vi.findViewById(R.id.VideoThumbnail);


        try {
            String x = FeedItems.getJSONObject(position).getString("name");
            x = Html.fromHtml(x).toString();
            Title.setText(x);


            String DescriptionText = FeedItems.getJSONObject(position).getString("value");

            DescriptionText = Html.fromHtml(DescriptionText).toString();
            Description.setText(DescriptionText + " Rs");

            Picasso.with(context).load(FeedItems.getJSONObject(position).getString("image")).into(iv);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("eroro", e.getMessage() + " ");
        }

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = null;
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FeedItems.getJSONObject(position).getString("url")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                context.startActivity(browserIntent);
            }
        });


        return vi;
    }

}