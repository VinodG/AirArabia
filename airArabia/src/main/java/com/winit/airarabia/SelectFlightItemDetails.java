package com.winit.airarabia;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.insider.android.insider.Insider;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.insider.GcmBroadcastReceiver;
import com.winit.airarabia.objects.AirPriceQuoteDO;
import com.winit.airarabia.objects.BundledServiceDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class SelectFlightItemDetails extends BaseActivity implements DataListener {

    private LinearLayout llSelectFlightDetails;
    private Button btn_back_SelectFlight;
    private TextView tvSourceFlight1, tvDestFlight1, tvHeadertitle_SelectFlight, tv_selectfare, tv_lable, tv_star;
    private ImageView ivHeaderSelectFlight, ivmenu_SelectFlight;

    private LinearLayout llFareDetailsMain;

    private ListView lvFares;
    // private LinearLayout llFaresList;

    private LinkedHashMap<String, BundledServiceDO> lhmFareValues, tempLhmFareValues;

    private final String DATAFAIL = "DATAFAIL";

    private FareAdapter fareAdapter;

    private int position = -1;

    private SelectFlightItemDetails.BCR bcr;

    private class BCR extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AppConstants.HOME_CLICK))
                finish();
            if (intent.getAction().equalsIgnoreCase(AppConstants.BOOK_SUCCESS))
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

        lltop.setVisibility(View.GONE);

        initClassMembers();

        Insider insider = new Insider();
        insider.openSession(this, AppConstants.ProjectName);
        insider.registerInBackground(this, AppConstants.GoogleProjectNumber);
        Insider.setLandingActivity(GcmBroadcastReceiver.class, this);
        Insider.setNotificationIcon(R.drawable.ic_launcher, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Insider.start(this, intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Insider.stop(this);
    }

    private void initClassMembers() {
        bcr = new BCR();
        intentFilter.addAction(AppConstants.HOME_CLICK);
        intentFilter.addAction(AppConstants.BOOK_SUCCESS);
        registerReceiver(bcr, intentFilter);

        btnSubmitNext.setVisibility(View.VISIBLE);
        btnSubmitNext.setText(getString(R.string.Continue));

        llSelectFlightDetails = (LinearLayout) layoutInflater.inflate(R.layout.select_flight_list_item_details, null);

        llMiddleBase.addView(llSelectFlightDetails, ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);

        btn_back_SelectFlight = (Button) llSelectFlightDetails.findViewById(R.id.btn_back_SelectFlight);
        ivmenu_SelectFlight = (ImageView) llSelectFlightDetails.findViewById(R.id.ivmenu_SelectFlight);

        ivHeaderSelectFlight = (ImageView) llSelectFlightDetails.findViewById(R.id.ivHeaderSelectFlight);

        tvSourceFlight1 = (TextView) llSelectFlightDetails.findViewById(R.id.tvSourceFlight1);
        tv_selectfare = (TextView) llSelectFlightDetails.findViewById(R.id.tv_selectfare);
        tvDestFlight1 = (TextView) llSelectFlightDetails.findViewById(R.id.tvDestFlight1);
        tvHeadertitle_SelectFlight = (TextView) llSelectFlightDetails.findViewById(R.id.tvHeadertitle_SelectFlight);
        tv_lable = (TextView) llSelectFlightDetails.findViewById(R.id.tv_lable);
        tv_star = (TextView) llSelectFlightDetails.findViewById(R.id.tv_star);

        llFareDetailsMain = (LinearLayout) llSelectFlightDetails.findViewById(R.id.llFareDetailsMain);

        lvFares = (ListView) llSelectFlightDetails.findViewById(R.id.lvFares);
        // llFaresList = (LinearLayout)
        // llSelectFlightDetails.findViewById(R.id.llFaresList);
        btn_back_SelectFlight.setVisibility(View.VISIBLE);
        setTypeFaceOpenSansLight(llSelectFlightDetails);
        btn_back_SelectFlight.setTypeface(typefaceOpenSansSemiBold);
        tvHeadertitle_SelectFlight.setTypeface(typefaceOpenSansSemiBold);
        tvSourceFlight1.setTypeface(typefaceOpenSansSemiBold);
        tvDestFlight1.setTypeface(typefaceOpenSansSemiBold);
        tv_selectfare.setTypeface(typefaceOpenSansSemiBold);
        btnSubmitNext.setTypeface(typefaceOpenSansSemiBold);
        tv_lable.setTypeface(typefaceOpenSansRegular);
        tv_star.setTypeface(typefaceOpenSansRegular);

    }

    @Override
    public void bindingControl() {

        lhmFareValues = new LinkedHashMap<String, BundledServiceDO>();
        tempLhmFareValues = new LinkedHashMap<String, BundledServiceDO>();

        loadData();

        lvFares.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
                showFareDetails(pos);
                position = pos;
                fareAdapter.notifyDataSetChanged();
            }
        });
        ivmenu_SelectFlight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ivmenu.performClick();
            }
        });
        btn_back_SelectFlight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Insider.tagEvent(AppConstants.BackButtonDetailView, SelectFlightItemDetails.this);
                //Insider.Instance.tagEvent(SelectFlightItemDetails.this,AppConstants.BackButtonDetailView);
                trackEvent("Select Fare", AppConstants.BackButtonDetailView, "");
                btnBack.performClick();
            }
        });
        btnSubmitNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Insider.tagEvent(AppConstants.ContinueDetailView, SelectFlightItemDetails.this);
                //Insider.Instance.tagEvent(SelectFlightItemDetails.this,AppConstants.ContinueDetailView);
                trackEvent("Select Fare", AppConstants.ContinueDetailView, "");
                if (llFareDetailsMain.getChildCount() > 0)
                    movetoFlightDetails();
                else
                    showCustomDialog(SelectFlightItemDetails.this, getString(R.string.Alert),
                            getString(R.string.please_Select_Fare_error), getString(R.string.Ok), "", "");
            }
        });
    }

    private void movetoFlightDetails() {
        Intent intenNext = new Intent(SelectFlightItemDetails.this, TotalPriceActivity.class);
        startActivity(intenNext);
    }

    private void loadData() {
        if(AppConstants.bookingFlightDO != null) {
            if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
                ivHeaderSelectFlight.setBackgroundResource(R.drawable.flight_return_logo);
            else
                ivHeaderSelectFlight.setBackgroundResource(R.drawable.flight_oneside);
        }

        tvSourceFlight1.setText(AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode);
        tvDestFlight1.setText(AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode);

        String strPersons = "";
        if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty > 0) {

            if (AppConstants.bookingFlightDO.myBookFlightDO.adtQty < 2)
                strPersons = AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " "
                        + getString(R.string.adult_flight_fareselection);
            else
                strPersons = AppConstants.bookingFlightDO.myBookFlightDO.adtQty + " "
                        + getString(R.string.adult_flight_fareselection);
        }
        if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty > 0) {

            if (AppConstants.bookingFlightDO.myBookFlightDO.chdQty < 2)
                strPersons = strPersons + ", " + AppConstants.bookingFlightDO.myBookFlightDO.chdQty + " "
                        + getString(R.string.Child);
            else
                strPersons = strPersons + ", " + AppConstants.bookingFlightDO.myBookFlightDO.chdQty + " "
                        + getString(R.string.Children);
        }
        if (AppConstants.bookingFlightDO.myBookFlightDO.infQty > 0) {

            if (AppConstants.bookingFlightDO.myBookFlightDO.infQty < 2)
                strPersons = strPersons + ", " + AppConstants.bookingFlightDO.myBookFlightDO.infQty + " "
                        + getString(R.string.Infant);
            else
                strPersons = strPersons + ", " + AppConstants.bookingFlightDO.myBookFlightDO.infQty + " "
                        + getString(R.string.Infantss);
        }

        tvHeadertitle_SelectFlight.setText(strPersons);

        if (AppConstants.bookingFlightDO.myODIDOReturn != null)
            callServicePriceReturn();
        else
            callServicePriceOneWay();
    }

    private Set<String> strKeys;
    private ArrayList<String> arlKeys, arlKeys1;

//=============Old updateFare==============================

//    private void updateFares() {
//        float farePromo = 0, fareFlexi = 0;
//
//        PTC_FareBreakdownDO mPTC_FareBreakdownDOPromoReturn;
//        PTC_FareBreakdownDO mPTC_FareBreakdownDOFlexiReturn;
//        if (AppConstants.bookingFlightDO.myODIDOReturn != null) {
//            mPTC_FareBreakdownDOPromoReturn = AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOPromo;
//            mPTC_FareBreakdownDOFlexiReturn = AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOFlexi;
//
//            if (mPTC_FareBreakdownDOPromoReturn != null && mPTC_FareBreakdownDOPromoReturn.totalFare != null)
//                farePromo = StringUtils.getFloat(mPTC_FareBreakdownDOPromoReturn.totalFare.amount);
//
//            if (mPTC_FareBreakdownDOFlexiReturn != null && mPTC_FareBreakdownDOFlexiReturn.totalFare != null)
//                fareFlexi = StringUtils.getFloat(mPTC_FareBreakdownDOFlexiReturn.totalFare.amount);
//
//            BundledServiceDO bundledServiceDOPromo = new BundledServiceDO();
//            bundledServiceDOPromo.fare = farePromo;
//            BundledServiceDO bundledServiceDOFlexi = new BundledServiceDO();
//            bundledServiceDOFlexi.fare = fareFlexi;
//
//            //lhmFareValues.put("PromoFare", bundledServiceDOPromo);
//            lhmFareValues.put("Basic", bundledServiceDOPromo);
//            //if (fareFlexi != 0)
//            //lhmFareValues.put("FlexiFare", bundledServiceDOFlexi);
//
//            if (AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs != null
//                    && AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs.size() > 0)
//                for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs.size(); i++) {
//
//                    BundledServiceDO bundledServiceDO = AppConstants.bookingFlightDO.myODIDOReturn.arlBundledServiceDOs
//                            .get(i);
//
//                    if (bundledServiceDO != null && !TextUtils.isEmpty(bundledServiceDO.bundledServiceName)) {
//                        float mFare = 0;
//
//                        if (lhmFareValues.containsKey(bundledServiceDO.bundledServiceName)) {
//                            mFare = lhmFareValues.get(bundledServiceDO.bundledServiceName).fare;
//                            mFare += StringUtils.getFloat(bundledServiceDO.perPaxBundledFee);
//                        } else {
//                            mFare = StringUtils.getFloat(bundledServiceDO.perPaxBundledFee);
//                            mFare += farePromo;
//                        }
//
//                        bundledServiceDO.fare = mFare;
//
//                        if (bundledServiceDO.bundledServiceName.equalsIgnoreCase("Saver Fare"))
//                            lhmFareValues.put("Saver Fare", bundledServiceDO);
//                        else if (bundledServiceDO.bundledServiceName.equalsIgnoreCase("Super Fare"))
//                            lhmFareValues.put("Super Fare", bundledServiceDO);
//                        else if (bundledServiceDO.bundledServiceName.equalsIgnoreCase("Smart Fare"))
//                            lhmFareValues.put("Smart Fare", bundledServiceDO);
//                        //*********************************************************************
//                        /*if(bundledServiceDO.bundledServiceName.equalsIgnoreCase("Basic")){
//							lhmFareValues.put("Basic", bundledServiceDO);
//						}else */
//                        if (bundledServiceDO.bundledServiceName.toLowerCase().contains("Value".toLowerCase())) {
//                            lhmFareValues.put("Value", bundledServiceDO);
//                        } else if (bundledServiceDO.bundledServiceName.toLowerCase().contains("Extra".toLowerCase())) {
//                            lhmFareValues.put("Extra", bundledServiceDO);
//                        }
//                        // else
//                        // lhmFareValues.put(bundledServiceDO.bundledServiceName,
//                        // bundledServiceDO);
//
//                    }
//                }
//
//            if (lhmFareValues.containsKey("PromoFare"))
//                tempLhmFareValues.put("PromoFare", lhmFareValues.get("PromoFare"));
//            if (lhmFareValues.containsKey("Smart Fare"))
//                tempLhmFareValues.put("Smart Fare", lhmFareValues.get("Smart Fare"));
//            if (lhmFareValues.containsKey("FlexiFare"))
//                tempLhmFareValues.put("FlexiFare", lhmFareValues.get("FlexiFare"));
//            if (lhmFareValues.containsKey("Saver Fare"))
//                tempLhmFareValues.put("Saver Fare", lhmFareValues.get("Saver Fare"));
//            if (lhmFareValues.containsKey("Super Fare"))
//                tempLhmFareValues.put("Super Fare", lhmFareValues.get("Super Fare"));
//            //*************************************************************************
//            if (lhmFareValues.containsKey("Basic"))
//                tempLhmFareValues.put("Basic", lhmFareValues.get("Basic"));
//            if (lhmFareValues.containsKey("Value"))
//                tempLhmFareValues.put("Value", lhmFareValues.get("Value"));
//            if (lhmFareValues.containsKey("Extra"))
//                tempLhmFareValues.put("Extra", lhmFareValues.get("Extra"));
//
//            lhmFareValues.clear();
//            lhmFareValues.putAll(tempLhmFareValues);
//
//        } else {
//            PTC_FareBreakdownDO mPTC_FareBreakdownDOPromo = AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOPromo;
//            PTC_FareBreakdownDO mPTC_FareBreakdownDOFlexi = AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOFlexi;
//
//            if (mPTC_FareBreakdownDOPromo != null && mPTC_FareBreakdownDOPromo.totalFare != null)
//                farePromo = StringUtils.getFloat(mPTC_FareBreakdownDOPromo.totalFare.amount);
//            //new comment
//            if (mPTC_FareBreakdownDOFlexi != null && mPTC_FareBreakdownDOFlexi.totalFare != null)
//                fareFlexi = StringUtils.getFloat(mPTC_FareBreakdownDOFlexi.totalFare.amount);
//
//            // fareFlexi = 0.0f ;
//            BundledServiceDO bundledServiceDOPromo = new BundledServiceDO();
//            bundledServiceDOPromo.fare = farePromo;
//            BundledServiceDO bundledServiceDOFlexi = new BundledServiceDO();
//            bundledServiceDOFlexi.fare = fareFlexi;
//
//            //lhmFareValues.put("PromoFare", bundledServiceDOPromo);
//            lhmFareValues.put("Basic", bundledServiceDOPromo);
//            //if (fareFlexi != 0)
//            //lhmFareValues.put("FlexiFare", bundledServiceDOFlexi);
//
//            if (AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs != null
//                    && AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs.size() > 0) {
//                for (int i = 0; i < AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs.size(); i++) {
//
//                    BundledServiceDO bundledServiceDO = AppConstants.bookingFlightDO.myODIDOOneWay.arlBundledServiceDOs
//                            .get(i);
//
//                    float mFare = 0;
//
//                    if (lhmFareValues.containsKey(bundledServiceDO.bundledServiceName)) {
//                        mFare = lhmFareValues.get(bundledServiceDO.bundledServiceName).fare;
//                        mFare += StringUtils.getFloat(bundledServiceDO.perPaxBundledFee);
//                    } else {
//                        mFare = StringUtils.getFloat(bundledServiceDO.perPaxBundledFee);
//                        mFare += farePromo;
//                    }
//                    bundledServiceDO.fare = mFare;
//
//                    if (bundledServiceDO.bundledServiceName.equalsIgnoreCase("Saver Fare"))
//                        lhmFareValues.put("Saver Fare", bundledServiceDO);
//                    else if (bundledServiceDO.bundledServiceName.equalsIgnoreCase("Super Fare"))
//                        lhmFareValues.put("Super Fare", bundledServiceDO);
//                    else if (bundledServiceDO.bundledServiceName.equalsIgnoreCase("Smart Fare"))
//                        lhmFareValues.put("Smart Fare", bundledServiceDO);
//                    //*************************************************************
//					/*if (bundledServiceDO.bundledServiceName.equalsIgnoreCase("Basic"))
//						lhmFareValues.put("Basic", bundledServiceDO);//  //toLowerCase().contains("Basic".toLowerCase())
//					else*/
//                    if (bundledServiceDO.bundledServiceName.toLowerCase().contains("Value".toLowerCase())) {
//                        lhmFareValues.put("Value", bundledServiceDO);
//                    } else if (bundledServiceDO.bundledServiceName.toLowerCase().contains("Extra".toLowerCase())) {
//                        lhmFareValues.put("Extra", bundledServiceDO);
//                    }
//                    // else
//                    // lhmFareValues.put(bundledServiceDO.bundledServiceName,
//                    // bundledServiceDO);
//
//                    // if(lhmFareValues.keySet().contains("Saver Fare")){
//                    // bundledServiceDO1 = lhmFareValues.get("Saver Fare");
//                    // }
//                }
//
//                if (lhmFareValues.containsKey("PromoFare"))
//                    tempLhmFareValues.put("PromoFare", lhmFareValues.get("PromoFare"));
//                if (lhmFareValues.containsKey("Smart Fare"))
//                    tempLhmFareValues.put("Smart Fare", lhmFareValues.get("Smart Fare"));
//                if (lhmFareValues.containsKey("FlexiFare"))
//                    tempLhmFareValues.put("FlexiFare", lhmFareValues.get("FlexiFare"));
//                if (lhmFareValues.containsKey("Saver Fare"))
//                    tempLhmFareValues.put("Saver Fare", lhmFareValues.get("Saver Fare"));
//                if (lhmFareValues.containsKey("Super Fare"))
//                    tempLhmFareValues.put("Super Fare", lhmFareValues.get("Super Fare"));
//
//                if (lhmFareValues.containsKey("Basic"))
//                    tempLhmFareValues.put("Basic", lhmFareValues.get("Basic"));
//                if (lhmFareValues.containsKey("Value"))
//                    tempLhmFareValues.put("Value", lhmFareValues.get("Value"));
//                if (lhmFareValues.containsKey("Extra"))
//                    tempLhmFareValues.put("Extra", lhmFareValues.get("Extra"));
//
//                lhmFareValues.clear();
//                lhmFareValues.putAll(tempLhmFareValues);
//            }
//        }
//
//        strKeys = lhmFareValues.keySet();
//        arlKeys = new ArrayList<String>();
//
//        arlKeys1 = new ArrayList<String>();
//        arlKeys1.addAll(strKeys);
//
//        // for(int i = 0; i<arlKeys1.size();i++){
//        // if(i==1&&arlKeys1.contains("PromoFare") &&
//        // !arlKeys1.contains("FlexiFare") )
//        // {
//        // if(arlKeys1.contains("Saver Fare"))
//        // arlKeys.add("Saver Fare");
//        // arlKeys.add(arlKeys1.get(1));
//        // }
//        // else if(i==2 && arlKeys1.contains("PromoFare") &&
//        // arlKeys1.contains("FlexiFare") &&
//        // !arlKeys1.get(2).equalsIgnoreCase("Saver Fare") ){
//        // //if(arlKeys1.contains("Saver Fare")){
//        // if(arlKeys1.contains("Saver Fare"))
//        // arlKeys.add("Saver Fare");
//        // arlKeys.add(arlKeys1.get(2));
//        // } else if(i==2 && arlKeys1.get(i).equalsIgnoreCase("Saver Fare")){
//        // arlKeys.add(arlKeys1.get(i));
//        // } else{
//        // arlKeys.add(arlKeys1.get(i));
//        // }
//        // // }
//        // }
//
//        arlKeys = new ArrayList<String>(new LinkedHashSet<String>(arlKeys1));
//
//        lvFares.setAdapter(fareAdapter = new FareAdapter());
//        // lvFares.setVisibility(View.GONE);
//
//        fareAdapter.notifyDataSetChanged();
//
//        // showFareDetails(0);
//    }

//=============New updateFare added on 28-Feb-2018 for getting different fare from service to minus admin fee from totalfare==============================

    private void updateFares() {
        float farePromo = 0, fareFlexi = 0;

        switch (AppConstants.updateFaresCount) {
            case 0:
                PTC_FareBreakdownDO mPTC_FareBreakdownDOPromo = null;
                if (AppConstants.hashForListOfDifferentPrice != null && AppConstants.hashForListOfDifferentPrice.containsKey("Basic"))
                    mPTC_FareBreakdownDOPromo = AppConstants.hashForListOfDifferentPrice.get("Basic");

                PTC_FareBreakdownDO mPTC_FareBreakdownDOFlexi = AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOFlexi;

                float adminFees = 0, totalAmount = 0;
                if (mPTC_FareBreakdownDOPromo != null) {

                    for (int i = 0; i < mPTC_FareBreakdownDOPromo.vecFees.size(); i++) {

                        if (mPTC_FareBreakdownDOPromo.vecFees.get(i).feeCode.equalsIgnoreCase("CC/Transaction Fees"))
                            adminFees = StringUtils.getFloat(mPTC_FareBreakdownDOPromo.vecFees.get(i).amount.toString());
                    }

                    totalAmount = StringUtils.getFloat(mPTC_FareBreakdownDOPromo.totalFare.amount.toString());

                    if (adminFees != 0.0)
                        totalAmount -= adminFees;
                }

                if (mPTC_FareBreakdownDOPromo != null && mPTC_FareBreakdownDOPromo.totalFare != null)
                    farePromo = totalAmount;

                if (mPTC_FareBreakdownDOFlexi != null && mPTC_FareBreakdownDOFlexi.totalFare != null)
                    fareFlexi = StringUtils.getFloat(mPTC_FareBreakdownDOFlexi.totalFare.amount);

                BundledServiceDO bundledServiceDOPromo = new BundledServiceDO();
                bundledServiceDOPromo.fare = farePromo;
                BundledServiceDO bundledServiceDOFlexi = new BundledServiceDO();
                bundledServiceDOFlexi.fare = fareFlexi;

                lhmFareValues.put("Basic", bundledServiceDOPromo);
//                hideLoader();
                int count = 0;

                for (int i = 0; AppConstants.arlBundledServiceDOs != null && i < AppConstants.arlBundledServiceDOs.size(); i++) {
                    if (AppConstants.arlBundledServiceDOs.get(i).bundledServiceName.trim().equalsIgnoreCase("Value") && count == 0) {

//                        showLoader("");
                        count++;
                        AppConstants.fareType = "Value";
                        if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null &&
                                AppConstants.bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase("Return")) {
                            new CommonBL(SelectFlightItemDetails.this, SelectFlightItemDetails.this)
                                    .getAirPriceQuote(
                                            AppConstants.bookingFlightDO.requestParameterDOReturn,
                                            AppConstants.AIRPORT_CODE,
                                            AppConstants.bookingFlightDO.myBookFlightDOReturn,
                                            null,
                                            AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
                                            AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs,
                                            false, false, "", "", null, null,
                                            AppConstants.arlBundledServiceDOs.get(i).bunldedServiceId);

                        } else {
                            new CommonBL(SelectFlightItemDetails.this, SelectFlightItemDetails.this)
                                    .getAirPriceQuote(
                                            AppConstants.bookingFlightDO.requestParameterDO,
                                            AppConstants.AIRPORT_CODE,
                                            AppConstants.bookingFlightDO.myBookFlightDO,
                                            AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
                                            AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
                                            null,
                                            false, false,
                                            AppConstants.BookingReferenceID_Id, AppConstants.BookingReferenceID_Type,
                                            AppConstants.flexiOperationsDOCancel, AppConstants.flexiOperationsDOModify,
                                            AppConstants.arlBundledServiceDOs.get(i).bunldedServiceId);
                        }
                    }
                }

                if (!AppConstants.fareType.equalsIgnoreCase("Value"))
                    hideLoader();
                break;

            case 1:

                PTC_FareBreakdownDO mPTC_FareBreakdownDOPromoValue = null;
                if (AppConstants.hashForListOfDifferentPrice != null && AppConstants.hashForListOfDifferentPrice.containsKey("Value"))
                    mPTC_FareBreakdownDOPromoValue = AppConstants.hashForListOfDifferentPrice.get("Value");

                float adminFeesValue = 0, totalAmountValue = 0;
                if (mPTC_FareBreakdownDOPromoValue != null) {

                    for (int i = 0; i < mPTC_FareBreakdownDOPromoValue.vecFees.size(); i++) {

                        if (mPTC_FareBreakdownDOPromoValue.vecFees.get(i).feeCode.equalsIgnoreCase("CC/Transaction Fees"))
                            adminFeesValue = StringUtils.getFloat(mPTC_FareBreakdownDOPromoValue.vecFees.get(i).amount.toString());
                    }

                    totalAmountValue = StringUtils.getFloat(mPTC_FareBreakdownDOPromoValue.totalFare.amount.toString());

                    if (adminFeesValue != 0.0)
                        totalAmountValue -= adminFeesValue;
                }

                if (mPTC_FareBreakdownDOPromoValue != null && mPTC_FareBreakdownDOPromoValue.totalFare != null)
                    farePromo = totalAmountValue;

                BundledServiceDO bundledServiceDOPromoValue = new BundledServiceDO();
                bundledServiceDOPromoValue.fare = farePromo;
                int countB1 = 0;
                for (int i = 0; AppConstants.arlBundledServiceDOs != null && i < AppConstants.arlBundledServiceDOs.size(); i++) {
                    if (AppConstants.arlBundledServiceDOs.get(i).bundledServiceName.trim().equalsIgnoreCase("Value") && countB1 == 0) {
                        countB1++;
                        bundledServiceDOPromoValue.bunldedServiceId = AppConstants.arlBundledServiceDOs.get(i).bunldedServiceId;
                        bundledServiceDOPromoValue.bundledServiceName = AppConstants.arlBundledServiceDOs.get(i).bundledServiceName;
                        bundledServiceDOPromoValue.perPaxBundledFee = AppConstants.arlBundledServiceDOs.get(i).perPaxBundledFee;
                        bundledServiceDOPromoValue.bookingClasses = AppConstants.arlBundledServiceDOs.get(i).bookingClasses;
                        bundledServiceDOPromoValue.description = AppConstants.arlBundledServiceDOs.get(i).description;
                        bundledServiceDOPromoValue.includedServies = AppConstants.arlBundledServiceDOs.get(i).includedServies;
                    }
                }
                if (bundledServiceDOPromoValue.fare != 0)
                    lhmFareValues.put("Value", bundledServiceDOPromoValue);
//                hideLoader();

                int count1 = 0;
                for (int i = 0; AppConstants.arlBundledServiceDOs != null && i < AppConstants.arlBundledServiceDOs.size(); i++) {
                    if (AppConstants.arlBundledServiceDOs.get(i).bundledServiceName!=null && AppConstants.arlBundledServiceDOs.get(i).bundledServiceName.trim().equalsIgnoreCase("Extra") && count1 == 0) {

//                        showLoader("");
                        count1++;
                        AppConstants.fareType = "Extra";
                        if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null &&
                                AppConstants.bookingFlightDO.myBookFlightDOReturn.travelType.equalsIgnoreCase("Return")) {

                            new CommonBL(SelectFlightItemDetails.this, SelectFlightItemDetails.this)
                                    .getAirPriceQuote(
                                            AppConstants.bookingFlightDO.requestParameterDOReturn,
                                            AppConstants.AIRPORT_CODE,
                                            AppConstants.bookingFlightDO.myBookFlightDOReturn,
                                            null,
                                            AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
                                            AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs,
                                            false, false, "", "", null, null,
                                            AppConstants.arlBundledServiceDOs.get(i).bunldedServiceId);


                        } else {
                            new CommonBL(SelectFlightItemDetails.this, SelectFlightItemDetails.this)
                                    .getAirPriceQuote(
                                            AppConstants.bookingFlightDO.requestParameterDO,
                                            AppConstants.AIRPORT_CODE,
                                            AppConstants.bookingFlightDO.myBookFlightDO,
                                            AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
                                            AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
                                            null,
                                            false, false,
                                            AppConstants.BookingReferenceID_Id, AppConstants.BookingReferenceID_Type,
                                            AppConstants.flexiOperationsDOCancel, AppConstants.flexiOperationsDOModify,
                                            AppConstants.arlBundledServiceDOs.get(i).bunldedServiceId);
                        }
                    }
                }
                if (!AppConstants.fareType.equalsIgnoreCase("Extra"))
                    hideLoader();
                break;

            case 2:

                PTC_FareBreakdownDO mPTC_FareBreakdownDOPromoExtra = null;
                if (AppConstants.hashForListOfDifferentPrice != null &&
                        AppConstants.hashForListOfDifferentPrice.containsKey("Extra"))
                    mPTC_FareBreakdownDOPromoExtra = AppConstants.hashForListOfDifferentPrice.get("Extra");

                float adminFeesExtra = 0, totalAmountExtra = 0;
                if (mPTC_FareBreakdownDOPromoExtra != null) {

                    for (int i = 0; i < mPTC_FareBreakdownDOPromoExtra.vecFees.size(); i++) {
                        if (mPTC_FareBreakdownDOPromoExtra.vecFees.get(i).feeCode.equalsIgnoreCase("CC/Transaction Fees"))
                            adminFeesExtra = StringUtils.getFloat(mPTC_FareBreakdownDOPromoExtra.vecFees.get(i).amount.toString());
                    }

                    totalAmountExtra = StringUtils.getFloat(mPTC_FareBreakdownDOPromoExtra.totalFare.amount.toString());

                    if (adminFeesExtra != 0.0)
                        totalAmountExtra -= adminFeesExtra;
                }

                if (mPTC_FareBreakdownDOPromoExtra != null && mPTC_FareBreakdownDOPromoExtra.totalFare != null)
                    farePromo = totalAmountExtra;

                BundledServiceDO bundledServiceDOPromoExtra = new BundledServiceDO();
                bundledServiceDOPromoExtra.fare = farePromo;
                int countB2 = 0;
                for (int i = 0; AppConstants.arlBundledServiceDOs != null && i < AppConstants.arlBundledServiceDOs.size(); i++) {
                    if (AppConstants.arlBundledServiceDOs.get(i).bundledServiceName.trim().equalsIgnoreCase("Extra") && countB2 == 0) {
                        countB2++;
                        bundledServiceDOPromoExtra.bunldedServiceId = AppConstants.arlBundledServiceDOs.get(i).bunldedServiceId;
                        bundledServiceDOPromoExtra.bundledServiceName = AppConstants.arlBundledServiceDOs.get(i).bundledServiceName;
                        bundledServiceDOPromoExtra.perPaxBundledFee = AppConstants.arlBundledServiceDOs.get(i).perPaxBundledFee;
                        bundledServiceDOPromoExtra.bookingClasses = AppConstants.arlBundledServiceDOs.get(i).bookingClasses;
                        bundledServiceDOPromoExtra.description = AppConstants.arlBundledServiceDOs.get(i).description;
                        bundledServiceDOPromoExtra.includedServies = AppConstants.arlBundledServiceDOs.get(i).includedServies;
                    }
                }
                if (bundledServiceDOPromoExtra.fare != 0)
                    lhmFareValues.put("Extra", bundledServiceDOPromoExtra);
                hideLoader();
                break;
        }
        strKeys = lhmFareValues.keySet();
        arlKeys = new ArrayList<String>();

        arlKeys1 = new ArrayList<String>();
        arlKeys1.addAll(strKeys);

        arlKeys = new ArrayList<String>(new LinkedHashSet<String>(arlKeys1));
        lvFares.setAdapter(fareAdapter = new FareAdapter());
        fareAdapter.notifyDataSetChanged();

        AppConstants.updateFaresCount++;
    }

    private void showFareDetails(int pos) {
        LinearLayout view = (LinearLayout) layoutInflater.inflate(R.layout.fare_details, null);

        TextView tvTitledetails = (TextView) view.findViewById(R.id.tvTitledetails);
        TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        tvTitledetails.setVisibility(View.VISIBLE);
        tvTitledetails.setText(" " + getString(R.string.Details));
        String title = "", msg = "";
        setTypefaceOpenSansRegular(view);
        if (pos == 0) {
            Insider.tagEvent(AppConstants.PromoFare, SelectFlightItemDetails.this);
            //Insider.Instance.tagEvent(SelectFlightItemDetails.this,AppConstants.BasicFare);
            trackEvent("Select Fare", AppConstants.BasicFare, "");
            //title = getString(R.string.PromoDetails_title);
            title = getString(R.string.basicFare__title);
            tvMsg.setText(getResources().getString(R.string.basicDec));
            /*if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("G9"))
                msg = getString(R.string.g_PromoDetails);
			else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("3O"))
				msg = getString(R.string.o_PromoDetails);
			else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("E5"))
				msg = getString(R.string.e_PromoDetails);
			else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("9P"))
				msg = getString(R.string.p_PromoDetails);*/

            AppConstants.bookingFlightDO.isFlexiOut = false;
            AppConstants.bookingFlightDO.isFlexiIn = false;
            AppConstants.bookingFlightDO.bundleServiceID = "";
            //tvTitledetails.setText(getString(R.string.PromoFare_details));
            tvTitledetails.setText(getString(R.string.basicFare_details));

        } /*else if (arlKeys.get(pos).equalsIgnoreCase("FlexiFare")) {
            Insider.Instance.tagEvent(SelectFlightItemDetails.this,AppConstants.FlexiFare);
			trackEvent("Select Fare", AppConstants.FlexiFare, "");
			title = getString(R.string.FlexiFare);
			if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("G9"))
				msg = getString(R.string.g_FlexiDetails);
			else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("3O"))
				msg = getString(R.string.o_FlexiDetails);
			else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("E5"))
				msg = getString(R.string.e_FlexiDetails);
			else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("9P"))
				msg = getString(R.string.p_FlexiDetails);

			if (AppConstants.bookingFlightDO.myODIDOReturn != null) {
				AppConstants.bookingFlightDO.isFlexiOut = true;
				AppConstants.bookingFlightDO.isFlexiIn = true;
			} else {
				AppConstants.bookingFlightDO.isFlexiOut = true;
				AppConstants.bookingFlightDO.isFlexiIn = false;
			}
			AppConstants.bookingFlightDO.bundleServiceID = "";
			tvTitledetails.setText(getString(R.string.FlexiFare_details));
		}*/ else {
            title = lhmFareValues.get(arlKeys.get(pos)).bundledServiceName;
            msg = lhmFareValues.get(arlKeys.get(pos)).description + ".";
            AppConstants.bookingFlightDO.bundleServiceID = lhmFareValues.get(arlKeys.get(pos)).bunldedServiceId;

            if (AppConstants.bookingFlightDO.myODIDOReturn != null) {
                AppConstants.bookingFlightDO.isFlexiOut = false;//if add 200 aed extra changed it true,true
                AppConstants.bookingFlightDO.isFlexiIn = false;
            } else {
                AppConstants.bookingFlightDO.isFlexiOut = true;
                AppConstants.bookingFlightDO.isFlexiIn = false;
            }
        }
        // String forSuperFare = "1 modification and cancellation without
        // penalty up to 4 hours prior to departure, 40 KG free checked baggage,
        // 1 hot meal + 1 water, a front row or exit seat and quick check-in or
        // quick exit";
        // String forSaverFare = "1 modification and cancellation without
        // penalty up to 4 hours prior to departure, 20 KG free checked baggage,
        // 1 sandwich and a free reserved seat from row 5 onwards";
        // String forSmartFare = "1 modification and cancellation without
        // penalty up to 4 hours prior to departure, a front row or exit seat
        // and quick check-in or quick exit";
        // String xFare = "Our cheapest available fare, modifications and
        // cancellation are possible up to 24th prior to departure and subjected
        // to fee";
        // tvTitle.setText(title);
        if (title.toLowerCase().contains("Super Fare".toLowerCase())) {
            Insider.tagEvent(AppConstants.SuperFare, SelectFlightItemDetails.this);
            //Insider.Instance.tagEvent(SelectFlightItemDetails.this,AppConstants.SuperFare);
            trackEvent("Select Fare", AppConstants.SuperFare, "");
            tvMsg.setText(getResources().getString(R.string.superFareDes));

            if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("G9"))
                tvMsg.setText(getResources().getString(R.string.g_superFareDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("3O"))
                tvMsg.setText(getResources().getString(R.string.o_superFareDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("E5"))
                tvMsg.setText(getResources().getString(R.string.e_superFareDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("9P"))
                tvMsg.setText(getResources().getString(R.string.P_superFareDes));
            else
                tvMsg.setText(getResources().getString(R.string.superFareDes));

            tvTitledetails.setText(getString(R.string.SuperFare_details));
        } else if (title.toLowerCase().contains("Saver Fare".toLowerCase())) {
            Insider.tagEvent(AppConstants.SaverFare, SelectFlightItemDetails.this);
            //Insider.Instance.tagEvent(SelectFlightItemDetails.this,AppConstants.SaverFare);
            trackEvent("Select Fare", AppConstants.SaverFare, "");
            if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("G9"))
                tvMsg.setText(getResources().getString(R.string.g_saverFareDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("3O"))
                tvMsg.setText(getResources().getString(R.string.o_saverFareDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("E5"))
                tvMsg.setText(getResources().getString(R.string.e_saverFareDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("9P"))
                tvMsg.setText(getResources().getString(R.string.P_saverFareDes));
            else
                tvMsg.setText(getResources().getString(R.string.saverFareDes));

            tvTitledetails.setText(getString(R.string.SaverFare_details));
        } else if (title.toLowerCase().contains("Smart Fare".toLowerCase())) {
            Insider.tagEvent(AppConstants.SmartFare, SelectFlightItemDetails.this);
            //Insider.Instance.tagEvent(SelectFlightItemDetails.this,AppConstants.SmartFare);
            trackEvent("Select Fare", AppConstants.SmartFare, "");
            if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("G9"))
                tvMsg.setText(getResources().getString(R.string.g_smartFareDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("3O"))
                tvMsg.setText(getResources().getString(R.string.o_smartFareDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("E5"))
                tvMsg.setText(getResources().getString(R.string.e_smartFareDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("9P"))
                tvMsg.setText(getResources().getString(R.string.P_smartFareDes));
            else
                tvMsg.setText(getResources().getString(R.string.smartFareDes));

            tvTitledetails.setText(getString(R.string.SmartFare_details));
        } else if (title.toLowerCase().contains("Basic".toLowerCase())) {
            AppConstants.fareType = "Basic";
            //Insider.Instance.tagEvent(SelectFlightItemDetails.this,AppConstants.SmartFare);
            trackEvent("Select Fare", AppConstants.SmartFare, "");
            tvMsg.setText(getResources().getString(R.string.basicDec));
            tvTitledetails.setText(getString(R.string.basicFare_details));

        } else if (title.toLowerCase().contains("Value".toLowerCase())) {
            AppConstants.fareType = "Value";
            if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("G9"))
                tvMsg.setText(getResources().getString(R.string.g_ValueDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("3O"))
                tvMsg.setText(getResources().getString(R.string.o_ValueDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("E5"))
                tvMsg.setText(getResources().getString(R.string.e_ValueDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("9P"))
                tvMsg.setText(getResources().getString(R.string.p_ValueDes));
            /*else
                tvMsg.setText(getResources().getString(R.string.smartFareDes));*/

            tvTitledetails.setText(getString(R.string.ValueFare_details));
        } else if (title.toLowerCase().contains("Extra".toLowerCase())) {
            AppConstants.fareType = "Extra";
            if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("G9"))
                tvMsg.setText(getResources().getString(R.string.g_ExtraDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("3O"))
                tvMsg.setText(getResources().getString(R.string.o_ExtraDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("E5"))
                tvMsg.setText(getResources().getString(R.string.e_ExtraDes));
            else if (AppConstants.bookingFlightDO.myBookFlightDO.srvUrlType.equalsIgnoreCase("9P"))
                tvMsg.setText(getResources().getString(R.string.p_ExtraDes));
            /*else
                tvMsg.setText(getResources().getString(R.string.smartFareDes));*/

            tvTitledetails.setText(getString(R.string.ExtraFare_details));
        }
        // else if(title.contains("Smart Fare")){
        // tvMsg.setText(xFare+"
        // "+getResources().getString(R.string.xFaredesc));
        // }
        else {
            tvMsg.setText(msg + "");
        }

        // if(title.equalsIgnoreCase("Smart Fare"))
        // tvTitledetails.setText(getString(R.string.SmartFare)+"
        // "+getResources().getString(R.string.Details));
        //
        // if(title.equalsIgnoreCase("Saver Fare"))
        // tvTitledetails.setText(getString(R.string.SaverFare)+"
        // "+getResources().getString(R.string.Details));

        llFareDetailsMain.removeAllViews();
        llFareDetailsMain.addView(view);

        llFareDetailsMain.setVisibility(View.VISIBLE);
        tvTitledetails.setTypeface(typefaceOpenSansSemiBold);
        tvMsg.setTypeface(typefaceOpenSansRegular);
    }

    private class FareAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            if (strKeys != null && strKeys.size() > 0)
                return strKeys.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup arg2) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = (LinearLayout) layoutInflater.inflate(R.layout.fare_list_item, null);

                // viewHolder.viewTop = (View)
                // convertView.findViewById(R.id.viewTop);
                // viewHolder.viewTop1 = (View)
                // convertView.findViewById(R.id.viewTop1);
                viewHolder.llFlightDetailsPromoFare = (LinearLayout) convertView
                        .findViewById(R.id.llFlightDetailsPromoFare);
                viewHolder.ivFlightDetailsFareInfo = (ImageView) convertView.findViewById(R.id.ivFlightDetailsFareInfo);
                viewHolder.tvFlightDetailsFareName = (TextView) convertView.findViewById(R.id.tvFlightDetailsFareName);
                viewHolder.tvFlightDetailsFare = (TextView) convertView.findViewById(R.id.tvFlightDetailsFare);

                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            setTypeFaceSemiBold(viewHolder.llFlightDetailsPromoFare);
            if (pos == 0) {
                // viewHolder.viewTop.setVisibility(View.VISIBLE);
                // viewHolder.viewTop1.setVisibility(View.VISIBLE);
                //if (lhmFareValues.get("PromoFare") != null)
                if (lhmFareValues.get("Basic") != null)
                    viewHolder.tvFlightDetailsFare.setText("" + AppConstants.CurrencyCodeAfterExchange + " "
                            + updateCurrencyByFactor(lhmFareValues.get("Basic").fare + "", 0));
                //viewHolder.tvFlightDetailsFareName.setText(getString(R.string.PromoFare) + ":");
                viewHolder.tvFlightDetailsFareName.setText(getString(R.string.basicFare) + ":");
                viewHolder.tvFlightDetailsFare.setPadding(0, 0, 0, 0);
                viewHolder.tvFlightDetailsFareName.setPadding(0, 0, 0, 0);
            }
            // else if(pos == 1)
            // {
            // if(lhmFareValues.get("FlexiFare") != null)
            // viewHolder.tvFlightDetailsFare.setText(""+AppConstants.CurrencyCodeAfterExchange+"
            // "+updateCurrencyByFactor(lhmFareValues.get("FlexiFare").fare+"",
            // 0));
            // viewHolder.tvFlightDetailsFareName.setText(getString(R.string.FlexiFare)+":");
            // }
            else if (pos > 0) {
                if ((pos + 1) == strKeys.size()) {
                    // viewHolder.viewTop.setVisibility(View.VISIBLE);
                    // viewHolder.viewTop1.setVisibility(View.VISIBLE);
                    // viewHolder.tvFlightDetailsFareName.setPadding(0, 0, 0,
                    // 0);
                }
                // if(lhmFareValues.get(arlKeys.get(pos))!= null)
                // viewHolder.tvFlightDetailsFare.setText(""+AppConstants.CurrencyCodeAfterExchange+"
                // "+updateCurrencyByFactor(lhmFareValues.get(arlKeys.get(pos)).fare+"",
                // 0));

                if (arlKeys.get(pos).contains("Smart Fare")) {
                    viewHolder.tvFlightDetailsFare.setText("" + AppConstants.CurrencyCodeAfterExchange + " "
                            + updateCurrencyByFactor(lhmFareValues.get(arlKeys.get(pos)).fare + "", 0));
                    viewHolder.tvFlightDetailsFareName.setText(getResources().getString(R.string.SmartFare) + ":");
                } /*else if (arlKeys.get(pos).contains("FlexiFare")) {
                    viewHolder.tvFlightDetailsFareName.setText(getResources().getString(R.string.FlexiFare) + ":");
					viewHolder.tvFlightDetailsFare.setText("" + AppConstants.CurrencyCodeAfterExchange + " "
							+ updateCurrencyByFactor(lhmFareValues.get(arlKeys.get(pos)).fare + "", 0));
				}else if (arlKeys.get(pos).contains("Super Fare")) {
					viewHolder.tvFlightDetailsFare.setText("" + AppConstants.CurrencyCodeAfterExchange + " "
							+ updateCurrencyByFactor(lhmFareValues.get(arlKeys.get(pos)).fare + "", 0));
					viewHolder.tvFlightDetailsFareName.setText(getResources().getString(R.string.SuperFare) + ":");
				} else if (arlKeys.get(pos).contains("Saver Fare")) {
					viewHolder.tvFlightDetailsFare.setText("" + AppConstants.CurrencyCodeAfterExchange + " "
							+ updateCurrencyByFactor(lhmFareValues.get(arlKeys.get(pos)).fare + "", 0));
					viewHolder.tvFlightDetailsFareName.setText(getResources().getString(R.string.SaverFare) + ":");
				}*/ else if (arlKeys.get(pos).toLowerCase().contains("Bundle Fare".toLowerCase())) {
                    viewHolder.tvFlightDetailsFare.setText("" + AppConstants.CurrencyCodeAfterExchange + " "
                            + updateCurrencyByFactor(lhmFareValues.get(arlKeys.get(pos)).fare + "", 0));
                    viewHolder.tvFlightDetailsFareName.setText(getResources().getString(R.string.BundleFare) + ":");
                }/*else if(arlKeys.get(pos).contains("Basic")){
                    viewHolder.tvFlightDetailsFare.setText("" + AppConstants.CurrencyCodeAfterExchange + " "
							+ updateCurrencyByFactor(lhmFareValues.get(arlKeys.get(pos)).fare + "", 0));
					viewHolder.tvFlightDetailsFareName.setText(getResources().getString(R.string.basicFare) + ":");
				}*/ else if (arlKeys.get(pos).toLowerCase().contains("Value".toLowerCase())) {
                    viewHolder.tvFlightDetailsFare.setText("" + AppConstants.CurrencyCodeAfterExchange + " "
                            + updateCurrencyByFactor(lhmFareValues.get(arlKeys.get(pos)).fare + "", 0));
                    viewHolder.tvFlightDetailsFareName.setText(getResources().getString(R.string.ValueFare) + ":");
                } else if (arlKeys.get(pos).toLowerCase().contains("Extra".toLowerCase())) {
                    viewHolder.tvFlightDetailsFare.setText("" + AppConstants.CurrencyCodeAfterExchange + " "
                            + updateCurrencyByFactor(lhmFareValues.get(arlKeys.get(pos)).fare + "", 0));
                    viewHolder.tvFlightDetailsFareName.setText(getResources().getString(R.string.ExtraFare) + ":");
                }

            }
            if (position == pos) {
                if (pos == 0) {
                    viewHolder.llFlightDetailsPromoFare.setBackgroundResource(R.drawable.bg_fare_top_selected);
                } else if (pos == (strKeys.size() - 1))
                    viewHolder.llFlightDetailsPromoFare.setBackgroundResource(R.drawable.bg_fare_bottom_select);
                else {
                    viewHolder.llFlightDetailsPromoFare.setBackgroundResource(R.drawable.bg_fare_middle_selected);
                    viewHolder.tvFlightDetailsFareName.setPadding(0, 0, 0, 15);
                    viewHolder.tvFlightDetailsFare.setPadding(0, 0, 0, 15);
                }
            } else {
                if (pos == 0) {
                    if (position == 1) {
                        viewHolder.llFlightDetailsPromoFare.setBackgroundResource(R.drawable.bg_fare_top_unselected);
                    } else {
                        viewHolder.llFlightDetailsPromoFare
                                .setBackgroundResource(R.drawable.bg_fare_top_unselected_two);
                    }

                } else if (pos == (strKeys.size() - 1))
                    viewHolder.llFlightDetailsPromoFare.setBackgroundResource(R.drawable.bg_fare_bottom_unselect);
                else if (pos == (position - 1))
                    viewHolder.llFlightDetailsPromoFare.setBackgroundResource(R.drawable.bg_fare_middle_selected_two);
                else {
                    viewHolder.llFlightDetailsPromoFare.setBackgroundResource(R.drawable.bg_fare_middle_unselect);
                    viewHolder.tvFlightDetailsFareName.setPadding(0, 0, 0, 15);
                    viewHolder.tvFlightDetailsFare.setPadding(0, 0, 0, 15);
                }
            }
            viewHolder.tvFlightDetailsFareName.setTypeface(typefaceOpenSansSemiBold);
            // viewHolder.llFlightDetailsPromoFare.setPadding(10, 20, 10, 30);
            return convertView;
        }

        class ViewHolder {
            // View viewTop,viewTop1;
            LinearLayout llFlightDetailsPromoFare;
            ImageView ivFlightDetailsFareInfo;
            TextView tvFlightDetailsFareName, tvFlightDetailsFare;
        }
    }

    @Override
    public void dataRetreived(Response data) {
        if (data != null) {
            switch (data.method) {
                case AIR_PRICEQUOTE:

                    if (!data.isError) {
                        AirPriceQuoteDO airPriceQuoteDO = new AirPriceQuoteDO();
                        airPriceQuoteDO = (AirPriceQuoteDO) data.data;

                        if (airPriceQuoteDO != null && airPriceQuoteDO.vecPricedItineraryDOs != null
                                && airPriceQuoteDO.vecPricedItineraryDOs.size() > 0
                                && AppConstants.bookingFlightDO != null) {
                            if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null) {
                                AppConstants.bookingFlightDO.myODIDOReturn.mPTC_FareBreakdownDOFlexi = airPriceQuoteDO.vecPricedItineraryDOs
                                        .get(0).vecPTC_FareBreakdownDOs.get(0);
                            } else {
                                AppConstants.bookingFlightDO.myODIDOOneWay.mPTC_FareBreakdownDOFlexi = airPriceQuoteDO.vecPricedItineraryDOs
                                        .get(0).vecPTC_FareBreakdownDOs.get(0);
                            }
                        }
                    } else {
                        showCustomDialog(SelectFlightItemDetails.this, getString(R.string.Alert),
                                getString(R.string.TechProblem), getString(R.string.Ok), "", DATAFAIL);
                    }
                    updateFares();

//                    if (AppConstants.arlBundledServiceDOs == null)
//                        hideLoader();
//
//                    if (AppConstants.bookingFlightDO.myBookFlightDO.travelType.equalsIgnoreCase("OneWay")
//                            && AppConstants.bookingFlightDO.myBookFlightDOReturn == null) {
//                        if (AppConstants.arlBundledServiceDOs != null && AppConstants.arlBundledServiceDOs.size() == AppConstants.count - 1)
//                            hideLoader();
//                    } else {
//                        if (AppConstants.arlBundledServiceDOs != null && AppConstants.arlBundledServiceDOs.size() / 2 == AppConstants.count - 1) {
//                            hideLoader();
//                        }
//                    }

                default:
                    break;
            }
        } else {
            if (data.data instanceof String) {
                if (((String) data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
                    showCustomDialog(getApplicationContext(), getString(R.string.Alert),
                            getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "",
                            AppConstants.INTERNET_PROBLEM);
                else
                    showCustomDialog(getApplicationContext(), getString(R.string.Alert),
                            getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
            } else {
                showCustomDialog(SelectFlightItemDetails.this, getString(R.string.Alert),
                        getString(R.string.TechProblem), getString(R.string.Ok), "", DATAFAIL);
                hideLoader();
            }
        }
    }

    private void callServicePriceOneWay() {
        showLoader("");
        // if(isManageBook)
        // {
        // if (new CommonBL(SelectFilghtItemDetails.this,
        // SelectFilghtItemDetails.this)
        // .getAirPriceQuote(
        // AppConstants.bookingFlightDO.requestParameterDO,
        // AppConstants.AIRPORT_CODE,
        // AppConstants.bookingFlightDO.myBookFlightDO,
        // AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
        // mOriginDestinationInformationDO.vecOriginDestinationOptionDOs,
        // null,
        // true, false,
        // AppConstants.BookingReferenceID_Id,
        // AppConstants.BookingReferenceID_Type,
        // AppConstants.flexiOperationsDOCancel,
        // AppConstants.flexiOperationsDOModify)) {
        // }
        // }
        // else
        // {

        AppConstants.hashForListOfDifferentPrice = new HashMap<String, PTC_FareBreakdownDO>();
        AppConstants.updateFaresCount = 0;
        AppConstants.fareType = "Basic";
        if (new CommonBL(SelectFlightItemDetails.this, SelectFlightItemDetails.this).getAirPriceQuote(
                AppConstants.bookingFlightDO.requestParameterDO, AppConstants.AIRPORT_CODE,
                AppConstants.bookingFlightDO.myBookFlightDO, null,
                AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs, null, true, false, "", "",
                null, null, "")) {

//            callbundleserviceforoneway();
        }
        // }
    }

    private void callServicePriceReturn() {
        showLoader("");
        // if(isManageBook)
        // {
        // if (new CommonBL(SelectFilghtItemDetails.this,
        // SelectFilghtItemDetails.this).getAirPriceQuote(
        // AppConstants.bookingFlightDO.requestParameterDO,
        // AppConstants.AIRPORT_CODE,
        // AppConstants.bookingFlightDO.myBookFlightDO,
        // AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
        // mOriginDestinationInformationDO.vecOriginDestinationOptionDOs,
        // null,
        // true, false, AppConstants.BookingReferenceID_Id,
        // AppConstants.BookingReferenceID_Type,
        // AppConstants.flexiOperationsDOCancel,
        // AppConstants.flexiOperationsDOModify)) {
        // }
        // }
        // else
        // {

        AppConstants.hashForListOfDifferentPrice = new HashMap<String, PTC_FareBreakdownDO>();
        AppConstants.updateFaresCount = 0;
        AppConstants.fareType = "Basic";

        if (new CommonBL(SelectFlightItemDetails.this, SelectFlightItemDetails.this).getAirPriceQuote(
                AppConstants.bookingFlightDO.requestParameterDOReturn,
                AppConstants.AIRPORT_CODE,
                AppConstants.bookingFlightDO.myBookFlightDOReturn, null,
                AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
                AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs, true, true, "", "", null,
                null, "")) {

//            callbundleServiceforReturn();
        }
        // }
    }

    @Override
    public void onButtonYesClick(String from) {
        super.onButtonYesClick(from);
        if (from.equalsIgnoreCase(DATAFAIL))
            clickHome();
    }

    public void callbundleserviceforoneway() {

        showLoader("");
        int count = 0, count1 = 0;
        Handler handler = new Handler();

        for (int i = 0; AppConstants.arlBundledServiceDOs != null && i < AppConstants.arlBundledServiceDOs.size(); i++) {
            if (AppConstants.arlBundledServiceDOs.get(i).bundledServiceName.trim().equalsIgnoreCase("Value") && count == 0) {
                count++;
                final int finalI = i;

                new CommonBL(SelectFlightItemDetails.this, SelectFlightItemDetails.this)
                        .getAirPriceQuote(
                                AppConstants.bookingFlightDO.requestParameterDO,
                                AppConstants.AIRPORT_CODE,
                                AppConstants.bookingFlightDO.myBookFlightDO,
                                AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
                                AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
                                null,
                                false, false,
                                AppConstants.BookingReferenceID_Id, AppConstants.BookingReferenceID_Type,
                                AppConstants.flexiOperationsDOCancel, AppConstants.flexiOperationsDOModify,
                                AppConstants.arlBundledServiceDOs.get(finalI).bunldedServiceId);


            }
            if (AppConstants.arlBundledServiceDOs.get(i).bundledServiceName.trim().equalsIgnoreCase("Extra") && count1 == 0) {
                count1++;
                final int finalI1 = i;
                new CommonBL(SelectFlightItemDetails.this, SelectFlightItemDetails.this)
                        .getAirPriceQuote(
                                AppConstants.bookingFlightDO.requestParameterDO,
                                AppConstants.AIRPORT_CODE,
                                AppConstants.bookingFlightDO.myBookFlightDO,
                                AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify,
                                AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
                                null,
                                false, false,
                                AppConstants.BookingReferenceID_Id, AppConstants.BookingReferenceID_Type,
                                AppConstants.flexiOperationsDOCancel, AppConstants.flexiOperationsDOModify,
                                AppConstants.arlBundledServiceDOs.get(finalI1).bunldedServiceId);

            }
        }
    }

    public void callbundleServiceforReturn() {

        showLoader("");
        int count = 0, count1 = 0;
        Handler handler = new Handler();
        for (int i = 0; AppConstants.arlBundledServiceDOs != null && i < AppConstants.arlBundledServiceDOs.size(); i++) {

            if (AppConstants.arlBundledServiceDOs.get(i).bundledServiceName.trim().equalsIgnoreCase("Value") && count == 0) {
                count++;

                final int finalI = i;

                new CommonBL(SelectFlightItemDetails.this, SelectFlightItemDetails.this)
                        .getAirPriceQuote(
                                AppConstants.bookingFlightDO.requestParameterDOReturn,
                                AppConstants.AIRPORT_CODE,
                                AppConstants.bookingFlightDO.myBookFlightDOReturn,
                                null,
                                AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
                                AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs,
                                false, false, "", "", null, null,
                                AppConstants.arlBundledServiceDOs.get(finalI).bunldedServiceId);

            }

            if (AppConstants.arlBundledServiceDOs.get(i).bundledServiceName.trim().equalsIgnoreCase("Extra") && count1 == 0) {
                count1++;
                final int finalI1 = i;

                new CommonBL(SelectFlightItemDetails.this, SelectFlightItemDetails.this)
                        .getAirPriceQuote(
                                AppConstants.bookingFlightDO.requestParameterDOReturn,
                                AppConstants.AIRPORT_CODE,
                                AppConstants.bookingFlightDO.myBookFlightDOReturn,
                                null,
                                AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs,
                                AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs,
                                false, false, "", "", null, null,
                                AppConstants.arlBundledServiceDOs.get(finalI1).bunldedServiceId);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (AppConstants.hashForListOfDifferentPrice != null) {
            AppConstants.hashForListOfDifferentPrice.clear();
            AppConstants.count = 0;
        }
    }
}
