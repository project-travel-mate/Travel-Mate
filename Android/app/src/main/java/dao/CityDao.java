package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import objects.City;

/**
 * Created by Santosh on 05/09/18.
 */

@Dao
public interface CityDao {

    @Insert
    void insert(City... city);

    @Update
    void update(City... city);

    @Delete
    void delete(City... city);

    @Query("Select * FROM city")
    City[] loadAll();

    @Query("Select * FROM city WHERE city_favourite = 1")
    City[] loadAllFavourite();

    @Query("DELETE FROM city")
    void deleteAll();
}