package com.winit.airarabia;

import java.util.Calendar;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.insider.android.insider.Insider;
import com.winit.airarabia.adapters.FlightOneWayAdapter;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.objects.AirAvailabilityDO;
import com.winit.airarabia.objects.OriginDestinationInformationDO;
import com.winit.airarabia.objects.PricedItineraryDO;
import com.winit.airarabia.utils.AnalyticsApplication;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.AnalyticsApplication.TrackerName;
import com.winit.airarabia.utils.SharedPrefUtils;

public class SelectFlightOneWayFragment extends Fragment {

    private ListView listView;
    private AirAvailabilityDO airAvailabilityDO;
    private FlightOneWayAdapter adapter;
    private Vector<PricedItineraryDO> vecForPriceShowing = null;
    private int flightPos = -1;
    private Tracker tracker;
    private String selectedCal;

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //Insider.Instance.start(getActivity());
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        //Insider.Instance.stop();
    }

    public void DateSelectionOneWayFlight(AirAvailabilityDO airAvailabilityDO, int flightPos) {
        this.airAvailabilityDO = airAvailabilityDO;
        this.flightPos = flightPos;
        if (adapter != null)
            adapter.refresh(airAvailabilityDO.vecOriginDestinationInformationDOs, flightPos);
        else
            adapter = new FlightOneWayAdapter(getContext(), airAvailabilityDO.vecOriginDestinationInformationDOs, flightPos);
    }

    public void DateSelectionOneWayFlight(Vector<PricedItineraryDO> vecForPriceShowing) {
        this.vecForPriceShowing = new Vector<PricedItineraryDO>();
        this.vecForPriceShowing = (Vector<PricedItineraryDO>) vecForPriceShowing.clone();

        if (adapter != null)
            adapter.refresh(airAvailabilityDO.vecOriginDestinationInformationDOs, this.flightPos, this.vecForPriceShowing);
        else
            adapter = new FlightOneWayAdapter(getContext(), airAvailabilityDO.vecOriginDestinationInformationDOs, this.flightPos);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        //tracker=((AnalyticsApplication)getActivity().getApplication()).getDefaultTracker();
        tracker = ((AnalyticsApplication) getActivity().getApplication()).getTracker(TrackerName.GLOBAL_TRACKER);

        container = (ViewGroup) inflater.inflate(R.layout.listview, null);
        listView = (ListView) container.findViewById(R.id.listView);
//		listView.setDividerHeight(-15);
        listView.setCacheColorHint(0);
        if (adapter != null)
            adapter.refresh(airAvailabilityDO.vecOriginDestinationInformationDOs, 0);
        else
            adapter = new FlightOneWayAdapter(getContext(), new Vector<OriginDestinationInformationDO>(), 0);
        listView.setAdapter(adapter);

        //Added by Mukesh on 07March2018 to remove default selection on the time of loa
        // ding the Flight selection page
        FlightOneWayAdapter.isFirstTimeSelected = false;

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View viewItem, int pos,
                                    long arg3) {

                ((SelectFlightActivityNew) getActivity()).flightCountOneWay = pos;
                adapter.refreshPos(pos);

                String selectedLanguageCode = SharedPrefUtils.getKeyValue(
                getActivity(),
                        SharedPreferenceStrings.APP_PREFERENCES,
                        SharedPreferenceStrings.USER_LANGUAGE);
                if(selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN))
                    ((SelectFlightActivityNew)getActivity()).showUmrahMessage(airAvailabilityDO.vecOriginDestinationInformationDOs.get(pos).vecOriginDestinationOptionDOs.get(0),AppConstants.ONE_WAY);

                Insider.tagEvent("Register_button_clicked", listView.getContext());
                //Insider.Instance.tagEvent((Activity)listView.getContext(),"Register_button_clicked");
                //Insider.tagEvent("Register_button_clicked", listView.getContext());
                tracker.send(new HitBuilders.EventBuilder().setCategory("SelectFlightOneWay").setAction("SelectFlightBox_button_clicked").build());

                selectedCal = airAvailabilityDO.vecOriginDestinationInformationDOs.get(pos).departureDateTime;
                AppConstants.CAL_VALUE = true;
                if (AppConstants.ISMANAGE_BOOK && AppConstants.DATE.equalsIgnoreCase(selectedCal)) {
                    AppConstants.CAL_VALUE = false;
                } else {

                    if (AppConstants.bookingFlightDO.myODIDOReturn != null) {
                        ((SelectFlightActivityNew) getActivity())
                                .callServicePriceReturn(
                                        ((SelectFlightActivityNew) getActivity()).flightCountOneWay,
                                        ((SelectFlightActivityNew) getActivity()).flightCountReturn, true);
                    } else {
                        //					SelectFlightActivityNew.currentFlightCountPrice = 0;
                        //					((SelectFlightActivityNew)getActivity())
                        //					.callServicePriceOneWay(
                        //							((SelectFlightActivityNew)getActivity()).flightCountOneWay,true);
                        ((SelectFlightActivityNew) getActivity())
                                .updateTotalPriceForOneWay(vecForPriceShowing.get(pos).vecPTC_FareBreakdownDOs.get(0).totalFare.amount + "", pos);
                    }

                }
            }
        });
        return container;
    }
}