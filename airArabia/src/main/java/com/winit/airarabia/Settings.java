package com.winit.airarabia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.AllAirportsMainDO;
import com.winit.airarabia.objects.CountryDO;
import com.winit.airarabia.objects.CurrencyDo;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.QuickSortCurrencyDo;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.webaccess.Response;

public class Settings extends BaseActivity implements DataListener {
	private LinearLayout llSettings;
	private TextView tvLang, tvCurrency, tvCountry;
	private boolean isClcked = false;
	private int CURRENCY_RESULT_CODE = 5000;
	private int COUNTRY_RESULT_CODE = 6000;
	private int LANGUAGE_RESULT_CODE = 7000;
	private boolean isLangChanged = false;
	private String language = "", currency = "", country = "";
	private ArrayList<CurrencyDo> arrListCurrency;

	@Override
	public void initilize() {
		// TODO Auto-generated method stub
		llSettings = (LinearLayout) layoutInflater.inflate(R.layout.settings, null);
		lltop.setVisibility(View.VISIBLE);
		btnBack.setVisibility(View.GONE);
		ivHeader.setVisibility(View.GONE);
		tvHeaderTitle.setVisibility(View.VISIBLE);
		tvHeaderTitle.setText(R.string.settings);
		llMiddleBase.addView(llSettings, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		tvLang = (TextView) llSettings.findViewById(R.id.tvLang);
		tvCurrency = (TextView) llSettings.findViewById(R.id.tvCurrency);
		tvCountry = (TextView) llSettings.findViewById(R.id.tvCountry);

		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(R.string.btn_save);
		btnSubmitNext.setTypeface(typefaceOpenSansSemiBold);
		tvCountry.setTypeface(typeFaceOpenSansLight);
		// tvCountry.setTypeface(typefaceOpenSansSemiBold);
		tvLang.setTypeface(typefaceOpenSansSemiBold);
		tvCurrency.setTypeface(typeFaceOpenSansLight);
		// tvCurrency.setTypeface(typefaceOpenSansSemiBold);
	}

	private void callCurrencyService() {
		showLoader("");
		if (new CommonBL(this, this).getAirportsData()) {
		} else {
			hideLoader();
			showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.InternetProblem),
					getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
		}
	}

	@Override
	public void bindingControl() {

		if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)) {
			tvLang.setText(getString(R.string.english_lang_code));
		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)) {
			tvLang.setText(getString(R.string.french_lang));
		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)) {
			tvLang.setText(getString(R.string.arabic_lang_code));

		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_RU)) {
			tvLang.setText(getString(R.string.russion_lang_code));
		}

		tvLang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				trackEvent("Setting Screen", AppConstants.LanguageButtonInSetting, "");
				Intent i = new Intent(Settings.this, SelectLanguage.class);
				startActivityForResult(i, LANGUAGE_RESULT_CODE);
			}
		});

		// =================Calling Country - City Service to get
		// City===================
		if (AppConstants.vecCountryDO == null)
			callServiceGetCountryNamesData();
		// ==============================================================================
		tvCurrency.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				trackEvent("Setting Screen", AppConstants.CurrencyButtonInSetting, "");
				Intent i = new Intent(Settings.this, SelectCurrency.class);
				i.putExtra("selected_currency", tvCurrency.getText().toString());
				startActivityForResult(i, CURRENCY_RESULT_CODE);

			}
		});
		tvCountry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				trackEvent("Setting Screen", AppConstants.CountryButtonInSetting, "");
				if (AppConstants.vecCountryDO != null && AppConstants.vecCountryDO.size() > 0) {
					Intent i = new Intent(Settings.this, SelectCountry.class);
					i.putExtra("vecCountry", AppConstants.vecCountryDO);
					if (tvCountry != null && !tvCountry.getText().toString().isEmpty())
						i.putExtra("SelectedCountry", tvCountry.getText().toString());
					i.putExtra("HeaderTitle", getString(R.string.please_select_country));
					startActivityForResult(i, COUNTRY_RESULT_CODE);

				}
			}
		});

		btnSubmitNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveCurrencyInPrefrences();
				showCustomDialog(Settings.this, "", getString(R.string.settings_pop_msg), getString(R.string.Ok), null,
						null);

			}
		});
	}

	private void callServiceGetCountryNamesData() {
		showLoader("");
		if (new CommonBL(Settings.this, Settings.this).getCountryNamesData()) {
		} else {
			hideLoader();
			showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
					getString(R.string.Ok), "", "");
		}
	}

	public void clickLangChangeHome() {
		if (!isClcked) {
			isClcked = true;
			if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)
					|| selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)
					|| selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)
					|| selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_RU)) {
				showCustomDialog(Settings.this, "", getString(R.string.settings_pop_msg), getString(R.string.Ok), null,
						null);
			}
			// Intent iHome = new Intent(Settings.this, HomeActivity.class);
			// iHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
			// Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(iHome);
		}
	}

	@Override
	public void dataRetreived(Response data) {
		hideLoader();
		if (data != null && !data.isError) {
			switch (data.method) {
			case AIR_PORT_SDATA:
				AllAirportsMainDO allAirportsMainDO = new AllAirportsMainDO();
				allAirportsMainDO = (AllAirportsMainDO) data.data;
				AppConstants.arrListCurrencies = (ArrayList<CurrencyDo>) allAirportsMainDO.arlCurrencies.clone();
				new QuickSortCurrencyDo().sort(AppConstants.arrListCurrencies);
				if ((tvCurrency.getText().toString().isEmpty()
						|| tvCurrency.getText().toString().equalsIgnoreCase(""))) {
					if (!getCurrencyFromPrefrences().equalsIgnoreCase("")) {
						tvCurrency.setText(getCurrencyFromPrefrences());
						tvCurrency.setTypeface(typefaceOpenSansSemiBold);
						tvCurrency.setTextColor(getResources().getColor(R.color.text_color));
					} else if (checkCurrency()) {
						tvCurrency.setText(CalendarUtility.getCurrencyCodeBasedonLocation(AppConstants.country));
						if (!tvCurrency.getText().toString().equalsIgnoreCase("")) {
							tvCurrency.setTypeface(typefaceOpenSansSemiBold);
							tvCurrency.setTextColor(getResources().getColor(R.color.text_color));
						}
					}
				}
				hideLoader();
				break;

			case WS_COUNTRYNAMES:
				AppConstants.vecCountryDO = (Vector<CountryDO>) data.data;

				Collections.sort(AppConstants.vecCountryDO, new Comparator<CountryDO>() {

					@Override
					public int compare(CountryDO compR, CountryDO compL) {

						return compR.CountryName.compareToIgnoreCase(compL.CountryName);
					}
				});
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
			} else
				showCustomDialog(this, getString(R.string.Alert), getString(R.string.InternetProblem),
						getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CURRENCY_RESULT_CODE && resultCode == RESULT_OK) {
			if (data.hasExtra("Currency_Selected")) {
				String selectedCurrencyCode = (String) data.getStringExtra("Currency_Selected");
				tvCurrency.setText(selectedCurrencyCode.toString());
				currency = selectedCurrencyCode.toString();
				tvCurrency.setTypeface(typefaceOpenSansSemiBold);
				tvCurrency.setTextColor(getResources().getColor(R.color.text_color));
			}

		} else if (requestCode == COUNTRY_RESULT_CODE && resultCode == RESULT_OK) {
			if (data.hasExtra("Country_Selected")) {
				CountryDO countryDo = (CountryDO) data.getSerializableExtra("Country_Selected");
				tvCountry.setText(countryDo.CountryName);
				tvCountry.setTypeface(typefaceOpenSansSemiBold);
				tvCountry.setTextColor(getResources().getColor(R.color.text_color));
				country = countryDo.CountryName;
			}

		} else if (requestCode == LANGUAGE_RESULT_CODE && resultCode == RESULT_OK) {
			if (data.hasExtra("Language_Selected")) {
				isLangChanged = true;
				String selectedLanguageResultCode = (String) data.getStringExtra("Language_Selected");
				tvLang.setText(selectedLanguageResultCode.toString());
				language = selectedLanguageResultCode.toString();
			}

			// clickLanguageChange();
			//
			// Intent iHome = new Intent(Settings.this, Settings.class);
			// finish();
			// startActivity(iHome);
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (isLangChanged) {
			String selectedLanguageCodeNew = SharedPrefUtils.getKeyValue(this, SharedPreferenceStrings.APP_PREFERENCES,
					SharedPreferenceStrings.USER_LANGUAGE);
			if (!selectedLanguageCode.equalsIgnoreCase(selectedLanguageCodeNew)) {

				Intent iHome = new Intent(Settings.this, Settings.class);
				finish();
				startActivity(iHome);
			}
		}
	}

	// ===============================new added for storing currency in sheared
	// prefrences============================//
	private void saveCurrencyInPrefrences() {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString("selectedCurrency", currency + "");
		editor.putString("Selected_Country", country + "");
		editor.commit();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if ((tvCountry.getText().toString().isEmpty() || tvCountry.getText().toString().equalsIgnoreCase(""))) {
			if (!getCountryFromPrefrences().equalsIgnoreCase("")) {

				tvCountry.setText(getCountryFromPrefrences());
				tvCountry.setTypeface(typefaceOpenSansSemiBold);
				tvCountry.setTextColor(getResources().getColor(R.color.text_color));
			} else if (checkCountry()) {
				tvCountry.setText(AppConstants.country);
				tvCountry.setTypeface(typefaceOpenSansSemiBold);
				tvCountry.setTextColor(getResources().getColor(R.color.text_color));
			} else {
				tvCountry.setText(getString(R.string.select_country));
			}

		}
		if ((tvCurrency.getText().toString().isEmpty() || tvCurrency.getText().toString().equalsIgnoreCase(""))) {
			if (!getCurrencyFromPrefrences().equalsIgnoreCase("")) {
				tvCurrency.setText(getCurrencyFromPrefrences());
				tvCurrency.setTypeface(typefaceOpenSansSemiBold);
				tvCurrency.setTextColor(getResources().getColor(R.color.text_color));
			} else if (checkCurrency()) {
				tvCurrency.setText(AppConstants.currencyCode);
				tvCurrency.setTypeface(typefaceOpenSansSemiBold);
				tvCurrency.setTextColor(getResources().getColor(R.color.text_color));
			} else {
				tvCurrency.setText(getString(R.string.selectCurrency));
			}

		}

		if ((tvLang.getText().toString().isEmpty() || tvLang.getText().toString().equalsIgnoreCase(""))) {
			if (!getLangFromPrefrences().equalsIgnoreCase(""))
				tvLang.setText(getLangFromPrefrences());
			else
				tvLang.setText("English");
		}

		if (AppConstants.arrListCurrencies == null)
			callCurrencyService();
		else {
			arrListCurrency = (ArrayList<CurrencyDo>) AppConstants.arrListCurrencies.clone();
		}

	}

	private String getCurrencyFromPrefrences() {
		currency = mPrefs.getString("selectedCurrency", "");
		if (!currency.isEmpty() && !currency.equalsIgnoreCase(""))
			return currency;
		else
			return "";
	}

	private String getCountryFromPrefrences() {
		country = mPrefs.getString("Selected_Country", "");
		if (!country.isEmpty() && !country.equalsIgnoreCase(""))
			return country;
		else
			return "";
	}

	private String getLangFromPrefrences() {
		// TODO Auto-generated method stub
		language = SharedPrefUtils.getKeyValue(this, SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);
		if (!language.isEmpty() && !language.equalsIgnoreCase(""))
			return language;
		else
			return "";

	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		clickHome();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// btnSubmitNext.performClick();
	}

	private boolean checkCountry() {
		boolean check = false;
		if (AppConstants.vecCountryDO != null && AppConstants.vecCountryDO.size() > 0) {
			if (AppConstants.country != null && !AppConstants.country.equals("")) {
				for (int i = 0; i < AppConstants.vecCountryDO.size(); i++) {
					if (AppConstants.vecCountryDO.get(i).CountryName.equalsIgnoreCase(AppConstants.country)) {
						check = true;
						break;
					}
				}
			}
		}
		return check;
	}

	private boolean checkCurrency() {
		boolean check = false;
		if (AppConstants.country != null && !AppConstants.country.equals("")) {
			String id = CalendarUtility.getCurrencyCodeBasedonLocation(AppConstants.country);
			AppConstants.currencyCode = id;
			if (AppConstants.arrListCurrencies != null && AppConstants.arrListCurrencies.size() > 0) {
				for (int i = 0; i < AppConstants.arrListCurrencies.size(); i++) {
					if (AppConstants.arrListCurrencies.get(i).code.equalsIgnoreCase(id)) {
						check = true;
						break;
					}
				}
			}
		}
		return check;
	}
}
