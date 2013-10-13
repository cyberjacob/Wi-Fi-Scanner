package uk.co.itvet.wifiscanner;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Initial creation info:
 * User: Jacob Mansfield
 * Date: 03/02/2013
 * Time: 6:47PM
 */
public class conversions {

    public static String ScanResults2JSONString(List<ScanResult> scanResults) {
        String toReturn = "[";
        int i = 0;
        for(ScanResult scanResult : scanResults) {
            i = i+1;
            toReturn = toReturn+"{\"SSID\":\""+scanResult.SSID+"\",";
            toReturn = toReturn+"\"BSSID\":\""+scanResult.BSSID+"\",";
            toReturn = toReturn+"\"capabilities\":\""+scanResult.capabilities+"\",";
            toReturn = toReturn+"\"frequency\":\""+scanResult.frequency+"\",";
            toReturn = toReturn+"\"level\":\""+scanResult.level+"\"";
            if(i!=scanResults.size()) {
                toReturn = toReturn + "},";
            } else {
                toReturn = toReturn + "}";
            }
        }
        toReturn = toReturn+"]";
        return toReturn;
    }

    public static String locationInformation2JSONString(locationInformation locationInformation) {
        String toReturn = "" +
                "{" +
                    "\"location\": {" +
                        "\"Latitude\": "+locationInformation.getLocation().getLatitude()+"," +
                        "\"Longitude\": "+locationInformation.getLocation().getLongitude() +
                    "},\n" +
                "\"networks\":"+
                ScanResults2JSONString(locationInformation.getScanResult())+"}";
        return toReturn;
    }

}
