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

import org.json.JSONArray;
import org.json.JSONException;

import tie.hackathon.travelguide.R;

/**
 * Created by swati on 9/10/15.
 */


public class mood_adapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater = null;

    public mood_adapter(Context context) {
        this.context = context;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 5;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    TextView mood;
    ImageView mood_image;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.mood_item, null);

        mood = (TextView) vi.findViewById(R.id.moodtext);
        mood_image = (ImageView) vi.findViewById(R.id.mood);


        switch (position){


            case 0 : mood.setText("Very Happy");mood_image.setImageResource(R.drawable.veryhappy);break;
            case 1 : mood.setText("Happy");mood_image.setImageResource(R.drawable.happy);break;
            case 2 : mood.setText("Normal");mood_image.setImageResource(R.drawable.normal);break;
            case 3 : mood.setText("Sad");mood_image.setImageResource(R.drawable.sap);break;
            case 4 : mood.setText("Very Sad");mood_image.setImageResource(R.drawable.verysad);break;
        }

        return vi;
    }

}