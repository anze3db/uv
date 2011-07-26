package com.smotko.uv;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class UvActivity extends Activity {

	private final String UV_URL = "http://www.temis.nl/uvradiation/nrt/uvindex.php?";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView label = (TextView) findViewById(R.id.label);
		label.setText("GETTING LOCATION");

		getLocation();

	}

	public void getData(double lat, double lon) {

		try {
			String address = UV_URL + "lon=" + Double.toString(lon) + "&lat="
					+ Double.toString(lat);

			URL url = new URL(address);
			URLConnection urlConnection = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String line = "";

			TextView label = (TextView) findViewById(R.id.label);
			while ((line = br.readLine()) != null) {
				if (line.contains("    <td align=right nowrap>")) {
					line = line.substring(28, 31);
					break;
				}
			}

			label.setText(line);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void getLocation() {
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				gotLocation(location);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
				TextView label = (TextView) findViewById(R.id.label);
				label.setText("PROVIDER ENABLED");

			}

			public void onProviderDisabled(String provider) {

				TextView label = (TextView) findViewById(R.id.label);
				label.setText("PROVIDER DISABLED");
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

	}

	public void gotLocation(Location location) {
		TextView label = (TextView) findViewById(R.id.label);

		getData(location.getLatitude(), location.getLongitude());

		/*
		 * label.setText("GOT LOCATION: Accuracy: " +
		 * Float.toString(location.getAccuracy()) + " Latitude: " +
		 * Double.toString(location.getLatitude()) + " Longitude: " +
		 * Double.toString(location.getLongitude()) + " Altitude: " +
		 * Double.toString(location.getAltitude()));
		 */
	}
}