package objects;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
public class FavouriteCities implements Serializable {
    @Entity(tableName = "Favourite Cities",
            foreignKeys =  {
                    @ForeignKey(entity = City.class,
                            parentColumns = "mId",
                            childColumns = "city_name")
                    ,
                    @ForeignKey(entity = User.class,
                            parentColumns = "mUsername",
                            childColumns = "user")})
    public class FavouriteCity {
        @PrimaryKey
        public int fav_city_id;

        @ColumnInfo(name = "city_name")
        public String CityName;
        @ColumnInfo(name = "user")
        public String userName;

        public FavouriteCity(String cityname, String username) {
            this.CityName = cityname;
            this.userName = username;
        }
    }
}