package adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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


public class SugMusic_adapter extends BaseAdapter {

    Context context;
    JSONArray FeedItems;
    private static LayoutInflater inflater = null;

    public SugMusic_adapter(Context context, JSONArray FeedItems) {
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
            vi = inflater.inflate(R.layout.sugsong_listitem, null);

        TextView Title = (TextView) vi.findViewById(R.id.VideoTitle);
        TextView Description = (TextView) vi.findViewById(R.id.VideoDescription);
        ImageView VideoThumbnail = (ImageView) vi.findViewById(R.id.PlayButton);


        try {
            Title.setText(FeedItems.getJSONObject(position).getString("title"));
            Description.setText(FeedItems.getJSONObject(position).getString("artist"));


            // imageLoader.DisplayImage(FeedItems.getJSONObject(position).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail"), VideoThumbnail, null);

//            Picasso.with(context).load(FeedItems.getJSONObject(position).getString("image")).into(VideoThumbnail);
       //     Log.e("FeedItem", FeedItems.getJSONObject(position).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail") + " ");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("eroro",e.getMessage()+" ");
        }

        VideoThumbnail.setOnClickListener(new View.OnClickListener() {
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