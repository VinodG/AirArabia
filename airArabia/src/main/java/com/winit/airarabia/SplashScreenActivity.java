package com.winit.airarabia;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.Signature;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.pushio.manager.PushIOManager;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.dataaccess.AirportDA;
import com.winit.airarabia.database.DatabaseHelper;
import com.winit.airarabia.objects.AirportNamesDO;
import com.winit.airarabia.objects.AirportsDO;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.AllAirportsMainDO;
import com.winit.airarabia.objects.CurrencyDo;
import com.winit.airarabia.utils.LogUtils;
import com.winit.airarabia.utils.NetworkUtility;
import com.winit.airarabia.utils.QuickSortCurrencyDo;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.webaccess.Response;

import android.*;
import android.Manifest;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.*;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static java.security.AccessController.getContext;


public class SplashScreenActivity extends BaseActivity implements DataListener {

	private LinearLayout llsplash;
	private List<Address> addresses = null;
	private TextView tvVersionName;
	private final String TAG = "SplashScreen";
	private GpsLocationReceiver gpsReciever;
	private LocationManager manager;
	private String gcmId ="";
	SharedPrefUtils preference;

	public static final int R_PERM = 2822;
	private static final int REQUEST= 112;

	@Override
	public void initilize() {
		llsplash = (LinearLayout) layoutInflater.inflate(R.layout.splash, null);
		lltop.setVisibility(View.GONE);
		llMiddleBase.addView(llsplash, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		preference = new SharedPrefUtils(SplashScreenActivity.this);
		gpsReciever = new GpsLocationReceiver();
		registerReceiver(gpsReciever, intentFilter);
//		gcmRegister();
//		if (checkPermission())

		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

		initializeSplash();
		gcmId =  preference.getStringFromPreference(SharedPrefUtils.gcmId, "");
	}



	public class GpsLocationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
				if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					Toast.makeText(SplashScreenActivity.this, "Please...! Turn on GPS", Toast.LENGTH_LONG);
				} else {
					loadData();
				}
			}
		}
	}

//	public void gcmRegister()
//	{
//
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//
//				/**
//				 * If the registration registration id is null, then we can
//				 * register as follows
//				 */
//				if (!GCMRegistrar.isRegistered(SplashScreenActivity.this))
//				{
//					if (NetworkUtility.isNetworkConnectionAvailable(SplashScreenActivity.this))
//					{
//						try {
//							/**
//							 * To receive GCM push notifications, device must be
//							 * at least API Level 8
//							 */
//							GCMRegistrar.checkDevice(SplashScreenActivity.this);
//
//							/**
//							 * Check manifest whether it is having
//							 * "permission.C2D_MESSAGE",
//							 * "com.google.android.c2dm.permission.SEND",
//							 * "com.google.android.c2dm.intent.REGISTRATION",
//							 * "com.google.android.c2dm.intent.RECEIVE"
//							 * permissions or not. By using manifest tag
//							 * "<service android:name=".GCMIntentService" />" we
//							 * can start GCM service,
//							 *
//							 */
//							GCMRegistrar.checkManifest(SplashScreenActivity.this);
//							AppConstants.GCMRegistrationAttempts++;
//							GCMRegistrar.register(SplashScreenActivity.this,AppConstants.SENDER_ID);
//						} catch (Exception e)
//						{
//							e.printStackTrace();
//						}
//					}
//				}
//				else
//				{
////					new GCMIntentService();
//				}
//			}
//		}).start();
//
//	}

	public void initializeSplash() {
		/*llsplash = (LinearLayout) layoutInflater.inflate(R.layout.splash, null);

		lltop.setVisibility(View.GONE);
		llMiddleBase.addView(llsplash, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);*/
		tvVersionName = (TextView) llsplash.findViewById(R.id.versionName);

		AppConstants.allAirportNamesDO = null;
		AppConstants.allAirportNamesDO = new AllAirportNamesDO();
		AppConstants.allAirportsDO = null;
		AppConstants.allAirportsDO = new AllAirportsDO();
		AppConstants.DIR_PATH = getApplication().getFilesDir().toString() + "/";
		tvVersionName.setVisibility(View.GONE);
		tvVersionName.setTypeface(typeFaceOpenSansLight);
		lockDrawer("SplashScreen");
		//	createDataBase();//uncomment if want to save airport list in data base
		// loadData();
	}

//	private final int REQUEST_CODE_ASK_PERMISSIONS = 1000;
//
//	public boolean checkPermission() {
//		int currentAPIVersion = Build.VERSION.SDK_INT;
//		if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
//			final ArrayList<String> permissions = new ArrayList<String>();
//
//			if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
//					android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//				permissions.add(android.Manifest.permission.CALL_PHONE);
//			}
//			if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
//					android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//				permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
//			}
//			if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
//					android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//				permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//			}
//
//			if (permissions.size() > 0) {
//
//				int permissionLength = permissions.size();
//				final String[] permissionArray = new String[permissionLength];
//				for (int i = 0; i < permissionLength; i++) {
//					permissionArray[i] = permissions.get(i);
//				}
//				ActivityCompat.requestPermissions(SplashScreenActivity.this, permissionArray,
//						REQUEST_CODE_ASK_PERMISSIONS);
//				return false;
//			} else {
//				return true;
//			}
//		} else {
//			return true;
//		}
//	}
//
//	@Override
//	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//		LogUtils.errorLog("Splash screen", "onRequestPermissionsResult");
//		switch (requestCode) {
//		case REQUEST_CODE_ASK_PERMISSIONS:
//			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//				initializeSplash();
//			} else {
//				Toast.makeText(SplashScreenActivity.this, "All permissions are required.", Toast.LENGTH_LONG).show();
//				finish();
//			}
//			break;
//		}
//	}

	@Override
	public void bindingControl() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		AppConstants.DEVICE_WIDTH= displaymetrics.widthPixels;
		AppConstants.DEVICE_HEIGHT=displaymetrics.heightPixels;
		/*Display display = getWindowManager().getDefaultDisplay();
		AppConstants.DEVICE_WIDTH = display.getWidth();
		AppConstants.DEVICE_HEIGHT = display.getHeight();*/

	}

	@Override
	protected void onStart() {
		super.onStart();
		try{
			PushIOManager.getInstance(this).registerApp();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			showCustomDialog(getApplicationContext(), getString(R.string.Alert),
					getString(R.string.please_enable_your_location_for_closest_airport), getString(R.string.skip),
					getString(R.string.enable_location), "GPS_splash");
		} else {
			loadData();
		}
	}

	private void loadData() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				callFlightListService(true);
			}

		}, 100);

	}

	public void onButtonYesClick(String from) {
		if (from.equalsIgnoreCase("GPS_splash")) {
			loadData();
		}
	}

	public void onButtonNoClick(String from) {
		if (from.equalsIgnoreCase("GPS_splash")) {
			startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}
		if (from.equalsIgnoreCase("GPS_REQ")) {
			finish();
			startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}

	}

	private void callFlightListService(boolean isLoaderShowing) {
		if (!isLoaderShowing)
			showLoader("");
		if (new CommonBL(this, this).getAirportsData()) {
		} else {
			hideLoader();
			showCustomDialog(getApplicationContext(),
					"" /* getString(R.string.Alert) */, getString(R.string.InternetProblem), getString(R.string.Ok), "",
					AppConstants.INTERNET_PROBLEM);
		}
	}

	private void moveToNextActivity() {
		String isLangSelected = SharedPrefUtils.getKeyValue(SplashScreenActivity.this,
				SharedPreferenceStrings.APP_PREFERENCES, SharedPreferenceStrings.LANGUAGE_IS_SELECTED);
		if (isLangSelected.equalsIgnoreCase("selected")) {

			Intent in = new Intent(getApplicationContext(), PreLogin.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
			finish();
		} else {
			Intent in = new Intent(getApplicationContext(), ChooseLanguageFirstScreen.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
			finish();
		}
	}

	public void createDataBase() {
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		try {
			dbHelper.createDataBase();
			DatabaseHelper.openDataBase();
		} catch (Exception e) {
			LogUtils.errorLog(TAG, "Database Error MSG" + e.getMessage());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(gpsReciever);
	}

	@Override
	public void dataRetreived(Response data) {

		if (data != null && !data.isError) {
			switch (data.method) {
				case AIR_PORT_SDATA:
					AppConstants.allAirportsDO = new AllAirportsDO();
					AppConstants.allAirportNamesDO = new AllAirportNamesDO();

					AppConstants.allAirportsDO = new AllAirportsDO();
					AppConstants.allAirportNamesDO = new AllAirportNamesDO();

					AllAirportsMainDO allAirportsMainDO = new AllAirportsMainDO();
					allAirportsMainDO = (AllAirportsMainDO) data.data;
					AppConstants.allAirportsDO = allAirportsMainDO.airportParserDO;
					AppConstants.allAirportNamesDO = allAirportsMainDO.allAirportNamesDO;
					AppConstants.arrListCurrencies = (ArrayList<CurrencyDo>) allAirportsMainDO.arlCurrencies.clone();
					new QuickSortCurrencyDo().sortAirPorts(AppConstants.allAirportsDO.vecAirport);
					new QuickSortCurrencyDo().sortAirPortNames(AppConstants.allAirportNamesDO.vecAirport);
					new QuickSortCurrencyDo().sort(AppConstants.arrListCurrencies);
					//uncomment if want to save airpost list in data base**********************
//				AirportDA airportDA = new AirportDA(this);
//
//				if (AppConstants.allAirportsDO.vecAirport.size() < 1
//						&& AppConstants.allAirportNamesDO.vecAirport.size() < 1
//						&& AppConstants.arrListCurrencies.size() < 1) {
//					airportDA.insertAllAirport(AppConstants.allAirportsDO.vecAirport);
//					airportDA.insertAllAirportName(AppConstants.allAirportNamesDO.vecAirport);
//					airportDA.insertCurrency(AppConstants.arrListCurrencies);
//				} else {
//					airportDA.deleteAllAirport();
//					airportDA.insertAllAirport(AppConstants.allAirportsDO.vecAirport);
//					airportDA.deleteAllAirportName();
//					airportDA.insertAllAirportName(AppConstants.allAirportNamesDO.vecAirport);
//					airportDA.deleteCurrency();
//					airportDA.insertCurrency(AppConstants.arrListCurrencies);
//				}
//***************************************************************************
				/*
				 * AppConstants.allAirportsDO =
				 * allAirportsMainDO.airportParserDO;
				 * AppConstants.allAirportNamesDO =
				 * allAirportsMainDO.allAirportNamesDO;
				 * AppConstants.arrListCurrencies = (ArrayList<CurrencyDo>)
				 * allAirportsMainDO.arlCurrencies.clone(); new
				 * QuickSortCurrencyDo().sortAirPorts(AppConstants.allAirportsDO
				 * .vecAirport); new
				 * QuickSortCurrencyDo().sortAirPortNames(AppConstants.
				 * allAirportNamesDO.vecAirport); new
				 * QuickSortCurrencyDo().sort(AppConstants.arrListCurrencies);
				 */
//==============Added on 21-Feb-2018 by Mukesh to stop splashscreen for user actions to the permissions dialog ==========================
					if (Build.VERSION.SDK_INT >= 23) {
						Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
						String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE,
								android.Manifest.permission.ACCESS_FINE_LOCATION,
								Manifest.permission.ACCESS_COARSE_LOCATION,
								Manifest.permission.READ_CONTACTS,
								Manifest.permission.WRITE_CALENDAR,
								Manifest.permission.READ_CALENDAR,
								Manifest.permission.ACCESS_NETWORK_STATE,
								Manifest.permission.INTERNET,
								Manifest.permission.ACCESS_WIFI_STATE,
								Manifest.permission.CHANGE_NETWORK_STATE,
								Manifest.permission.VIBRATE,
								Manifest.permission.WAKE_LOCK,
								android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
						};


						if (!hasPermissions(SplashScreenActivity.this, PERMISSIONS)) {
							Log.d("TAG","@@@ IN IF hasPermissions");
							ActivityCompat.requestPermissions((Activity) SplashScreenActivity.this, PERMISSIONS, REQUEST );
						} else {
							Log.d("TAG","@@@ IN ELSE hasPermissions");
							moveToNextActivity();
						}
					} else {
						Log.d("TAG","@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
						moveToNextActivity();
					}
//======================================================================================================================
//				moveToNextActivity();

					hideLoader();
					break;

				default:
					break;
			}
		} else {
			if (data.data instanceof String) {
				if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					showCustomDialog(getApplicationContext(), getString(R.string.Alert),
							getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "",
							AppConstants.INTERNET_PROBLEM);
				else
					showCustomDialog(getApplicationContext(), getString(R.string.Alert),
							getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
			}
			hideLoader();
		}

	}
	//==============Added on 21-Feb-2018 by Mukesh to stop splashscreen for user actions to the permissions dialog ==========================
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQUEST: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d("TAG","@@@ PERMISSIONS grant");
					moveToNextActivity();
				} else {
					Log.d("TAG","@@@ PERMISSIONS Denied");
//					Toast.makeText(SplashScreenActivity.this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	private static boolean hasPermissions(Context context, String... permissions) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
			for (String permission : permissions) {
				if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
					return false;
				}
			}
		}
		return true;
	}

	//        ===========================================================================================
}