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


public class Books_adapter extends BaseAdapter {

    Context context;
    JSONArray FeedItems;
    private static LayoutInflater inflater = null;

    public Books_adapter(Context context, JSONArray FeedItems) {
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
            vi = inflater.inflate(R.layout.book_listitem, null);

        TextView Title = (TextView) vi.findViewById(R.id.VideoTitle);
        TextView Description = (TextView) vi.findViewById(R.id.VideoDescription);
        ImageView VideoThumbnail = (ImageView) vi.findViewById(R.id.VideoThumbnail);


        try {
            Title.setText(FeedItems.getJSONObject(position).getJSONObject("volumeInfo").getString("title"));
            if(FeedItems.getJSONObject(position).getJSONObject("volumeInfo").has("description")) {
                String DescriptionText = FeedItems.getJSONObject(position).getJSONObject("volumeInfo").getString("description");
                if (DescriptionText.equals("")) {
                    Description.setText("No description for this book.");
                } else {
                    Description.setText(DescriptionText);
                }
            }

           // imageLoader.DisplayImage(FeedItems.getJSONObject(position).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail"), VideoThumbnail, null);

            Picasso.with(context).load(FeedItems.getJSONObject(position).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail")).into(VideoThumbnail);
       //     Log.e("FeedItem", FeedItems.getJSONObject(position).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail") + " ");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("eroro",e.getMessage()+" ");
        }

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = null;
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FeedItems.getJSONObject(position).getJSONObject("volumeInfo").getString("previewLink")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                context.startActivity(browserIntent);
            }
        });

        return vi;
    }

}