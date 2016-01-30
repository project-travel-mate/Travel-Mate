package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Util.Constants;
import Util.Utils;
import adapters.Books_adapter;
import adapters.SongAdapter;
import adapters.SugMusic_adapter;
import objects.MusicController;
import objects.Song;

public class Music extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private ArrayList<Song> songList;
    private ListView songView;
    TwoWayView sugsongView;
    private MusicController controller;
    private MusicService musicSrv;
    SharedPreferences s ;
    SharedPreferences.Editor e;
    ImageView iv;
    private Intent playIntent;
    private boolean musicBound=false;
    private boolean paused=false, playbackPaused=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        songView = (ListView)findViewById(R.id.music_list);
        songList = new ArrayList<Song>();
        iv = (ImageView) findViewById(R.id.iv);

        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();

        getSongList();
        setController();

        Integer moods = Integer.parseInt(s.getString(Constants.CURRENT_SCORE, "2"));

        if (moods > 10) {
            iv.setBackgroundResource(R.color.verhappy);
        } else if (moods > 2) {
            iv.setBackgroundResource(R.color.happy);
        } else if (moods > -2) {
            iv.setBackgroundResource(R.color.normal);
        } else if (moods > -10) {
            iv.setBackgroundResource(R.color.sad);
        } else {
            iv.setBackgroundResource(R.color.versad);
        }



        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });


        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);

        sugsongView = (TwoWayView) findViewById(R.id.suggested_music_list);


        new Book_RetrieveFeed2().execute();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void getSongList() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Uri musicUri2 = android.provider.MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, android.provider.MediaStore.Audio.Media._ID+" ASC");
       // Cursor musicCursor2 = musicResolver.query(musicUri2, null, null, null, android.provider.MediaStore.Audio.Media._ID+" ASC");
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
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
                if(thisTitle2==0)
                    continue;

                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String album = musicCursor.getString(musicColumn);
//                String genre = musicCursor.getString(genre_id);

  //              Log.e("here!!",genre + "!! " + thisTitle);
                songList.add(new Song(thisId, thisTitle, thisArtist, album));
            }
            while (musicCursor.moveToNext());
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setController(){
        controller = new MusicController(Music.this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.music_list));
        controller.setEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void songPicked(View view){
        try {
            musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
            musicSrv.playSong();
            if (playbackPaused) {
                setController();
                playbackPaused = false;
            }

            try {
                new Book_RetrieveFeed(songList.get(Integer.parseInt(view.getTag().toString())).getTitle(),
                        songList.get(Integer.parseInt(view.getTag().toString())).getArtist()).execute();

            } catch (Exception e) {

            }

            controller.show(0);
        }catch(Exception e){
            Log.e("eroro",e.getMessage()+" ");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {

            case android.R.id.home :
                stopService(playIntent);
                musicSrv=null;
                System.exit(0);
                break;

            case R.id.action_end:
                stopService(playIntent);
                musicSrv=null;
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
        musicSrv=null;
        super.onDestroy();
    }

    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
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
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }
    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
        return musicSrv.getDur();
        else return 0;
    }

    public class Book_RetrieveFeed extends AsyncTask<String, Void, String> {

        String sing,art;

        public Book_RetrieveFeed(String name,String play){
            sing=name;
            art=play;
            if(art.equals("<unknown>"))
                art="";

        }
        protected String doInBackground(String... urls) {
            try {
                String id;
                id = s.getString(Constants.USER_ID,"1");
                String uri = "http://csinsit.org/prabhakar/tie/music-genre.php?artist=" +
                        art +
                        "&song=" +
                        sing +
                        "&userid=" +
                        id;

                uri = uri.replace(" ","+");
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());

                Log.e("here", url+ readStream + " ");
                return readStream;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String Result) {
            Log.e("vfdfav d",Result+"  ");
            try {
                JSONObject YTFeed = new JSONObject(String.valueOf(Result));
                e.putString(Constants.CURRENT_SCORE,YTFeed.getString("mood"));
                e.commit();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("fbdabd",e.getMessage()+" ");
            }
        }
    }

    public class Book_RetrieveFeed2 extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String uri =
                        "http://csinsit.org/prabhakar/tie/suggested-music.php?userid="+s.getString(Constants.USER_ID,"1");
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());

                return readStream;
            } catch (Exception e) {
                this.exception = e;
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String Result) {
            try {
                JSONObject YTFeed = new JSONObject(String.valueOf(Result));
                JSONArray YTFeedItems = YTFeed.getJSONArray("songs");
                Log.e("response", YTFeedItems + " ");
                sugsongView.setAdapter(new SugMusic_adapter(Music.this, YTFeedItems));
               } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void pause() {
        playbackPaused=true;
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
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
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

}
