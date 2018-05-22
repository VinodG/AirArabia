package com.winit.airarabia.controls;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.SelectDateActivityOneWay;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.utils.CalendarUtility;
import com.winit.airarabia.utils.StringUtils;

@SuppressLint("ViewConstructor")
public class CustomCalendarOneWay extends LinearLayout {
	private Context context;
	private View selView;
	private int mDateCur, mMonthCur, mYearCur, days, preDays, mDateDep,
			mMonthDep, mYearDep, mDateArr, mMonthArr, mYearArr, mDateLocal,
			mMonthLocal, mYearLocal;
	// private int pevId;
	private Calendar calCur;
	private int minWidth;

	public CustomCalendarOneWay(Context context, int mDateDep, int mMonthDep,
			int mYearDep, int mDateArr, int mMonthArr, int mYearArr) {
		super(context);

		this.context = context;

		calCur = Calendar.getInstance();

		mDateCur = calCur.get(Calendar.DATE);
		mMonthCur = calCur.get(Calendar.MONTH);
		mYearCur = calCur.get(Calendar.YEAR);

		mDateLocal = mDateCur;
		mMonthLocal = mMonthCur;
		mYearLocal = mYearCur;

		this.mDateDep = mDateDep;
		this.mMonthDep = mMonthDep;
		this.mYearDep = mYearDep;

		this.mDateArr = mDateArr;
		this.mMonthArr = mMonthArr;
		this.mYearArr = mYearArr;

		setOrientation(1);
		// pevId = -1;
		minWidth = (int) (((AppConstants.DEVICE_WIDTH + 6) / 7));
		// ((BaseActivity)context).lockDrawer("Calendare Activity");
	}

	public LinearLayout makeCalendar() {

		int totalMonth = 12;

		if (mDateLocal > 1)
			totalMonth = 13;

		Calendar calSel = new GregorianCalendar(mYearLocal, mMonthLocal, 1);
		Calendar calPrevMonth = new GregorianCalendar(mYearLocal,
				mMonthLocal - 1, 1);
		for (int k = 0; k < totalMonth; k++) {

			LinearLayout llSelectDate = (LinearLayout) ((BaseActivity) context).layoutInflater
					.inflate(R.layout.calender_month_header, null);
			TextView tvSelMonthYear = (TextView) llSelectDate
					.findViewById(R.id.tvSelMonthYear);
			ImageView ivHeaderBar = (ImageView) llSelectDate
					.findViewById(R.id.ivHeaderBar);

			if (Locale.getDefault().equals(
					new Locale(AppConstants.LANG_LOCAL_AR))) {
				Locale.setDefault(new Locale(AppConstants.LANG_LOCAL_EN));
				tvSelMonthYear.setText(toTitleCase(CalendarUtility
						.getMonthMMM(mMonthLocal) + " " + mYearLocal));
				Locale.setDefault(new Locale(AppConstants.LANG_LOCAL_AR));
			} else {
				tvSelMonthYear.setText(toTitleCase(CalendarUtility
						.getMonthMMM(mMonthLocal).toString().replace(".", "") + " " + mYearLocal));
			}

			tvSelMonthYear.setGravity(Gravity.CENTER);
			if (k > 0)
				ivHeaderBar.setVisibility(View.VISIBLE);
			this.addView(llSelectDate);
			BaseActivity.setTypefaceHelveticaLight(llSelectDate);
			tvSelMonthYear.setTypeface(BaseActivity.typefaceOpenSansSemiBold);
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
							minWidth + 5));
				}
				for (int i = count; i < 7; i++, count++) {
					if (date > days)
						break;
					llHorizontal.addView(
							addButtonWithDate(date++, mMonthLocal, mYearLocal),
							new LayoutParams(minWidth, minWidth));
				}
				for (int i = count; i < 7; i++) {
					llHorizontal.addView(addButtonWithOutDate(j++),
							new LayoutParams(minWidth, minWidth + 5));
				}
				dayOfWeek = 0;
				this.addView(llHorizontal);
			}
			if (k < totalMonth - 1) {
				calSel.add(Calendar.MONTH, 1);
				calPrevMonth.add(Calendar.MONTH, 1);

				mMonthLocal = calSel.get(Calendar.MONTH);
				mYearLocal = calSel.get(Calendar.YEAR);
			}
		}
		return this;
	}

	public Button addButtonWithDate(int date, int month, int year) {
		final Button btnDate = new Button(context);
		btnDate.setId(date);
		btnDate.setText(date + "");
		btnDate.setTextColor(Color.parseColor("#414042"));
		btnDate.setTypeface(BaseActivity.typefaceHelveticaLight);
		btnDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		btnDate.setPadding(0, 1, 0, 0);
		btnDate.setGravity(Gravity.CENTER);
		btnDate.setBackgroundResource(0);
		// btnDate.setBackgroundResource(R.drawable.cal_bg_unselected);

		String tagDate = date + "-" + month + "-" + year;
		btnDate.setTag(tagDate);

		btnDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getViewClicked(v);
			}
		});
		if (year < mYearCur) {
			btnDate.setTextColor(getResources().getColor(
					R.color.cal_unsel_btn_text));
			btnDate.setClickable(false);
		} else if (year == mYearCur && month < mMonthCur) {
			btnDate.setTextColor(getResources().getColor(
					R.color.cal_unsel_btn_text));
			btnDate.setClickable(false);
		} else if (year == mYearCur && month == mMonthCur && date < mDateCur) {
			btnDate.setTextColor(getResources().getColor(
					R.color.cal_unsel_btn_text));
			btnDate.setClickable(false);
		}

		else if (year == mYearCur + 1 && month == mMonthCur
				&& date > mDateCur - 1) {
			btnDate.setTextColor(getResources().getColor(
					R.color.cal_unsel_btn_text));
			btnDate.setClickable(false);
		}

		else if (mDateArr < 0 && date == mDateDep && month == mMonthDep
				&& year == mYearDep) {
			getViewClicked(btnDate);
		}
		// else if (date == mDateDep
		// && month == mMonthDep && year == mYearDep) {
		// btnDate.setBackgroundResource(R.drawable.bluecal);
		// btnDate.setTextColor(getResources().getColor(R.color.white));
		// }
		else if (mDateArr >= 0) {
			if (year < mYearDep) {
				btnDate.setTextColor(getResources().getColor(
						R.color.cal_unsel_btn_text));
				btnDate.setClickable(false);
			} else if (month < mMonthDep && year == mYearDep) {
				btnDate.setTextColor(getResources().getColor(
						R.color.cal_unsel_btn_text));
				btnDate.setClickable(false);
			} else if (date < mDateDep && month == mMonthDep
					&& year == mYearDep) {
				btnDate.setTextColor(getResources().getColor(
						R.color.cal_unsel_btn_text));
				btnDate.setClickable(false);
			} else if (date == mDateArr && month == mMonthArr
					&& year == mYearArr) {
				getViewClicked(btnDate);
			} else {
				btnDate.setTextColor(getResources()
						.getColor(R.color.text_color));
			}
		}
		return btnDate;
	}

	public Button addButtonWithOutDate(int prevDate) {
		Button btnEmptyDate = new Button(context);
		btnEmptyDate.setId(prevDate);
		btnEmptyDate.setTag(prevDate + "");
		btnEmptyDate.setText("");
		btnEmptyDate.setTextColor(Color.parseColor("#414042"));
		btnEmptyDate.setTypeface(BaseActivity.typefaceHelveticaLight);
		btnEmptyDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		btnEmptyDate.setPadding(0, 1, 0, 0);
//		btnEmptyDate.setGravity(Gravity.CENTER);
		// btnEmptyDate.setBackgroundResource(R.drawable.cal_bg_unselected);
		btnEmptyDate.setBackgroundResource(0);
		return btnEmptyDate;
	}

	private void getViewClicked(final View v) {
		Button button = (Button) v;

		String strDateBtn = (String) ((Button) v).getTag();
		String strDateOldSel = "";

		if (selView != null)
			strDateOldSel = (String) ((Button) selView).getTag();

		if (!strDateBtn.equals(strDateOldSel)) {
			if (v.getId() != -1) {
				if (selView != null) {

//					if(mDateArr >= 0)
//					{
//						int selDateOld = StringUtils.getInt(((Button)selView).getText().toString());
//						
//						int monthOld = StringUtils.getInt(strDateOldSel.split("-")[1]);
//						int yesrOld = StringUtils.getInt(strDateOldSel.split("-")[2]);
//						if (selDateOld == mDateDep 
//								&& monthOld == mMonthDep && yesrOld == mYearDep) {
//						}
//						else{
//							
//							selView.setBackgroundResource(0);
//							((Button)selView).setTextColor(getResources().getColor(R.color.text_color));
////							((Button)selView).setTypeface(BaseActivity.typefaceHelveticaLight);
//						}
//					}
//					else
//					{
						
						selView.setBackgroundResource(0);
						((Button) selView).setTextColor(getResources()
								.getColor(R.color.text_color));
//					}
				}

				selView = (Button) v;

				selView.setBackgroundResource(R.drawable.bluecal);
				((Button) selView).setTextColor(getResources().getColor(
						R.color.white));
				// ((Button)selView).setTypeface(BaseActivity.typefaceHelveticaLight);
			}
			// pevId = v.getId();
		}

		int selDate = StringUtils.getInt(button.getText().toString());

		String strDate = (String) button.getTag();
		int monthNew = StringUtils.getInt(strDate.split("-")[1]);
		int yesrNew = StringUtils.getInt(strDate.split("-")[2]);

		Calendar calNew = Calendar.getInstance();
		calNew.set(yesrNew, monthNew, selDate);

		if (mDateArr >= 0)
			((SelectDateActivityOneWay) context).mCalendarArrival = calNew;
		else
			((SelectDateActivityOneWay) context).mCalendarDep = calNew;
	}

	public String getFirstLetterCapital(String str) {
		char c = str.charAt(0);
		char[] tmp = str.toCharArray();
		c = Character.toUpperCase(c);
		tmp[0] = c;
		return String.valueOf(tmp);

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