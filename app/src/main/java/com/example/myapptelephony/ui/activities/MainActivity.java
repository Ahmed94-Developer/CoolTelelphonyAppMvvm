package com.example.myapptelephony.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapptelephony.R;
import com.example.myapptelephony.audioFile.CallRecordingService;
import com.example.myapptelephony.databinding.ActivityMainBinding;
import com.example.myapptelephony.model.NetWork;
import com.example.myapptelephony.viewmodel.NetWorkViewModel;
import com.example.myapptelephony.worker.NetWorker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String KEY_TASK_DESC = "key_task_desc";
    TextView SpeedText, ConnectivityText, ipText, LocationText, packetDropText;
    ImageButton MicOn;
    NetWorkViewModel netWorkViewModel;
    ActivityMainBinding binding1;
    private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    NetWork netWork;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpeedText = (TextView) findViewById(R.id.textView13);
        ConnectivityText = (TextView) findViewById(R.id.textView15);
        ipText = (TextView) findViewById(R.id.textView17);
        LocationText = (TextView) findViewById(R.id.textView19);
        packetDropText = (TextView) findViewById(R.id.textView23);
        MicOn = (ImageButton) findViewById(R.id.imageButton5);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();


        netWorkViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication())).get(NetWorkViewModel.class);
       binding1 = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding1.setViewModel(netWorkViewModel);
        binding1.invalidateAll();
        binding1.setLifecycleOwner(this);
        binding1.toolbar.inflateMenu(R.menu.main_menu);

        binding1.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.item){
                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(i);
                }
                return false;
            }
        });


       netWorkViewModel.getNetWorkInfo().observe(this, new Observer<NetWork>() {
            @Override
            public void onChanged(NetWork netWorks) {

                binding1.textView13.setText(netWorks.getSpeed()+"");
                binding1.textView17.setText(netWorks.getIpAddress()+"");
                binding1.textView15.setText(netWorks.getConnectivity()+"");
                binding1.textView15.setMovementMethod(new ScrollingMovementMethod());
                binding1.textView19.setText("Latitude : " + netWorks.getLatitude()
                        + " ;" + "Longtitude :" + netWorks.getLongtitude() + "\n" + "Location : "
                        + netWorks.getLocality() + " ; " + netWorks.getCountryName());
                binding1.textView23.setText(" Latency :" + netWorks.getLatency()+"" + " ;" + " PacketLost : "
                        + netWorks.getPacketLoss()+"" + "\n" + " PacketDelay : " + netWorks.getPacketDelay()+"");

            }
        });


       Timer time = new Timer();

        time.schedule(new TimerTask() {
           public void run() {

         Data data = new Data.Builder()
                .putString(KEY_TASK_DESC, "Sending netWork data").build();
        Constraints constraints = new Constraints.Builder()
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NetWorker.class)
                .setInputData(data)
                .addTag("tag")
                 .setConstraints(constraints).build();
        WorkManager.getInstance(MainActivity.this)
                .enqueue(request);
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs1", MODE_PRIVATE);
                String Speed = prefs.getString("speed", "");
                String ip = prefs.getString("ip1", "");
                String connection = prefs.getString("connect1", "");
                String latency = prefs.getString("latency", "");
                String delay = prefs.getString("delay", "");
                String loss = prefs.getString("lost", "");


                netWork = new NetWork(Speed, connection, ip, latitude1, longtitude1, locality, countryName, latency, loss, delay);
                netWork.setId(1);
                netWorkViewModel.Update(netWork);

          }}, 500, 10000);


        binding1.imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CallRecordingService.class);
                startService(intent);
            }
        });
    }

   public static  String latitude1;
   public static String longtitude1;
   public static String locality;
   public static String countryName;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            //locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
            Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();

                String add = "";
                if (addresses.size() > 0) {
                     latitude1 = ""+location.getLatitude();
                     longtitude1 = ""+location.getLongitude();
                     locality =addresses.get(0).getLocality();
                     countryName =  addresses.get(0).getCountryName();

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,this);

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            //locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
            Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();

                String add = "";
                if (addresses.size() > 0) {
                    String latitude1 = ""+location.getLatitude();
                    String longtitude1 = ""+location.getLongitude();

                    editor.putString("lat", latitude1);
                    editor.putString("long", longtitude1);
                    editor.putString("locality", addresses.get(0).getLocality());
                    editor.putString("countryName", addresses.get(0).getCountryName());
                    editor.apply();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop location updates
        if (googleApiClient != null  &&  googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(MainActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }

                break;
        }

    }

}

