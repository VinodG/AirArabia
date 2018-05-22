package com.winit.airarabia;

import java.util.Calendar;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.controls.CustomCalendar;

public class SelectDateActivityNew extends BaseActivity implements OnClickListener {

	private LinearLayout llSelectDateMain,llCalendarMain;
	public Calendar mCalendarDep, mCalendarArrival;
	public int mDateDep = -1, mMonthDep = -1, mYearDep = -1, mDateArr = -1, mMonthArr = -1, mYearArr = -1;
	private CustomCalendar customCalender;
	public static String calSelectedDate;
	private TextView tvDone,tvCancel;
	public static int width;
	private String from = "";
	
	private final String ManageYourBookingActivity = "ManageYourBookingActivity";
	private final String FlightScheduleActivity = "FlightScheduleActivity";
	
	public boolean isArrClicked = false;
	
	private SelectDateActivityNew.BCR bcr;
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
		tvHeaderTitle.setText(getString(R.string.calendar));
		lltop.setVisibility(View.GONE);

		llSelectDateMain = (LinearLayout) layoutInflater.inflate(R.layout.calendar_main,null);
		llMiddleBase.addView(llSelectDateMain, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
		llCalendarMain = (LinearLayout) llSelectDateMain.findViewById(R.id.llCalendarMain);
		TextView tvSelectHeader = (TextView) llSelectDateMain.findViewById(R.id.tvSelectHeader);
		tvCancel = (TextView) llSelectDateMain.findViewById(R.id.tvCancel);
		tvDone = (TextView) llSelectDateMain.findViewById(R.id.tvDone);
		
		btnSubmitNext.setVisibility(View.GONE);
		btnSubmitNext.setText(R.string.donecal);
		
		Bundle b = getIntent().getExtras();
		if (b.containsKey(AppConstants.FROM))
			from = b.getString(AppConstants.FROM);
		
		if (b.containsKey(AppConstants.RETURN))
			isArrClicked = b.getBoolean(AppConstants.RETURN);
		
		mCalendarDep = (Calendar) b.getSerializable(AppConstants.SEL_DATE);
		if (b.containsKey(AppConstants.SEL_DATE_ARR)) {
			mCalendarArrival = (Calendar) b.getSerializable(AppConstants.SEL_DATE_ARR);
			
			mDateArr = 0;
			mMonthArr = 0;
			mYearArr = 0;
			
			if(mCalendarArrival != null)
			{
				mDateArr = mCalendarArrival.get(Calendar.DAY_OF_MONTH);
				mMonthArr = mCalendarArrival.get(Calendar.MONTH);
				mYearArr = mCalendarArrival.get(Calendar.YEAR);
			}
		}
		else
			mCalendarArrival = null;
		
		if (from.equalsIgnoreCase(ManageYourBookingActivity)
				|| from.equalsIgnoreCase(FlightScheduleActivity)) {
			tvHeaderTitle.setText(getString(R.string.Select_Date));
		}
		else
		{
			if (mCalendarArrival != null)
				tvHeaderTitle.setText(getString(R.string.Select_Date)+" "+getString(R.string.Return));
			else
				tvHeaderTitle.setText(getString(R.string.Select_Date)+" "+getString(R.string.Depart));
		}
		
		if(mCalendarDep != null)
		{
			mDateDep = mCalendarDep.get(Calendar.DAY_OF_MONTH);
			mMonthDep = mCalendarDep.get(Calendar.MONTH);
			mYearDep = mCalendarDep.get(Calendar.YEAR);
		}
		tvSelectHeader.setTypeface(typefaceOpenSansSemiBold);
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		lockDrawer("SelectDate");
	}

	@Override
	public void bindingControl() {
		
		btnSubmitNext.setOnClickListener(this);
		tvDone.setOnClickListener(this);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnBack.performClick();
			}
		});
		
		showCalender();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnSubmitNext) {
			Intent intent = new Intent();
			if(mCalendarArrival != null)
				intent.putExtra(AppConstants.SEL_DATE, mCalendarArrival);
			else
				intent.putExtra(AppConstants.SEL_DATE, mCalendarDep);
			setResult(RESULT_OK, intent);
			finish();
		} else if (v.getId() == R.id.tvDone) {
			Intent intent = new Intent();
//			if(mDateArr >= 0)
//			{
//				if(mCalendarArrival != null)
//				{
					intent.putExtra(AppConstants.SEL_DATE_ARR, mCalendarArrival);
					intent.putExtra(AppConstants.SEL_DATE_DEP, mCalendarDep);
					setResult(RESULT_OK, intent);
					finish();
//				}
//				else
//					showCustomDialog(SelectDateActivityNew.this, getString(R.string.Alert),getString(R.string.DateOfBooking), getString(R.string.Ok), "", "");
//			}
//			else
//			{
//				if(mCalendarDep != null)
//				{
//					intent.putExtra(AppConstants.SEL_DATE, mCalendarDep);
//					setResult(RESULT_OK, intent);
//					finish();
//				}
//				else
//					showCustomDialog(SelectDateActivityNew.this, getString(R.string.Alert), getString(R.string.DepartureDateErr), getString(R.string.Ok), "", "");
//			}
		}
	}

	public void showCalender() {
		
		if (mYearArr < mYearDep) {
			mCalendarArrival = null;
			 mDateArr = 0;
			 mMonthArr = 0;
			 mYearArr = 0;
		}
		else if (mMonthArr < mMonthDep && mYearArr == mYearDep) {
			mCalendarArrival = null;
			mDateArr = 0;
			 mMonthArr = 0;
			 mYearArr = 0;
		}
		else if (mDateArr < mDateDep && mMonthArr == mMonthDep && mYearArr == mYearDep) {
			mCalendarArrival = null;
			mDateArr = 0;
			 mMonthArr = 0;
			 mYearArr = 0;
		}
//		if (mCalendarArrival != null)
			customCalender = new CustomCalendar(SelectDateActivityNew.this, mDateDep, mMonthDep, mYearDep, mDateArr, mMonthArr, mYearArr);
//		else
//			customCalender = new CustomCalendar(SelectDateActivityNew.this, mDateDep, mMonthDep, mYearDep, mDateArr, mMonthArr, mYearArr, false);
		
		if (!(llCalendarMain.getChildCount()<0))
			llCalendarMain.removeAllViews();
		llCalendarMain.addView(customCalender.makeCalendar());
	}
}