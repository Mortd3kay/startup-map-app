package com.skyletto.startappbackend.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity(name = "users_locations")
public class UserLocation {
    @Id
    private long userId;
    private double lat;
    private double lng;

    public UserLocation() {
    }

    public UserLocation(long userId, double lat, double lng) {
        this.userId = userId;
        this.lat = lat;
        this.lng = lng;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Location{" +
                "userId=" + userId +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLocation userLocation = (UserLocation) o;
        return userId == userLocation.userId && Double.compare(userLocation.lat, lat) == 0 && Double.compare(userLocation.lng, lng) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, lat, lng);
    }
}
