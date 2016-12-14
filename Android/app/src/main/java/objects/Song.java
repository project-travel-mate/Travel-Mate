package objects;

/**
 * Created by swati on 10/10/15.
 */
public class Song {
    private final long id;
    private final String title;
    private final String artist;
    private final String album_id;
    public Song(long songID, String songTitle, String songArtist,String album_Id) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        album_id = album_Id;
    }
    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbum_id(){return album_id;}
}
