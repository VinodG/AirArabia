package com.winit.airarabia;

import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.google.android.gms.internal.is;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.objects.AirBookDO;
import com.winit.airarabia.objects.BookFlightDO;
import com.winit.airarabia.objects.BookingFlightDO;
import com.winit.airarabia.objects.BookingModificationDO;
import com.winit.airarabia.objects.FareDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.ModifiedPNRResDO;
import com.winit.airarabia.objects.OriginDestinationInformationDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.objects.PassengerInfoDO;
import com.winit.airarabia.objects.PassengerInfoPersonDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.objects.RequestParameterDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.Response;

@SuppressLint("NewApi")
public class PaymentSummaryActivity extends BaseActivity implements DataListener {

    private LinearLayout llpayment, llTotalPaid, llPaymentSumPolicyCode,
            llBaggageSelectionCost, llSeatSelectionCost, llMealSelectionCost,
            llAirportServicesCost, llPaymentSumReservationDate,
            llPaymentSumAmountPaid, llPaymentSumStatus, llContactInfo, llPaymentInfo, llPassengerInfo,
            llPersonInfoMain, llNote;
    private TextView tvPaymentSumReservationNumber,
            tvPaymentSumReservationDate, tvPaymentSumAmountPaid,
            tvAirFareCost, tvTaxesCost, tvBaggageSelectionCost,
            tvOtherTaxeFeeSurchargeCost, tvTotalAmountCost, tvName,
            tvCountryOfResidence, tvMobile, tvEmail, tvPaymentSumModifySegment,
            tvPaymentSumStatus, tvTotalPaid, tvImportantNote, tvPaymentImpNote,
            tvPaymentImpNoteDesk, tvPaymentImpNoteDesk2, tvSeatSelectionCost,
            tvMealSelectionCost, tvAirportServicesCost, tvPaymentSumPolicyCode;
    private ImageView ivInboundCheck, ivOutboundCheck;
    private AirBookDO airBookDO;
    private boolean isManageBook = false, isModifiedBook = false,
            isOutBoundClick = false, isInBoundClick = false, isManageView = false, isModify = false, isCancel = false;
    private final static String OUT_BOUND = "OUT_BOUND", IN_BOUND = "IN_BOUND";
    private OriginDestinationInformationDO mODIDO = new OriginDestinationInformationDO(),
            mODIDOReturn = new OriginDestinationInformationDO();
    private Vector<FlightSegmentDO> vecFlightSegmentDOsOutBound, vecFlightSegmentDOsInBound;

    private int stops = 0, siz = 0;
    private String flightNo = "";
    private String pnr;
    private RequestParameterDO requestParameterDO;
    private LinearLayout llOutSrcDest, llInSrcDest, llOutFilghtDetails, llInFilghtDetails;

    private PaymentSummaryActivity.BCR bcr;

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
    protected void onResume() {

        super.onResume();
        isOutBoundClick = false;
        isInBoundClick = false;
        ivOutboundCheck.setImageResource(R.drawable.uncheck);
        ivInboundCheck.setImageResource(R.drawable.uncheck);
        AppConstants.bookingFlightDO.isOutBoundClick = false;
        AppConstants.bookingFlightDO.isInBoundClick = false;
    }

    @Override
    public void initilize() {

        tvHeaderTitle.setText(getString(R.string.ReservationSummary));
        btnSubmitNext.setVisibility(View.VISIBLE);
        btnSubmitNext.setText(getString(R.string.Continue));

        airBookDO = (AirBookDO) getIntent().getExtras().getSerializable(AppConstants.AIR_BOOK);
        if (getIntent().hasExtra("MANAGE_BOOKING"))
            isManageBook = getIntent().getBooleanExtra("MANAGE_BOOKING", isManageBook);
        if (getIntent().hasExtra(AppConstants.FROM))
            isModifiedBook = getIntent().getBooleanExtra(AppConstants.FROM, false);
        if (getIntent().hasExtra("MANAGE_VIEW"))
            isManageView = getIntent().getBooleanExtra("MANAGE_VIEW", false);
        if (getIntent().hasExtra("MODIFY"))
            isModify = getIntent().getBooleanExtra("MODIFY", false);
        if (getIntent().hasExtra("CANCEL"))
            isCancel = getIntent().getBooleanExtra("CANCEL", false);
        if (!isManageBook)
            bookComplete();
        if (isCancel) {
            tvHeaderTitle.setText(getString(R.string.CancelBooking));
            btnSubmitNext.setText(getString(R.string.ConfirmCancellation));
        }

        bcr = new BCR();
        intentFilter.addAction(AppConstants.HOME_CLICK);
        intentFilter.addAction(AppConstants.BOOK_SUCCESS);
        registerReceiver(bcr, intentFilter);

        llpayment = (LinearLayout) layoutInflater.inflate(R.layout.payment_summary, null);
        llMiddleBase.addView(llpayment, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        llNote = (LinearLayout) llpayment.findViewById(R.id.llNote);
        tvImportantNote = (TextView) llpayment.findViewById(R.id.tvImportantNote);
        tvPaymentImpNote = (TextView) llpayment.findViewById(R.id.tvPaymentImpNote);
        tvPaymentImpNoteDesk = (TextView) llpayment.findViewById(R.id.tvPaymentImpNoteDesk);
        tvPaymentImpNoteDesk2 = (TextView) llpayment.findViewById(R.id.tvPaymentImpNoteDesk2);
        tvPaymentSumReservationNumber = (TextView) llpayment.findViewById(R.id.tvPaymentSumReservationNumber);
        tvPaymentSumReservationDate = (TextView) llpayment.findViewById(R.id.tvPaymentSumReservationDate);
        tvPaymentSumAmountPaid = (TextView) llpayment.findViewById(R.id.tvPaymentSumAmountPaid);
        tvAirFareCost = (TextView) llpayment.findViewById(R.id.tvAirFareCost);
        tvTaxesCost = (TextView) llpayment.findViewById(R.id.tvTaxesCost);
        tvBaggageSelectionCost = (TextView) llpayment.findViewById(R.id.tvBaggageSelectionCost);
        tvSeatSelectionCost = (TextView) llpayment.findViewById(R.id.tvSeatSelectionCost);
        tvMealSelectionCost = (TextView) llpayment.findViewById(R.id.tvMealSelectionCost);
        tvAirportServicesCost = (TextView) llpayment.findViewById(R.id.tvAirportServicesCost);
        tvOtherTaxeFeeSurchargeCost = (TextView) llpayment.findViewById(R.id.tvOtherTaxeFeeSurchargeCost);
        tvTotalAmountCost = (TextView) llpayment.findViewById(R.id.tvTotalAmountCost);
        tvName = (TextView) llpayment.findViewById(R.id.tvName);
        tvCountryOfResidence = (TextView) llpayment.findViewById(R.id.tvCountryOfResidence);
        tvMobile = (TextView) llpayment.findViewById(R.id.tvMobile);
        tvEmail = (TextView) llpayment.findViewById(R.id.tvEmail);
        tvPaymentSumModifySegment = (TextView) llpayment.findViewById(R.id.tvPaymentSumModifySegment);
        ivOutboundCheck = (ImageView) llpayment.findViewById(R.id.ivOutboundCheck);
        ivInboundCheck = (ImageView) llpayment.findViewById(R.id.ivInboundCheck);
        llPaymentSumReservationDate = (LinearLayout) llpayment.findViewById(R.id.llPaymentSumReservationDate);
        llPaymentSumAmountPaid = (LinearLayout) llpayment.findViewById(R.id.llPaymentSumAmountPaid);
        llPaymentSumStatus = (LinearLayout) llpayment.findViewById(R.id.llPaymentSumStatus);
        llTotalPaid = (LinearLayout) llpayment.findViewById(R.id.llTotalPaid);
        tvPaymentSumStatus = (TextView) llpayment.findViewById(R.id.tvPaymentSumStatus);
        tvTotalPaid = (TextView) llpayment.findViewById(R.id.tvTotalPaid);
        tvPaymentSumPolicyCode = (TextView) llpayment.findViewById(R.id.tvPaymentSumPolicyCode);
        llPaymentSumPolicyCode = (LinearLayout) llpayment.findViewById(R.id.llPaymentSumPolicyCode);
        llBaggageSelectionCost = (LinearLayout) llpayment.findViewById(R.id.llBaggageSelectionCost);
        llSeatSelectionCost = (LinearLayout) llpayment.findViewById(R.id.llSeatSelectionCost);
        llMealSelectionCost = (LinearLayout) llpayment.findViewById(R.id.llMealSelectionCost);
        llAirportServicesCost = (LinearLayout) llpayment.findViewById(R.id.llAirportServicesCost);
        llPersonInfoMain = (LinearLayout) llpayment.findViewById(R.id.llPersonInfoMain);
        llPassengerInfo = (LinearLayout) llpayment.findViewById(R.id.llPassengerInfo);
        llPaymentInfo = (LinearLayout) llpayment.findViewById(R.id.llPaymentInfo);
        llContactInfo = (LinearLayout) llpayment.findViewById(R.id.llContactInfo);

        llOutSrcDest = (LinearLayout) llpayment.findViewById(R.id.llOutSrcDest);
        llInSrcDest = (LinearLayout) llpayment.findViewById(R.id.llInSrcDest);
        llOutFilghtDetails = (LinearLayout) llpayment.findViewById(R.id.llOutFilghtDetails);
        llInFilghtDetails = (LinearLayout) llpayment.findViewById(R.id.llInFilghtDetails);

        if (!isModifiedBook) {
            AppConstants.bookingFlightDO = new BookingFlightDO();
        }

        if (isManageBook)
            btnBack.setVisibility(View.VISIBLE);
        else
            btnBack.setVisibility(View.INVISIBLE);

        setTypeFaceSemiBold(llpayment);

        isOutBoundClick = false;
        isInBoundClick = false;
    }

    @Override
    public void bindingControl() {

        if (isManageBook) {
            llNote.setVisibility(View.GONE);
            tvImportantNote.setVisibility(View.GONE);
            tvPaymentImpNote.setVisibility(View.GONE);
            tvPaymentImpNoteDesk.setVisibility(View.GONE);
            tvPaymentImpNoteDesk2.setVisibility(View.GONE);
            if (isModify) {
                tvPaymentSumModifySegment.setVisibility(View.VISIBLE);
                ivOutboundCheck.setVisibility(View.VISIBLE);
                ivInboundCheck.setVisibility(View.VISIBLE);
            }
            llPaymentSumReservationDate.setVisibility(View.GONE);
            llPaymentSumAmountPaid.setVisibility(View.GONE);
            llPaymentSumStatus.setVisibility(View.VISIBLE);
            llTotalPaid.setVisibility(View.VISIBLE);
            btnSubmitNext.setVisibility(View.VISIBLE);
            tvPaymentSumPolicyCode.setVisibility(View.VISIBLE);
            llPaymentSumPolicyCode.setVisibility(View.VISIBLE);
            if (isCancel) {
                llPassengerInfo.setVisibility(View.GONE);
                llPaymentInfo.setVisibility(View.GONE);
                llContactInfo.setVisibility(View.GONE);
            }
            updateModifyBookingClick();
        } else {
            tvImportantNote.setVisibility(View.GONE);
            tvPaymentImpNote.setVisibility(View.VISIBLE);
            tvPaymentImpNoteDesk.setVisibility(View.VISIBLE);
            tvPaymentImpNoteDesk2.setVisibility(View.VISIBLE);
            tvPaymentSumModifySegment.setVisibility(View.GONE);
            llPaymentSumReservationDate.setVisibility(View.VISIBLE);
            llPaymentSumAmountPaid.setVisibility(View.VISIBLE);
            llPaymentSumStatus.setVisibility(View.GONE);
            llTotalPaid.setVisibility(View.GONE);
            ivOutboundCheck.setVisibility(View.GONE);
            ivInboundCheck.setVisibility(View.GONE);
            btnSubmitNext.setVisibility(View.VISIBLE);
            tvPaymentSumPolicyCode.setVisibility(View.GONE);
            llPaymentSumPolicyCode.setVisibility(View.GONE);
        }

        if (airBookDO != null && airBookDO.vecPaymentDO != null
                && airBookDO.vecPaymentDO.size() > 0) {

            vecFlightSegmentDOsOutBound = new Vector<FlightSegmentDO>();
            vecFlightSegmentDOsInBound = new Vector<FlightSegmentDO>();
            Vector<OriginDestinationOptionDO> vecOriginDestinationOptionDOs = new Vector<OriginDestinationOptionDO>();

            if (isManageBook && !isModifiedBook) {

                for (OriginDestinationOptionDO originDestinationOptionDO : airBookDO.vecOriginDestinationOptionDOs) {
//					if (originDestinationOptionDO.vecFlightSegmentDOs.get(0).status.equalsIgnoreCase("35")
//							&& vecOriginDestinationOptionDOs.size() < 2)
                    vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
                }
            } else if (!isManageBook && isModifiedBook) {
                for (OriginDestinationOptionDO originDestinationOptionDO : airBookDO.vecOriginDestinationOptionDOs) {
//					if (originDestinationOptionDO.vecFlightSegmentDOs.get(0).status.equalsIgnoreCase("35"))
                    vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
                }
//				for (OriginDestinationOptionDO originDestinationOptionDO : airBookDO.vecOriginDestinationOptionDOs) {
//					if (originDestinationOptionDO.vecFlightSegmentDOs.get(0).status.equalsIgnoreCase("35")) {
//						if (isOutBound) {
//							for (OriginDestinationOptionDO originDestinationOptionDOMod : AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs) {
//								if (originDestinationOptionDOMod.vecFlightSegmentDOs.get(0).flightNumber
//										.equalsIgnoreCase(originDestinationOptionDO.vecFlightSegmentDOs.get(0).flightNumber)
//										&& vecOriginDestinationOptionDOs.size() < 1)
//									vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//							}
//						} else {
//							for (OriginDestinationOptionDO originDestinationOptionDOMod : AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs) {
//								if (originDestinationOptionDOMod.vecFlightSegmentDOs.get(0).flightNumber
//										.equalsIgnoreCase(originDestinationOptionDO.vecFlightSegmentDOs.get(0).flightNumber)
//										&& vecOriginDestinationOptionDOs.size() < 1)
//									vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
//							}
//						}
//					}
//				}
            } else {
                for (OriginDestinationOptionDO originDestinationOptionDO : airBookDO.vecOriginDestinationOptionDOs) {
//					if (originDestinationOptionDO.vecFlightSegmentDOs.get(0).status.equalsIgnoreCase("35"))
                    vecOriginDestinationOptionDOs.add(originDestinationOptionDO);
                }
            }

            if (vecOriginDestinationOptionDOs != null
                    && vecOriginDestinationOptionDOs.size() > 0) {
//				if (vecOriginDestinationOptionDOs.size() > 1) 
                if (vecOriginDestinationOptionDOs.size() >= 1) {
                    AppConstants.BookingComment = vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.get(0).comment;
                    AppConstants.BookingStatus = vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.get(0).status;
                    FlightSegmentDO flightSegmentDO1 = null;

                    if (vecOriginDestinationOptionDOs.size() == 4 || (vecOriginDestinationOptionDOs.size() == 2 && airBookDO.vecSeatRequestDOs.size() == 1)) {
                        vecFlightSegmentDOsOutBound.addAll(vecOriginDestinationOptionDOs.get(1).vecFlightSegmentDOs);
                        mODIDO.vecOriginDestinationOptionDOs.add(vecOriginDestinationOptionDOs.get(1));
                        flightSegmentDO1 = vecOriginDestinationOptionDOs.get(1).vecFlightSegmentDOs.get(0);
                    } else if (vecOriginDestinationOptionDOs.size() == 1 || (vecOriginDestinationOptionDOs.size() == 2 && airBookDO.vecSeatRequestDOs.size() == 2)) {
                        vecFlightSegmentDOsOutBound.addAll(vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs);
                        mODIDO.vecOriginDestinationOptionDOs.add(vecOriginDestinationOptionDOs.get(0));
                        flightSegmentDO1 = vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.get(0);
                    }


//					int lastFlightCount = vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.size()-1;
//					FlightSegmentDO flightSegmentDO2 = vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs.get(lastFlightCount);
                    if(flightSegmentDO1!=null) {
                        mODIDO.departureDateTime = flightSegmentDO1.departureDateTime;
                        mODIDO.arrivalDateTime = flightSegmentDO1.arrivalDateTime;
                        mODIDO.originLocationCode = flightSegmentDO1.departureAirportCode;
                        mODIDO.destinationLocationCode = flightSegmentDO1.arrivalAirportCode;
                    }

                    for (int i = 0; i < vecFlightSegmentDOsOutBound.size(); i++) {

                        FlightSegmentDO flightSegmentDO = vecFlightSegmentDOsOutBound.get(i);

                        LinearLayout llFlightDetails = (LinearLayout) getLayoutInflater().inflate(R.layout.totalprice_flight_details, null);
                        TextView tvOriginName = (TextView) llFlightDetails.findViewById(R.id.tvOriginName);
                        TextView tvDestName = (TextView) llFlightDetails.findViewById(R.id.tvDestName);

                        TextView tvFlightOriginDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginDate);
                        TextView tvFlightOriginTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginTime);
                        TextView tvFlightDestDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestDate);
                        TextView tvFlightDestTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestTime);
                        TextView tvFlightNo = (TextView) llFlightDetails.findViewById(R.id.tvFlightNo);

                        tvOriginName.setText(flightSegmentDO.departureAirportCode);
                        tvDestName.setText(flightSegmentDO.arrivalAirportCode);

                        tvFlightOriginDate.setText(CalendarUtility.
                                getDateWithNameofDayFromDate(flightSegmentDO.departureDateTime));
                        tvFlightOriginTime.setText(CalendarUtility.
                                getTimeInHourMinuteFromDate(flightSegmentDO.departureDateTime));

                        tvFlightDestDate.setText(CalendarUtility.
                                getDateWithNameofDayFromDate(flightSegmentDO.arrivalDateTime));
                        tvFlightDestTime.setText(CalendarUtility.
                                getTimeInHourMinuteFromDate(flightSegmentDO.arrivalDateTime));

                        tvFlightNo.setText(flightSegmentDO.flightNumber);

                        llOutFilghtDetails.addView(llFlightDetails);
                    }

                    updateAllowModifyBookingClick(vecFlightSegmentDOsOutBound.get(0).departureDateTime, OUT_BOUND);


                    if (vecOriginDestinationOptionDOs.size() > 1) {
                        FlightSegmentDO flightSegmentDO1R = null;
                        if (vecOriginDestinationOptionDOs.size() == 4) {
                            vecFlightSegmentDOsInBound.addAll(vecOriginDestinationOptionDOs.get(2).vecFlightSegmentDOs);

                            mODIDOReturn.vecOriginDestinationOptionDOs.add(vecOriginDestinationOptionDOs.get(2));

                            flightSegmentDO1R = vecOriginDestinationOptionDOs.get(2).vecFlightSegmentDOs.get(0);
                        } else if (vecOriginDestinationOptionDOs.size() == 2 && airBookDO.vecSeatRequestDOs.size() == 2) {
                            vecFlightSegmentDOsInBound.addAll(vecOriginDestinationOptionDOs.get(1).vecFlightSegmentDOs);

                            mODIDOReturn.vecOriginDestinationOptionDOs.add(vecOriginDestinationOptionDOs.get(1));

                            flightSegmentDO1R = vecOriginDestinationOptionDOs.get(1).vecFlightSegmentDOs.get(0);
                        }

//						int lastFlightCountR = vecOriginDestinationOptionDOs.get(1).vecFlightSegmentDOs.size()-1;
//						FlightSegmentDO flightSegmentDO2R = vecOriginDestinationOptionDOs.get(1).vecFlightSegmentDOs.get(lastFlightCountR);

                        mODIDOReturn.departureDateTime = flightSegmentDO1R.departureDateTime;
                        mODIDOReturn.arrivalDateTime = flightSegmentDO1R.arrivalDateTime;
                        mODIDOReturn.originLocationCode = flightSegmentDO1R.departureAirportCode;
                        mODIDOReturn.destinationLocationCode = flightSegmentDO1R.arrivalAirportCode;

                        for (int i = 0; i < vecFlightSegmentDOsInBound.size(); i++) {

                            FlightSegmentDO flightSegmentDO = vecFlightSegmentDOsInBound.get(i);

                            LinearLayout llFlightDetails = (LinearLayout) getLayoutInflater().inflate(R.layout.totalprice_flight_details, null);
                            TextView tvOriginName = (TextView) llFlightDetails.findViewById(R.id.tvOriginName);
                            TextView tvDestName = (TextView) llFlightDetails.findViewById(R.id.tvDestName);

                            TextView tvFlightOriginDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginDate);
                            TextView tvFlightOriginTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginTime);
                            TextView tvFlightDestDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestDate);
                            TextView tvFlightDestTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestTime);
                            TextView tvFlightNo = (TextView) llFlightDetails.findViewById(R.id.tvFlightNo);

                            tvOriginName.setText(flightSegmentDO.departureAirportCode);
                            tvDestName.setText(flightSegmentDO.arrivalAirportCode);
                            tvFlightOriginDate.setText(CalendarUtility.
                                    getDateWithNameofDayFromDate(flightSegmentDO.departureDateTime));
                            tvFlightOriginTime.setText(CalendarUtility.
                                    getTimeInHourMinuteFromDate(flightSegmentDO.departureDateTime));

                            tvFlightDestDate.setText(CalendarUtility.
                                    getDateWithNameofDayFromDate(flightSegmentDO.arrivalDateTime));
                            tvFlightDestTime.setText(CalendarUtility.
                                    getTimeInHourMinuteFromDate(flightSegmentDO.arrivalDateTime));

                            tvFlightNo.setText(flightSegmentDO.flightNumber);

                            llInFilghtDetails.addView(llFlightDetails);
                        }
                        llInSrcDest.setVisibility(View.VISIBLE);
                        updateAllowModifyBookingClick(vecFlightSegmentDOsOutBound.get(0).departureDateTime, IN_BOUND);
                    }

                } else {
                    vecFlightSegmentDOsOutBound.addAll(vecOriginDestinationOptionDOs.get(0).vecFlightSegmentDOs);
                    mODIDO.vecOriginDestinationOptionDOs.add(vecOriginDestinationOptionDOs.get(0));

                    for (int i = 0; i < vecFlightSegmentDOsOutBound.size(); i++) {

                        FlightSegmentDO flightSegmentDO = vecFlightSegmentDOsOutBound.get(i);

                        LinearLayout llFlightDetails = (LinearLayout) getLayoutInflater().inflate(R.layout.totalprice_flight_details, null);
                        TextView tvOriginName = (TextView) llFlightDetails.findViewById(R.id.tvOriginName);
                        TextView tvDestName = (TextView) llFlightDetails.findViewById(R.id.tvDestName);

                        TextView tvFlightOriginDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginDate);
                        TextView tvFlightOriginTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightOriginTime);
                        TextView tvFlightDestDate = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestDate);
                        TextView tvFlightDestTime = (TextView) llFlightDetails.findViewById(R.id.tvFlightDestTime);
                        TextView tvFlightNo = (TextView) llFlightDetails.findViewById(R.id.tvFlightNo);

                        tvOriginName.setText(flightSegmentDO.departureAirportCode);
                        tvDestName.setText(flightSegmentDO.arrivalAirportCode);

                        tvFlightOriginDate.setText(CalendarUtility.
                                getDateWithNameofDayFromDate(flightSegmentDO.departureDateTime));
                        tvFlightOriginTime.setText(CalendarUtility.
                                getTimeInHourMinuteFromDate(flightSegmentDO.departureDateTime));

                        tvFlightDestDate.setText(CalendarUtility.
                                getDateWithNameofDayFromDate(flightSegmentDO.arrivalDateTime));
                        tvFlightDestTime.setText(CalendarUtility.
                                getTimeInHourMinuteFromDate(flightSegmentDO.arrivalDateTime));

                        tvFlightNo.setText(flightSegmentDO.flightNumber);

                        llOutFilghtDetails.addView(llFlightDetails);
                    }
                }
            }

            float airFare = 0.0f;
            float taxes = 0.0f;
            float baggageSelection = 0.0f;
            float seatSelection = 0.0f;
            float mealSelection = 0.0f;
            float airportServices = 0.0f;
            float otherServices = 0.0f;

            if (airBookDO.vecPTC_FareBreakdownDOs != null
                    && airBookDO.vecPTC_FareBreakdownDOs.size() > 0) {
                for (PTC_FareBreakdownDO pTC_FareBreakdownDO : airBookDO.vecPTC_FareBreakdownDOs) {
                    for (FareDO fareDO : pTC_FareBreakdownDO.vecFees) {
                        if (fareDO.feeCode.equalsIgnoreCase("HALA"))
                            airportServices = airportServices + StringUtils.getFloat(fareDO.amount);
                        if (fareDO.feeCode.equalsIgnoreCase("SM"))
                            seatSelection = seatSelection + StringUtils.getFloat(fareDO.amount);
                        if (fareDO.feeCode.equalsIgnoreCase("ML"))
                            mealSelection = mealSelection + StringUtils.getFloat(fareDO.amount);
                        if (fareDO.feeCode.equalsIgnoreCase("BG"))
                            baggageSelection = baggageSelection + StringUtils.getFloat(fareDO.amount);
                    }
                }

                for (PTC_FareBreakdownDO pTC_FareBreakdownDO : airBookDO.vecPTC_FareBreakdownDOs) {
                    for (FareDO fareDO : pTC_FareBreakdownDO.vecTaxes) {
                        taxes = taxes + StringUtils.getFloat(fareDO.amount);
                    }
                }
            }
            if (isManageBook) {
                AppConstants.currencyConversionFactor = 1.0D;
                AppConstants.CurrencyCodeAfterExchange = airBookDO.itinTotalFareDO.baseFare.currencyCode;
                airFare = StringUtils.getFloat(airBookDO.itinTotalFareDO.baseFare.amount);

                for (FareDO fareDO : airBookDO.itinTotalFareDO.vecTaxes) {
                    taxes = taxes
                            + StringUtils.getFloat(fareDO.amount);
                }

                otherServices = StringUtils.getFloat(airBookDO.itinTotalFareDO.vecFees.get(0).amount);
            } else {
                airFare = StringUtils.getFloat(airBookDO.itinTotalFareDO.baseFare.amount);

                otherServices = StringUtils.getFloat(airBookDO.itinTotalFareDO.totalFare.amount)
                        - (airFare + taxes + airportServices + seatSelection
                        + mealSelection + baggageSelection);
            }

            tvPaymentSumReservationNumber.setText(airBookDO.bookingID);
            tvPaymentSumReservationDate.setText(CalendarUtility.getBookingTimeDate());
//			tvPaymentSumAmountPaid.setText(airBookDO.itinTotalFareDO.totalFare.currencyCode
//					+ " " + airBookDO.itinTotalFareDO.totalFare.amount);
//
//			tvAirFareCost.setText(airBookDO.itinTotalFareDO.totalFare.currencyCode
//					+ " " + StringUtils.getStringFromFloat(airFare));
//
//			tvTaxesCost.setText(airBookDO.itinTotalFareDO.totalFare.currencyCode
//					+ " " + StringUtils.getStringFromFloat(taxes));

            tvPaymentSumAmountPaid.setText(AppConstants.CurrencyCodeAfterExchange
                    + " " + updateCurrencyByFactor(airBookDO.itinTotalFareDO.totalFare.amount, 2));

            tvAirFareCost.setText(AppConstants.CurrencyCodeAfterExchange
                    + " " + updateCurrencyByFactor(airFare + "", 2));

            tvTaxesCost.setText(AppConstants.CurrencyCodeAfterExchange
                    + " " + updateCurrencyByFactor(taxes + "", 2));


            if (baggageSelection != 0.0f) {
//				tvBaggageSelectionCost.setText(airBookDO.itinTotalFareDO.totalFare.currencyCode
//						+ " "
//						+ StringUtils.getStringFromFloat(baggageSelection));
                tvBaggageSelectionCost.setText(AppConstants.CurrencyCodeAfterExchange
                        + " "
                        + updateCurrencyByFactor(baggageSelection + "", 2));
            } else {
                llBaggageSelectionCost.setVisibility(View.GONE);
            }
            if (seatSelection != 0.0f) {
//				tvSeatSelectionCost.setText(airBookDO.itinTotalFareDO.totalFare.currencyCode
//						+ " "
//						+ StringUtils.getStringFromFloat(seatSelection));
                tvSeatSelectionCost.setText(AppConstants.CurrencyCodeAfterExchange
                        + " "
                        + updateCurrencyByFactor(seatSelection + "", 2));
            } else {
                llSeatSelectionCost.setVisibility(View.GONE);
            }
            if (mealSelection != 0.0f) {
//				tvMealSelectionCost.setText(airBookDO.itinTotalFareDO.totalFare.currencyCode
//						+ " "
//						+ StringUtils.getStringFromFloat(mealSelection));

                tvMealSelectionCost.setText(AppConstants.CurrencyCodeAfterExchange
                        + " "
                        + updateCurrencyByFactor(mealSelection + "", 2));
            } else {
                llMealSelectionCost.setVisibility(View.GONE);
            }

            if (airportServices != 0.0f) {
//				tvAirportServicesCost.setText(airBookDO.itinTotalFareDO.totalFare.currencyCode
//						+ " "
//						+ StringUtils.getStringFromFloat(airportServices));

                tvAirportServicesCost.setText(AppConstants.CurrencyCodeAfterExchange
                        + " "
                        + updateCurrencyByFactor(airportServices + "", 2));
            } else {
                llAirportServicesCost.setVisibility(View.GONE);
            }

//			tvOtherTaxeFeeSurchargeCost.setText(airBookDO.itinTotalFareDO.totalFare.currencyCode
//					+ " "
//					+ StringUtils.getStringFromFloat(otherServices));

            tvOtherTaxeFeeSurchargeCost.setText(AppConstants.CurrencyCodeAfterExchange
                    + " "
                    + updateCurrencyByFactor(otherServices + "", 2));


//			tvTotalAmountCost.setText(airBookDO.itinTotalFareDO.totalFare.currencyCode
//					+ " " + airBookDO.itinTotalFareDO.totalFare.amount);
            tvTotalAmountCost.setText(AppConstants.CurrencyCodeAfterExchange
                    + " " + updateCurrencyByFactor(airBookDO.itinTotalFareDO.totalFare.amount + "", 2));

            tvName.setText(airBookDO.contactInfo.contacttitle + " "
                    + airBookDO.contactInfo.contactfirstname + " "
                    + airBookDO.contactInfo.contactlastname);

            tvCountryOfResidence.setText(airBookDO.contactInfo.countryName);

            tvMobile.setText(airBookDO.contactInfo.contactphonenumCountryCode
                    + "-" + airBookDO.contactInfo.contactphonenum);

            tvEmail.setText(airBookDO.contactInfo.contactemail);

            if (isManageBook) {
                if (airBookDO.ticketingStatus.equalsIgnoreCase("3"))
                    tvPaymentSumStatus.setText("CONFIRMED");
                llBaggageSelectionCost.setVisibility(View.GONE);
                llSeatSelectionCost.setVisibility(View.GONE);
                llMealSelectionCost.setVisibility(View.GONE);
                llAirportServicesCost.setVisibility(View.GONE);
                llTotalPaid.setVisibility(View.VISIBLE);


//				tvTotalPaid.setText(airBookDO.itinTotalFareDO.totalFare.currencyCode
//						+ " "
//						+ airBookDO.itinTotalFareDO.totalFare.amount);
                tvTotalPaid.setText(AppConstants.CurrencyCodeAfterExchange
                        + " "
                        + updateCurrencyByFactor(airBookDO.itinTotalFareDO.totalFare.amount, 2));


                if (airBookDO.vecInsuranceRequestDOs != null
                        && airBookDO.vecInsuranceRequestDOs.size() > 0) {
                    tvPaymentSumPolicyCode.setText(airBookDO.vecInsuranceRequestDOs.get(0).policyCode);
                    llPaymentSumPolicyCode.setVisibility(View.VISIBLE);
                } else {
                    llPaymentSumPolicyCode.setVisibility(View.GONE);
                }
                updatePnrInfo();
            }
            updatePassengerInfo();
        }

        tvPaymentSumModifySegment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                movetoBookFlightActivity();
            }
        });

        btnSubmitNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                trackEvent("Reservation Summary", "button_submit_clicked", "");
                if (isManageBook)
                {
                    if (isCancel)
                    {
                        requestParameterDO = new RequestParameterDO();
                        requestParameterDO.transactionIdentifier = airBookDO.requestParameterDO.transactionIdentifier;
                        pnr = airBookDO.bookingID;
                        showLoader("");
                        String tempPnrStarting = String.valueOf(pnr.charAt(0));
                        if (tempPnrStarting.equals("3") || tempPnrStarting.equals("4") || tempPnrStarting.equals("9")) {
                            requestParameterDO.srvUrlType = AppConstants.SERVICE_URL_TYPE_G9;
                        } else if (tempPnrStarting.equals("5")) {
                            requestParameterDO.srvUrlType = AppConstants.SERVICE_URL_TYPE_E5;
                        } else if (tempPnrStarting.equals("7")) {
                            requestParameterDO.srvUrlType = AppConstants.SERVICE_URL_TYPE_3O;
                        } else if (tempPnrStarting.equals("1")) {
                            requestParameterDO.srvUrlType = AppConstants.SERVICE_URL_TYPE_9P;
                        }

                        airBookDO.requestParameterDO.srvUrlType = requestParameterDO.srvUrlType;
                        if (new CommonBL(PaymentSummaryActivity.this, PaymentSummaryActivity.this).getCancelFlight(requestParameterDO, pnr, airBookDO.bookingType))
                        {

                        }
                        else
                            hideLoader();

                    }
                    else
                    {
                        onBackPressed();
                    }
                }
                else
                    clickHome();
            }
        });
        ivOutboundCheck.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isOutBoundClick) {
                    return;
                } else clickOutBoundCheck();
            }
        });
        ivInboundCheck.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isInBoundClick) {
                    return;
                } else clickInBoundCheck();
            }
        });

        if (!isManageBook) {
            if (isModifiedBook)
                callServiceModifyPnr(airBookDO);
            else
                callServiceLogPnr(airBookDO);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void updatePassengerInfo() {
        AppConstants.bookingFlightDO.myBookFlightDO.adtQty = airBookDO.adultCount;
        AppConstants.bookingFlightDO.myBookFlightDO.chdQty = airBookDO.chdCount;
        AppConstants.bookingFlightDO.myBookFlightDO.infQty = airBookDO.infCount;
        PassengerInfoDO passengerInfoDO = new PassengerInfoDO();
        PassengerInfoPersonDO passengerInfoPersonDO;

        passengerInfoDO.passengerInfoContactDO.contacttitle = airBookDO.contactInfo.contacttitle;
        passengerInfoDO.passengerInfoContactDO.contactnationality = airBookDO.contactInfo.contactnationality;
        passengerInfoDO.passengerInfoContactDO.contactlanguage = airBookDO.contactInfo.contactlanguage;
        passengerInfoDO.passengerInfoContactDO.contactcity = "";
        passengerInfoDO.passengerInfoContactDO.contactfirstname = airBookDO.contactInfo.contactfirstname;
        passengerInfoDO.passengerInfoContactDO.contactlastname = airBookDO.contactInfo.contactlastname;
        passengerInfoDO.passengerInfoContactDO.contactphonenumCountryCode = airBookDO.contactInfo.contactphonenumCountryCode;
        passengerInfoDO.passengerInfoContactDO.contactphonenum = airBookDO.contactInfo.contactphonenum;
        passengerInfoDO.passengerInfoContactDO.contactemail = airBookDO.contactInfo.contactemail;
        passengerInfoDO.passengerInfoContactDO.countryCode = airBookDO.contactInfo.countryCode;
        passengerInfoDO.passengerInfoContactDO.countryName = airBookDO.contactInfo.countryName;

        Vector<String> vecAdtTrRph = new Vector<String>();

        if (airBookDO.adultCount > 0) {
            for (int i = 0; i < AppConstants.bookingFlightDO.myBookFlightDO.adtQty; i++) {

                passengerInfoPersonDO = new PassengerInfoPersonDO();

                passengerInfoPersonDO.persontype = AppConstants.PERSON_TYPE_ADULT;
                passengerInfoPersonDO.travelerRefNumberRPHList = "A" + (i + 1);
                vecAdtTrRph.add(passengerInfoPersonDO.travelerRefNumberRPHList);

                for (int j = 0; j < airBookDO.vecAirTravelers.size(); j++) {
                    if (airBookDO.vecAirTravelers.get(j).contactRPH.split("\\$")[0]
                            .contains(passengerInfoPersonDO.travelerRefNumberRPHList)
                            && !airBookDO.vecAirTravelers.get(j).contactRPH.split("\\$")[0]
                            .contains("I")) {
                        passengerInfoPersonDO.persontitle = airBookDO.vecAirTravelers
                                .get(j).persontitle;
                        passengerInfoPersonDO.personfirstname = airBookDO.vecAirTravelers
                                .get(j).personfirstname;
                        passengerInfoPersonDO.personlastname = airBookDO.vecAirTravelers
                                .get(j).personlastname;
                        passengerInfoPersonDO.personnationality = airBookDO.vecAirTravelers
                                .get(j).personnationality;

                        LinearLayout llPersonItemSub = (LinearLayout) layoutInflater.inflate(R.layout.summary_person_info_item_sub, null);
                        llPersonInfoMain.addView(llPersonItemSub);

                        TextView tvPaymentSumAdults = (TextView) llPersonItemSub.findViewById(R.id.tvPaymentSumAdults);
                        TextView tvPaymentSumAdditionalServiceDetails = (TextView) llPersonItemSub.findViewById(R.id.tvPaymentSumAdditionalServiceDetails);

                        tvPaymentSumAdults.setText(passengerInfoPersonDO.persontitle + " "
                                + passengerInfoPersonDO.personfirstname + " "
                                + passengerInfoPersonDO.personlastname);

                        if (isManageBook) {
                            tvPaymentSumAdditionalServiceDetails.setVisibility(View.GONE);
                        } else {
                            String additionalServDet = "";
                            if (airBookDO.vecSeatRequestDOs != null
                                    && airBookDO.vecSeatRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecSeatRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.seatNumber;
                                        else
                                            additionalServDet = additionalServDet + ", " + requestDO.seatNumber;
                                    }
                                }
                            }
                            if (airBookDO.vecMealRequestDOs != null
                                    && airBookDO.vecMealRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecMealRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.mealQuantity + "x" + requestDO.mealCode;
                                        else
                                            additionalServDet = additionalServDet + ", "
                                                    + requestDO.mealQuantity + "x" + requestDO.mealCode;
                                    }
                                }
                            }

                            if (airBookDO.vecBaggageRequestDOs != null
                                    && airBookDO.vecBaggageRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecBaggageRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.baggageCode;
                                        else
                                            additionalServDet = additionalServDet + ", " + requestDO.baggageCode;
                                    }
                                }
                            }

                            if (airBookDO.vecHalaRequestDOs != null
                                    && airBookDO.vecHalaRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecHalaRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.ssrCode;
                                        else
                                            additionalServDet = additionalServDet + ", " + requestDO.ssrCode;
                                    }
                                }
                            }
                            tvPaymentSumAdditionalServiceDetails.setText(additionalServDet);
                            tvPaymentSumAdditionalServiceDetails.setVisibility(View.VISIBLE);
                        }
                    }
                }

                passengerInfoDO.vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
            }
        }
        if (airBookDO.chdCount > 0) {

            for (int i = 0; i < AppConstants.bookingFlightDO.myBookFlightDO.chdQty; i++) {

                passengerInfoPersonDO = new PassengerInfoPersonDO();

                passengerInfoPersonDO.persontype = AppConstants.PERSON_TYPE_CHILD;
                passengerInfoPersonDO.travelerRefNumberRPHList = "C"
                        + (AppConstants.bookingFlightDO.myBookFlightDO.adtQty + i + 1);

                passengerInfoPersonDO.persontitle = "Mstr";
                for (int j = 0; j < airBookDO.vecAirTravelers.size(); j++) {
                    if (airBookDO.vecAirTravelers.get(j).contactRPH.split("\\$")[0]
                            .contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                        passengerInfoPersonDO.persontitle = airBookDO.vecAirTravelers
                                .get(j).persontitle;
                        passengerInfoPersonDO.personfirstname = airBookDO.vecAirTravelers
                                .get(j).personfirstname;
                        passengerInfoPersonDO.personlastname = airBookDO.vecAirTravelers
                                .get(j).personlastname;
                        passengerInfoPersonDO.personnationality = airBookDO.vecAirTravelers
                                .get(j).personnationality;

                        LinearLayout llPersonItemSub = (LinearLayout) layoutInflater.inflate(R.layout.summary_person_info_item_sub, null);
                        llPersonInfoMain.addView(llPersonItemSub);

                        TextView tvPaymentSumAdults = (TextView) llPersonItemSub.findViewById(R.id.tvPaymentSumAdults);
                        TextView tvPaymentSumAdditionalServiceDetails = (TextView) llPersonItemSub.findViewById(R.id.tvPaymentSumAdditionalServiceDetails);

                        tvPaymentSumAdults.setText(passengerInfoPersonDO.persontitle + " "
                                + passengerInfoPersonDO.personfirstname + " "
                                + passengerInfoPersonDO.personlastname);

                        if (isManageBook) {
                            tvPaymentSumAdditionalServiceDetails.setVisibility(View.GONE);
                        } else {
                            String additionalServDet = "";
                            if (airBookDO.vecSeatRequestDOs != null
                                    && airBookDO.vecSeatRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecSeatRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.seatNumber;
                                        else
                                            additionalServDet = additionalServDet + ", " + requestDO.seatNumber;
                                    }
                                }
                            }
                            if (airBookDO.vecMealRequestDOs != null
                                    && airBookDO.vecMealRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecMealRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.mealQuantity + "x" + requestDO.mealCode;
                                        else
                                            additionalServDet = additionalServDet + ", "
                                                    + requestDO.mealQuantity + "x" + requestDO.mealCode;
                                    }
                                }
                            }

                            if (airBookDO.vecBaggageRequestDOs != null
                                    && airBookDO.vecBaggageRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecBaggageRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.baggageCode;
                                        else
                                            additionalServDet = additionalServDet + ", " + requestDO.baggageCode;
                                    }
                                }
                            }

                            if (airBookDO.vecHalaRequestDOs != null
                                    && airBookDO.vecHalaRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecHalaRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.ssrCode;
                                        else
                                            additionalServDet = additionalServDet + ", " + requestDO.ssrCode;
                                    }
                                }
                            }
                            tvPaymentSumAdditionalServiceDetails.setText(additionalServDet);
                            tvPaymentSumAdditionalServiceDetails.setVisibility(View.VISIBLE);
                        }
                    }
                }

                passengerInfoDO.vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
            }
        }
        if (airBookDO.infCount > 0) {
            for (int i = 0; i < AppConstants.bookingFlightDO.myBookFlightDO.infQty; i++) {

                passengerInfoPersonDO = new PassengerInfoPersonDO();

                passengerInfoPersonDO.persontype = AppConstants.PERSON_TYPE_INFANT;
                passengerInfoPersonDO.travelerRefNumberRPHList = "I"
                        + (AppConstants.bookingFlightDO.myBookFlightDO.adtQty
                        + AppConstants.bookingFlightDO.myBookFlightDO.chdQty + i + 1)
                        + "/" + vecAdtTrRph.get(i);

                for (int j = 0; j < airBookDO.vecAirTravelers.size(); j++) {
                    if (airBookDO.vecAirTravelers.get(j).contactRPH.split("\\$")[0]
                            .contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                        passengerInfoPersonDO.persontitle = airBookDO.vecAirTravelers
                                .get(j).persontitle;
                        passengerInfoPersonDO.personfirstname = airBookDO.vecAirTravelers
                                .get(j).personfirstname;
                        passengerInfoPersonDO.personlastname = airBookDO.vecAirTravelers
                                .get(j).personlastname;
                        passengerInfoPersonDO.personnationality = airBookDO.vecAirTravelers
                                .get(j).personnationality;
                        passengerInfoPersonDO.personDOB = airBookDO.vecAirTravelers
                                .get(j).personDOB;

                        LinearLayout llPersonItemSub = (LinearLayout) layoutInflater.inflate(R.layout.summary_person_info_item_sub, null);
                        llPersonInfoMain.addView(llPersonItemSub);

                        TextView tvPaymentSumAdults = (TextView) llPersonItemSub.findViewById(R.id.tvPaymentSumAdults);
                        TextView tvPaymentSumAdditionalServiceDetails = (TextView) llPersonItemSub.findViewById(R.id.tvPaymentSumAdditionalServiceDetails);

                        tvPaymentSumAdults.setText(passengerInfoPersonDO.persontitle + " "
                                + passengerInfoPersonDO.personfirstname + " "
                                + passengerInfoPersonDO.personlastname);

                        if (isManageBook) {
                            tvPaymentSumAdditionalServiceDetails.setVisibility(View.GONE);
                        } else {
                            String additionalServDet = "";
                            if (airBookDO.vecSeatRequestDOs != null
                                    && airBookDO.vecSeatRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecSeatRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.seatNumber;
                                        else
                                            additionalServDet = additionalServDet + ", " + requestDO.seatNumber;
                                    }
                                }
                            }
                            if (airBookDO.vecMealRequestDOs != null
                                    && airBookDO.vecMealRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecMealRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.mealQuantity + "x" + requestDO.mealCode;
                                        else
                                            additionalServDet = additionalServDet + ", "
                                                    + requestDO.mealQuantity + "x" + requestDO.mealCode;
                                    }
                                }
                            }

                            if (airBookDO.vecBaggageRequestDOs != null
                                    && airBookDO.vecBaggageRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecBaggageRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.baggageCode;
                                        else
                                            additionalServDet = additionalServDet + ", " + requestDO.baggageCode;
                                    }
                                }
                            }

                            if (airBookDO.vecHalaRequestDOs != null
                                    && airBookDO.vecHalaRequestDOs.size() > 0) {
                                for (RequestDO requestDO : airBookDO.vecHalaRequestDOs) {
                                    if (requestDO.travelerRefNumberRPHList.split("\\$")[0].contains(passengerInfoPersonDO.travelerRefNumberRPHList)) {
                                        if (additionalServDet.equalsIgnoreCase(""))
                                            additionalServDet = requestDO.ssrCode;
                                        else
                                            additionalServDet = additionalServDet + ", " + requestDO.ssrCode;
                                    }
                                }
                            }

                            if (additionalServDet.equalsIgnoreCase(""))
                                additionalServDet = "NA";
                            tvPaymentSumAdditionalServiceDetails.setText(additionalServDet);
                            tvPaymentSumAdditionalServiceDetails.setVisibility(View.VISIBLE);
                        }
                    }
                }

                passengerInfoDO.vecPassengerInfoPersonDO.add(passengerInfoPersonDO);
            }
        }
        AppConstants.bookingFlightDO.passengerInfoDO = passengerInfoDO;
    }

    private void updatePnrInfo() {
        AppConstants.bookingFlightDO.pnr = airBookDO.bookingID;
        AppConstants.bookingFlightDO.pnrType = airBookDO.bookingType;
        AppConstants.bookingFlightDO.modificationType = "";
    }

    private void clickOutBoundCheck() {
        if (!isOutBoundClick) {
            isOutBoundClick = true;
            ivOutboundCheck.setImageResource(R.drawable.check);
            isInBoundClick = false;
            ivInboundCheck.setImageResource(R.drawable.uncheck);
        } else {
            isOutBoundClick = false;
            ivOutboundCheck.setImageResource(R.drawable.uncheck);
        }
        updateModifyBookingClick();
    }

    private void clickInBoundCheck() {
        if (!isInBoundClick) {
            isInBoundClick = true;
            ivInboundCheck.setImageResource(R.drawable.check);
            isOutBoundClick = false;
            ivOutboundCheck.setImageResource(R.drawable.uncheck);
        } else {
            isInBoundClick = false;
            ivInboundCheck.setImageResource(R.drawable.uncheck);
        }
        updateModifyBookingClick();
    }

    private void updateAllowModifyBookingClick(String depTime, String filterTrip) {

        if (!airBookDO.flexiOperationsDOModify.flexiOperationCutoverTimeInMinutes.equalsIgnoreCase("")) {
            if (CalendarUtility.getHourDiffFromCurDate(depTime) < CalendarUtility.getHourFromMin(airBookDO.flexiOperationsDOModify.flexiOperationCutoverTimeInMinutes)) {
                if (filterTrip.equalsIgnoreCase(OUT_BOUND)) {
                    isOutBoundClick = true;
                } else if (filterTrip.equalsIgnoreCase(IN_BOUND)) {
                    isInBoundClick = true;
                }
            }
        } else if (CalendarUtility.getHourDiffFromCurDate(depTime) < 24) {
            if (filterTrip.equalsIgnoreCase(OUT_BOUND)) {
                isOutBoundClick = true;
            } else if (filterTrip.equalsIgnoreCase(IN_BOUND)) {
                isInBoundClick = true;
            }
        }
    }

    private void updateModifyBookingClick() {

        if (isInBoundClick || isOutBoundClick) {
            tvPaymentSumModifySegment.setEnabled(true);
            tvPaymentSumModifySegment.setTextColor(resources.getColor(R.color.white));
        } else {
            tvPaymentSumModifySegment.setTextColor(resources.getColor(R.color.hover_text));
            tvPaymentSumModifySegment.setEnabled(false);
        }
    }

    @SuppressWarnings("unchecked")
    private void updateDataToModify() {

        AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify = null;
        AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify = new Vector<OriginDestinationOptionDO>();

        AppConstants.bookingFlightDO.myODIDOOneWay = new OriginDestinationInformationDO();
        AppConstants.bookingFlightDO.myODIDOReturn = new OriginDestinationInformationDO();

        AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs = mODIDO.vecOriginDestinationOptionDOs;
        AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs = mODIDOReturn.vecOriginDestinationOptionDOs;

        Vector<OriginDestinationOptionDO> vec = (Vector<OriginDestinationOptionDO>) AppConstants.bookingFlightDO.myODIDOOneWay.vecOriginDestinationOptionDOs.clone();

        if (isOutBoundClick) {
            if (airBookDO.vecOriginDestinationOptionDOs.size() == 4 || (airBookDO.vecOriginDestinationOptionDOs.size() == 2 && airBookDO.vecSeatRequestDOs.size() == 1))

                AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.add(airBookDO.vecOriginDestinationOptionDOs.get(0));
            AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.add(vec.get(0));
            AppConstants.bookingFlightDO.isOutBoundClick = true;
        } else if (isInBoundClick) {
            if (vec.size() > 2) {
                if (airBookDO.vecOriginDestinationOptionDOs.size() == 4)
                    AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.add(airBookDO.vecOriginDestinationOptionDOs.get(3));
                AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.add(vec.get(1));
            } else {
                vec.clear();
                vec = (Vector<OriginDestinationOptionDO>) AppConstants.bookingFlightDO.myODIDOReturn.vecOriginDestinationOptionDOs.clone();
                if (airBookDO.vecOriginDestinationOptionDOs.size() == 4)
                    AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.add(airBookDO.vecOriginDestinationOptionDOs.get(3));
                AppConstants.bookingFlightDO.vecOriginDestinationOptionDOsModify.add(vec.get(0));
            }
            AppConstants.bookingFlightDO.isInBoundClick = true;
        }
    }

    private void updateOneWayData() {

        AppConstants.bookingFlightDO.myBookFlightDOReturn = null;
        AppConstants.bookingFlightDO.myODIDOReturn = null;

        AppConstants.bookingFlightDO.myBookFlightDO = new BookFlightDO();
        AppConstants.bookingFlightDO.myBookFlightDO.travelType = AppConstants.TRAVEL_TYPE_ONEWAY;
        AppConstants.bookingFlightDO.myBookFlightDO.adtQty = airBookDO.adultCount;
        AppConstants.bookingFlightDO.myBookFlightDO.chdQty = airBookDO.chdCount;
        AppConstants.bookingFlightDO.myBookFlightDO.infQty = airBookDO.infCount;

        AppConstants.bookingFlightDO.myBookFlightDO.departureDateTime = mODIDO.departureDateTime;
        AppConstants.bookingFlightDO.myBookFlightDO.departureDateTimeReturn = mODIDO.arrivalDateTime;
        AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode = mODIDO.originLocationCode;
        AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode = mODIDO.destinationLocationCode;

        AppConstants.bookingFlightDO.myODIDOOneWay = new OriginDestinationInformationDO();
        AppConstants.bookingFlightDO.myODIDOOneWay.originLocationCode = AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode;
        AppConstants.bookingFlightDO.myODIDOOneWay.destinationLocationCode = AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationCode;
        AppConstants.bookingFlightDO.myODIDOOneWay.originLocation = AppConstants.bookingFlightDO.myBookFlightDO.originLocationName;
        AppConstants.bookingFlightDO.myODIDOOneWay.destinationLocation = AppConstants.bookingFlightDO.myBookFlightDO.destinationLocationName;
        AppConstants.bookingFlightDO.requestParameterDO = airBookDO.requestParameterDO;
    }

    private void updateReturnData() {
        AppConstants.bookingFlightDO.myBookFlightDO = null;
        AppConstants.bookingFlightDO.myODIDOOneWay = null;

        AppConstants.bookingFlightDO.myBookFlightDOReturn = new BookFlightDO();

        AppConstants.bookingFlightDO.requestParameterDOReturn = airBookDO.requestParameterDO;
        AppConstants.bookingFlightDO.myBookFlightDOReturn.travelType = AppConstants.TRAVEL_TYPE_ONEWAY;
        AppConstants.bookingFlightDO.myBookFlightDOReturn.adtQty = airBookDO.adultCount;
        AppConstants.bookingFlightDO.myBookFlightDOReturn.chdQty = airBookDO.chdCount;
        AppConstants.bookingFlightDO.myBookFlightDOReturn.infQty = airBookDO.infCount;

        AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTime = mODIDOReturn.departureDateTime;
        AppConstants.bookingFlightDO.myBookFlightDOReturn.departureDateTimeReturn = mODIDOReturn.arrivalDateTime;
        AppConstants.bookingFlightDO.myBookFlightDOReturn.originLocationCode = mODIDOReturn.originLocationCode;
        AppConstants.bookingFlightDO.myBookFlightDOReturn.destinationLocationCode = mODIDOReturn.destinationLocationCode;

        AppConstants.bookingFlightDO.myODIDOReturn = new OriginDestinationInformationDO();
        AppConstants.bookingFlightDO.myODIDOReturn.originLocationCode = AppConstants.bookingFlightDO.myBookFlightDOReturn.originLocationCode;
        AppConstants.bookingFlightDO.myODIDOReturn.destinationLocationCode = AppConstants.bookingFlightDO.myBookFlightDOReturn.destinationLocationCode;
        AppConstants.bookingFlightDO.myODIDOReturn.originLocation = AppConstants.bookingFlightDO.myBookFlightDOReturn.originLocationName;
        AppConstants.bookingFlightDO.myODIDOReturn.destinationLocation = AppConstants.bookingFlightDO.myBookFlightDOReturn.destinationLocationName;
    }

    @Override
    public void onButtonYesClick(String from) {
        super.onButtonYesClick(from);
        if (from.equalsIgnoreCase("FLIGHT_BOOK"))
            clickHome();
        else if(from.equalsIgnoreCase("CANCEL_FLIGHT"))
        {
            Intent i=new Intent(PaymentSummaryActivity.this,ManageYourBookingActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void movetoBookFlightActivity() {

        updateDataToModify();

        if (isInBoundClick)
            updateReturnData();
        else
            updateOneWayData();

        AppConstants.BookingReferenceID_Id = airBookDO.bookingID;
        AppConstants.BookingReferenceID_Type = airBookDO.bookingType;
        AppConstants.flexiOperationsDOCancel = airBookDO.flexiOperationsDOCancel;
        AppConstants.flexiOperationsDOModify = airBookDO.flexiOperationsDOModify;

        Intent in = new Intent(PaymentSummaryActivity.this, BookFlightActivity.class);
//		in.putExtra(AppConstants.AIR_BOOK, airBookDO);
        startActivity(in);
    }

    private void callServiceLogPnr(AirBookDO airBookDO) {
        String selectedLanguageCode = "";
        selectedLanguageCode = SharedPrefUtils.getKeyValue(PaymentSummaryActivity.this,
                SharedPreferenceStrings.APP_PREFERENCES,
                SharedPreferenceStrings.USER_LANGUAGE);

        if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)) {
            selectedLanguageCode = "Arabic";
        } else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)) {
            selectedLanguageCode = "English";
        } else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)) {
            selectedLanguageCode = "French";
        } else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_RU)) {
            selectedLanguageCode = "Russian";
        }

        BookingModificationDO bookingModificationDO = new BookingModificationDO();

        bookingModificationDO.bookingId = "00000000-0000-0000-0000-000000000000";
        if (AppConstants.bookingFlightDO.myBookFlightDOReturn != null)
            bookingModificationDO.journeyType = "Return";
        else
            bookingModificationDO.journeyType = "OneWay";
        bookingModificationDO.departureAirport = mODIDO.originLocation;
        bookingModificationDO.arrivalAirport = mODIDO.destinationLocation;
        bookingModificationDO.amountPaid = airBookDO.itinTotalFareDO.totalFare.amount;
        bookingModificationDO.currency = airBookDO.itinTotalFareDO.totalFare.currencyCode;
        bookingModificationDO.passengerName = airBookDO.contactInfo.contacttitle + " " + airBookDO.contactInfo.contactfirstname + " " + airBookDO.contactInfo.contactlastname;
        bookingModificationDO.departureDate = mODIDO.departureDateTime;
        bookingModificationDO.pNR = airBookDO.bookingID;
        bookingModificationDO.adults = airBookDO.adultCount + "";
        bookingModificationDO.children = airBookDO.chdCount + "";
        bookingModificationDO.infants = airBookDO.infCount + "";
        bookingModificationDO.stops = stops + "";
        bookingModificationDO.flightType = AppConstants.flightType;
        bookingModificationDO.flightNumber = flightNo;
        bookingModificationDO.language = selectedLanguageCode;

        bookingModificationDO.bookedBy = "Android";
        bookingModificationDO.bookedOn = CalendarUtility.getCurrentDateLog();
        bookingModificationDO.isModified = false;

        if (new CommonBL(PaymentSummaryActivity.this, PaymentSummaryActivity.this).getLogPNR(bookingModificationDO)) {
        }
    }

    private void callServiceModifyPnr(AirBookDO airBookDO) {
        String selectedLanguageCode = "";
        selectedLanguageCode = SharedPrefUtils.getKeyValue(PaymentSummaryActivity.this,
                SharedPreferenceStrings.APP_PREFERENCES,
                SharedPreferenceStrings.USER_LANGUAGE);

        if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)) {
            selectedLanguageCode = "Arabic";
        } else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)) {
            selectedLanguageCode = "English";
        } else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)) {
            selectedLanguageCode = "French";
        } else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_RU)) {
            selectedLanguageCode = "Russian";
        }
        BookingModificationDO bookingModificationDO = new BookingModificationDO();
        bookingModificationDO.modificationId = "00000000-0000-0000-0000-000000000000";
        bookingModificationDO.modifiedOn = CalendarUtility.getCurrentDateLog();
        bookingModificationDO.modifiedBy = "Android";

        bookingModificationDO.bookingId = "00000000-0000-0000-0000-000000000000";
        if (mODIDOReturn != null) {
            bookingModificationDO.journeyType = "Return";
            bookingModificationDO.departureAirport = mODIDOReturn.originLocationCode;
            bookingModificationDO.arrivalAirport = mODIDOReturn.destinationLocationCode;
            bookingModificationDO.departureDate = mODIDOReturn.departureDateTime;
        } else {
            bookingModificationDO.journeyType = "OneWay";
            bookingModificationDO.departureAirport = mODIDO.originLocationCode;
            bookingModificationDO.arrivalAirport = mODIDO.destinationLocationCode;
            bookingModificationDO.departureDate = mODIDO.departureDateTime;
        }
        bookingModificationDO.amountPaid = airBookDO.itinTotalFareDO.totalFare.amount;
        bookingModificationDO.currency = airBookDO.itinTotalFareDO.totalFare.currencyCode;
        bookingModificationDO.passengerName = airBookDO.contactInfo.contacttitle + " " + airBookDO.contactInfo.contactfirstname + " " + airBookDO.contactInfo.contactlastname;
        bookingModificationDO.pNR = airBookDO.bookingID;
        bookingModificationDO.adults = airBookDO.adultCount + "";
        bookingModificationDO.children = airBookDO.chdCount + "";
        bookingModificationDO.infants = airBookDO.infCount + "";
        bookingModificationDO.stops = stops + "";
        bookingModificationDO.flightType = AppConstants.flightType;
        bookingModificationDO.flightNumber = flightNo;
        bookingModificationDO.language = selectedLanguageCode;

        bookingModificationDO.bookedBy = "";
        bookingModificationDO.bookedOn = "";
        bookingModificationDO.isModified = true;
        if (new CommonBL(PaymentSummaryActivity.this, PaymentSummaryActivity.this).getLogModifyPNR(bookingModificationDO)) {
        }
    }

    @Override
    public void dataRetreived(Response data) {
        if(!(data != null && data.data.equals("1")))
        {
            if(!data.isError)
            {
//                hideLoader();
//				Intent i=new Intent(PaymentSummaryActivity.this,ManageYourBookingActivity.class);
//				startActivity(i);
                AirBookDO modifiedPNR = (AirBookDO) data.data;
                if(modifiedPNR.ticketingStatus.equals("3"))
                {
                    modifiedPNR.requestParameterDO.srvUrlType = airBookDO.requestParameterDO.srvUrlType;
                    if(new CommonBL(PaymentSummaryActivity.this,PaymentSummaryActivity.this).getCancelFlightConfirm(modifiedPNR.requestParameterDO,pnr,airBookDO.bookingType))
                    {

                    }
                    else
                        hideLoader();

                }
                else
                {
                    AirBookDO cancelled_pnr = (AirBookDO) data.data;
                    if(cancelled_pnr.ticketingStatus.equals("6"))
                    {
                        hideLoader();
                        showCustomDialog(getApplicationContext(), getResources().getString(R.string.success), getResources().getString(R.string.alert_msg_flight_cancelled), getResources().getString(R.string.Ok), "", getResources().getString(R.string.Cancel_Flight));
                    }
                    else
                    {
                        hideLoader();
                        showCustomDialog(getApplicationContext(), getResources().getString(R.string.success), getResources().getString(R.string.alert_msg_flight_not_cancelled), getResources().getString(R.string.Ok), "", getResources().getString(R.string.Cancel_Flight));
                    }

                }
            }

        }
    }
}