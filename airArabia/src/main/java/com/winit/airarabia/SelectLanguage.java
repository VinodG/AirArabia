package com.winit.airarabia;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.adapters.SelectLanguageAdapter;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.objects.KeyValue;
import com.winit.airarabia.utils.SharedPrefUtils;

public class SelectLanguage extends BaseActivity{

	private LinearLayout llSelectCurrency;
	private ListView list;
	private SelectLanguageAdapter languageAdapter;
	private EditText etSearch;
	private TextView tvCancel,tvSelectCurrencyTitle, tvNotFound;
	private ArrayList<String> arrListCurrency;
	private int prevPosition =-1,position = -1;
	private KeyValue keyValue = new KeyValue(SharedPreferenceStrings.USER_LANGUAGE, "En");
	private boolean isClcked = false;
	@Override
	public void initilize() {
		// TODO Auto-generated method stub
		llSelectCurrency = (LinearLayout) layoutInflater.inflate(R.layout.select_currency_new, null);
		// lltop.setVisibility(View.VISIBLE);
		lltop.setVisibility(View.GONE);
		llMiddleBase.addView(llSelectCurrency, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		
		list = (ListView) llSelectCurrency.findViewById(R.id.list);
		etSearch = (EditText) llSelectCurrency.findViewById(R.id.etSearch);
		tvCancel = (TextView) llSelectCurrency.findViewById(R.id.tvCancel);
		tvSelectCurrencyTitle = (TextView) llSelectCurrency.findViewById(R.id.tvSelectCurrency);
		tvNotFound=(TextView) llSelectCurrency.findViewById(R.id.tvNoItem);
		tvSelectCurrencyTitle.setText(getString(R.string.select_language));
		
		setTypeFaceOpenSansLight(llSelectCurrency);
		
		tvSelectCurrencyTitle.setTypeface(typefaceOpenSansSemiBold);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
	}

	@Override
	public void bindingControl() {
		updateLanguageList();
		updateLanguage();
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String text = etSearch.getText().toString();
				languageAdapter.filter(text);
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
//				String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
//				spinneradap.filter(text);
			}
		});
		
		tvCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	private void updateLanguageEnglish()
	{
		keyValue.value = AppConstants.LANG_EN;
		SharedPrefUtils.setValue(SelectLanguage.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				keyValue);
		selectedLanguageCode = SharedPrefUtils.getKeyValue(
				SelectLanguage.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);

		if (selectedLanguageCode
				.equalsIgnoreCase(AppConstants.LANG_EN)
				&& !configuration.locale
						.getLanguage()
						.toString()
						.equalsIgnoreCase(
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
		SharedPrefUtils.setValue(SelectLanguage.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				keyValue);
		selectedLanguageCode = SharedPrefUtils.getKeyValue(
				SelectLanguage.this,
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
		SharedPrefUtils.setValue(SelectLanguage.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				keyValue);
		selectedLanguageCode = SharedPrefUtils.getKeyValue(
				SelectLanguage.this,
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
	private void updateLanguageRussion()
	{
		keyValue.value = AppConstants.LANG_RU;
		SharedPrefUtils.setValue(SelectLanguage.this,
				SharedPreferenceStrings.APP_PREFERENCES,
				keyValue);
		selectedLanguageCode = SharedPrefUtils.getKeyValue(
				SelectLanguage.this,
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
	
//	public void clickLangChangeHome() {
//		if (!isClcked) {
//			isClcked = true;
//			Intent iHome = new Intent(SelectLanguage.this, Settings.class);
////			iHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(iHome);
//		}
//	}
	
	private void updateLanguageList() {
		arrListCurrency = new ArrayList<String>();
		arrListCurrency.add(getString(R.string.english_lang_code));
		arrListCurrency.add(getString(R.string.french_lang_code));
		arrListCurrency.add(getString(R.string.arabic_lang_code));
		//arrListCurrency.add(getString(R.string.russion_lang_code));
		
	}

	private void updateLanguage()
	{
		if (selectedLanguageCode.equalsIgnoreCase(""))
		{
			selectedLanguageCode = AppConstants.LANG_EN;
			position=0;
			prevPosition = 0;
		}
		else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)) {
			position=0;
			prevPosition = 0;
		}
		else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)) {
			position=1;
			prevPosition = 1;
		}
		else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)) {
			position=2;
			prevPosition = 2;
		}
		else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_RU)) {
			position=3;
			prevPosition = 3;
		}
		languageAdapter = new SelectLanguageAdapter(SelectLanguage.this, arrListCurrency,selectedLanguageCode,list,tvNotFound);
		list.setAdapter(languageAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				position = pos;
				if (position >= 0) {
					arg0.getChildAt(prevPosition).findViewById(R.id.ivCurrencyChecked).setVisibility(View.INVISIBLE);
					prevPosition = position;
				}
				else {
					prevPosition = pos;
				}

				arg1.findViewById(R.id.ivCurrencyChecked).setVisibility(View.VISIBLE);
				
				hideKeyBoard(llSelectCurrency);
				
//=============newly added for changes=========================================				
				if (position >= 0 
						&& languageAdapter != null 
						&& languageAdapter.getData() != null 
						&& position < languageAdapter.getData().size()) {
					
					if (position == 0) {
						updateLanguageEnglish();
					} else if (position == 1) {
						updateLanguageFrench();
					}else if (position == 2) {
						updateLanguageArabic();
					}
				else if (position == 3) {
					updateLanguageRussion();
				}
					
					Intent intent = new Intent();
					intent.putExtra("Language_Selected",languageAdapter.getData().get(position));
					
					setResult(RESULT_OK, intent);        
					finish();
//					clickLanguageChange();
				}
			}
		});

	}

}
