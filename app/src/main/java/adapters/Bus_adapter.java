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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import tie.hackathon.travelguide.R;

/**
 * Created by swati on 9/10/15.
 */


public class Bus_adapter extends BaseAdapter {

    Context context;
    JSONArray FeedItems;
    private static LayoutInflater inflater = null;

    public Bus_adapter(Context context, JSONArray FeedItems) {
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
            vi = inflater.inflate(R.layout.bus_listitem, null);

        TextView Title = (TextView) vi.findViewById(R.id.bus_name);
        TextView Description = (TextView) vi.findViewById(R.id.bustype);
        TextView add = (TextView) vi.findViewById(R.id.add);
        Button contact = (Button) vi.findViewById(R.id.call);
        Button url = (Button) vi.findViewById(R.id.book);
        TextView fair = (TextView) vi.findViewById(R.id.fair);


        try {
            Title.setText(FeedItems.getJSONObject(position).getString("name"));
            Description.setText(FeedItems.getJSONObject(position).getString("type"));
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
                    Intent browserIntent = null;
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://redbus.in"));

                    context.startActivity(browserIntent);
                }
            });

            fair.setText(FeedItems.getJSONObject(position).getString("fair")+" Rs");
         } catch (JSONException e) {
            e.printStackTrace();
            Log.e("eroro",e.getMessage()+" ");
        }

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return vi;
    }

}