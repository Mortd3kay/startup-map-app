package com.skyletto.startappfrontend.domain.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cities")
public class City {
    @PrimaryKey
    private int id;
    private int country_id;
    private String name;

    public City() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    @Override
    public String toString() {
        return name;
    }
}
