package com.winit.airarabia;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.controls.CustomCalendarDobNew;
import com.winit.airarabia.utils.CalendarUtility;

public class SelectDateDobActivityNew extends BaseActivity implements OnClickListener {

	private LinearLayout llSelectdobDate;
	private TextView tvSelMonthYear;
	private LinearLayout llcalBody;
	public static Calendar selCalendar, applicableCalEnd, applicableCalStart;
	private Calendar CalNext;
	private Calendar CalPrev;
	public static int aeDate, aeMonth, aeYear, sDate, sMonth, sYear, asDate, asMonth, asYear, cYear;
	private CustomCalendarDobNew customCalender;
	public static String calSelectedDate;
	private ImageView ivCalRightArrow, ivCalLeftArrow;
	public static int width;
	public static String mMonth, mDay;
	public static int mYear, mDate, mMonthPos;

	private TextView tvCancel,tvDone, tvSelectHeader;
	private boolean isChild;

	private SelectDateDobActivityNew.BCR bcr;

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

		if(getIntent().hasExtra(AppConstants.SEL_DATE_CHILD))
			isChild = true;

		llSelectdobDate = (LinearLayout) layoutInflater.inflate(R.layout.calenderdob, null);

		tvCancel = (TextView) llSelectdobDate.findViewById(R.id.tvCancel);
		tvSelectHeader = (TextView) llSelectdobDate.findViewById(R.id.tvSelectHeader);
		tvDone = (TextView) llSelectdobDate.findViewById(R.id.tvDone);

		tvSelMonthYear = (TextView) llSelectdobDate.findViewById(R.id.tvSelMonthYeardob);
		llcalBody = (LinearLayout) llSelectdobDate.findViewById(R.id.llcalBodydob);
		ivCalLeftArrow = (ImageView) llSelectdobDate.findViewById(R.id.ivCalLeftArrowdob);
		ivCalRightArrow = (ImageView) llSelectdobDate.findViewById(R.id.ivCalRightArrowdob);
		llMiddleBase.addView(llSelectdobDate, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		Bundle b = getIntent().getExtras();
		selCalendar = (Calendar) b.getSerializable(AppConstants.SEL_DATE);
		tvSelectHeader.setText(getString(R.string.calendar));

		applicableCalEnd = Calendar.getInstance();
		applicableCalStart = Calendar.getInstance();

		cYear = applicableCalEnd.get(Calendar.YEAR);

		//		applicableCalEnd.set(Calendar.DATE,selCalendar.get(Calendar.DATE));
		//		applicableCalEnd.set(Calendar.MONTH, selCalendar.get(Calendar.MONTH));
		//		applicableCalEnd.set(Calendar.YEAR, selCalendar.get(Calendar.YEAR));
		//		applicableCalEnd.set(Calendar.DAY_OF_YEAR,selCalendar.get(Calendar.DAY_OF_YEAR));

		if(!isChild)
			applicableCalEnd.add(Calendar.DAY_OF_MONTH, -15);
		else
			applicableCalEnd.add(Calendar.YEAR, -2);

		if(!isChild)
			applicableCalStart.add(Calendar.YEAR, - 2);
		else
			applicableCalStart.add(Calendar.YEAR, - 12);
		
		applicableCalStart.add(Calendar.DAY_OF_MONTH, +1);

		aeDate = applicableCalEnd.get(Calendar.DAY_OF_MONTH);
		aeMonth = applicableCalEnd.get(Calendar.MONTH);
		aeYear = applicableCalEnd.get(Calendar.YEAR);

		asDate = applicableCalStart.get(Calendar.DAY_OF_MONTH);
		asMonth = applicableCalStart.get(Calendar.MONTH);
		asYear = applicableCalStart.get(Calendar.YEAR);

		sDate = selCalendar.get(Calendar.DAY_OF_MONTH);
		sMonth = selCalendar.get(Calendar.MONTH);
		sYear = selCalendar.get(Calendar.YEAR);

		CalPrev = Calendar.getInstance();
		CalNext = Calendar.getInstance();
		
		setTypeFaceOpenSansLight(llSelectdobDate);
		setTypeFaceOpenSansLight(llcalBody);
		tvSelectHeader.setTypeface(typefaceOpenSansSemiBold);
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		lockDrawer("selected DOB");
	}

	@Override
	public void bindingControl() {
		CalPrev.add(Calendar.YEAR, -1);
		CalNext.add(Calendar.YEAR, 1);

//		tvSelMonthYear.setText(getFirstLetterCapital(CalendarUtility.getMonth(aeMonth) + " " + aeYear));
		tvSelMonthYear.setText(getFirstLetterCapital(getString(R.string.year_for_cal) + " " + CalendarUtility.getYear(aeYear)));
		tvSelMonthYear.setTypeface(typefaceHelveticaLight);
		showCalender(aeDate, aeMonth, aeYear,isChild);

		ivCalLeftArrow.setOnClickListener(this);
		ivCalRightArrow.setOnClickListener(this);
		btnSubmitNext.setOnClickListener(this);
		tvDone.setOnClickListener(this);

    	tvCancel.setVisibility(View.VISIBLE);

		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ivCalLeftArrowdob) {
			if (((asYear) == applicableCalEnd.get(Calendar.YEAR))) {
				ivCalLeftArrow.setClickable(true);
				ivCalLeftArrow.setEnabled(true);

			} else {
				ivCalRightArrow.setClickable(true);
				ivCalRightArrow.setEnabled(true);

				applicableCalEnd.add(Calendar.YEAR, -1);
				tvSelMonthYear.setText(getFirstLetterCapital(getString(R.string.year_for_cal)
						+ " "
						+ CalendarUtility.getYear(applicableCalEnd.get(Calendar.YEAR))));
				showCalender(0, applicableCalEnd.get(Calendar.MONTH),
						applicableCalEnd.get(Calendar.YEAR),isChild);
			}
		} else if (v.getId() == R.id.ivCalRightArrowdob) {
			if ((aeYear == applicableCalEnd.get(Calendar.YEAR))) {
				ivCalRightArrow.setClickable(true);
				ivCalRightArrow.setEnabled(true);
			} else {
				ivCalLeftArrow.setClickable(true);
				ivCalLeftArrow.setEnabled(true);
				applicableCalEnd.add(Calendar.YEAR, +1);
				tvSelMonthYear.setText(getFirstLetterCapital(getString(R.string.year_for_cal)
						+ " "
						+ CalendarUtility.getYear(applicableCalEnd.get(Calendar.YEAR))));
				showCalender(0, applicableCalEnd.get(Calendar.MONTH),
						applicableCalEnd.get(Calendar.YEAR),isChild);
			}
		} else if (v.getId() == R.id.btnSubmitNext) {

			Intent intent = new Intent();
			intent.putExtra(AppConstants.SEL_DATE, selCalendar);
			setResult(RESULT_OK, intent);
			finish();
		}else if (v.getId() == R.id.tvDone) {
			Calendar c = new GregorianCalendar();
			c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			Date d1 = c.getTime();
			Intent intent = new Intent();
			if(selCalendar.compareTo(c) == -1)
			{
				intent.putExtra(AppConstants.SEL_DATE, selCalendar);
				setResult(RESULT_OK, intent);
			}
			finish();
		}
	}

	public void showCalender(int mdate, int month, int year,boolean isChild) {
		llcalBody.removeAllViews();
		customCalender = new CustomCalendarDobNew(SelectDateDobActivityNew.this,mdate, month, year, isChild);
		llcalBody.addView(customCalender.makeCalendar());
	}
}