package com.skyletto.startappfrontend.data.database.dao;

import com.skyletto.startappfrontend.domain.entities.Country;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CountryDao {

    @Query("select * from countries order by name asc")
    LiveData<List<Country>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<Country> countries);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Country country);

    @Delete
    void remove(Country country);

    @Query("delete from countries")
    void removeAll();

}
