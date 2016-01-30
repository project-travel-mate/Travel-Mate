package adapters;

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

import tie.hackathon.travelguide.R;

/**
 * Created by swati on 9/10/15.
 */


public class Music_adapter extends BaseAdapter {

    Context context;
    JSONArray FeedItems;
    private static LayoutInflater inflater = null;

    public Music_adapter(Context context, JSONArray FeedItems) {
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
        Button call,map,book;
        call = (Button) vi.findViewById(R.id.call);
        map = (Button) vi.findViewById(R.id.map);
        book = (Button) vi.findViewById(R.id.book);

        try {
            Title.setText(FeedItems.getJSONObject(position).getString("name"));
            Description.setText(FeedItems.getJSONObject(position).getString("address"));

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    try {
                        intent.setData(Uri.parse("tel:"+FeedItems.getJSONObject(position).getString("phone")));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = null;
                    try {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=" +
                                        FeedItems.getJSONObject(position).getString("name") +
                                        "+(name)+@" +
                                        FeedItems.getJSONObject(position).getString("lat") +
                                        "," +
                                        FeedItems.getJSONObject(position).getString("lng")
                        ));
                        context.startActivity(browserIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = null;
                    try {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FeedItems.getJSONObject(position).getString("websites")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(browserIntent);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("eroro",e.getMessage()+" ");
        }

        return vi;
    }

}