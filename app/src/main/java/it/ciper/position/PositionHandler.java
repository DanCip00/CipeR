package it.ciper.position;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.vmadalin.easypermissions.EasyPermissions;
import com.vmadalin.easypermissions.dialogs.SettingsDialog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import it.ciper.MainActivity;
import it.ciper.data.DataCenter;

public class PositionHandler implements EasyPermissions.PermissionCallbacks, Callable<Boolean> {
    static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    static final long SEC_INTERVAL = 20*1;//->5min
    static final long SEC_INTERVAL_FASTER = 10;

    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected DataCenter dataCenter;

    protected FusedLocationProviderClient fusedLocationProviderClient;
    protected com.google.android.gms.location.LocationRequest locationRequest;
    protected LocationCallback locationCallback;


    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter) {
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
    }

    public void setAccuracy(int n){
        switch (n){
            case 1:
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                break;
            default:
                locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        }
    };

    public void updateGPS(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        if(ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestLocationPermission();
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null) {
                    Location newLocation = new Location(LocationManager.GPS_PROVIDER);
                    newLocation.setLongitude(location.getLongitude());
                    newLocation.setLatitude(location.getLatitude());
                    dataCenter.setLocation(newLocation);
                    System.out.println("lat-->" + location.getLatitude());
                    System.out.println("lon-->" + location.getLongitude());
                    try {
                        Toast.makeText(context,((new Geocoder(context)).getFromLocation(location.getLatitude(),location.getLongitude(),1).get(0).getLocality()),Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public Boolean call() {
        updateGPS();
        return true;
    }


    public FusedLocationProviderClient getFusedLocationProviderClient() {
        return fusedLocationProviderClient;
    }

    //PERMESSI

    protected boolean hasLocationPermission(){
        return  EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }

    public void requestLocationPermission(){
        EasyPermissions.requestPermissions(
                activity,
                "è richieta la posizione",
                PERMISSION_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }

    @Override
    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if(EasyPermissions.somePermissionPermanentlyDenied(activity,list)){
            new SettingsDialog.Builder(activity).build().show();
        }else
            requestLocationPermission();
    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {
        Toast.makeText(context,"Permesso concesso", Toast.LENGTH_LONG).show();
    }

    public void getPermission(){
        if (hasLocationPermission()){
            //TODO nel caso si ha il permesso
        }else{
            requestLocationPermission();
            //TODO nel caso il permesso non sia concesso
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode){
                case 99:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        updateGPS();
                    else
                        Toast.makeText(context,"Permesso non accettato :(",Toast.LENGTH_LONG).show();
                    break;
            }
    }

}
