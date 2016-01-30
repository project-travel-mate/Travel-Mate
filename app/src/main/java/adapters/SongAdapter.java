package adapters;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import Util.Constants;
import Util.Utils;
import objects.Song;
import tie.hackathon.travelguide.R;
import tie.hackathon.travelguide.Shopping_currentcity;

/**
 * Created by swati on 10/10/15.
 */public class SongAdapter extends BaseAdapter {

    private ArrayList<Song> songs;
    private LayoutInflater songInf;
    Context context;

    public SongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        context = c;
        songInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }


        TextView songView,artistView;
        ImageView iv;
    LinearLayout l;
    


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        //map to song layout

        LinearLayout songLay = (LinearLayout)songInf.inflate
                (R.layout.song, parent, false);
        //get title and artist views
         songView = (TextView)songLay.findViewById(R.id.song_title);
         artistView = (TextView)songLay.findViewById(R.id.song_artist);
        l = (LinearLayout) songLay.findViewById(R.id.ll);
        iv = (ImageView) songLay.findViewById(R.id.image);
        //get song using position
        final Song currSong = songs.get(position);
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(songs.get(position).getAlbum_id()));
        ContentResolver res = context.getContentResolver();
        InputStream in = null;
        iv.setImageResource(R.drawable.images);
        try {
            in = res.openInputStream(uri);
            Bitmap artwork = BitmapFactory.decodeStream(in);
            if (null != artwork) {
                iv.setImageBitmap(artwork);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

       /* l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/
        //set position as tag
        songLay.setTag(position);
        return songLay;




       // if rawArt is null then no cover art is embedded in the file or is not
// recognized as such.

    }




    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }


}
