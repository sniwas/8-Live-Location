package com.example.ex_no_6_current_location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements IBaseGpsListener{
    private static final int PERMISSION_LOCATION=1000;
    TextView tv;
    Button b;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.textView);
        b=findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For location permission checking
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_LOCATION);
                }
                else {
                    showLocation();
                }
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_LOCATION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showLocation();
            }
            else {
                Toast.makeText(getApplicationContext(), "Permission not granted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void showLocation(){
        LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //gps enabled or not
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //startLocating
            tv.setText("Loading Location..");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        }
        else {
            //enable gps
            Toast.makeText(getApplicationContext(), "Enabled GPS !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    //show location as string
    private String hereLocation(Location location){
        return "Latitude:"+location.getLatitude()+"\nLongitude:"+location.getLongitude();
    }
    @Override
    public void onLocationChanged(Location location) {
        //update location
       tv.setText(hereLocation(location));
       /*final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Live Location").setMessage(hereLocation(location)).setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             dialog.cancel();
            }
        });
        final AlertDialog alert=builder.create();
        alert.show();*/
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }
}