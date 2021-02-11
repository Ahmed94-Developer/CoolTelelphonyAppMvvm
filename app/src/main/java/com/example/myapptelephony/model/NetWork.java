package com.example.myapptelephony.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "network_table")
public class NetWork {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String speed;
    private String connectivity;
    private String ipAddress;
    private String latitude;
    private String longtitude;
    private String locality;
    private String countryName;
    private String latency;
    private String packetLoss;
    private String packetDelay;

 public NetWork(String speed,String connectivity,String ipAddress,String latitude,String longtitude,String locality
                   ,String countryName,String latency,String packetLoss,String packetDelay) {
       this.speed = speed;
        this.connectivity = connectivity;
        this.ipAddress = ipAddress;
        this.latitude = latitude;
        this.longtitude = longtitude;
       this.locality = locality;
       this.countryName = countryName;
        this.latency = latency;
        this.packetLoss = packetLoss;
        this.packetDelay = packetDelay;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public void setPacketLoss(String packetLoss) {
        this.packetLoss = packetLoss;
    }

    public void setPacketDelay(String packetDelay) {
        this.packetDelay = packetDelay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpeed() {
      return speed;
    }

    public String getConnectivity() {
       return connectivity;
    }

    public String getIpAddress() {
      return ipAddress;
    }

    public String getLatitude() {
      return latitude;
    }

   public String getLongtitude() {
        return longtitude;
    }

    public String getLocality() {
        return locality;
    }

    public String getCountryName() {
      return countryName;
   }

    public String getLatency() {
        return latency;
    }

   public String getPacketLoss() {
       return packetLoss;
    }

   public String getPacketDelay() {
        return packetDelay;
    }
}
