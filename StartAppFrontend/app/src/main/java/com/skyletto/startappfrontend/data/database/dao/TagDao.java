package com.skyletto.startappfrontend.data.database.dao;

import com.skyletto.startappfrontend.domain.entities.Tag;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TagDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long add(Tag tag);

    @Query("select * from tags")
    List<Tag> getAll();

    @Query("select * from tags where id=:id")
    Tag getById(Long id);
}
