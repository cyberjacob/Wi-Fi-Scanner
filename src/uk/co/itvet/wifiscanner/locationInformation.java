package uk.co.itvet.wifiscanner;

import android.location.Location;
import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Initial creation info:
 * User: Jacob Mansfield
 * Date: 03/02/2013
 * Time: 6:02PM
 */
public class locationInformation {

    private Location location;
    private List<ScanResult> scanResults;
    //private ScanResult scanResult;

    public Location getLocation() {
        return this.location;
    }

    public List<ScanResult> getScanResult() {
        return this.scanResults;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setScanResult(List<ScanResult> scanResults) {
        this.scanResults = scanResults;
    }

    public locationInformation(Location location, List<ScanResult> scanResults) {
        this.setLocation(location);
        this.setScanResult(scanResults);
    }
}
