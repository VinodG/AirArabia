package com.winit.airarabia;

import java.util.Vector;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.winit.airarabia.adapters.FlightReturnAdapter;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.objects.AirAvailabilityDO;
import com.winit.airarabia.objects.OriginDestinationInformationDO;
import com.winit.airarabia.utils.AnalyticsApplication;
import com.winit.airarabia.utils.AnalyticsApplication.TrackerName;
import com.winit.airarabia.utils.SharedPrefUtils;

public class SelectFlightReturnFragment extends Fragment implements OnClickListener {
	
	private ListView listView;
	private AirAvailabilityDO airAvailabilityDO;
	private FlightReturnAdapter adapter;
	private Tracker tracker;
	public void DateSelectionReturnFlight(AirAvailabilityDO airAvailabilityDO, int flightPos)
	{
		if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null) {
			this.airAvailabilityDO = airAvailabilityDO;
			if(adapter != null)
				adapter.refresh(airAvailabilityDO.vecOriginDestinationInformationDOs,flightPos);
			else
				adapter = new FlightReturnAdapter(getContext(), airAvailabilityDO.vecOriginDestinationInformationDOs,flightPos);
		}
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		// tracker=((AnalyticsApplication)getActivity().getApplication()).getDefaultTracker();
		tracker = ((AnalyticsApplication) getActivity().getApplication()).getTracker(TrackerName.GLOBAL_TRACKER);
		container = (ViewGroup) inflater.inflate(R.layout.listview, null);
		listView				= (ListView) container.findViewById(R.id.listView);
				
//		listView.setDividerHeight(-15);
		listView.setCacheColorHint(0);
		if(adapter != null)
			adapter.refresh(airAvailabilityDO.vecOriginDestinationInformationDOs,0);
		else
			adapter = new FlightReturnAdapter(getContext(), new Vector<OriginDestinationInformationDO>(),0);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View viewItem, int pos,
					long arg3) {
				tracker.send(new HitBuilders.EventBuilder().setCategory("SelectFlightReturn").setAction("SelectFlightBox_button_clicked").build());
				((SelectFlightActivityNew)getActivity()).flightCountReturn = pos;
				
				adapter.refreshPos(pos);
				String selectedLanguageCode = SharedPrefUtils.getKeyValue(
						getActivity(),
						SharedPreferenceStrings.APP_PREFERENCES,
						SharedPreferenceStrings.USER_LANGUAGE);
				if(selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN))
					((SelectFlightActivityNew)getActivity()).showUmrahMessage(airAvailabilityDO.vecOriginDestinationInformationDOs.get(pos).vecOriginDestinationOptionDOs.get(0),AppConstants.RETURN_WAY);
				((SelectFlightActivityNew)getActivity())
				.callServicePriceReturn(
						((SelectFlightActivityNew)getActivity()).flightCountOneWay, 
						((SelectFlightActivityNew)getActivity()).flightCountReturn,true);
			}
		});
		
		return container ;//super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onClick(View arg0) {

	}
	
}