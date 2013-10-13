package uk.co.itvet.wifiscanner;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ccuk
 * Date: 01/02/2013
 * Time: 11:57PM
 * To change this template use File | Settings | File Templates.
 */
public class MyLocationListener implements LocationListener {

    // to store our Runable and List
    private Runnable returner;
    private List<Location> LocationList;

    // when the location gets changed
    public void onLocationChanged(Location location) {
        // empty the list
        this.LocationList.clear();

        // store the location
        this.LocationList.add(location);

        // and call the runable
        this.returner.run();
    }

    public MyLocationListener(Runnable returner, List<Location> locationList) {
        // store the runable and list localy
        this.returner = returner;
        this.LocationList = LocationList;
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onProviderEnabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onProviderDisabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
