package com.smotko.uv;

import android.app.Activity;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.location.*;
import android.os.Bundle;
import android.widget.TextView;

public class UvActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView label = (TextView) findViewById(R.id.label);
    	label.setText("GETTING LOCATION");
        getLocation();
                
    }
    
    public void getLocation(){
		// Acquire a reference to the system Location Manager
	    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	
	    // Define a listener that responds to location updates
	    LocationListener locationListener = new LocationListener() {
	        public void onLocationChanged(Location location) {
	          // Called when a new location is found by the network location provider.
	          gotLocation(location);
	        }
	
	        public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	        public void onProviderEnabled(String provider) {
	        	TextView label = (TextView) findViewById(R.id.label);
	        	label.setText("PROVIDER ENABLED");
	        	
	        }
	
	        public void onProviderDisabled(String provider) {
	        	
	        	TextView label = (TextView) findViewById(R.id.label);
	        	label.setText("PROVIDER DISABLED");
	        }
	      };
	
	    // Register the listener with the Location Manager to receive location updates
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    	
    }
    
    public void gotLocation(Location location){
    	TextView label = (TextView) findViewById(R.id.label);
    	label.setText("GOT LOCATION: Accuracy: " + Float.toString(location.getAccuracy())
    			    + " Latitude: " + Double.toString(location.getLatitude())
    			    + " Longitude: " + Double.toString(location.getLongitude())
    			    + " Altitude: " + Double.toString(location.getAltitude()));
    }
}