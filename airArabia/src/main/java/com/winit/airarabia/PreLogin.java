package com.winit.airarabia;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.insider.android.insider.Insider;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.insider.GcmBroadcastReceiver;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.AllAirportsMainDO;
import com.winit.airarabia.objects.CountryDO;
import com.winit.airarabia.objects.CurrencyDo;
import com.winit.airarabia.objects.KeyValue;
import com.winit.airarabia.objects.PosterImagesDO;
import com.winit.airarabia.utils.QuickSortCurrencyDo;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.webaccess.Response;

public class PreLogin extends BaseActivity implements DataListener{

	private LinearLayout llPreLogin;
	private Button btnLogin, btnRegister, btnContinueGuest;
	private PosterImagesDO posterImagesDO;

	@Override
	public void initilize() {
		llPreLogin = (LinearLayout) layoutInflater.inflate(R.layout.activity_pre_login, null);
		setTypefaceOpenSansRegular(llPreLogin);
		lltop.setVisibility(View.GONE);
		lockDrawer("PreLogin");
		llMiddleBase.addView(llPreLogin, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		initClassMembers();

		Insider insider = new Insider();
		insider.openSession(this, AppConstants.ProjectName);
		insider.registerInBackground(this, AppConstants.GoogleProjectNumber);
		Insider.setLandingActivity(GcmBroadcastReceiver.class, this);
		Insider.setNotificationIcon(R.drawable.ic_launcher, this);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		Insider.start(this, intent);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Insider.stop(this);
	}

	@Override
	public void bindingControl() {


		btnLogin.setTypeface(typefaceOpenSansSemiBold);
		btnRegister.setTypeface(typefaceOpenSansSemiBold);
		btnContinueGuest.setTypeface(typefaceOpenSansSemiBold);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {    
								Insider.tagEvent(AppConstants.LogInButtonGlobal, PreLogin.this);
				//Insider.Instance.tagEvent(PreLogin.this,AppConstants.LogInButtonGlobal);
    			trackEvent("PreLogin Screen",AppConstants.LogInButtonGlobal,"");
				Intent in = new Intent(PreLogin.this, LoginActivityScreen.class);
				startActivity(in);
			}
		});

		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
								Insider.tagEvent(AppConstants.RegisterButtonGlobal, PreLogin.this);
				//Insider.Instance.tagEvent(PreLogin.this,AppConstants.RegisterButtonGlobal);
				trackEvent("PreLogin Screen",AppConstants.RegisterButtonGlobal,"");
				Intent in = new Intent(PreLogin.this, Register.class);
				startActivity(in);
			}
		});
		btnContinueGuest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
								Insider.tagEvent("ContinueAsGuest_button_clicked", PreLogin.this);
				//Insider.Instance.tagEvent(PreLogin.this,"ContinueAsGuest_button_clicked");
				trackEvent("PreLogin Screen","ContinueAsGuest_button_clicked","");
				Intent in = new Intent(PreLogin.this, HomeActivity.class);
				KeyValue keyValue = new KeyValue(SharedPreferenceStrings.isLoggedIn, 0+"");
				SharedPrefUtils.setValue(PreLogin.this, SharedPreferenceStrings.APP_PREFERENCES, keyValue);
				startActivity(in);
			}
		});

		///===================Here we are calling poster Service to show poster on Home screen ===================//	
		callPosterService();
		///=======================================================================================================//        



	}

	private void initClassMembers()
	{
		btnLogin            = (Button) findViewById(R.id.btnLogin);
		btnContinueGuest    = (Button) findViewById(R.id.btnContinueAsGuest);
		btnRegister         = (Button) findViewById(R.id.btnRegister);
	}

	@Override
	protected void onResume() {
		super.onResume();
		synchronized (LOCATION_SERVICE) {
			refreshLocation();
		}
		/* tracker.setScreenName("PreLogin");
	        tracker.send(new HitBuilders.ScreenViewBuilder().build());*/
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		String selectedLanguageCodeNew = SharedPrefUtils.getKeyValue(this,
				SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);
		if (!selectedLanguageCode.equalsIgnoreCase(selectedLanguageCodeNew)) {

			Intent iHome = new Intent(PreLogin.this, PreLogin.class);
			finish();
			startActivity(iHome);
		}

	}

	private void callPosterService() {
		showLoader("");
		if (new CommonBL(PreLogin.this, PreLogin.this).getBannerImages())
		{
		} 
		else 
		{
			hideLoader();
			showCustomDialog(getApplicationContext(),
					getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "",
					AppConstants.INTERNET_PROBLEM);
		}
	}

	/*private void callFlightListService(boolean isLoaderShowing)
	{
		if(!isLoaderShowing)
			showLoader("");
		if(new CommonBL(this, this).getAirportsData())
		{}else
		{
			hideLoader();				
			showCustomDialog(getApplicationContext(),"" getString(R.string.Alert), getString(R.string.InternetProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
		}
	}
*/
	private void callServiceGetCountryNamesData(boolean isLoaderShowing) {
		if(!isLoaderShowing)
			showLoader("");
		if (new CommonBL(PreLogin.this, PreLogin.this).getCountryNamesData()) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	@Override
	public void dataRetreived(Response data) {

		if (data != null && !data.isError) {
			switch (data.method) {
			case WS_POSTER_DOWNLOAD:
				if (data.data instanceof PosterImagesDO) {
					posterImagesDO = (PosterImagesDO) data.data;

					if (posterImagesDO != null) {
						AppConstants.imagePosterArr = new PosterImagesDO();
						AppConstants.imagePosterArr = posterImagesDO;
					}
					for (int i = 0; i < AppConstants.imagePosterArr.arrayListEN.size(); i++) {
							downloadFile(AppConstants.imagePosterArr.arrayListEN.get(i));
					}
					for (int i = 0; i < AppConstants.imagePosterArr.arrayListAR.size(); i++) {
							downloadFile(AppConstants.imagePosterArr.arrayListAR.get(i));
					}
					for (int i = 0; i < AppConstants.imagePosterArr.arrayListFR.size(); i++) {
							downloadFile(AppConstants.imagePosterArr.arrayListFR.get(i));
					}

				}

				callServiceGetCountryNamesData(true);
				break;

			case WS_COUNTRYNAMES:
				AppConstants.vecCountryDO = (Vector<CountryDO>) data.data;

				Collections.sort(AppConstants.vecCountryDO, new Comparator<CountryDO>() {

					@Override
					public int compare(CountryDO compR , CountryDO compL) 
					{

						return compR.CountryName.compareToIgnoreCase(compL.CountryName);
					}
				});

/*				if(AppConstants.allAirportsDO != null
						&& AppConstants.allAirportsDO.vecAirport != null
						&& AppConstants.allAirportsDO.vecAirport.size()>0 
						&& AppConstants.allAirportNamesDO != null
						&& AppConstants.allAirportNamesDO.vecAirport != null
						&& AppConstants.allAirportNamesDO.vecAirport.size() > 0)
				{
					callFlightListService(true);
				}
				else */
					hideLoader();
				break;

			case AIR_PORT_SDATA:

				AppConstants.allAirportsDO = new AllAirportsDO();
				AppConstants.allAirportNamesDO = new AllAirportNamesDO();

				AllAirportsMainDO allAirportsMainDO = new AllAirportsMainDO();
				allAirportsMainDO 				= (AllAirportsMainDO)data.data;
				AppConstants.allAirportsDO 		= allAirportsMainDO.airportParserDO;
				AppConstants.allAirportNamesDO 	= allAirportsMainDO.allAirportNamesDO;
				AppConstants.arrListCurrencies	= (ArrayList<CurrencyDo>) allAirportsMainDO.arlCurrencies.clone();
				new QuickSortCurrencyDo().sortAirPorts(AppConstants.allAirportsDO.vecAirport);
				new QuickSortCurrencyDo().sortAirPortNames(AppConstants.allAirportNamesDO.vecAirport);
				new QuickSortCurrencyDo().sort(AppConstants.arrListCurrencies);
				hideLoader();
				break;



			default:
				break;
			}
		} else {
			if (data.data instanceof String) {
				if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					showCustomDialog(getApplicationContext(),
							getString(R.string.Alert),
							getString(R.string.ConnenectivityTimeOutExpMsg),
							getString(R.string.Ok), "",
							AppConstants.INTERNET_PROBLEM);
				else
					showCustomDialog(getApplicationContext(),
							getString(R.string.Alert),
							getString(R.string.TechProblem),
							getString(R.string.Ok), "",
							AppConstants.INTERNET_PROBLEM);
			}
			hideLoader();
		}



	}

	public void downloadFile(String uRl) {

		try {


			String [] urlTemp = uRl.split("/");
			String photoName = urlTemp[urlTemp.length-1];
			File direct = new File(getApplication().getFilesDir().toString()
					+ "/AirArabiaFiles/"+photoName);

			if (!direct.exists()) {
				File direct1 = new File(getApplication().getFilesDir().toString()
						+ "/AirArabiaFiles");
				if (!direct1.exists()) {
					direct1.mkdir();
				}
				DownloadManager mgr = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);

				Uri downloadUri = Uri.parse(uRl);
				DownloadManager.Request request = new DownloadManager.Request(
						downloadUri);

				request.setAllowedNetworkTypes(
						DownloadManager.Request.NETWORK_WIFI
						| DownloadManager.Request.NETWORK_MOBILE)
						.setAllowedOverRoaming(true).setTitle(photoName)
						.setDescription("Downloading images of new offers.")
						.setDestinationUri(Uri.parse(getApplication().getFilesDir().toString()+"/AirArabiaFiles/"+ photoName));

				mgr.enqueue(request);
			}



		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}
}
