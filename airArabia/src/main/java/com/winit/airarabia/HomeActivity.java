package com.winit.airarabia;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.squareup.picasso.Picasso;
import com.insider.android.insider.Insider;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.insider.GcmBroadcastReceiver;
import com.winit.airarabia.objects.PosterImagesDO;
import com.winit.airarabia.utils.AutoScrollViewPager;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.webaccess.Response;

public class HomeActivity extends BaseActivity implements DataListener {

	private LinearLayout llHome;
	private Intent in;
	private TextView tvBookflight, tvManagebooking, tvContactus, tvCheckinonline, tvFlightschedule, tvAirRewards;
	private boolean isClcked = false;
	private Timer timer;
	private int i = 0;
	private PosterImagesDO posterImagesDO;
	private AutoScrollViewPager mViewPager;
	// private ViewPager mViewPager;
	private CustomPagerAdapter mCustomPagerAdapter;
	private int j = 0;
	// we are going to use a handler to be able to run in our TimerTask
	private final Handler handler = new Handler();
	private ArrayList<String> imageArray = new ArrayList<String>();
	private LinearLayout llPagerTab;

	private HomeActivity.BCR bcr;

	private class BCR extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(AppConstants.LANGUAGE_CHANGE)) {
				Intent i = new Intent(HomeActivity.this, HomeActivity.class);
				finish();
				startActivity(i);
			}
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
		if (isClcked) {
			isClcked = false;
		}
		AppConstants.CurrencyCodeActual = "";
		AppConstants.Cookie = "";
		AppConstants.BookingStatus = "";
		AppConstants.BookingComment = "";
		AppConstants.BookingReferenceID_Id = "";
		AppConstants.BookingReferenceID_Type = "";
		AppConstants.flexiOperationsDOCancel = null;
		AppConstants.flexiOperationsDOModify = null;
		AppConstants.bookingFlightDO = null;
		AppConstants.vecOriginDestinationOptionDOsModify = null;

	/*	tracker.setScreenName("Home Activity");
		tracker.send(new HitBuilders.ScreenViewBuilder().build());*/
		// GoogleAnalytics.getInstance(this).dispatchLocalHits();
		// startTimer();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void initilize() {

		bcr = new BCR();
		intentFilter.addAction(AppConstants.LANGUAGE_CHANGE);
		registerReceiver(bcr, intentFilter);

		llHome = (LinearLayout) layoutInflater.inflate(R.layout.homepage, null);

		// lltop.setVisibility(View.VISIBLE);
		ivHeader.setVisibility(View.VISIBLE);
		btnBack.setVisibility(View.INVISIBLE);
		tvHeaderTitle.setVisibility(View.GONE);
		llMiddleBase.addView(llHome, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		tvBookflight = (TextView) llHome.findViewById(R.id.tv_book_flight);
		tvCheckinonline = (TextView) llHome.findViewById(R.id.tv_check_in);
		tvManagebooking = (TextView) llHome.findViewById(R.id.tv_managebooking);
		tvFlightschedule = (TextView) llHome.findViewById(R.id.tv_flightschedule);
		tvAirRewards = (TextView) llHome.findViewById(R.id.tv_airRewards);
		tvContactus = (TextView) llHome.findViewById(R.id.tv_contactus);

		setTypefaceOpenSansRegular(llHome);

		tvBookflight.setTypeface(typefaceOpenSansSemiBold);
		tvCheckinonline.setTypeface(typefaceOpenSansSemiBold);
		tvManagebooking.setTypeface(typefaceOpenSansSemiBold);
		tvFlightschedule.setTypeface(typefaceOpenSansSemiBold);
		tvAirRewards.setTypeface(typefaceOpenSansSemiBold);
		tvContactus.setTypeface(typefaceOpenSansSemiBold);
		/*
		 * ======================== View Pager
		 * ====================================
		 */

		mCustomPagerAdapter = new CustomPagerAdapter(this);
		mViewPager = (AutoScrollViewPager) llHome.findViewById(R.id.pager);
		// mViewPager = (ViewPager) llHome.findViewById(R.id.pager);

		llPagerTab = (LinearLayout) llHome.findViewById(R.id.llPagerTab);
		mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

		refreshPageController();
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int postion) {

				for (int i = 0; i <= (mCustomPagerAdapter.getCount() - 1); i++) {
					((ImageView) llPagerTab.getChildAt(i)).setImageResource(R.drawable.image_unselected);
				}
				((ImageView) llPagerTab.getChildAt(postion)).setImageResource(R.drawable.image_selected);

			}

			@Override
			public void onPageScrolled(int postion, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int postion) {

			}
		});

		/* ============================================================ */

		/// ===================Here we are calling poster Service to show poster
		/// on home screen ===================//
		if (AppConstants.imagePosterArr == null) {
			callPosterService();
		} else {
			if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)) {

				imageArray.addAll(AppConstants.imagePosterArr.arrayListAR);
			} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)) {

				imageArray.addAll(AppConstants.imagePosterArr.arrayListFR);
			} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_RU)) {

				imageArray.addAll(AppConstants.imagePosterArr.arrayListRU);
			}else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)) {

				imageArray.addAll(AppConstants.imagePosterArr.arrayListEN);
			} else {
				imageArray.addAll(AppConstants.imagePosterArr.arrayListEN);
			}
			if (imageArray != null && imageArray.size() > 0) {
				mViewPager.setAdapter(mCustomPagerAdapter);
				refreshPageController();
				startViewPager();
			}
		}
		/// =======================================================================================================//

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
		Insider.stop(this);	}

	private void refreshPageController() {
		int pagerPosition = 0;
		llPagerTab.removeAllViews();
		for (int i = 0; i <= (mCustomPagerAdapter.getCount() - 1); i++) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			final ImageView imgvPagerController = new ImageView(HomeActivity.this);
			imgvPagerController.setPadding(7, 8, 7, 8);

			imgvPagerController.setImageResource(R.drawable.image_unselected);

			llPagerTab.addView(imgvPagerController);
		}

		pagerPosition = mViewPager.getCurrentItem();

		if (((ImageView) llPagerTab.getChildAt(pagerPosition)) != null)
			((ImageView) llPagerTab.getChildAt(pagerPosition)).setImageResource(R.drawable.image_selected);

	}

	@Override
	public void bindingControl() {

		tvBookflight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isClcked) {
					Insider.tagEvent("BookFlight_button_clicked", HomeActivity.this);
					//Insider.Instance.tagEvent(HomeActivity.this,AppConstants.BookFlight_Button);
					trackEvent("DashBoard",AppConstants.BookFlight_Button,"");
					isClcked = true;
					showLoader("");
					AppConstants.Cookie = "";
					in = new Intent(HomeActivity.this, BookFlightActivity.class);
					startActivity(in);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							hideLoader();
						}
					}, 800);
				}
			}
		});

		tvManagebooking.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (!isClcked) {
//					Insider.tagEvent("ManageBooking_button_clicked", HomeActivity.this);
//					//Insider.Instance.tagEvent(HomeActivity.this,AppConstants.ManageBooking_Button);
//					trackEvent("DashBoard",AppConstants.ManageBooking_Button,"");
//					isClcked = true;
//					if(!checkLangArabic()&&!checkLangFrench()){
//						showLoader("");
//						in = new Intent(HomeActivity.this, ManageYourBookingActivity.class);
//						startActivity(in);
//						new Handler().postDelayed(new Runnable() {
//
//							@Override
//							public void run() {
//								hideLoader();
//							}
//						}, 200);
//					}else{
//						showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.under_dev),
//								getString(R.string.Ok), "", "");
//					}
//
//				}

				Intent i=new Intent(HomeActivity.this,MangageBookingWebViewActivity.class);
				startActivity(i);
			}
		});

		tvCheckinonline.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Insider.tagEvent("CheckIn_button_clicked", HomeActivity.this);
				//Insider.Instance.tagEvent(HomeActivity.this,AppConstants.CheckIn_Button);
				trackEvent("DashBoard",AppConstants.CheckIn_Button,"");
				in = new Intent(HomeActivity.this, CheckinOnlineActivity.class);
				startActivity(in);
			}
		});

		tvFlightschedule.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isClcked) {
					Insider.tagEvent("Timetable_button_clicked", HomeActivity.this);
					//Insider.Instance.tagEvent(HomeActivity.this,AppConstants.Timetable_Button);
					trackEvent("DashBoard",AppConstants.Timetable_Button,"");
					isClcked = true;
					showLoader("");
					in = new Intent(HomeActivity.this, FlightScheduleActivity.class);
					startActivity(in);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							hideLoader();
						}
					}, 200);
				}
			}
		});

		tvContactus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Insider.tagEvent("ContactUs_button_clicked", HomeActivity.this);
				//Insider.Instance.tagEvent(HomeActivity.this,AppConstants.Contact_Button);
				trackEvent("DashBoard",AppConstants.Contact_Button,"");
				Intent in = new Intent(HomeActivity.this, ReachUsActivity.class);
				startActivity(in);
			}
		});
		tvAirRewards.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Insider.tagEvent("Airewards_button_clicked", HomeActivity.this);
				//Insider.Instance.tagEvent(HomeActivity.this,AppConstants.Airewards_Button);
				trackEvent("DashBoard",AppConstants.Airewards_Button,"");
				Intent in = new Intent(HomeActivity.this, Airewards.class);
				startActivity(in);
			}
		});
	}

	@Override
	public void dataRetreived(Response data) {
		if (data != null && !data.isError) {
			switch (data.method) {
				case WS_POSTER_DOWNLOAD:
					if (data.data instanceof PosterImagesDO) {
						posterImagesDO = (PosterImagesDO) data.data;

						if (posterImagesDO != null) {
						/*
						 * Intent in = new Intent(HomeActivity.this,
						 * HomeActivity.class); startActivity(in);
						 */
							AppConstants.imagePosterArr = new PosterImagesDO();
							AppConstants.imagePosterArr = posterImagesDO;

							if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)) {

								imageArray.addAll(AppConstants.imagePosterArr.arrayListAR);
							} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)) {

								imageArray.addAll(AppConstants.imagePosterArr.arrayListFR);
							}else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_RU)) {

								imageArray.addAll(AppConstants.imagePosterArr.arrayListRU);
							} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)) {

								imageArray.addAll(AppConstants.imagePosterArr.arrayListEN);
							} else {
								imageArray.addAll(AppConstants.imagePosterArr.arrayListEN);
							}
							if (imageArray != null && imageArray.size() > 0) {
								mViewPager.setAdapter(mCustomPagerAdapter);
								refreshPageController();
								startViewPager();
							}
						}
					}
					hideLoader();

					break;
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
			}
			hideLoader();
		}

	}

	public void stoptimertask(View v) {
		// stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		if (isUserLoggedIn)
			showCustomDialog(HomeActivity.this, getResources().getString(R.string.Logoutq),
					getResources().getString(R.string.surelogout), getResources().getString(R.string.logout),
					getResources().getString(R.string.Cancel), "HomeActivity");
		else
			super.onBackPressed();
	}

	@Override
	public void onButtonYesClick(String from) {
		super.onButtonYesClick(from);
		if (from.equalsIgnoreCase("HomeActivity")) {
			clearUserInfo();
			finish();
		}
	}

	@Override
	public void onButtonNoClick(String from) {
		super.onButtonNoClick(from);
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		String selectedLanguageCodeNew = SharedPrefUtils.getKeyValue(this, SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);
		if (!selectedLanguageCode.equalsIgnoreCase(selectedLanguageCodeNew)) {

			Intent iHome = new Intent(HomeActivity.this, HomeActivity.class);
			finish();
			startActivity(iHome);
		}

	}

	private class CustomPagerAdapter extends PagerAdapter {

		Context mContext;
		LayoutInflater mLayoutInflater;
		File direct = new File(getApplication().getFilesDir().toString() + "/AirArabiaFiles/");;

		public CustomPagerAdapter(Context context) {
			mContext = context;
			mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			return imageArray.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((LinearLayout) object);
		}

		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

			ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

			if (imageArray != null && position < imageArray.size()) {
				String[] urlTemp = imageArray.get(position).split("/");
				String photoName = urlTemp[urlTemp.length - 1];
				direct = new File(getApplication().getFilesDir().toString() + "/AirArabiaFiles/" + photoName);

				if (direct != null && direct.exists()) {
					if (position == 0)
						Picasso.with(HomeActivity.this).load(direct)
								.error(getResources().getDrawable(R.drawable.dashboard_temp)).into(imageView);
					else
						Picasso.with(HomeActivity.this).load(direct)
								.error(getResources().getDrawable(R.drawable.dashboard_temp2)).into(imageView);
				} else {
					if (position == 0)
						Picasso.with(HomeActivity.this).load(imageArray.get(position))
								.error(getResources().getDrawable(R.drawable.dashboard_temp)).into(imageView);
					else
						Picasso.with(HomeActivity.this).load(imageArray.get(position))
								.error(getResources().getDrawable(R.drawable.dashboard_temp2)).into(imageView);
				}
			}

			container.addView(itemView);

			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((LinearLayout) object);
		}
	}

	private void startViewPager() {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {

				while (j <= mCustomPagerAdapter.getCount()) {
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.post(new Runnable() {
						@Override
						public void run() {
							mViewPager.setCurrentItem(j, true);
							if (j >= mCustomPagerAdapter.getCount())
								j = 0;
							else
								j++;

						}
					});

				}
			}
		};
		new Thread(runnable).start();
	}

	private void callPosterService() {
		showLoader("");
		if (new CommonBL(HomeActivity.this, HomeActivity.this).getBannerImages()) {
		} else {
			hideLoader();
			showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.InternetProblem),
					getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
		}
	}

}

class ZoomOutPageTransformer implements ViewPager.PageTransformer {
	private static final float MIN_SCALE = 0.85f;
	private static final float MIN_ALPHA = 0.5f;

	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();

		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);

		} else if (position <= 1) { // [-1,1]
			// Modify the default slide transition to shrink the page as well
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			if (position < 0) {
				view.setTranslationX(horzMargin - vertMargin / 2);
			} else {
				view.setTranslationX(-horzMargin + vertMargin / 2);
			}

			// Scale the page down (between MIN_SCALE and 1)
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);

			// Fade the page relative to its size.
			view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
			view.setAlpha(0);
		}
	}

}
