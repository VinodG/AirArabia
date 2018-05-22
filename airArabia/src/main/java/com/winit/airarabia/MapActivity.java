package com.winit.airarabia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.parsers.ParseLocation;
import com.winit.airarabia.utils.LogUtils;
import com.winit.airarabia.utils.StringUtils;

public class MapActivity extends BaseActivity implements OnMapReadyCallback{

	private LinearLayout llMap;
	private LatLng latlang;
	private GoogleMap Map;
	private ParseLocation parse_loc;
	private String location, lat, lng;
	private MapActivity.BCR bcr;

	@Override
	public void onMapReady(GoogleMap googleMap) {
			Map = googleMap;
	}

	private class BCR extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(AppConstants.HOME_CLICK))
				finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(bcr);
	}

	@Override
	public void initilize() {
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		registerReceiver(bcr, intentFilter);
		llMap = (LinearLayout) layoutInflater.inflate(R.layout.map, null);
//		Map = ((SupportMapFragment) getSupportFragmentManager()
//				.findFragmentById(R.id.map)).getMap();

		MapFragment mapFragment = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map));
		mapFragment.getMapAsync(this);

		llMiddleBase.addView(llMap, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		location = getIntent().getStringExtra(AppConstants.LOCATION);
		lat		 = getIntent().getStringExtra(AppConstants.LATITUDE);
		lng	 = getIntent().getStringExtra(AppConstants.LONGITUDE);
		
		latlang = null;
		
		try {
			new Thread(new Runnable()
			{
				@Override
				public void run() 
				{
					latlang = new LatLng(StringUtils.getDouble(lat), StringUtils.getDouble(lng));
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
								Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
								if (latlang != null) {
									Map.addMarker(new MarkerOptions()
									.position(latlang)
									.title(location.replace("+", ", "))
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

									Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang,
											9.0f));
								}
						}
					});
				}
			}).start();
		} catch (Exception e)
		{
			LogUtils.debugLog("Exception in MAP", e.getMessage());
		}
		
		
		
		

		
	}

	/*private class GetMap extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
//			parse_loc.parsing(location);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				latlang = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
//				latlang = parse_loc.getlatlng();
				Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				if (latlang != null) {
					Map.addMarker(new MarkerOptions()
					.position(latlang)
					.title(location.replace("+", ", "))
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

					Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang,
							9.0f));
					super.onPostExecute(result);
				}

			} catch (Exception e) {
			}
		}
	}*/

	@Override
	public void bindingControl() {

	}

}
