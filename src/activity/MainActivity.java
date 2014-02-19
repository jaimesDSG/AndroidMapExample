package activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jaimes.map.R;

public class MainActivity extends Activity implements 
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	
	GoogleMap map;
	
	LocationRequest mLocationRequest;
	LocationClient mLocationClient;
	Location mCurrentLocation;
	
	Marker marker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		// Create the client and try to connect.
		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.e(getClass().getCanonicalName(),"Connection failed. Retry connection.");
		mLocationClient.connect();
	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.i(getClass().getCanonicalName(),"Connected to Google play services");
		
		// Created the location request and ask for update
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(60000);
		mLocationRequest.setFastestInterval(30000);
		
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onDisconnected() {
		Log.i(getClass().getCanonicalName(),"Connection closed. Try to reconnect.");
		mLocationClient.connect();
	}

	@Override
	public void onLocationChanged(Location location) {
		if(marker == null) {
			// Move the map to your position and add a marker there.
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
			marker = map.addMarker(new MarkerOptions().title("Vous êtes ici").position(new LatLng(location.getLatitude(), location.getLongitude())));
		} else {
			marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
		}
	}

}
