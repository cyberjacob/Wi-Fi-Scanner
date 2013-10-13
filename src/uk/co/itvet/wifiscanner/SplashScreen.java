package uk.co.itvet.wifiscanner;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SplashScreen extends Activity {
    Handler mHandler,actHandler;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Thread(){
            public void run(){
                try{
                    Thread.sleep(4000);
                }
                catch(Exception ex){

                    Log.e("Welcome Exception :", ex.toString());
                }
                try{
                    Message msg=mHandler.obtainMessage();
                    mHandler.sendMessage(msg);


                }
                catch(NullPointerException ex){
                    Log.e("Handler Exception :",ex.toString());
                }
            }

        }.start();
        mHandler=new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);

                Intent i = new Intent(SplashScreen.this, wifi_info.class);
                startActivity(i);
                finish();

            }
        };



    }
}
