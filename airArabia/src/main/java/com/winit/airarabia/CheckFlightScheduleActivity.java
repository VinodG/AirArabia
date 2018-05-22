package com.winit.airarabia;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.adapters.FlightSheduleAdapter;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirScheduleDO;

public class CheckFlightScheduleActivity extends BaseActivity {

	private LinearLayout llCheckFlightSchedule, llcheckflightschedule_to,
	llcheckflightschedule_from, llHeaderlvflightschedule_to,
	llHeaderlvflightschedule_from;
	private TextView tvcheckflightschedule_toSourceFlight,
	tvcheckflightschedule_toDestFlight,
	tvcheckflightschedule_fromSourceFlight,
	tvcheckflightschedule_fromDestFlight, tvNoFlightFrom, tvNoFlightTo;
	private ListView lvflightschedule_to, lvflightschedule_from;
	private String FromLocation, ToLocation;
	private boolean roundtrip = false;
	private AirScheduleDO airScheduleDOto, airScheduleDOreturn;
	private CheckFlightScheduleActivity.BCR bcr;

	private class BCR extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(AppConstants.HOME_CLICK))
				finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(bcr);
	}

	@Override
	public void initilize() {
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		registerReceiver(bcr, intentFilter);
		tvHeaderTitle.setText(getString(R.string.FlightSchedule));
		Intent intent = getIntent();
		roundtrip = intent.getBooleanExtra(AppConstants.ROUND_TRIP, false);
		FromLocation = intent.getStringExtra(AppConstants.FROM_LOCATION);
		ToLocation = intent.getStringExtra(AppConstants.TO_LOCATION);
		if (intent.hasExtra(AppConstants.ONEWAY))
			airScheduleDOto = (AirScheduleDO) intent
			.getSerializableExtra("OneWay");
		if (intent.hasExtra(AppConstants.RETURN))
			airScheduleDOreturn = (AirScheduleDO) intent
			.getSerializableExtra("Return");
		llCheckFlightSchedule = (LinearLayout) layoutInflater.inflate(
				R.layout.flightschedule_check, null);
		llMiddleBase.addView(llCheckFlightSchedule, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		llcheckflightschedule_to = (LinearLayout) llCheckFlightSchedule
				.findViewById(R.id.llcheckflightschedule_to);
		llcheckflightschedule_from = (LinearLayout) llCheckFlightSchedule
				.findViewById(R.id.llcheckflightschedule_from);
		tvcheckflightschedule_toSourceFlight = (TextView) llCheckFlightSchedule
				.findViewById(R.id.tvcheckflightschedule_toSourceFlight);
		tvcheckflightschedule_toDestFlight = (TextView) llCheckFlightSchedule
				.findViewById(R.id.tvcheckflightschedule_toDestFlight);
		tvcheckflightschedule_fromSourceFlight = (TextView) llCheckFlightSchedule
				.findViewById(R.id.tvcheckflightschedule_fromSourceFlight);
		tvcheckflightschedule_fromDestFlight = (TextView) llCheckFlightSchedule
				.findViewById(R.id.tvcheckflightschedule_fromDestFlight);
		tvNoFlightTo = (TextView) llCheckFlightSchedule
				.findViewById(R.id.tvNoFlightTo);
		tvNoFlightFrom = (TextView) llCheckFlightSchedule
				.findViewById(R.id.tvNoFlightFrom);
		lvflightschedule_to = (ListView) llCheckFlightSchedule
				.findViewById(R.id.lvflightschedule_to);
		lvflightschedule_from = (ListView) llCheckFlightSchedule
				.findViewById(R.id.lvflightschedule_from);
		llHeaderlvflightschedule_to = (LinearLayout) llCheckFlightSchedule
				.findViewById(R.id.llHeaderlvflightschedule_to);
		llHeaderlvflightschedule_from = (LinearLayout) llCheckFlightSchedule
				.findViewById(R.id.llHeaderlvflightschedule_from);
		
		setTypefaceOpenSansRegular(llCheckFlightSchedule);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		btnSubmitNext.setTypeface(typefaceHelveticaLight);
		setListViewHeightBasedOnItems(lvflightschedule_from);
		setListViewHeightBasedOnItems(lvflightschedule_to);
		tvcheckflightschedule_toSourceFlight.setTypeface(typefaceOpenSansSemiBold);
		tvcheckflightschedule_toDestFlight.setTypeface(typefaceOpenSansSemiBold);
		tvcheckflightschedule_fromSourceFlight.setTypeface(typefaceOpenSansSemiBold);
		tvcheckflightschedule_fromDestFlight.setTypeface(typefaceOpenSansSemiBold);
	}

	@Override
	public void bindingControl() {
		if (!roundtrip) {
			llcheckflightschedule_to.setVisibility(View.VISIBLE);
			tvcheckflightschedule_toSourceFlight.setText(FromLocation);
			
			if(FromLocation.length() > 7) {
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tvcheckflightschedule_toSourceFlight.getLayoutParams();
				ll.width = 150 ;
				ll.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
				tvcheckflightschedule_toSourceFlight.setLayoutParams(ll);
			}
			tvcheckflightschedule_toDestFlight.setText(ToLocation);
			if (airScheduleDOto != null	&& airScheduleDOto.vecOriginDestinationOptionDOs.size() > 0) {
				llHeaderlvflightschedule_to.setVisibility(View.VISIBLE);
				lvflightschedule_to.setVisibility(View.VISIBLE);
				tvNoFlightTo.setVisibility(View.GONE);
				lvflightschedule_to.setAdapter(new FlightSheduleAdapter(this,
						airScheduleDOto.vecOriginDestinationOptionDOs));
			}
			else
			{
				tvNoFlightTo.setVisibility(View.VISIBLE);
			}
		} else {
			llcheckflightschedule_to.setVisibility(View.VISIBLE);
			llcheckflightschedule_from.setVisibility(View.VISIBLE);
			tvcheckflightschedule_toSourceFlight.setText(FromLocation);
			if(FromLocation.length() > 7) {
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tvcheckflightschedule_toSourceFlight.getLayoutParams();
				ll.width = 150 ;
				ll.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
				tvcheckflightschedule_toSourceFlight.setLayoutParams(ll);
			}
			tvcheckflightschedule_toDestFlight.setText(ToLocation);
			tvcheckflightschedule_fromSourceFlight.setText(ToLocation);
			
			if(ToLocation.length() > 7) {
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tvcheckflightschedule_fromSourceFlight.getLayoutParams();
				ll.width = 180 ;
				ll.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
				tvcheckflightschedule_fromSourceFlight.setLayoutParams(ll);
			}
			
			tvcheckflightschedule_fromDestFlight.setText(FromLocation);
			if (airScheduleDOto != null
					&& airScheduleDOto.vecOriginDestinationOptionDOs.size() > 0) {
				llHeaderlvflightschedule_to.setVisibility(View.VISIBLE);
				lvflightschedule_to.setVisibility(View.VISIBLE);
				tvNoFlightTo.setVisibility(View.GONE);
				lvflightschedule_to.setAdapter(new FlightSheduleAdapter(this,
						airScheduleDOto.vecOriginDestinationOptionDOs));
			}
			else
			{
				tvNoFlightTo.setVisibility(View.VISIBLE);
			}
			if (airScheduleDOreturn != null
					&& airScheduleDOreturn.vecOriginDestinationOptionDOs.size() > 0) {
				llHeaderlvflightschedule_from.setVisibility(View.VISIBLE);
				lvflightschedule_from.setVisibility(View.VISIBLE);
				tvNoFlightFrom.setVisibility(View.GONE);
				lvflightschedule_from.setAdapter(new FlightSheduleAdapter(this,
						airScheduleDOreturn.vecOriginDestinationOptionDOs));
			}
			else{
				tvNoFlightFrom.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public static boolean setListViewHeightBasedOnItems(ListView listView) {

	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter != null) {

	        int numberOfItems = listAdapter.getCount();

	        // Get total height of all items.
	        int totalItemsHeight = 0;
	        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
	            View item = listAdapter.getView(itemPos, null, listView);
	            item.measure(0, 0);
	            totalItemsHeight += item.getMeasuredHeight();
	        }

	        // Get total height of all item dividers.
	        int totalDividersHeight = listView.getDividerHeight() * 
	                (numberOfItems - 1);

	        // Set list height.
	        ViewGroup.LayoutParams params = listView.getLayoutParams();
	        params.height = totalItemsHeight + totalDividersHeight;
	        listView.setLayoutParams(params);
	        listView.requestLayout();

	        return true;

	    } else {
	        return false;
	    }

	}
}
