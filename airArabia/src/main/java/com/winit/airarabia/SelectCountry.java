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

import com.winit.airarabia.adapters.CountryAdapter;
import com.winit.airarabia.objects.CountryDO;

public class SelectCountry extends BaseActivity {

	private LinearLayout llSelectCurrency;
	private ListView list;
	private CountryAdapter countryAdapter;
	private EditText etSearch;
	private TextView tvSelectCurrencyTitle, tvCancel,tvNoItem, tv_label;
	private ArrayList<CountryDO> vecCountry;
	private int position = -1;
	private String countryNameToCompare = "";
	private int tempPosFromPassengerInformation=-1;
	private String headerTitle="";
	
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
		tvNoItem=(TextView) llSelectCurrency.findViewById(R.id.tvNoItem);
		tvSelectCurrencyTitle.setText(getString(R.string.please_select_your_nationality));
		tv_label=(TextView) llSelectCurrency.findViewById(R.id.tv_label);
		
		if (getIntent().hasExtra("vecCountry")) {
			vecCountry = (ArrayList<CountryDO>) getIntent().getSerializableExtra("vecCountry");
		}
		if(getIntent().hasExtra("SelectedCountry"))
			countryNameToCompare = getIntent().getStringExtra("SelectedCountry");
		
		if (getIntent().hasExtra("pos")) {
			tempPosFromPassengerInformation = getIntent().getExtras().getInt("pos");
		}
		
//		if(getIntent().hasExtra("HeaderTitle"))
//		{
//			headerTitle = getIntent().getStringExtra("HeaderTitle").toString();
//			tvSelectCurrencyTitle.setText(headerTitle);
//		}
		if (headerTitle.equalsIgnoreCase(getString(R.string.selectNationality))) {
//			tv_label.setVisibility(View.VISIBLE);
			tv_label.setText(getString(R.string.all_nationality));
		}
		else if (headerTitle.equalsIgnoreCase(getString(R.string.selectCountry))) {
//			tv_label.setVisibility(View.VISIBLE);
			tv_label.setText(getString(R.string.all_country));
		}else {
			tv_label.setVisibility(View.GONE);
		}
		
		setTypeFaceOpenSansLight(llSelectCurrency);
		
		tvSelectCurrencyTitle.setTypeface(typefaceOpenSansSemiBold);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		
		tvSelectCurrencyTitle.setText(getString(R.string.please_select_your_nationality));
		tvCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			finish();	
			}
		});
		
	}

	@Override
	public void bindingControl() {
		updateCurrency();
		
		//countryAdapter.setNoItemListener(this);
		
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String text = etSearch.getText().toString();
				countryAdapter.filter(text, headerTitle);
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
		
	}


	private void updateCurrency()
	{
//		AppConstants.allAirportsDO.vecAirport.remove(0);
		
			countryAdapter = new CountryAdapter(SelectCountry.this, vecCountry,list,tvNoItem, countryNameToCompare);
			list.setCacheColorHint(0);
			list.setAdapter(countryAdapter);
			list.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int pos,
						long arg3) {
					position = pos;
//					countryNameToCompare = view.getTag(R.string.add).toString();
//					if (!countryNameToCompare.isEmpty() && !countryNameToCompare.equalsIgnoreCase(""))
//						countryAdapter.refresh(countryNameToCompare);
					hideKeyBoard(llSelectCurrency);
					if (position >= 0 && countryAdapter != null 
							&& countryAdapter.getData() != null 
							&& position < countryAdapter.getData().size()) 
					{
						Intent intent = new Intent();
						if(position<countryAdapter.getData().size())
						intent.putExtra("Country_Selected",countryAdapter.getData().get(position));
						if(tempPosFromPassengerInformation != -1)
							intent.putExtra("pos",tempPosFromPassengerInformation);
						setResult(RESULT_OK, intent);        
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
				if (vecCountry.get(i).CountryName.equalsIgnoreCase(countryName)) {
					result = i;
					return result;
				}
			}
		}
		return result;
	}

//	@Override
//	public void noItem() {
//		// TODO Auto-generated method stub
//		list.setVisibility(View.GONE);
//		tvNoItem.setVisibility(View.VISIBLE);
//	}

}
