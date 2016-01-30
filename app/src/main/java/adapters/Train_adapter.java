package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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


/**
 * Created by Sidharth Patro on 22-Jun-15.
 */
public class Train_adapter extends BaseAdapter {

    Context context;
    JSONArray FeedItems;
    private static LayoutInflater inflater = null;

    public Train_adapter(Context context, JSONArray FeedItems) {
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
            vi = inflater.inflate(R.layout.train_listitem, null);

        TextView Title = (TextView) vi.findViewById(R.id.bus_name);
        TextView Description = (TextView) vi.findViewById(R.id.bustype);
        TextView atime = (TextView) vi.findViewById(R.id.arr);
        TextView dtime = (TextView) vi.findViewById(R.id.dep);
        Button more = (Button) vi.findViewById(R.id.more);
        Button book = (Button) vi.findViewById(R.id.book);
      TextView d0,d1,d2,d3,d4,d5,d6;
        d0 = (TextView) vi.findViewById(R.id.d0);
        d1 = (TextView) vi.findViewById(R.id.d1);
        d2 = (TextView) vi.findViewById(R.id.d2);
        d3 = (TextView) vi.findViewById(R.id.d3);
        d4 = (TextView) vi.findViewById(R.id.d4);
        d5 = (TextView) vi.findViewById(R.id.d5);
        d6 = (TextView) vi.findViewById(R.id.d6);

        try {
            Title.setText(FeedItems.getJSONObject(position).getString("name"));
            Description.setText("Train Number : " + FeedItems.getJSONObject(position).getString("train_number"));
            atime.setText("Arrival Time : " +FeedItems.getJSONObject(position).getString("arrival_time"));
            dtime.setText("Departure Time : "+FeedItems.getJSONObject(position).getString("departure_time"));

            JSONArray ar = FeedItems.getJSONObject(position).getJSONArray("days");
            for(int i = 0;i<ar.length();i++){
                int m= ar.getInt(i);
                if(m==1)
                    continue;
                switch(i){

                    case 0 : d0.setText("N");
                        d0.setBackgroundResource(R.color.red);break;
                    case 1 : d1.setText("N");
                        d1.setBackgroundResource(R.color.red);break;
                    case 2 : d2.setText("N");
                        d2.setBackgroundResource(R.color.red);break;
                    case 3 : d3.setText("N");
                        d3.setBackgroundResource(R.color.red);break;
                    case 4 : d4.setText("N");
                        d4.setBackgroundResource(R.color.red);break;
                    case 5 : d5.setText("N");
                        d5.setBackgroundResource(R.color.red);break;
                    case 6 : d6.setText("N");
                        d6.setBackgroundResource(R.color.red);break;
                }

            }

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = null;
                    try {
                        browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.cleartrip.com/trains/"+
                                        FeedItems.getJSONObject(position).getString("train_number")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(browserIntent);

                }
            });
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    try {
                        intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.cleartrip.com/trains/"+
                                        FeedItems.getJSONObject(position).getString("train_number")));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
         } catch (JSONException e) {
            e.printStackTrace();
            Log.e("eroro",e.getMessage()+" ");
        }



        return vi;
    }

}