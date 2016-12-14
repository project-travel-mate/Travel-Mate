package tie.hackathon.travelguide;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import objects.Song;

/**
 * Created by swati on 10/10/15.
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private static final int NOTIFY_ID = 1;
    private final IBinder musicBind = new MusicBinder();
    SharedPreferences s;
    SharedPreferences.Editor e;
    private boolean shuffle = false;
    private Random rand;
    private String songTitle = "";
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;


    public void onCreate() {
        //create the service
        //create the service
        super.onCreate();
        rand = new Random();
//initialize position
        songPosn = 0;
//create player
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void setShuffle() {
        shuffle = !shuffle;
    }

    public void playNext() {
        if (shuffle) {
            int newSong = songPosn;
            while (newSong == songPosn) {
                newSong = rand.nextInt(songs.size());
            }
            songPosn = newSong;
        } else {
            songPosn++;
            if (songPosn >= songs.size()) songPosn = 0;
        }
        playSong();
    }

    private void initMusicPlayer() {
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs) {
        songs = theSongs;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public void playSong() {
        //play a song
        player.reset();
        //get song
        Song playSong = songs.get(songPosn);
//get id
        songTitle = playSong.getTitle();
        long currSong = playSong.getID();
//set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();
        Intent notIntent = new Intent(this, Music.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.android_music_player_play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public void playPrev() {
        songPosn--;
        if (songPosn < 0) songPosn = songs.size() - 1;
        playSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}


