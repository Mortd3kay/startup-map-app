package com.skyletto.startappbackend.utils;

public class Utils {
    static public double diff(int zoom){
        return 0.06*((20-zoom)*(20-zoom));
    }
}
