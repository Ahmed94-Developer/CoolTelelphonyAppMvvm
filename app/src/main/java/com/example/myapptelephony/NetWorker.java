package com.example.myapptelephony;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.core.app.NotificationCompat;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.myapptelephony.Activities.MainActivity;


import java.io.BufferedReader;

import java.io.InputStreamReader;


import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WIFI_SERVICE;

public class NetWorker extends Worker{

    public NetWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

         Data data = getInputData();
        final String desc = data.getString(MainActivity.KEY_TASK_DESC);


        displayNotification("Hey Iam your work", desc);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        NetworkCapabilities nc = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            }
        }
        int downSpeed = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            downSpeed = nc.getLinkDownstreamBandwidthKbps();
        }
        int upSpeed = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            upSpeed = nc.getLinkUpstreamBandwidthKbps();
        }

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        String latitude = prefs.getString("lat", "latitude");
        String longtitude = prefs.getString("long", "longtitude");
        String locality = prefs.getString("locality", "local");
        String countryName = prefs.getString("countryName", "country");

        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("prefs1", MODE_PRIVATE).edit();


        String Speed = String.valueOf(" DownSpeed :  " + downSpeed + " gb/second" + "\n" + " UpSpeed :   " + upSpeed + "  gb/second");

        String connection = String.valueOf(netInfo);

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        String lost = new String();
        String delay = new String();

        String latency = "";
        String ips = ip;
        String pingCmd = "ping -c 4 " + ips;
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            String latencyResult = null;
            while ((inputLine = in.readLine()) != null) {
                latencyResult = inputLine;
                if (inputLine.contains("packet loss")) {
                    int i = inputLine.indexOf("received");
                    int j = inputLine.indexOf("%");
                    System.out.println(":" + inputLine.substring(i + 10, j + 1));
//                  System.out.println(":"+str.substring(j-3, j+1));
                    lost = inputLine.substring(i + 10, j + 1);
                    editor.putString("lost", lost);
                    editor.apply();
                }
                if (inputLine.contains("avg")) {
                    int i = inputLine.indexOf("/", 20);
                    int j = inputLine.indexOf(".", i);
                    System.out.println(":" + inputLine.substring(i + 1, j));
                    delay = inputLine.substring(i + 1, j);
                    delay = " " + delay + " ms";
                    editor.putString("delay", delay);
                    editor.apply();
                }

            }
            String[] keyValue = latencyResult.split("=");
            String[] value = keyValue[1].split("/");
            latency = value[1];
            String latenc = String.valueOf(" " + latency);


            editor.putString("latency", latenc);
            editor.apply();
        } catch (Exception e) {
            Log.d("", "Exception..." + e);
        }
        editor.putString("lati1", latitude);
        editor.putString("lon1", longtitude);
        editor.putString("local1", locality);
        editor.putString("country1", countryName);
        editor.putString("speed", Speed);
        editor.putString("connect1", connection);
        editor.putString("ip1", ip);
        editor.apply();

        return Result.success();
    }
    private void displayNotification(String title,String desc){
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("simplifiedcoding","simplifiedcoding",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"simplifiedcoding")
                .setContentText(desc)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(1,builder.build());
    }
}
