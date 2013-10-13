package uk.co.itvet.wifiscanner;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Initial creation info:
 * User: Jacob Mansfield
 * Date: 02/02/2013
 * Time: 12:14AM
 */
public class wifi_info extends Activity implements LocationListener {

    TextView LatView;
    TextView LonView;
    TextView AltView;
    Double   Latitude   = (double) 0;
    Double   Longitude  = (double) 0;
    Double   Altitude   = (double) 0;
    Boolean  showSplash = true;
    WifiManager wifiManager;
    List<ScanResult> wifiNetworks = new LinkedList<ScanResult>();
    List<locationInformation> locationInformationLinkedList = new LinkedList<locationInformation>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        ((TextView)findViewById(R.id.loading_text)).setText("Getting GPS fix...");

        // get a LocationManager
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // request the updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,     0, 0, this);

        this.wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiManager.startScan();

    }

    @Override
    public void onLocationChanged(Location location) {

        if(this.showSplash) {
            setContentView(R.layout.wifi_info);
            this.showSplash = false;
        }

        // extract coords from out Location
        Double MessyLat = location.getLatitude();
        Double MessyLon = location.getLongitude();
        Double Altitude = location.getAltitude();

        // Make them look nice
        DecimalFormat twoDotTwo = new DecimalFormat("00.00");
        DecimalFormat two       = new DecimalFormat("00");

        // initialise empty strings
        String Lat, Lon = "";

        // change the decimal degrees to degrees minutes seconds
        double tempf;
        // first do the latitude
        tempf = java.lang.Math.abs(MessyLat);
        int latd = (int)tempf;
        tempf = (tempf - latd) * 60.0;
        int latm = (int)tempf;
        double lats = (tempf - latm) * 60.0;
        String latS = twoDotTwo.format(lats);
        if(MessyLat < 0) {		// put the sign back on the degrees
            Lat = latd+"° "+latm+"' "+latS+"\" N";
        } else {
            Lat = latd+"° "+latm+"' "+latS+"\" S";
        }

        tempf = java.lang.Math.abs(MessyLon);
        Integer lond = (int)tempf;
        tempf = (tempf - lond) * 60.0;
        Integer lonm = (int)tempf;
        Double  lons = (tempf - lonm) * 60.0;
        String lonS = twoDotTwo.format(lons);
        String lonD = two.format(lond);
        String lonM = two.format(lonm);
        if(MessyLon < 0){
            Lon = lonD+"° "+lonM+"' "+lonS+"\" N";
        } else {
            Lon = lonD+"° "+lonM+"' "+lonS+"\" N";
        }

        // send them to the screen
        if(this.LatView==null|this.LonView==null|this.AltView==null){
            this.LatView = (TextView)findViewById(R.id.LatitudeTextView);
            this.LonView = (TextView)findViewById(R.id.LongitudeTextView);
            this.AltView = (TextView)findViewById(R.id.AltitudeTextView);
        }

        this.LatView.setText(Lat);
        this.Latitude = location.getLatitude();
        this.LonView.setText(Lon);
        this.Longitude = location.getLongitude();
        if ((this.Altitude==(double)0)|(location.hasAltitude()&&location.getProvider()==LocationManager.GPS_PROVIDER)) {
            this.AltView.setText(Altitude.toString()+"'");
            this.Altitude = location.getAltitude();
        }

        //Log.e(wifi_info.class.getCanonicalName(), "Starting scan");

        // get Wi-Fi network info
        wifiManager.startScan();
        Long now = System.currentTimeMillis();

        List<ScanResult> newWifiNetworks = wifiManager.getScanResults();

        //Log.e(wifi_info.class.getCanonicalName(), "Fetching scan results");

        for(ScanResult scanResult : newWifiNetworks) {
            Boolean found = false;
            for(ScanResult storedResult : this.wifiNetworks) {
                if (storedResult.BSSID.toString().equals(scanResult.BSSID.toString())) {
                    storedResult.level = scanResult.level;
                    storedResult.timestamp = now;
                    found = true;
                } else {
                }
            }
            if(found==false) {
                scanResult.timestamp = now;
                this.wifiNetworks.add(scanResult);
            }
        }

        for(ScanResult storedResult : this.wifiNetworks) {
            if(storedResult.timestamp != now) {
                storedResult.level = 0;
                storedResult.timestamp = now;
            }
        }

        String networkInfo = "";

        for(ScanResult scanResult : this.wifiNetworks) {
            if(scanResult.level!=0){
                networkInfo = networkInfo + scanResult.SSID+": "+scanResult.level+" dBm\n";
            } else {
                networkInfo = networkInfo + scanResult.SSID+": -∞ dBm\n";
            }
        }
        ((TextView)findViewById(R.id.WifiNetworks)).setText(networkInfo);

        locationInformation here = new locationInformation(location, newWifiNetworks);

        Log.e(wifi_info.class.getCanonicalName(),conversions.locationInformation2JSONString(here));

        this.locationInformationLinkedList.add(here);

        FileOutputStream storage = null;

        try {
            storage = openFileOutput("locationInformationStore", MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Log.e(wifi_info.class.getCanonicalName(), e.getStackTrace().toString());
        }

        try {
            storage.write("test".getBytes());
        } catch (IOException e) {
            Log.e(wifi_info.class.getCanonicalName(), e.getStackTrace().toString());
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //To change body of implemented methods use File | Settings | File Templates.
        //Log.e(wifi_info.class.getCanonicalName(), "s: "+s+", i:"+i+", bundle: "+bundle.toString());
    }

    @Override
    public void onProviderEnabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
        Log.e(wifi_info.class.getCanonicalName(), "Provider Enabled: "+s);
    }

    @Override
    public void onProviderDisabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
        Log.e(wifi_info.class.getCanonicalName(), "Provider Disabled: "+s);
    }
}