package com.winit.airarabia;

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

import com.winit.airarabia.adapters.CityAdapterOfficeNew;
import com.winit.airarabia.objects.OfficeLocationDO;

public class SelectOfficeLocationsCity extends BaseActivity{

	private LinearLayout llSelectCurrency;
	private ListView list;
	private CityAdapterOfficeNew countryAdapter;
	private EditText etSearch;
	private TextView tvCancel,tvSelectCurrencyTitle, tvDone, tvNoItem;
//	private ArrayList<CountriesDO> vecCountry;\
	
	private int position = -1;
	private String countryNameToCompare = "";
	private int tempPosFromPassengerInformation=-1;
	private OfficeLocationDO countryDO;
	
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
		
		tvSelectCurrencyTitle.setText(getString(R.string.select_country));
		if (getIntent().hasExtra("countryDO")) {
//			vecCountry = new ArrayList<CountriesDO>();
//			
//			vecCountry.add((CountriesDO) getIntent().getSerializableExtra("countryDO"));
			countryDO=new OfficeLocationDO();
			countryDO=(OfficeLocationDO) getIntent().getSerializableExtra("countryDO");
		}
		if (getIntent().hasExtra("pos")) {
			tempPosFromPassengerInformation = getIntent().getExtras().getInt("pos");
		}
		if (getIntent().hasExtra("presentlySelectedCity")) {
			countryNameToCompare = getIntent().getExtras().getString("presentlySelectedCity");
		}
		
		
	}

	@Override
	public void bindingControl() {
		updateCurrency();
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String text = etSearch.getText().toString();
				countryAdapter.filter(text, getString(R.string.noCity) );
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
				
				
				
				
//				Intent intent = new Intent();
//				if (position >= 0) {
//					if(countryAdapter.getData().size()>position)
//					intent.putExtra("Country_Selected",countryAdapter.getData().get(position));
//					if(tempPosFromPassengerInformation != -1)
//						intent.putExtra("pos",tempPosFromPassengerInformation);
//				}
//				setResult(RESULT_OK, intent);        
//				finish();
			}
		});
		
	}


	private void updateCurrency()
	{
//		AppConstants.allAirportsDO.vecAirport.remove(0);
if (countryDO.vecOfficeDO!=null && countryDO.vecOfficeDO.size()>0) {
			
		
			countryAdapter = new CityAdapterOfficeNew(SelectOfficeLocationsCity.this, countryDO.vecOfficeDO, countryNameToCompare,list, tvNoItem);
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
					
					Intent intent = new Intent();
					if (position >= 0) {
						if(countryAdapter.getData().size()>position)
						intent.putExtra("Country_Selected",countryAdapter.getData().get(position));
						if(tempPosFromPassengerInformation != -1)
							intent.putExtra("pos",tempPosFromPassengerInformation);
					}
					setResult(RESULT_OK, intent);        
					finish();
				}
			});
       }else{
    	   list.setVisibility(View.GONE);
    	   tvNoItem.setVisibility(View.VISIBLE);
    	   tvNoItem.setText(getString(R.string.noCity));
    	   
        }
	}

}
