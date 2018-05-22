package com.winit.airarabia;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.AllAirportsMainDO;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.webaccess.Response;

public class FavouriteListActivity extends BaseActivity implements DataListener{
	private LinearLayout llFavourite;
	private ListView lvFav;
	private EditText etSearch;
	private TextView tvCancel, tvDone,tvNoItem, tvSelectCurrency;
	private ArrayList<String> favDestination, selectedList;
	private FavouritemAdapter favAdapter;
	private AllAirportsDO allAirportsDo;
	private AllAirportNamesDO allAirportsNamesDO;
	private AllAirportsMainDO allAirportsMainDO;
	private SharedPreferences  mPrefs ;
	private String Email_LoggedInPrevious = "";

	@Override
	public void initilize() {
		// TODO Auto-generated method stub
		llFavourite = (LinearLayout) layoutInflater.inflate(
				R.layout.activity_favourite_list, null);
		lltop.setVisibility(View.VISIBLE);
		llMiddleBase.addView(llFavourite, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		lvFav = (ListView) llFavourite.findViewById(R.id.lvFav);
		tvCancel = (TextView) llFavourite.findViewById(R.id.tvCancel);
		
		tvNoItem=(TextView) llFavourite.findViewById(R.id.tvNoItem);
		tvSelectCurrency=(TextView) llFavourite.findViewById(R.id.tvSelectCurrency);
		tvDone = (TextView) llFavourite.findViewById(R.id.tvDone);
		etSearch = (EditText) llFavourite.findViewById(R.id.etSearch);
		favDestination = new ArrayList<String>();
		selectedList = new ArrayList<String>();
		mPrefs = this.getSharedPreferences(SharedPreferenceStrings.APP_PREFERENCES, Context.MODE_PRIVATE);
		Email_LoggedInPrevious = mPrefs.getString("Email_LoggedInPrevious", "");

		lltop.setVisibility(View.GONE);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		btnSubmitNext.setTypeface(typefaceOpenSansSemiBold);
		tvSelectCurrency.setTypeface(typefaceOpenSansSemiBold);
		etSearch.setTypeface(typefaceOpenSansRegular);
///============================ From here calling All Airport Service =========================/////
		
		callFlightListService();
///======================================================================================////
//		if (AppConstants.currentUserEmail.toString().equalsIgnoreCase(Email_LoggedInPrevious))
			selectedList = SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.FAVLIST, FavouriteListActivity.this);
		favAdapter = new FavouritemAdapter(favDestination);
		lvFav.setAdapter(favAdapter);

		lockDrawer("Favourite Destinations");
		bindingControl();

	}

	private void updateAllAirports() {
		
	////==================== Here Updating All Airports in one ArrayList ===================================///	
		if (allAirportsMainDO.allAirportNamesDO.vecAirport != null) {
			for (int i = 0; i < allAirportsMainDO.allAirportNamesDO.vecAirport.size(); i++) {
				favDestination.add(allAirportsMainDO.allAirportNamesDO.vecAirport.get(i).en);
			}
			favAdapter.refreshListView(favDestination);
			
		}
		
	}

	@Override
	public void bindingControl() {
		tvDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyBoard(v);
				
				if (selectedList!= null && selectedList.size() >0 && (View.VISIBLE!=tvNoItem.getVisibility())) {
					SharedPrefUtils.saveArrayInPreference(selectedList,
							SharedPreferenceStrings.FAVLIST,
							FavouriteListActivity.this);
					finish();
				}else{
					showCustomDialog(FavouriteListActivity.this, getString(R.string.Alert), getString(R.string.Please_select_destination), getString(R.string.Ok), "", "FavouriteListActivity");
				}
			}
		});

		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				int j = 0;
				if (etSearch != null) {
					ArrayList<String> tempList = new ArrayList<String>();
					String temp = etSearch.getText().toString();
					int templength = temp.length();
					if (temp != null && !temp.equalsIgnoreCase("")) {
						for (String check : favDestination) {
							if (check.regionMatches(true, 0, temp, 0,
									templength)) {

								tempList.add(check);
							}
						}
						for (int i = 0; i < favDestination.size(); i++) {

						}
						if (temp != null && tempList.size() > 0) {
							lvFav.setVisibility(View.VISIBLE);
							tvNoItem.setVisibility(View.GONE);
							favAdapter.refreshListView(tempList);
							
						}else{
							
							lvFav.setVisibility(View.GONE);
							tvNoItem.setVisibility(View.VISIBLE);
							tvNoItem.setText(getString(R.string.NoDestination));
							favAdapter.refreshListView(favDestination);
							
						}
					} else {
						lvFav.setVisibility(View.VISIBLE);
						tvNoItem.setVisibility(View.GONE);
						favAdapter.refreshListView(favDestination);
					}

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	class FavouritemAdapter extends BaseAdapter {
		ArrayList<String> favDestination;

		public FavouritemAdapter(ArrayList<String> favDestination) {
			// TODO Auto-generated constructor stub
			this.favDestination = favDestination;
		}

		public void refreshListView(ArrayList<String> tempList) {
			// TODO Auto-generated method stub
			this.favDestination = tempList;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (favDestination != null && favDestination.size() > 0) {
				return favDestination.size();
			} else
				return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return favDestination.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			ViewHolderItem viewHolder;
			if (convertView == null) {


				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.favourite_item, null);


				viewHolder = new ViewHolderItem();
				viewHolder.tvFavItem = (TextView) convertView
						.findViewById(R.id.tvFavItem);
				viewHolder.ivFavImage = (ImageView) convertView
						.findViewById(R.id.ivFavItem);

				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolderItem) convertView.getTag();
			}
			viewHolder.tvFavItem.setText(favDestination.get(pos) + "");
			viewHolder.tvFavItem.setTypeface(typefaceOpenSansRegular);

			if (selectedList.contains(favDestination.get(pos) + "")) {
				viewHolder.ivFavImage
						.setImageResource(R.drawable.img_redcircleminus);
			} else {
				viewHolder.ivFavImage
						.setImageResource(R.drawable.plus_sign);
			}
			
			
			
			
			

			lvFav.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(android.widget.AdapterView<?> arg0,
						View view, int position, long arg3) {
						
						if (selectedList.contains(favDestination.get(position))) {
							selectedList.remove(favDestination.get(position));
							((ImageView) view.findViewById(R.id.ivFavItem)).setImageResource(R.drawable.plus_sign);
						}
						else
						{
							selectedList.add(favDestination.get(position));
							((ImageView) view.findViewById(R.id.ivFavItem)).setImageResource(R.drawable.img_redcircleminus);
						}

//						

				}

			});
			return convertView;
		}
	}

	class ViewHolderItem {
		TextView tvFavItem;
		ImageView ivFavImage;
	}
	
	@Override
	public void dataRetreived(Response data) {
		
		if (data != null && !data.isError) {
			switch (data.method) {
			case AIR_PORT_SDATA:
				allAirportsDo = new AllAirportsDO();
				allAirportsNamesDO = new AllAirportNamesDO();
				allAirportsDo = new AllAirportsDO();
				allAirportsNamesDO = new AllAirportNamesDO();

				allAirportsMainDO = new AllAirportsMainDO();
				allAirportsMainDO = (AllAirportsMainDO) data.data;
				allAirportsDo = allAirportsMainDO.airportParserDO;
				allAirportsNamesDO = allAirportsMainDO.allAirportNamesDO;
				updateAllAirports();
				hideLoader();
				break;
				
			default:
				break;
			}
		} else {
			if (data.data instanceof String) {
				if (((String) data.data)
						.equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
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
	
	private void callFlightListService() {
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
}
