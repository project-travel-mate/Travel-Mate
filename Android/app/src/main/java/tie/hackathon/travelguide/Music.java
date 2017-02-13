package tie.hackathon.travelguide;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Util.Constants;
import butterknife.BindView;
import objects.MusicController;
import objects.Song;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Music extends AppCompatActivity implements MediaController.MediaPlayerControl {

    @BindView(R.id.iv)          ImageView   iv;
    @BindView(R.id.music_list)  ListView    songView;

    private SharedPreferences s;
    private SharedPreferences.Editor e;
    private TwoWayView sugsongView;
    private ArrayList<Song> songList;
    private Handler mHandler;
    private MusicController controller;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private boolean paused = false, playbackPaused = false;

    private final ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicSrv = binder.getService();
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler(Looper.getMainLooper());

        songList = new ArrayList<>();

        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();

        getSongList();
        setController();

        Integer moods = Integer.parseInt(s.getString(Constants.CURRENT_SCORE, "2"));

        if (moods > 10)
            iv.setBackgroundResource(R.color.verhappy);
        else if (moods > 2)
            iv.setBackgroundResource(R.color.happy);
        else if (moods > -2)
            iv.setBackgroundResource(R.color.normal);
        else if (moods > -10)
            iv.setBackgroundResource(R.color.sad);
        else
            iv.setBackgroundResource(R.color.versad);

        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);

        sugsongView = (TwoWayView) findViewById(R.id.suggested_music_list);

        getSuggestedMusic();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getSongList() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Uri musicUri2 = android.provider.MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, MediaStore.Audio.Media._ID + " ASC");
        // Cursor musicCursor2 = musicResolver.query(musicUri2, null, null, null, android.provider.MediaStore.Audio.Media._ID+" ASC");
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int musicColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int musicType = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.IS_MUSIC);
            //  int genre_id = musicCursor2.getColumnIndex(MediaStore.Audio.Genres.NAME);

            //add songs to list
            do {
                // Log.e("music type", musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE))+" ");
                long thisId = musicCursor.getLong(idColumn);

                Integer thisTitle2 = musicCursor.getInt(musicType);
                if (thisTitle2 == 0)
                    continue;

                String thisTitle    = musicCursor.getString(titleColumn);
                String thisArtist   = musicCursor.getString(artistColumn);
                String album        = musicCursor.getString(musicColumn);
                //                String genre = musicCursor.getString(genre_id);

                //              Log.e("here!!",genre + "!! " + thisTitle);
                songList.add(new Song(thisId, thisTitle, thisArtist, album));
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setController() {
        controller = new MusicController(Music.this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.this.playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.this.playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.music_list));
        controller.setEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void songPicked(View view) {
        try {
            musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
            musicSrv.playSong();
            if (playbackPaused) {
                setController();
                playbackPaused = false;
            }

            updateMood(songList.get(Integer.parseInt(view.getTag().toString())).getTitle(),
                    songList.get(Integer.parseInt(view.getTag().toString())).getArtist());

            controller.show(0);
        } catch (Exception e) {
            Log.e("ERROR : ", e.getMessage() + " ");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {

            case android.R.id.home:
                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;
            case R.id.action_shuffle:
                musicSrv.setShuffle();
                break;
            case R.id.action_pause:
                musicSrv.pausePlayer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_music, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    private void playNext() {
        musicSrv.playNext();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    private void playPrev() {
        musicSrv.playPrev();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    /**
     * Update user's mood
     */
    private void updateMood(String sing, String art) {

        if (art.equals("<unknown>"))
            art = "";
        // to fetch song names
        String id;
        id = s.getString(Constants.USER_ID, "1");
        String uri = Constants.apilink +
                "music-genre.php?artist=" +
                art +
                "&song=" +
                sing +
                "&userid=" +
                id;
        Log.e("executing", uri + " ");

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String res = response.body().string();
                mHandler.post(() -> {
                    try {
                        JSONObject YTFeed = new JSONObject(res);
                        e.putString(Constants.CURRENT_SCORE, YTFeed.getString("mood"));
                        e.commit();

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        Log.e("EXCEPTION : ", e1.getMessage() + " ");
                    }
                });

            }
        });
    }

    /**
     * Get suggestedMusic
     */
    private void getSuggestedMusic() {

        String uri = Constants.apilink + "suggested-music.php?userid=" + s.getString(Constants.USER_ID, "1");

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String res = response.body().string();
                mHandler.post(() -> {
                    try {
                        JSONObject YTFeed = new JSONObject(String.valueOf(res));
                        JSONArray YTFeedItems = YTFeed.getJSONArray("songs");
                        Log.e("response", YTFeedItems + " ");
                        sugsongView.setAdapter(new SugMusic_adapter(Music.this, YTFeedItems));

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        Log.e("EXCEPTION : ", e1.getMessage() + " ");
                    }
                });

            }
        });
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            setController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public class SongAdapter extends BaseAdapter {

        final Context context;
        TextView songView, artistView;
        ImageView iv;
        LinearLayout l;
        private final ArrayList<Song> songs;
        private final LayoutInflater songInf;
        public SongAdapter(Context c, ArrayList<Song> theSongs) {
            songs = theSongs;
            context = c;
            songInf = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return songs.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            //map to song layout

            LinearLayout songLay = (LinearLayout) songInf.inflate
                    (R.layout.song, parent, false);
            //get title and artist views
            songView = (TextView) songLay.findViewById(R.id.song_title);
            artistView = (TextView) songLay.findViewById(R.id.song_artist);
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

    public class SugMusic_adapter extends BaseAdapter {

        final Context context;
        final JSONArray FeedItems;
        private LayoutInflater inflater = null;

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
                Log.e("eroro", e.getMessage() + " ");
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
}
