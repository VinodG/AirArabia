package com.winit.airarabia;

import java.util.ArrayList;
import java.util.Collection;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.winit.airarabia.adapters.CurrencyAdapter;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirportsDO;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.AllAirportsMainDO;
import com.winit.airarabia.objects.CurrencyDo;
import com.winit.airarabia.utils.QuickSortCurrencyDo;
import com.winit.airarabia.webaccess.Response;

public class SelectCurrency extends BaseActivity implements DataListener{

	private LinearLayout llSelectCurrency;
	private ListView lv;
	private CurrencyAdapter currencyAdapter;
	private EditText etSearch;
	private AirportsDO airportsArrivalDo;
	private TextView tvCancel,tvSelectCurrencyTitle, tvNoItem;
	private ArrayList<CurrencyDo> arrListCurrency;
	private int prevPosition =-1,position = -1;
	private String currencyNameToCompare = "";
	@Override
	public void initilize() {
		// TODO Auto-generated method stub
		llSelectCurrency = (LinearLayout) layoutInflater.inflate(R.layout.select_currency_new, null);
		// lltop.setVisibility(View.VISIBLE);
		lltop.setVisibility(View.GONE);
		llMiddleBase.addView(llSelectCurrency, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		lv = (ListView) llSelectCurrency.findViewById(R.id.list);
		etSearch = (EditText) llSelectCurrency.findViewById(R.id.etSearch);
		tvCancel = (TextView) llSelectCurrency.findViewById(R.id.tvCancel);
		tvSelectCurrencyTitle = (TextView) llSelectCurrency.findViewById(R.id.tvSelectCurrency);
		tvNoItem=(TextView) llSelectCurrency.findViewById(R.id.tvNoItem);
		if(getIntent().hasExtra("selected_currency"))
			currencyNameToCompare = getIntent().getStringExtra("selected_currency");
		
		setTypeFaceOpenSansLight(llSelectCurrency);
		tvSelectCurrencyTitle.setTypeface(typefaceOpenSansSemiBold);
		btnSubmitNext.setTypeface(typefaceOpenSansSemiBold);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
	}

	@Override
	public void bindingControl() {
		
		arrListCurrency = new ArrayList<CurrencyDo>();
		
		if(AppConstants.arrListCurrencies == null)
			callCurrencyService();
		else
		{
			arrListCurrency	= (ArrayList<CurrencyDo>) AppConstants.arrListCurrencies.clone();
		}
		
		currencyAdapter = new CurrencyAdapter(SelectCurrency.this, arrListCurrency,lv,tvNoItem, currencyNameToCompare);
		lv.setAdapter(currencyAdapter);
		lv.setCacheColorHint(0);
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {
				position = pos;
				trackEvent("Currency Screen",AppConstants.SelectCurrency,"");
//				currencyNameToCompare = view.getTag(R.string.add).toString();
//				if (!currencyNameToCompare.isEmpty() && !currencyNameToCompare.equalsIgnoreCase(""))
//					currencyAdapter.refresh(currencyNameToCompare);
				hideKeyBoard(llSelectCurrency);
				Intent intent = new Intent();
				if (position >= 0 && currencyAdapter != null && currencyAdapter.getData() != null 
						&& position < currencyAdapter.getData().size()) 
				{
					if(currencyAdapter.getData().size()>0)
						intent.putExtra("Currency_Selected",currencyAdapter.getData().get(position).code);
//				else if(!currencyNameToCompare.isEmpty() && !currencyNameToCompare.toString().equalsIgnoreCase(""))
//					intent.putExtra("Currency_Selected",currencyNameToCompare);
				setResult(RESULT_OK, intent);        
				finish();
				}
			}
		});
//		list.setOnItemClickListener(new OnItemClickListener() {
//			
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
//					long arg3) {
//				
//				if (position >= 0) {
//					prevPosition = position;
//					arg0.getChildAt(prevPosition).findViewById(R.id.ivCurrencyChecked).setVisibility(View.INVISIBLE);
//				}
//				else {
//					prevPosition = pos;
//				}
//				position = pos;
//				
//				arg1.findViewById(R.id.ivCurrencyChecked).setVisibility(View.VISIBLE);
//			}
//		});
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				trackEvent("Currency Screen",AppConstants.SearchFieldCurrency,"");
				String text = etSearch.getText().toString();
//				currencyAdapter.filter(text);
				synchronized (MY_LOCK ) 
				{
					
						ArrayList<CurrencyDo> arrListCurrencyt = getSearchItems(text);
						currencyAdapter.refresh(arrListCurrencyt);
						if(arrListCurrencyt!=null && arrListCurrencyt.size()>0)
						{
							lv.setVisibility(View.VISIBLE);
							tvNoItem.setVisibility(View.GONE);
						}
						else
						{
							lv.setVisibility(View.GONE);
							tvNoItem.setVisibility(View.VISIBLE);
							tvNoItem.setText(getString(R.string.noCurrency));
						}
				}
				
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
	public String MY_LOCK = "MY_LOCK";
	 @SuppressWarnings("unchecked")
	public ArrayList<CurrencyDo> getSearchItems(final String searchText) {
		    Predicate<CurrencyDo> searchItem =null;
		    if(!TextUtils.isEmpty(searchText))
		    {
		     searchItem = new Predicate<CurrencyDo>() 
		       {
			     public boolean apply(CurrencyDo currencyDo) 
			     {
			      return (currencyDo.code.toLowerCase().contains(searchText.toLowerCase()));
			     }
			    };
		    
		    }
		    else
		    {
		    	 searchItem = new Predicate<CurrencyDo>() 
  		       {
  			     public boolean apply(CurrencyDo currencyDo) 
  			     {
  			      return true;
  			     }
  			    };
		    }
		    Collection<CurrencyDo> filteredResult;
		     ArrayList<CurrencyDo> temp = (ArrayList<CurrencyDo>) arrListCurrency.clone();
		     filteredResult = filter(temp, searchItem);
		    if(filteredResult!=null)
		     return ((ArrayList<CurrencyDo>) filteredResult);
		    else return new ArrayList<CurrencyDo>();
		   }
	
	public static <T> Collection<T> filter(Collection<T> col, Predicate<T> predicate) {

		  Collection<T> result = new ArrayList<T>();
		  if(col!=null)
		  {
		   for (T element : col) {
		    if (predicate.apply(element)) {
		     result.add(element);
		    }
		   }
		  }
		  return result;
		 }
	private void updateCurrency()
	{
		AppConstants.allAirportsDO.vecAirport.remove(0);
			currencyAdapter = new CurrencyAdapter(SelectCurrency.this, arrListCurrency,lv,tvNoItem, currencyNameToCompare);
			lv.setAdapter(currencyAdapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int pos,
						long arg3) {
					position = pos;
					currencyNameToCompare = view.getTag(R.string.add).toString();
					if (!currencyNameToCompare.isEmpty() && !currencyNameToCompare.equalsIgnoreCase(""))
						currencyAdapter.refresh(currencyNameToCompare);
				}
			});

	}
	
	private void callCurrencyService() {
		showLoader("");
		if (new CommonBL(this, this).getAirportsData()) {
		} else {
			hideLoader();
			showCustomDialog(getApplicationContext(),
					getString(R.string.Alert),
					getString(R.string.InternetProblem),
					getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
		}
	}

	@Override
	public void dataRetreived(Response data) {
		if (data != null && !data.isError) {
			switch (data.method) {
			case AIR_PORT_SDATA:

				AppConstants.allAirportsDO = new AllAirportsDO();
				AppConstants.allAirportNamesDO = new AllAirportNamesDO();

				AllAirportsMainDO allAirportsMainDO = new AllAirportsMainDO();
				allAirportsMainDO = (AllAirportsMainDO) data.data;
				AppConstants.allAirportsDO = allAirportsMainDO.airportParserDO;
				AppConstants.allAirportNamesDO = allAirportsMainDO.allAirportNamesDO;
				arrListCurrency	= (ArrayList<CurrencyDo>) allAirportsMainDO.arlCurrencies.clone();
//				arrListCurrency.addAll(arrListCurrency);
				new QuickSortCurrencyDo().sort(arrListCurrency);
				currencyAdapter.refresh(arrListCurrency);
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

}
