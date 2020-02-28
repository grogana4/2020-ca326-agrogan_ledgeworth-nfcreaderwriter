//this code was primarily worked on by Abe Grogan.

package com.example.cardconnect;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationFinder{ //Location finder starts up when reading nfc
    Timer timer1; //timer variable created
    LocationManager lm; //location manager and location result created
    LocationResult locationResult;
    boolean gps_enabled = false; //gps and network enabled variables created and assigned
    boolean network_enabled = false;

    public boolean getLocation(Context context, LocationResult result) {//LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result;
        if (lm == null) //is locationmanager is null
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); //get location services


        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER); //check if gps is enabled
        } catch (Exception ex) { //exceptions thrown if provider is not permitted.
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER); //check if network is enabled
        } catch (Exception ex) { //exceptions thrown if provider is not permitted.
        }


        if (!gps_enabled | !network_enabled) { //don't start listeners if no provider is enabled
            MainActivity.location_address = ""; //set location to empty string
            return false;
        }

        if (gps_enabled) //if gps is permitted
            try {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps); //request update for current location
            } catch (SecurityException e) { //catch security exception if provider isn't enabled

            }
        if (network_enabled) //if network is permitted
            try {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork); //request update for current location
            } catch (SecurityException e) { //catch security exception if provider isn't enabled

            }
        timer1 = new Timer(); //initialise timer
        timer1.schedule(new GetLastLocation(), 10000); //try to get location within the next five seconds
        return true;
    }

    LocationListener locationListenerGps = new LocationListener() { //initialise listener for gps
        public void onLocationChanged(Location location) { //if the location has changed
            timer1.cancel(); //cancel timer
            locationResult.gotLocation(location); //get location
            lm.removeUpdates(this); //remove updates for gps listener and network listener
            lm.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) { //other functions not needed
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() { //initialise listener for network
        public void onLocationChanged(Location location) { //if the location has changed
            timer1.cancel(); //cancel timer
            locationResult.gotLocation(location);//get location
            lm.removeUpdates(this); //remove updates for network listener and gps listener
            lm.removeUpdates(locationListenerGps);
        }

        public void onProviderDisabled(String provider) { //other functions not needed
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() { //remove updates for both listeners
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);

            Location net_loc = null, gps_loc = null; //set location for networks and gps to null
            if (gps_enabled)
                try {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps); //try to get updated location
                } catch (SecurityException e) { //catch security exception if provider isn't enabled

                }
            if (network_enabled)
                try {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork); //try to get updated location
                } catch (SecurityException e) { //catch security exception if provider isn't enabled

                }

            if (gps_loc != null && net_loc != null) { //if there are both values use the latest one
                if (gps_loc.getTime() > net_loc.getTime())
                    locationResult.gotLocation(gps_loc); //get gps location
                else
                    locationResult.gotLocation(net_loc); //else get network location
                return;
            }

            if (gps_loc != null) { //if only one is a value get the location
                locationResult.gotLocation(gps_loc);
                return;
            }
            if (net_loc != null) {
                locationResult.gotLocation(net_loc);
                return;
            }
        }
    }

    public static abstract class LocationResult { //run gotLocation
        public abstract void gotLocation(Location location);
    }
}