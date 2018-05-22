package com.winit.airarabia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.winit.airarabia.adapters.selectAirportDestAdapter;
import com.winit.airarabia.adapters.selectAirportSrcAdapter;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.objects.AirportsDO;
import com.winit.airarabia.objects.AirportsDestDO;
import com.winit.airarabia.utils.SharedPrefUtils;

public class SelectAirport_new extends BaseActivity{

	private LinearLayout llSelectCountry;
	private ExpandableListView expandableListViewAirport;
	private selectAirportSrcAdapter spinneradapnew;
	private selectAirportDestAdapter destAdapternew;
	private EditText etSearch;
	private AirportsDO airportsArrivalDo;
	private TextView tvCancel,tvSelectAirportTitle,tvNoItem;

	private HashMap<String, Vector<AirportsDO>> hashMapSrc= new HashMap<String, Vector<AirportsDO>>();
	private HashMap<String, Vector<AirportsDestDO>> hashMapDest= new HashMap<String, Vector<AirportsDestDO>>();
	private ArrayList<String> header= new ArrayList<String>();
	private Vector<AirportsDO> vecAirportcounrtry;
	private Vector<AirportsDO> vecAllAirportsSrc;

	private Vector<AirportsDestDO> vecAirportcounrtrydest;
	private Vector<AirportsDestDO> vecAllAirportsDest;

	//====================newly added for fav destination=================================================

	private ArrayList<String> arrListFavouriteAirports, arrListRecentAirportsSrc,arrListRecentAirportsDest;

	@Override
	public void initilize() {
		// TODO Auto-generated method stub
		llSelectCountry = (LinearLayout) layoutInflater.inflate(R.layout.select_airport_new, null);
		// lltop.setVisibility(View.VISIBLE);
		lltop.setVisibility(View.GONE);
		llMiddleBase.addView(llSelectCountry, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		if (getIntent().hasExtra("Arrival_Object")) {
			airportsArrivalDo= (AirportsDO) getIntent().getSerializableExtra("Arrival_Object");
		}
		else
			airportsArrivalDo = null;
		//		list=(ListView) llSelectCountry.findViewById(R.id.list_new);
		expandableListViewAirport = (ExpandableListView) llSelectCountry.findViewById(R.id.elv_list);
		expandableListViewAirport.setCacheColorHint(0);
		etSearch = (EditText) llSelectCountry.findViewById(R.id.etSearch);
		tvCancel = (TextView) llSelectCountry.findViewById(R.id.tvCancel);
		tvNoItem=(TextView) llSelectCountry.findViewById(R.id.tvNoItem);
		tvSelectAirportTitle = (TextView) llSelectCountry.findViewById(R.id.tvSelectAirportTitle);
		
		synchronized (LOCATION_SERVICE) {
    		refreshLocation();
		}
		
		if(airportsArrivalDo == null)
			tvSelectAirportTitle.setText(getString(R.string.select_u_origin));
		else
			tvSelectAirportTitle.setText(getString(R.string.select_u_destination));

		//================================newly added for fav destination=====================================================

		arrListFavouriteAirports=new ArrayList<String>();
		arrListRecentAirportsSrc	=new ArrayList<String>();
		mPrefs = this.getSharedPreferences(SharedPreferenceStrings.APP_PREFERENCES, Context.MODE_PRIVATE);

		String islogin = SharedPrefUtils.getKeyValue(SelectAirport_new.this,SharedPreferenceStrings.APP_PREFERENCES,SharedPreferenceStrings.isLoggedIn);

		if(islogin.equals("1"))
		if(arrListFavouriteAirports == null || arrListFavouriteAirports.size()<=0)
			arrListFavouriteAirports=SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.FAVLIST, SelectAirport_new.this);
		
		if(arrListRecentAirportsSrc == null || arrListRecentAirportsSrc.size()<=0)
			arrListRecentAirportsSrc=SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.RECENT_AIRPORTS_SRC, SelectAirport_new.this);
		
		if(arrListRecentAirportsDest == null || arrListRecentAirportsDest.size()<=0)
			arrListRecentAirportsDest=SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.RECENT_AIRPORTS_DEST, SelectAirport_new.this);
		
		setTypeFaceOpenSansLight(llSelectCountry);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		btnSubmitNext.setTypeface(typefaceOpenSansSemiBold);
		etSearch.setTypeface(typeFaceOpenSansLight);
		tvSelectAirportTitle.setTypeface(typefaceOpenSansSemiBold);
	}
	@Override
	public void bindingControl() {

		updateSpinner_new();
		//		updateSpinner();

		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String text = etSearch.getText().toString();
				if (airportsArrivalDo == null)
					spinneradapnew.filter(text);
				else
					destAdapternew.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyBoard(v);
				finish();
			}
		});

	}

	private void updateSpinner_new()
	{
		if(airportsArrivalDo == null)
		{
			vecAirportcounrtry = new Vector<AirportsDO>();
			vecAllAirportsSrc = new Vector<AirportsDO>();
			for(int i=0;i<AppConstants.allAirportsDO.vecAirport.size();i++)
			{
				if(!(arrListRecentAirportsSrc.size()>0))
				if(AppConstants.allAirportsDO.vecAirport.get(i).countryname.equalsIgnoreCase(AppConstants.country))
					vecAirportcounrtry.add(AppConstants.allAirportsDO.vecAirport.get(i));
				vecAllAirportsSrc.add(AppConstants.allAirportsDO.vecAirport.get(i));
			}

			getSrcData();

			spinneradapnew = new selectAirportSrcAdapter(SelectAirport_new.this, hashMapSrc,expandableListViewAirport,tvNoItem,header);
			expandableListViewAirport.setAdapter(spinneradapnew);

			expandableListViewAirport.setOnChildClickListener(new OnChildClickListener() {

				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
					Intent intent = new Intent();
					if(groupPosition==0)
						intent.putExtra("Departure_Airport",spinneradapnew.getcloseAirData().get(childPosition));
					else if(groupPosition==1)
						intent.putExtra("Departure_Airport",spinneradapnew.getRecentData().get(childPosition));
					else if(groupPosition==2)
						intent.putExtra("Departure_Airport",spinneradapnew.getAllData().get(childPosition));
					setResult(RESULT_OK, intent);   
					hideKeyBoard(expandableListViewAirport);
					finish();
					return false;
				}
			});
		}
		else
		{
			vecAirportcounrtrydest = new Vector<AirportsDestDO>();
			vecAllAirportsDest = new Vector<AirportsDestDO>();
			for(int i=0;i<airportsArrivalDo.destList.size();i++)
			{
				for (int j = 0; j < arrListFavouriteAirports.size(); j++) {
					if(airportsArrivalDo.destList.get(i).en.equalsIgnoreCase(arrListFavouriteAirports.get(j)))
						vecAirportcounrtrydest.add(airportsArrivalDo.destList.get(i));
				}

				if(airportsArrivalDo.destList.get(i).name.equalsIgnoreCase(""))
					airportsArrivalDo.destList.get(i).name = airportsArrivalDo.destList.get(i).en +" ("+ airportsArrivalDo.destList.get(i).code +")";
				
				vecAllAirportsDest.add(airportsArrivalDo.destList.get(i));
			}
			getDestdata();
			destAdapternew = new selectAirportDestAdapter(SelectAirport_new.this, hashMapDest,expandableListViewAirport,tvNoItem,header);
			expandableListViewAirport.setAdapter(destAdapternew);
			expandableListViewAirport.setOnChildClickListener(new OnChildClickListener() {

				public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id)
				{
					Intent intent = new Intent();
					if(groupPosition==0)
						intent.putExtra("Arrival_Airport",destAdapternew.getcloseAirData().get(childPosition));
					else if(groupPosition==1)
						intent.putExtra("Arrival_Airport",destAdapternew.getRecentData().get(childPosition));
					else if(groupPosition==2)
						intent.putExtra("Arrival_Airport",destAdapternew.getAllData().get(childPosition));					

					setResult(RESULT_OK, intent);  
					hideKeyBoard(v);
					finish();
					return false;

				}
			});
		}
	}	

	
	private void getDestdata() {

		header.add("Favourite Airport");
		header.add("Recent Airport");
		header.add("All Airport");

		//for Recent airport
		Vector<AirportsDestDO> vecRecentAirportDest= new Vector<AirportsDestDO>();
		
		for (int i = 0; i < vecAllAirportsDest.size(); i++) {
			for (int j = 0; j < arrListRecentAirportsDest.size(); j++) {
				if(vecAllAirportsDest.get(i).code.equalsIgnoreCase(arrListRecentAirportsDest.get(j)))
					vecRecentAirportDest.add(vecAllAirportsDest.get(i));
			}
		}

		hashMapDest.put("Favourite Airport", vecAirportcounrtrydest);
		hashMapDest.put("Recent Airport", vecRecentAirportDest);
		hashMapDest.put("All Airport", vecAllAirportsDest);

	}

	private void getSrcData() {

		header.add("Closest Airport");
		header.add("Recent Airport");
		header.add("All Airport");

		//for Recent airport
		
		Vector<AirportsDO> vecRecentAirports= new Vector<AirportsDO>();

		for (int i = 0; i < vecAllAirportsSrc.size(); i++) {
			for (int j = 0; j < arrListRecentAirportsSrc.size(); j++) {
				if(vecAllAirportsSrc.get(i).code.equalsIgnoreCase(arrListRecentAirportsSrc.get(j)))
					vecRecentAirports.add(vecAllAirportsSrc.get(i));
			}
		}
		hashMapSrc.put("Closest Airport", vecAirportcounrtry);
		hashMapSrc.put("Recent Airport", vecRecentAirports);
		hashMapSrc.put("All Airport", vecAllAirportsSrc);

	}


	//==========================newly added for destinatins=========================================

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		synchronized (DOWNLOAD_SERVICE) {
			showLoader("");
			
			if(arrListFavouriteAirports == null)
				arrListFavouriteAirports=SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.FAVLIST, SelectAirport_new.this);
			if(arrListRecentAirportsSrc == null)
				arrListRecentAirportsSrc=getRecentAirportsSrcFromPref();
			
			if(arrListRecentAirportsDest == null)
				arrListRecentAirportsDest=getRecentAirportsDestFromPref();
	
			hideLoader();
		}
				
	}
	
	private ArrayList<String> getRecentAirportsSrcFromPref()
	{
		return SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.RECENT_AIRPORTS_SRC, getApplicationContext());
	}
	private ArrayList<String> getRecentAirportsDestFromPref()
	{
		return SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.RECENT_AIRPORTS_DEST, getApplicationContext());
	}
	
	
}
