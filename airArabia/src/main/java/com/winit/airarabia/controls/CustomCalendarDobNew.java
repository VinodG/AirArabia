package com.winit.airarabia.controls;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.SelectDateDobActivityNew;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.StringUtils;

@SuppressLint("ViewConstructor")
public class CustomCalendarDobNew extends LinearLayout {
	private Context context;
	private View selView;
	private int  mMonth, mYear, days, preDays;
	private Vector<Button> vecViewsButtons;
//	private int pevId;
	private int minWidth;
	private boolean isChild;
//	private Calendar calCur;
//	private int mDateCur, mMonthCur, mYearCur;

	public CustomCalendarDobNew(Context context, int mDate, int mMonth, int mYear, boolean isChild) {
		super(context);
		this.context = context;
		this.mMonth = mMonth;
		this.mYear = mYear;
		this.isChild = isChild;
		setOrientation(1);
//		pevId = -1;
		vecViewsButtons = new Vector<Button>();
		minWidth = (AppConstants.DEVICE_WIDTH + 6) / 7;
		
//		calCur = Calendar.getInstance();
//		
//		mDateCur = calCur.get(Calendar.DATE);
//		mMonthCur = calCur.get(Calendar.MONTH);
//		mYearCur = calCur.get(Calendar.YEAR);
	}

	public LinearLayout makeCalendar() {
		selView = null;
		
		Calendar calSel = new GregorianCalendar(mYear, mMonth, 1);
		
		Calendar calPrevMonth = new GregorianCalendar(mYear, mMonth - 1, 1);

		int totalMonth = 12;
		
		for (int k = 0; k < totalMonth; k++) {
			
			LinearLayout llSelectDate = (LinearLayout) ((BaseActivity)context).layoutInflater.inflate(R.layout.calender_month_header,null);
			TextView tvSelMonthYear = (TextView) llSelectDate.findViewById(R.id.tvSelMonthYear);
			tvSelMonthYear.setText(toTitleCase(CalendarUtility.getMonthMMM(mMonth).toString().replace(".", "")/*CalendarUtility.getMonth(mMonth)*/ )+ " "+ mYear);
			tvSelMonthYear.setTypeface(BaseActivity.typefaceHelveticaLight);
			this.addView(llSelectDate);
			
			days = calSel.getActualMaximum(Calendar.DAY_OF_MONTH);
			int dayOfWeek = calSel.get(Calendar.DAY_OF_WEEK);
			
			preDays = calPrevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			for (int date = 1; date <= days;) {
				int count = 0, j = 1;
				LinearLayout llHorizontal = new LinearLayout(context);
				for (int i = 1; i < dayOfWeek; i++) {
					count++;
					llHorizontal.addView(addButtonWithOutDate((preDays
							- (dayOfWeek - 1) + i)), new LayoutParams(minWidth,
									minWidth));
				}
				for (int i = count; i < 7; i++, count++) {
					if (date > days)
						break;
					llHorizontal.addView(addButtonWithDate(date++,mMonth,mYear),
							new LayoutParams(minWidth, minWidth));
				}
				for (int i = count; i < 7; i++) {
					llHorizontal.addView(addButtonWithOutDate(j++),
							new LayoutParams(minWidth, minWidth));
				}
				dayOfWeek = 0;
				BaseActivity.setTypefaceHelveticaLight(llHorizontal);
				tvSelMonthYear.setTypeface(BaseActivity.typefaceHelveticaLight);
				this.addView(llHorizontal);
			}
			
			if(k < totalMonth-1)
			{
				mMonth++;
				
				calSel.add(Calendar.MONTH, 1);
				calPrevMonth.add(Calendar.MONTH, 1);
				
				mMonth = calSel.get(Calendar.MONTH) ;
				mYear = calSel.get(Calendar.YEAR) ;
			}
		}
		
		return this;
	}

	public Button addButtonWithDate(int date,int month,int year) {
		final Button btnDate = new Button(context);
		btnDate.setId(date);
		btnDate.setTag(date + "");
		btnDate.setText(date + "");
		btnDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		btnDate.setPadding(0, 1, 0, 0);
//		btnDate.setGravity(Gravity.CENTER);
		btnDate.setTypeface(BaseActivity.typefaceHelveticaLight);
		btnDate.setBackgroundResource(R.drawable.cal_bg_unselected);
		btnDate.setTextColor(Color.parseColor("#414042"));
		vecViewsButtons.add(btnDate);
		
		String tagDate = date+"-"+month+"-"+year;
		btnDate.setTag(tagDate);

		btnDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getViewClicked(v);
			}
		});
		
		/* Start Checking for applicable date limitation*/
		if (year < SelectDateDobActivityNew.asDate) {
			btnDate.setTextColor(getResources().getColor(R.color.cal_unsel_btn_text));
			btnDate.setClickable(false);
		}
		else if (year == SelectDateDobActivityNew.asYear 
				&& month < SelectDateDobActivityNew.asMonth) {
			btnDate.setTextColor(getResources().getColor(R.color.cal_unsel_btn_text));
			btnDate.setClickable(false);
		}
		else if (year == SelectDateDobActivityNew.asYear 
				&& month == SelectDateDobActivityNew.asMonth 
				&& date < SelectDateDobActivityNew.asDate) {
			btnDate.setTextColor(getResources().getColor(R.color.cal_unsel_btn_text));
			btnDate.setClickable(false);
		}
		/* End Checking for applicable date limitation*/
		
		/* Start Checking for future date limitation*/
		else if (year > SelectDateDobActivityNew.aeYear) {
			btnDate.setTextColor(getResources().getColor(R.color.cal_unsel_btn_text));
			btnDate.setClickable(false);
		}
		else if (year == SelectDateDobActivityNew.aeYear 
				&& month > SelectDateDobActivityNew.aeMonth) {
			btnDate.setTextColor(getResources().getColor(R.color.cal_unsel_btn_text));
			btnDate.setClickable(false);
		}
		else if (year == SelectDateDobActivityNew.aeYear
				&& month == SelectDateDobActivityNew.aeMonth
				&& date > SelectDateDobActivityNew.aeDate) {
			btnDate.setTextColor(getResources().getColor(R.color.cal_unsel_btn_text));
			btnDate.setClickable(false);
		}
		/* End Checking for future date limitation*/
		/* Checking for current date */
		else if (SelectDateDobActivityNew.sDate == date
				&& SelectDateDobActivityNew.sMonth == mMonth
				&& SelectDateDobActivityNew.sYear == mYear) {
			btnDate.setTextColor(getResources().getColor(R.color.white));
			btnDate.setBackgroundResource(R.drawable.bluecal);
			selView = btnDate;
			getViewClicked(btnDate);
		} else {
			btnDate.setTextColor(getResources().getColor(R.color.text_color));
		}
		return btnDate;
	}

	public Button addButtonWithOutDate(int prevDate) {
		Button btnEmptyDate = new Button(context);
		btnEmptyDate.setId(prevDate);
		btnEmptyDate.setTag(prevDate + "");
		btnEmptyDate.setText("");
		btnEmptyDate.setTypeface(BaseActivity.typefaceHelveticaLight);
		btnEmptyDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		btnEmptyDate.setPadding(0, 1, 0, 0);
//		btnEmptyDate.setGravity(Gravity.CENTER);
		btnEmptyDate.setTextColor(Color.parseColor("#414042"));
		btnEmptyDate.setBackgroundResource(R.drawable.cal_bg_unselected);
		return btnEmptyDate;
	}

	private void getViewClicked(final View v) {
		Button button = (Button) v;

		String strDateBtn = (String) ((Button)v).getTag();
		String strDateOldSel = "";
		
		if (selView != null)
			strDateOldSel = (String) ((Button)selView).getTag();
		
		if (!strDateBtn.equals(strDateOldSel)) {
			if (v.getId() != -1) {
				if (selView != null) {
					selView.setBackgroundResource(0);
					((Button)selView).setTextColor(getResources().getColor(R.color.text_color));
				}

				selView = (Button) v;

				selView.setBackgroundResource(R.drawable.bluecal);
				((Button)selView).setTextColor(getResources().getColor(R.color.white));
			}
//			pevId = v.getId();
		}
		
		int selDate = StringUtils.getInt(button.getText().toString());
		
		String strDate = (String) button.getTag();
		int monthNew = StringUtils.getInt(strDate.split("-")[1]);
		int yesrNew = StringUtils.getInt(strDate.split("-")[2]);
		
		Calendar calNew = Calendar.getInstance();
		calNew.set(yesrNew, monthNew, selDate);
		
		SelectDateDobActivityNew.sDate = selDate;
		SelectDateDobActivityNew.sMonth = monthNew;
		SelectDateDobActivityNew.sYear = yesrNew;
		SelectDateDobActivityNew.selCalendar = calNew;
	}
	
	public static String toTitleCase(String givenString) {
		String[] arr = givenString.split(" ");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < arr.length; i++) {
			sb.append(Character.toUpperCase(arr[i].charAt(0)))
					.append(arr[i].substring(1)).append(" ");
		}
		return sb.toString().trim();
	}
}