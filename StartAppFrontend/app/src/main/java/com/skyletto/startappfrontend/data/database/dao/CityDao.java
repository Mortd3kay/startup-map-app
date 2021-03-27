package com.skyletto.startappfrontend.data.database.dao;

import com.skyletto.startappfrontend.domain.entities.City;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
public interface CityDao {

    @Query("select * from cities order by name asc")
    LiveData<List<City>> getAll();

    @Query("select * from cities where country_id=:country_id order by name asc")
    List<City> getByCountryId(int country_id);

    @Query("select * from cities where name = :name")
    Single<City> getCityByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<City> cities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(City city);

    @Query("delete from cities")
    void removeAll();

    @Delete
    void remove(City city);
}
