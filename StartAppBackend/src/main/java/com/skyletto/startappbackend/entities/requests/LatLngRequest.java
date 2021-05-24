package com.skyletto.startappbackend.entities.requests;

public class LatLngRequest {
    private double lat;
    private double lng;
    private int zoom = 17;

    public LatLngRequest(double lat, double lng, int zoom) {
        this.lat = lat;
        this.lng = lng;
        this.zoom = Math.min(zoom, 17);
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

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = Math.min(zoom, 17);
    }
}
