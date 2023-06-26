package com.example.hotelapp.DTO.Hotel;

import java.awt.geom.Point2D;

public class MapLocation {
    //Map coordinates
    private double longitude;
    private double latitude;
    //Constructor for the map location

    public MapLocation(double longitude,double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public Point2D getMapLocation(){
        return  new Point2D.Double(longitude,latitude);
    }
    public void setMapLocation(Point2D mapLocation){
        this.latitude = mapLocation.getX();
        this.longitude = mapLocation.getY();
    }
}
