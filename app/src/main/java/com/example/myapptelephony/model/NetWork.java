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
