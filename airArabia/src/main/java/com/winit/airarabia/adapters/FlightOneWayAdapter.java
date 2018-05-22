package com.winit.airarabia.adapters;

import java.util.Vector;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.OriginDestinationInformationDO;
import com.winit.airarabia.objects.PTC_FareBreakdownDO;
import com.winit.airarabia.objects.PricedItineraryDO;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.StringUtils;

public class FlightOneWayAdapter extends BaseAdapter {

    private Vector<OriginDestinationInformationDO> vecFlightOriginDestinationDo;
    private Vector<PricedItineraryDO> vecForPriceShowing = null;
    private Context con;
    private int pos;
    public static boolean isFirstTimeSelected = false;
    public static Typeface typefaceOpenSansSemiBold, typeFaceOpenSansLight, typefaceOpenSansRegular;

    public FlightOneWayAdapter(Context ctx, Vector<OriginDestinationInformationDO> vecAirport2, int pos) {
        this.con = ctx;
        this.vecFlightOriginDestinationDo = vecAirport2;
        this.pos = pos;
        typefaceOpenSansSemiBold = Typeface.createFromAsset(con.getAssets(), "OpenSans-Semibold.ttf");
        typeFaceOpenSansLight = Typeface.createFromAsset(con.getAssets(), "OpenSans-Light.ttf");
        typefaceOpenSansRegular = Typeface.createFromAsset(con.getAssets(), "OpenSans-Regular.ttf");
    }


    @Override
    public int getCount() {
        if (vecFlightOriginDestinationDo != null && vecFlightOriginDestinationDo.size() > 0)
            return vecFlightOriginDestinationDo.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        OriginDestinationInformationDO originDestinationDO = vecFlightOriginDestinationDo.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(con).inflate(R.layout.select_flight_list_item_new, null);

            viewHolder.llSelectFlightMain = (LinearLayout) convertView.findViewById(R.id.llSelectFlightMain);
            viewHolder.llPriceTag = (LinearLayout) convertView.findViewById(R.id.llPriceTag);
            viewHolder.tvSelectedOriginTime = (TextView) convertView.findViewById(R.id.tvSelectedOriginTime);
            viewHolder.tvSelectedDestTime = (TextView) convertView.findViewById(R.id.tvSelectedDestTime);
            viewHolder.tvSelectedOrigin = (TextView) convertView.findViewById(R.id.tvSelectedOrigin);
            viewHolder.tvSelectedDest = (TextView) convertView.findViewById(R.id.tvSelectedDest);
            viewHolder.tvSelectedFlightType = (TextView) convertView.findViewById(R.id.tvSelectedFlightType);
            viewHolder.tvSelectedDestTime1D = (TextView) convertView.findViewById(R.id.tvSelectedDestTime1D);
            viewHolder.tvCurrencyType = (TextView) convertView.findViewById(R.id.tvCurrencyType);
            viewHolder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tvTotalPrice);

            viewHolder.ivFlightConnectType = (ImageView) convertView.findViewById(R.id.ivFlightConnectType);
            viewHolder.viewSpace5 = convertView.findViewById(R.id.viewSpace5);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        setTypeFaceSemiBold(viewHolder.llSelectFlightMain);
        viewHolder.tvSelectedFlightType.setTypeface(typeFaceOpenSansLight);
        viewHolder.llPriceTag.setVisibility(View.GONE);

        int totalFlightSeg = vecFlightOriginDestinationDo.size();

        if (totalFlightSeg > 1) {
            if (pos == position) {
                if (position == 0)
                    viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.blue_box_n);
                else if (position == totalFlightSeg - 1) {
                    viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.blue_box_n);
                } else
                    viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.blue_box_n);

                if (position == 0 && !isFirstTimeSelected && vecForPriceShowing != null) {
                    if (pos == 1)
                        viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
                    else
                        viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
                }
            } else {
                if (position == 0) {
                    if (pos == 1)
                        viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
                    else
                        viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
                } else if (position == totalFlightSeg - 1)
                    viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
                else if (position == pos - 1)
                    viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
                else
                    viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
            }
        } else {
//			if(pos == position)
//				viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_single_select);
//			else
//				viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.f_single_unselected);

            if (pos == position) {
                viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.blue_box_n);
                isFirstTimeSelected = true;
            } else {
                isFirstTimeSelected = false;
                viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);
            }
        }

//		if(totalFlightSeg == 1 && pos == 0 && position == 0 && !isFirstTimeSelected && AppConstants.bookingFlightDO.myBookFlightDOReturn!=null)
//			viewHolder.llSelectFlightMain.setBackgroundResource(R.drawable.white_box_n);

        String strTimeDip = CalendarUtility.getBookingTimeFromDate(originDestinationDO.departureDateTime);
        String strTimeArr = CalendarUtility.getBookingTimeFromDate(originDestinationDO.arrivalDateTime);

        viewHolder.tvSelectedOriginTime.setText(strTimeDip);
        viewHolder.tvSelectedDestTime.setText(strTimeArr);
        viewHolder.tvSelectedOrigin.setText(originDestinationDO.originLocationCode);
        viewHolder.tvSelectedDest.setText(originDestinationDO.destinationLocationCode);

//		if(StringUtils.getFloat(strTimeArr.split(":")[0]) < StringUtils.getFloat(strTimeDip.split(":")[0]))
//		{
//			viewHolder.tvSelectedDestTime1D.setVisibility(View.VISIBLE);
//		}
//		else
//		{
//			viewHolder.tvSelectedDestTime1D.setVisibility(View.GONE);
//		}

        int totalConnectedFlights = 0;
        if (originDestinationDO.vecOriginDestinationOptionDOs != null
                && originDestinationDO.vecOriginDestinationOptionDOs.size() > 0)
            totalConnectedFlights = originDestinationDO.vecOriginDestinationOptionDOs.size();

        if (totalConnectedFlights == 1) {
            viewHolder.ivFlightConnectType.setBackgroundResource(R.drawable.f_stops_direct);
            viewHolder.tvSelectedFlightType.setText(con.getString(R.string.direct));
        } else if (totalConnectedFlights > 1 && totalConnectedFlights <= 2) {
            viewHolder.ivFlightConnectType.setBackgroundResource(R.drawable.f_stops_1);
            viewHolder.tvSelectedFlightType.setText(totalConnectedFlights - 1 + " " + con.getString(R.string.Stop));
        } else {
            viewHolder.ivFlightConnectType.setBackgroundResource(R.drawable.f_stops_1);
            viewHolder.tvSelectedFlightType.setText("1 " + con.getString(R.string.Stops));
        }

        if (vecForPriceShowing != null && position < vecForPriceShowing.size()) {
            viewHolder.llPriceTag.setVisibility(View.VISIBLE);
            viewHolder.viewSpace5.setVisibility(View.VISIBLE);
            viewHolder.tvCurrencyType.setText(AppConstants.CurrencyCodeAfterExchange);
//			viewHolder.tvTotalPrice.setText(vecForPriceShowing.get(position).vecPTC_FareBreakdownDOs.get(0).totalFare.amount+"");

//===========Added on 20-Feb-2018 by Mukesh to calculate Total price without Admin Fee==========================================
            PTC_FareBreakdownDO fareBreakDownDos = vecForPriceShowing.get(position).vecPTC_FareBreakdownDOs.get(0);
            double adminFees = 0.0, totalAmount = 0.0;

            for (int i = 0; i < fareBreakDownDos.vecFees.size(); i++) {

                if (fareBreakDownDos.vecFees.get(i).feeCode.equalsIgnoreCase("CC/Transaction Fees"))
                    adminFees = StringUtils.getDouble(fareBreakDownDos.vecFees.get(i).amount.toString());
            }

            totalAmount = StringUtils.getDouble(fareBreakDownDos.totalFare.amount.toString());

            if (adminFees != 0.0)
                totalAmount -= adminFees;

            viewHolder.tvTotalPrice.setText(((BaseActivity) con).updateCurrencyByFactor(totalAmount + "", 0));
//=======================================================================================================================================
            ///Need to change it here
            viewHolder.ivFlightConnectType.setBackground(con.getResources().getDrawable(R.drawable.flight_connecting));

        }


        return convertView;
    }

    public static void setTypeFaceSemiBold(ViewGroup group) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button || v instanceof EditText/*etc.*/)
                ((TextView) v).setTypeface(typefaceOpenSansSemiBold);
            else if (v instanceof ViewGroup)
                setTypeFaceSemiBold((ViewGroup) v);
        }
    }

    class ViewHolder {
        LinearLayout llSelectFlightMain, llPriceTag;
        View viewSpace5;
        ImageView ivFlightConnectType;
        TextView tvSelectedOriginTime, tvSelectedDestTime, tvSelectedOrigin, tvSelectedDest, tvSelectedFlightType, tvSelectedDestTime1D,
                tvCurrencyType, tvTotalPrice;
    }


    public void refresh(Vector<OriginDestinationInformationDO> arrayListPa, int pos) {
        vecFlightOriginDestinationDo = arrayListPa;
        this.pos = pos;
        notifyDataSetChanged();
    }

    public void refresh(Vector<OriginDestinationInformationDO> arrayListPa, int pos, Vector<PricedItineraryDO> vecForPriceShowing) {
        vecFlightOriginDestinationDo = arrayListPa;
        this.pos = pos;
        this.vecForPriceShowing = new Vector<PricedItineraryDO>();
        this.vecForPriceShowing = vecForPriceShowing;
//		if(AppConstants.bookingFlightDO.myODIDOReturn == null)
//			((BaseActivity)con).showLoader();
        notifyDataSetChanged();
    }

    public void refreshPos(int pos) {
        this.pos = pos;
        notifyDataSetChanged();
        isFirstTimeSelected = true;
    }

    public Vector<OriginDestinationInformationDO> getData() {
        return vecFlightOriginDestinationDo;
    }

}