package com.smotko.uv;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class UvActivity extends ListActivity {

	private final String UV_URL = "http://www.temis.nl/uvradiation/nrt/uvindex.php?";
	private final String[] SKIN_TYPES = new String[] { "Skin type 1",
			"Skin type 2", "Skin type 3" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.skin_name, SKIN_TYPES));

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
				
			}
		});

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
	
	private class OrderAdapter extends ArrayAdapter<Skin> {

        private ArrayList<Skin> items;

        public SkinAdapter(Context context, int textViewResourceId, ArrayList<Skin> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                Skin o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        if (tt != null) {
                              tt.setText("Name: "+o.getSkinName());                            }
                        if(bt != null){
                              bt.setText("Status: "+ o.getSkinStatus());
                        }
                }
                return v;
        }
}