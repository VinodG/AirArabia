package com.winit.airarabia;

import java.util.Locale;

import android.app.ActionBar;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.insider.android.insider.Insider;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.insider.GcmBroadcastReceiver;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.KeyValue;
import com.winit.airarabia.utils.SharedPrefUtils;

public class ChooseLanguageFirstScreen extends BaseActivity {

    private LinearLayout llPreLogin;
    private Button btnEnglishLang, btnFrenchLang, btnArabicLang,btnRussianLang;
    private KeyValue keyValue = new KeyValue(SharedPreferenceStrings.USER_LANGUAGE, "En");
    private KeyValue keyValueLangFirstPage = new KeyValue(SharedPreferenceStrings.LANGUAGE_IS_SELECTED, "");
  
    @Override
    public void initilize() {
        llPreLogin = (LinearLayout) layoutInflater.inflate(R.layout.activity_choose_lang_first_screen, null);
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
		//Insider.Instance.start(this);
		Intent intent = getIntent();
		Insider.start(this, intent);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
			Insider.stop(this);	}
	
    @Override
    public void bindingControl() {
    	
    	btnEnglishLang.setTypeface(typefaceOpenSansSemiBold);
    	btnFrenchLang.setTypeface(typefaceOpenSansSemiBold);
    	btnArabicLang.setTypeface(typefaceOpenSansSemiBold);
    	btnRussianLang.setTypeface(typefaceOpenSansSemiBold);


    	btnEnglishLang.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			//Insider.tagEvent("English_button_clicked", ChooseLanguageFirstScreen.this);
    			    			Insider.tagEvent("English_button_clicked", ChooseLanguageFirstScreen.this);
    			//Insider.Instance.tagEvent( ChooseLanguageFirstScreen.this,"English_button_clicked");
				trackEvent("Launch Screen","English_button_clicked","");
    			updateLanguageEnglish();
    			moveToNextActivity();
    		}
    	});
    	btnFrenchLang.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			//Insider.tagEvent("French_button_clicked", ChooseLanguageFirstScreen.this);
    			    			Insider.tagEvent("French_button_clicked", ChooseLanguageFirstScreen.this);
    			//Insider.Instance.tagEvent( ChooseLanguageFirstScreen.this,"French_button_clicked");
    			trackEvent("Launch Screen","French_button_clicked","");
    			updateLanguageFrench();
    			moveToNextActivity();
    		}
    	});
    	btnArabicLang.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			//Insider.tagEvent("Arabic_button_clicked", ChooseLanguageFirstScreen.this);
    			    			Insider.tagEvent("Arabic_button_clicked", ChooseLanguageFirstScreen.this);
    			//Insider.Instance.tagEvent( ChooseLanguageFirstScreen.this,"Arabic_button_clicked");
    			trackEvent("Launch Screen","Arabic_button_clicked","");
    			updateLanguageArabic();
    			moveToNextActivity();
    		}
    	});
    	btnRussianLang.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			//Insider.tagEvent("Arabic_button_clicked", ChooseLanguageFirstScreen.this);
    			Insider.tagEvent("Russian_button_clicked", ChooseLanguageFirstScreen.this);
    			//Insider.Instance.tagEvent( ChooseLanguageFirstScreen.this,"Arabic_button_clicked");
    			trackEvent("Launch Screen","Russian_button_clicked","");
    			updateLanguageRussian();
    			moveToNextActivity();
    		}
    	});
        
    }

    private void initClassMembers()
    {
    	btnEnglishLang            = (Button) findViewById(R.id.btnEnglishLang);
        btnFrenchLang    = (Button) findViewById(R.id.btnFrenchLang);
        btnArabicLang         = (Button) findViewById(R.id.btnArabicLang);
        btnRussianLang         = (Button) findViewById(R.id.btnRussianLang);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	synchronized (LOCATION_SERVICE) {
    		refreshLocation();
		}
    	Log.i("Choose Lang", "Setting screen name: ");
        tracker.setScreenName("Choose Langauge");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    	AppConstants.allAirportsDO = new AllAirportsDO();
		AppConstants.allAirportNamesDO = new AllAirportNamesDO();
    }
    
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		String selectedLanguageCodeNew = SharedPrefUtils.getKeyValue(this,
				SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);
		if (!selectedLanguageCode.equalsIgnoreCase(selectedLanguageCodeNew)) {

			Intent iHome = new Intent(ChooseLanguageFirstScreen.this, ChooseLanguageFirstScreen.class);
			finish();
			startActivity(iHome);
		}
		
	}
    
    public void moveToNextActivity()
    {
    	keyValueLangFirstPage.value = "selected";
		SharedPrefUtils.setValue(ChooseLanguageFirstScreen.this,SharedPreferenceStrings.APP_PREFERENCES,keyValueLangFirstPage);
    	Intent intent = new Intent(getApplicationContext(), PreLogin.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    	finish();
    }
    
    
	private void updateLanguageEnglish()
	{
		keyValue.value = AppConstants.LANG_EN;
		SharedPrefUtils.setValue(ChooseLanguageFirstScreen.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				keyValue);
		selectedLanguageCode = SharedPrefUtils.getKeyValue(
				ChooseLanguageFirstScreen.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);

		if (selectedLanguageCode
				.equalsIgnoreCase(AppConstants.LANG_EN)&& !configuration.locale.getLanguage().toString().equalsIgnoreCase(
								AppConstants.LANG_LOCAL_EN)) {
			locale2 = new Locale(AppConstants.LANG_LOCAL_EN);
			Locale.setDefault(locale2);
			configuration.locale = locale2;
			resources.updateConfiguration(configuration,
					getBaseContext().getResources()
							.getDisplayMetrics());
//			clickLangChangeHome();
		}
	}
	private void updateLanguageFrench()
	{
		keyValue.value = AppConstants.LANG_FR;
		SharedPrefUtils.setValue(ChooseLanguageFirstScreen.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				keyValue);
		selectedLanguageCode = SharedPrefUtils.getKeyValue(
				ChooseLanguageFirstScreen.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);

		if (selectedLanguageCode
				.equalsIgnoreCase(AppConstants.LANG_FR)
				&& !configuration.locale
				.getLanguage()
				.toString()
				.equalsIgnoreCase(
						AppConstants.LANG_LOCAL_FR)) {
			locale2 = new Locale(AppConstants.LANG_LOCAL_FR);
			Locale.setDefault(locale2);
			configuration.locale = locale2;
			resources.updateConfiguration(configuration,
					getBaseContext().getResources()
					.getDisplayMetrics());
//			clickLangChangeHome();
		}}
	private void updateLanguageArabic()
	{
		keyValue.value = AppConstants.LANG_AR;
		SharedPrefUtils.setValue(ChooseLanguageFirstScreen.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				keyValue);
		selectedLanguageCode = SharedPrefUtils.getKeyValue(
				ChooseLanguageFirstScreen.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);
		
		if (selectedLanguageCode
				.equalsIgnoreCase(AppConstants.LANG_AR)
				&& !configuration.locale
				.getLanguage()
				.toString()
				.equalsIgnoreCase(
						AppConstants.LANG_LOCAL_AR)) {
			locale2 = new Locale(AppConstants.LANG_LOCAL_AR);
			Locale.setDefault(locale2);
			configuration.locale = locale2;
			resources.updateConfiguration(configuration,
					getBaseContext().getResources()
					.getDisplayMetrics());
//			clickLangChangeHome();
		}
	}
	private void updateLanguageRussian()
	{
		keyValue.value = AppConstants.LANG_RU;
		SharedPrefUtils.setValue(ChooseLanguageFirstScreen.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				keyValue);
		selectedLanguageCode = SharedPrefUtils.getKeyValue(
				ChooseLanguageFirstScreen.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);
		
		if (selectedLanguageCode
				.equalsIgnoreCase(AppConstants.LANG_RU)
				&& !configuration.locale
				.getLanguage()
				.toString()
				.equalsIgnoreCase(
						AppConstants.LANG_LOCAL_RU)) {
			locale2 = new Locale(AppConstants.LANG_LOCAL_RU);
			Locale.setDefault(locale2);
			configuration.locale = locale2;
			resources.updateConfiguration(configuration,
					getBaseContext().getResources()
					.getDisplayMetrics());
//			clickLangChangeHome();
		}
	}
}
