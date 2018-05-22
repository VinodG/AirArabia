package com.winit.airarabia;

import java.util.Vector;

import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.AirBaggageDetailsDO;
import com.winit.airarabia.objects.AirBookDO;
import com.winit.airarabia.objects.BookingFlightDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.OriginDestinationInformationDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.webaccess.Response;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

public class FindBooking extends BaseActivity implements DataListener{
	
	private LinearLayout llFindbooking;
	private LinearLayout llModify;
	private LinearLayout llViewReservation;
	private LinearLayout llCancelFlight;
	private LinearLayout llAddExtras;
	private FindBooking.BCR bcr;
	private AirBookDO airBookDO;
	private Vector<AirBaggageDetailsDO> vecAirBaggageDetailsDO;
	private boolean isServiceCalled= false;
	
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
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(bcr);
	}
	
	@Override
	public void initilize() {
		// TODO Auto-generated method stub
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		intentFilter.addAction(AppConstants.BOOK_SUCCESS);
		registerReceiver(bcr, intentFilter);
		tvHeaderTitle.setText(getString(R.string.FindBooking));
		llFindbooking = (LinearLayout) layoutInflater.inflate(R.layout.find_booking, null);
		llMiddleBase.addView(llFindbooking, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		llViewReservation=(LinearLayout) llFindbooking.findViewById(R.id.llViewReservation);
		llModify=(LinearLayout) llFindbooking.findViewById(R.id.llModify);
		llCancelFlight=(LinearLayout) llFindbooking.findViewById(R.id.llCancelFlight);

		
		Bundle extras= getIntent().getExtras();
		airBookDO = (AirBookDO) getIntent().getExtras().getSerializable(AppConstants.AIR_BOOK);
		
		llViewReservation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i=new Intent(FindBooking.this,PaymentSummaryActivity.class);
				i.putExtra(AppConstants.AIR_BOOK, airBookDO);
				i.putExtra("MANAGE_BOOKING", true);
				i.putExtra("MANAGE_VIEW",true);
				startActivity(i);				
			}
		});
		
		llModify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i=new Intent(FindBooking.this,PaymentSummaryActivity.class);
				i.putExtra(AppConstants.AIR_BOOK, airBookDO);
				i.putExtra("MANAGE_BOOKING", true);
				i.putExtra("MODIFY",true);
				startActivity(i);
			}
		});
		
		llCancelFlight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i=new Intent(FindBooking.this,PaymentSummaryActivity.class);
				i.putExtra(AppConstants.AIR_BOOK, airBookDO);
				i.putExtra("MANAGE_BOOKING", true);
				i.putExtra("CANCEL",true);
				startActivity(i);
			}
		});	
			
	}

	@Override
	public void dataRetreived(Response data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bindingControl() {
		// TODO Auto-generated method stub
		
	}	
}
