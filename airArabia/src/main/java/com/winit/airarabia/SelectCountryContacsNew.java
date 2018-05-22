package com.winit.airarabia;

import java.util.ArrayList;

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

import com.winit.airarabia.adapters.CountryAdapterContactsNew;
import com.winit.airarabia.objects.CountriesDO;

public class SelectCountryContacsNew extends BaseActivity{

	private LinearLayout llSelectCurrency;
	private ListView list;
	private CountryAdapterContactsNew countryAdapter;
	private EditText etSearch;
	private TextView tvCancel,tvSelectCurrencyTitle, tvDone,tvNoItem;
	private ArrayList<CountriesDO> vecCountry;
	private int position = -1;
	private String countryNameToCompare = "";
	private int tempPosFromPassengerInformation=-1;
	
	@Override
	public void initilize() {
		// TODO Auto-generated method stub
		llSelectCurrency = (LinearLayout) layoutInflater.inflate(R.layout.select_currency, null);
		// lltop.setVisibility(View.VISIBLE);
		lltop.setVisibility(View.GONE);
		llMiddleBase.addView(llSelectCurrency, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		
		list = (ListView) llSelectCurrency.findViewById(R.id.list);
		etSearch = (EditText) llSelectCurrency.findViewById(R.id.etSearch);
		tvCancel = (TextView) llSelectCurrency.findViewById(R.id.tvCancel);
		tvNoItem = (TextView) llSelectCurrency.findViewById(R.id.tvNoItem);
		tvDone = (TextView) llSelectCurrency.findViewById(R.id.tvDone);
		tvSelectCurrencyTitle = (TextView) llSelectCurrency.findViewById(R.id.tvSelectCurrency);
		
		tvSelectCurrencyTitle.setText(getString(R.string.please_select_your_country));
		if (getIntent().hasExtra("vecCountry")) {
			vecCountry = (ArrayList<CountriesDO>) getIntent().getSerializableExtra("vecCountry");
		}
		if (getIntent().hasExtra("pos")) {
			tempPosFromPassengerInformation = getIntent().getExtras().getInt("pos");
		}
		if (getIntent().hasExtra("presentlySelectedCountry")) {
			countryNameToCompare = getIntent().getExtras().getString("presentlySelectedCountry");
		}
		
		setTypeFaceOpenSansLight(llSelectCurrency);
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		tvSelectCurrencyTitle.setTypeface(typefaceOpenSansSemiBold);
	}

	@Override
	public void bindingControl() {
		updateCurrency();
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String text = etSearch.getText().toString();
				countryAdapter.filter(text,getString(R.string.noCountry));
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
		tvDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if(!countryNameToCompare.isEmpty() && !countryNameToCompare.equalsIgnoreCase(""))
//					position = getCountryPositionFromString(countryNameToCompare);
				hideKeyBoard(etSearch);
				finish();
			}
		});
		
	}


	private void updateCurrency()
	{
//		AppConstants.allAirportsDO.vecAirport.remove(0);
		
			countryAdapter = new CountryAdapterContactsNew(SelectCountryContacsNew.this, vecCountry,countryNameToCompare, list, tvNoItem);
			list.setCacheColorHint(0);
			list.setAdapter(countryAdapter);
			list.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int pos,
						long arg3) {
					position = pos;
					countryNameToCompare = view.getTag(R.string.add).toString();
					if (!countryNameToCompare.isEmpty() && !countryNameToCompare.equalsIgnoreCase(""))
						countryAdapter.refresh(countryNameToCompare);
					
					if (position >= 0 
							&& countryAdapter != null 
							&& countryAdapter.getData() != null 
							&& position < countryAdapter.getData().size()) {
						Intent intent = new Intent();
						if(countryAdapter.getData().size()>position){
						intent.putExtra("Country_Selected",countryAdapter.getData().get(position));
						if(tempPosFromPassengerInformation != -1)
							intent.putExtra("pos",tempPosFromPassengerInformation);
					}
						setResult(RESULT_OK, intent);
						hideKeyBoard(etSearch);
						finish();
					}
				}
			});

	}
	
	private int getCountryPositionFromString(String countryName)
	{
		int result = -1;
		if (vecCountry!= null && vecCountry.size()>0) 
		{
			for (int i = 0; i < vecCountry.size(); i++) {
				if (vecCountry.get(i).name.equalsIgnoreCase(countryName)) {
					result = i;
					return result;
				}
			}
		}
		return result;
	}

}
