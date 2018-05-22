package com.winit.airarabia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger.LogLevel;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.controls.CustomDialog;
import com.winit.airarabia.controls.Flip3dAnimation;
import com.winit.airarabia.objects.AirportNamesDO;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.LoginDO;
import com.winit.airarabia.objects.OfficeLocationDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;
import com.winit.airarabia.objects.OtherPassengerDo;
import com.winit.airarabia.utils.AnalyticsApplication;
import com.winit.airarabia.utils.AnalyticsApplication.TrackerName;
import com.winit.airarabia.utils.GPSTracker;
import com.winit.airarabia.utils.LogUtils;
import com.winit.airarabia.utils.MathUtils;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.utils.StringUtils;
import com.winit.airarabia.webaccess.ServiceURLs;



public abstract class BaseActivity extends FragmentActivity {

	protected LinearLayout llMiddleBase, lltop;
	public LayoutInflater layoutInflater;
	public Tracker tracker, tracker2;
	public Button btnBack/* , btnSubmitNext */;
	protected LinearLayout llSubmitNext;
	protected TextView tvHeaderTitle, btnSubmitNext;
	protected Dialog dialog;
	protected Animation rotateXaxis;
	protected boolean isCancelableLoader;
	protected ImageView ivOutsideImage, ImageView01, ImageView02;
	protected CustomDialog customDialog;
	protected Resources resources;
	protected IntentFilter intentFilter;
	protected Configuration configuration;
	protected Locale locale2;
	protected String selectedLanguageCode = "";
	// protected int selectedLanguage = -1;
	public static float px;
	public String versionName;
	public String dateOfBuild = "24-June-2015";
	public String selectedNumberToCall;
	public ListView drawer_list;
	DrawerLayout drawerLayout;
	private FrameLayout flMenu;
	private ActionBarDrawerToggle mDrawerToggle;
	public ImageView ivmenu, ivHeader;
	private float lastTranslate = 0.0f;

	protected ArrayList<String> arrMenu;
	// protected ArrayList<String> arrGuestLoginMenu;
	protected Boolean isUserLoggedIn = false;

	private LinearLayout llHeader;
	private LinearLayout llFooter;

	protected AlertDialog.Builder builder;
	protected AlertDialog aDialog;

	public TextView tvUserNameLoggedIn, tvNeedToOpenInWebView;
	public SharedPreferences mPrefs;
	private LoginDO userLoginDo;
	public static Typeface typefaceOpenSansSemiBold, typeFaceOpenSansLight, typefaceOpenSansRegular,
			typefaceHelveticaLight;

	protected static int SessionTimeOut = 5;
	protected static Date previousInteraction, currentInteraction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(BaseActivity.this));
		setContentView(R.layout.base_drawer);
		// tracker = ((AnalyticsApplication)
		// this.getApplication()).getDefaultTracker();
		tracker = ((AnalyticsApplication) getApplication()).getTracker(TrackerName.GLOBAL_TRACKER);
		// tracker2 = ((AnalyticsApplication)
		// getApplication()).getTracker(TrackerName.ECOMMERCE_TRACKER);
		// GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);
//		 tracker.setScreenName(this.getClass().getName());
//		 tracker.send(new HitBuilders.ScreenViewBuilder().build());
//		 trackEvent();
		resources = getResources();
		px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());
		// if (!(BaseActivity.this instanceof SplashScreenActivity) &&
		// !(BaseActivity.this instanceof PreLogin)) {
		// setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));
		// }
		// else
		// setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.light_blue));
		// setStatusBarColor();

		updateLang();
		onConfigurationChanged(configuration);

		intentFilter = new IntentFilter();
		layoutInflater = getLayoutInflater();
		lltop = (LinearLayout) findViewById(R.id.lltop);
		llMiddleBase = (LinearLayout) findViewById(R.id.llmiddle);
		btnBack = (Button) lltop.findViewById(R.id.btn_back);
		tvHeaderTitle = (TextView) lltop.findViewById(R.id.text_headertitle);
		ivmenu = (ImageView) lltop.findViewById(R.id.ivmenu);
		ivHeader = (ImageView) lltop.findViewById(R.id.ivHeader);
		// btnSubmitNext = (Button) findViewById(R.id.btnSubmitNext);
		llSubmitNext = (LinearLayout) findViewById(R.id.llSubmit);
		btnSubmitNext = (TextView) findViewById(R.id.btnSubmitNext);
		flMenu = (FrameLayout) findViewById(R.id.baseFrame);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawer_list = (ListView) findViewById(R.id.drawer_list);
		mPrefs = this.getSharedPreferences(SharedPreferenceStrings.APP_PREFERENCES, Context.MODE_PRIVATE);

		tvHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		// btnBack.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		// btnSubmitNext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
		ivHeader.setVisibility(View.GONE);
		arrMenu = new ArrayList<String>();
		typefaceOpenSansSemiBold = Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf");
		typeFaceOpenSansLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
		typefaceOpenSansRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		typefaceHelveticaLight = Typeface.createFromAsset(getAssets(), "HelvLight_Regular.ttf");

		AppConstants.typefaceOpenSansSemiBold = Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf");
		AppConstants.typeFaceOpenSansLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
		AppConstants.typefaceOpenSansRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		AppConstants.typefaceHelveticaLight = Typeface.createFromAsset(getAssets(), "HelvLight_Regular.ttf");

		setTypeFaceSemiBold(lltop);
		setTypeFaceSemiBold(llMiddleBase);
		btnSubmitNext.setTypeface(typefaceOpenSansSemiBold);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);

		// arrGuestLoginMenu = new ArrayList<String>();

		// To Fetch either user is logged in or not
		// =================================================================================================
		if ((SharedPrefUtils.getKeyValue(BaseActivity.this, SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.isLoggedIn)).equalsIgnoreCase("1")) {

			isUserLoggedIn = true;
		} else {
			isUserLoggedIn = false;
		}
		if (isUserLoggedIn) {
			getMenuListUser();
		} else
			getMenuListGuest();
		// ==================================================================================================

		try {

			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			LogUtils.debugLog("Exception in version Code", e.getMessage());
		}

		// final UncaughtExceptionHandler defaultHandler =
		// Thread.getDefaultUncaughtExceptionHandler();
		// Thread.setDefaultUncaughtExceptionHandler(new
		// UncaughtExceptionHandler() {
		// public void uncaughtException(Thread thread, Throwable ex) {
		// PendingIntent intent =
		// PendingIntent.getActivity(getApplicationContext(), 0,
		// new Intent(getIntent()), getIntent().getFlags());
		// AlarmManager mgr = (AlarmManager)
		// getSystemService(Context.ALARM_SERVICE);
		// mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);
		// System.exit(2);
		// // defaultHandler.uncaughtException(thread, ex);
		// }
		// });

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		llHeader = (LinearLayout) layoutInflater.inflate(R.layout.drawer_menu_list_header, null);
		tvUserNameLoggedIn = (TextView) llHeader.findViewById(R.id.tvUserNameLoggedIn);
		llFooter = (LinearLayout) layoutInflater.inflate(R.layout.drawer_menu_footer, null);
		tvNeedToOpenInWebView = (TextView) llFooter.findViewById(R.id.tvNeedToOpenInWebView);
		setTypeFaceOpenSansLight(llHeader);
		setTypeFaceOpenSansLight(llFooter);
		tvUserNameLoggedIn.setTypeface(typefaceOpenSansSemiBold);
		tvNeedToOpenInWebView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.winitsoftware.com"));
				startActivity(browserIntent);

			}
		});
		drawer_list.setCacheColorHint(0);
		drawer_list.addHeaderView(llHeader);
		drawer_list.addFooterView(llFooter);
		drawer_list.setAdapter(new DrawerAdapter());

		ivmenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyBoard(v);
				// hideCustomKeyBoard();
				TopBarMenuClick();
				// ==================================================Assigning
				// Name Of Login
				// User=====================================================//
				if (isUserLoggedIn) {
					userLoginDo = new LoginDO();
					userLoginDo = (LoginDO) gettingObjecFromSharedPrerence("LoginDO");
					if (userLoginDo != null && !userLoginDo.firstName.equalsIgnoreCase("")
							&& !userLoginDo.lastName.equalsIgnoreCase("")) {
						tvUserNameLoggedIn.setText(userLoginDo.firstName + " " + userLoginDo.lastName);
					} else
						tvUserNameLoggedIn.setText("Welcome");
				} else
					tvUserNameLoggedIn.setText("Guest");
				// ====================================================================================================================================//
			}
		});
		initilize();
		bindingControl();

		// --------------------------------------------------------------------------------------

		mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.icon_home, R.string.drawer_open,
				R.string.drawer_close) {

			@SuppressLint("NewApi")
			public void onDrawerSlide(View drawerView, float slideOffset) {
				float moveFactor = (drawer_list.getWidth() * -slideOffset);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					flMenu.setTranslationX(moveFactor);
				} else

				{
					TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
					anim.setDuration(0);
					anim.setFillAfter(true);
					flMenu.startAnimation(anim);

					lastTranslate = moveFactor;
				}
			}

			/**
			 * Called when a drawer has settled in a completely closed state.
			 */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				// getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();

			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}

		};

		drawerLayout.setDrawerListener(mDrawerToggle);

		// --------------------------------------------------------------------------------------
		// ========================Drawer List on Click LIstener
		// Implementation================
		drawer_list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position - 1 >= 0 && (position - 1 < arrMenu.size())) {
					{
						int i = position - 1;

						switch (i) {
						case 0:
							if (!(BaseActivity.this instanceof HomeActivity)) {
								trackEvent("Menu Button action", "menu_button_Home_clicked", "");
								if (isUserLoggedIn) {
									Intent intent = new Intent(BaseActivity.this, HomeActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
								} else {
									Intent intent = new Intent(BaseActivity.this, PreLogin.class);
									startActivity(intent);
									clickHome();
								}
							}
							break;

						case 1:
							if (!(BaseActivity.this instanceof MyProfileActivity)) {
								trackEvent("Menu Button action", "menu_button_myProfile_clicked", "");
								if (isUserLoggedIn) {
									Intent intent = new Intent(BaseActivity.this, MyProfileActivity.class);
									startActivity(intent);
									clickHome();
								} else {
									Intent intent = new Intent(BaseActivity.this, PreLogin.class);
									startActivity(intent);
									clickHome();
								}

							}
							break;
						case 2:
							if (!(BaseActivity.this instanceof Settings)) {
								trackEvent("Menu Button action", AppConstants.MenuButtonSetting, "");
								Intent intent = new Intent(BaseActivity.this, Settings.class);
								startActivity(intent);

								clickHome();
							}
							break;
						case 3:
							if (!(BaseActivity.this instanceof BookFlightActivity)) {
								trackEvent("Menu Button action", AppConstants.MenuButtonBookFlight, "");
								Intent intent = new Intent(BaseActivity.this, BookFlightActivity.class);
								startActivity(intent);
								clickHome();
							}
							break;
						case 4:
							if (!(BaseActivity.this instanceof ManageYourBookingActivity)) {
								trackEvent("Menu Button action", AppConstants.MenuButtonManageBooking, "");
								Intent intent = new Intent(BaseActivity.this, ManageYourBookingActivity.class);
								startActivity(intent);
								clickHome();
							}
							break;
						case 5:
							if (!(BaseActivity.this instanceof CheckinOnlineActivity)) {
								trackEvent("Menu Button action", AppConstants.MenuButtonCheckIn, "");
								Intent intent = new Intent(BaseActivity.this, CheckinOnlineActivity.class);
								startActivity(intent);
								clickHome();
							}
							break;
						case 6:
							if (!(BaseActivity.this instanceof FlightScheduleActivity)) {
								trackEvent("Menu Button action", AppConstants.MenuButtonTimetable, "");
								Intent intent = new Intent(BaseActivity.this, FlightScheduleActivity.class);
								startActivity(intent);
								clickHome();
							}
							break;
						case 7:
							if (!(BaseActivity.this instanceof Airewards)) {
								trackEvent("Menu Button action", AppConstants.MenuButtonAirewards, "");
								Intent intent = new Intent(BaseActivity.this, Airewards.class);
								startActivity(intent);
								clickHome();
							}
							break;

						case 8:
							if (!(BaseActivity.this instanceof ReachUsActivity)) {
								trackEvent("Menu Button action", AppConstants.MenuButtonContactUs, "");
								Intent intent = new Intent(BaseActivity.this, ReachUsActivity.class);
								startActivity(intent);
								clickHome();
							}
							break;

//						case 9:
//							if (!(BaseActivity.this instanceof EmailUs)) {
//								trackEvent("Menu Button action", AppConstants.MenuButtonEmailUs, "");
//								Intent intent = new Intent(BaseActivity.this, Feedback.class);
//								startActivity(intent);
//								clickHome();
//							}
//							break;
						case 9:
							trackEvent("Menu Button action", AppConstants.MenuButtonLogout, "");
							Intent intent = new Intent(BaseActivity.this, PreLogin.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							clearUserInfo();
							startActivity(intent);
							break;

						default:
							break;
						}
						TopBarMenuClick();
					}
				}
			}
		});

	}



    public void trackEvent(String category, String action, String label) {
		// Build and send an Event.
		tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());

	}

	protected void clearUserInfo() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("My Prefereence", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

	private void getMenuListUser() {
		// TODO Auto-generated method stub
		// For User Menu List
		arrMenu.clear();
		arrMenu.add(getString(R.string.home));
		arrMenu.add(getString(R.string.my_profile));
		arrMenu.add(getString(R.string.settings));
		arrMenu.add(getString(R.string.BookFlight));
		arrMenu.add(getString(R.string.ManageBooking));
		arrMenu.add(getString(R.string.CheckInOnline));
		arrMenu.add(getString(R.string.TimeTable));
		arrMenu.add(getString(R.string.airewards));
		arrMenu.add(getString(R.string.ContactUs));
//		arrMenu.add(getString(R.string.email_us));
		arrMenu.add(getString(R.string.logout));

	}

	private void getMenuListGuest() {
		// TODO Auto-generated method stub
		// For User Menu List
		arrMenu.clear();
		arrMenu.add(getString(R.string.home));
		arrMenu.add(getString(R.string.my_profile));
		arrMenu.add(getString(R.string.settings));
		arrMenu.add(getString(R.string.BookFlight));
		arrMenu.add(getString(R.string.ManageBooking));
		arrMenu.add(getString(R.string.CheckInOnline));
		arrMenu.add(getString(R.string.TimeTable));
		arrMenu.add(getString(R.string.airewards));
		arrMenu.add(getString(R.string.ContactUs));
//		arrMenu.add(getString(R.string.email_us));
		// arrMenu.add(getString(R.string.logout));

	}

	public abstract void initilize();

	public abstract void bindingControl();

	public void TopBarMenuClick() {
		if (drawerLayout.isDrawerOpen(Gravity.END))
			drawerLayout.closeDrawer(Gravity.END);
		else
			drawerLayout.openDrawer(Gravity.END);
	}

	protected boolean checkLangArabic() {
		selectedLanguageCode = SharedPrefUtils.getKeyValue(this, SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);
		if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR))
			return true;
		return false;
	}

	protected boolean checkLangFrench() {
		selectedLanguageCode = SharedPrefUtils.getKeyValue(this, SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);
		if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR))
			return true;
		return false;
	}
	protected boolean checkLangRussian() {
		selectedLanguageCode = SharedPrefUtils.getKeyValue(this, SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);
		if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_RU))
			return true;
		return false;
	}

	public void showLoader(String str) {

//		if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)) {
//			str = getString(R.string.loader_msg);
//			str = "";
//		}
		runOnUiThread(new RunShowLoader(str));
	}

	/**
	 * Name: RunShowLoader Description: This is to show the loading progress
	 * dialog when some other functionality is taking place.
	 **/
	class RunShowLoader implements Runnable {
		private String strMsg;

		public RunShowLoader(String strMsg) {
			this.strMsg = strMsg;
			this.strMsg = "";
		}

		@Override
		public void run() {
			try {
				if (dialog != null && dialog.isShowing())
					dialog.dismiss();
				if (dialog == null)
					dialog = new Dialog(BaseActivity.this, R.style.Theme_Dialog_Translucent);

				dialog.setContentView(R.layout.loading);

				if (!isCancelableLoader)
					dialog.setCancelable(false);
				else
					dialog.setCancelable(true);
				dialog.show();

				ivOutsideImage = (ImageView) dialog.findViewById(R.id.ivOutsideImage);
				ImageView01 = (ImageView) dialog.findViewById(R.id.ImageView01);
				ImageView02 = (ImageView) dialog.findViewById(R.id.ImageView02);

				ImageView02.setVisibility(View.GONE);

				TextView tvLoading = (TextView) dialog.findViewById(R.id.tvLoading);
				if (!strMsg.equalsIgnoreCase("")) {
					tvLoading.setText(strMsg);
					tvLoading.setVisibility(View.VISIBLE);
				} else {
					tvLoading.setVisibility(View.GONE);
				}
				rotateXaxis = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.rotate_x_axis);
				rotateXaxis.setInterpolator(new LinearInterpolator());

				ivOutsideImage.setAnimation(rotateXaxis);
				applyRotation(0, 360);
			} catch (Exception e) {
			}
		}

		private void applyRotation(final float start, final float end) {
			BitmapDrawable bd = (BitmapDrawable) BaseActivity.this.getResources().getDrawable(R.drawable.flightloader);
			float centerY = bd.getBitmap().getHeight() / 2.0f;
			float centerX = bd.getBitmap().getWidth() / 2.0f;
			final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
			rotation.setDuration(2000);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new LinearInterpolator());
			ImageView01.startAnimation(rotation);
		}
	}

	/** Method to Show the alert dialog **/
	public void showCustomDialog(Context context, String strTitle, String strMessage, String firstBtnName,
			String secondBtnName, String from) {
		runOnUiThread(
				new RunshowCustomDialogs(context, strTitle, strMessage, firstBtnName, secondBtnName, from, false));
	}

	/**
	 * For showing Dialog message.
	 */
	class RunshowCustomDialogs implements Runnable {
		private String strTitle;// Title of the dialog
		private String strMessage;// Message to be shown in dialog
		private String firstBtnName;
		private String secondBtnName;
		private String from;
		private boolean isCancelable = false;
		private OnClickListener posClickListener;
		private OnClickListener negClickListener;

		public RunshowCustomDialogs(Context context, String strTitle, String strMessage, String firstBtnName,
				String secondBtnName, String from, boolean isCancelable) {
			// this.mContext = context;
			this.strTitle = strTitle;
			this.strMessage = strMessage;
			this.firstBtnName = firstBtnName;
			this.secondBtnName = secondBtnName;
			this.isCancelable = isCancelable;
			if (from != null)
				this.from = from;
			else
				this.from = "";

		}

		@Override
		public void run() {
			if (customDialog != null && customDialog.isShowing())
				customDialog.dismiss();

			View view = getLayoutInflater().inflate(R.layout.custom_popup, null);
			customDialog = new CustomDialog(BaseActivity.this, view, AppConstants.DEVICE_WIDTH - 40,
					LayoutParams.WRAP_CONTENT, isCancelable);
			customDialog.setCancelable(isCancelable);

			TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
			TextView tvMessage = (TextView) view.findViewById(R.id.tvMassage);
			TextView btnYes = (TextView) view.findViewById(R.id.btnYes);
			TextView btnNo = (TextView) view.findViewById(R.id.btnNo);
			View ivSep = (View) view.findViewById(R.id.ivSep);
			if (from.equalsIgnoreCase("PaymentActivityError")) {
				tvMessage.setGravity(Gravity.LEFT);
				tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				tvMessage.setText(Html.fromHtml(strMessage));

			}else{
				tvMessage.setText("" + strMessage);

			}
			tvTitle.setText("" + strTitle);
			btnYes.setText("" + firstBtnName);
			tvTitle.setTypeface(typefaceOpenSansSemiBold);
			tvMessage.setTypeface(typefaceOpenSansRegular);
			btnYes.setTypeface(typefaceOpenSansSemiBold);
			btnNo.setTypeface(typefaceOpenSansSemiBold);

			if (secondBtnName != null && !secondBtnName.equalsIgnoreCase(""))
				btnNo.setText("" + secondBtnName);
			else {
				ivSep.setVisibility(View.GONE);
				btnNo.setVisibility(View.GONE);
			}

			if (posClickListener == null)
				btnYes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						customDialog.dismiss();
						onButtonYesClick(from);
					}
				});
			else
				btnYes.setOnClickListener(posClickListener);

			if (negClickListener == null)
				btnNo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						customDialog.dismiss();
						onButtonNoClick(from);
					}
				});
			else
				btnNo.setOnClickListener(negClickListener);
			try {
				if (!customDialog.isShowing())
					customDialog.show();
			} catch (Exception e) {
			}
		}
	}

	public void onButtonYesClick(String from) {
		if (from.equalsIgnoreCase("GPS_splash")) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					String isLangSelected = SharedPrefUtils.getKeyValue(BaseActivity.this,
							SharedPreferenceStrings.APP_PREFERENCES, SharedPreferenceStrings.LANGUAGE_IS_SELECTED);
					if (isLangSelected.equalsIgnoreCase("selected")) {

						Intent in = new Intent(getApplicationContext(), PreLogin.class);
						in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(in);
						finish();
					} else {
						Intent in = new Intent(getApplicationContext(), ChooseLanguageFirstScreen.class);
						in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(in);
						finish();
					}
				}

			}, 1000);
		}

		if (from.equalsIgnoreCase("SESSION")) {

			Intent in = new Intent(getApplicationContext(), SplashScreenActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
			finish();
		}
	}

	public void onButtonNoClick(String from) {
		if (from.equalsIgnoreCase("GPS_splash")) {
			startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}
		if (from.equalsIgnoreCase("GPS_REQ")) {
			finish();
			startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}

	}

	/**
	 * Name: RunShowLoader Description: For hiding progress dialog (Loader ).
	 **/
	public void hideLoader() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (dialog != null && dialog.isShowing())
						dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void hideKeyBoard(View v) {
		if (v != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	public void clickHome() {
		Intent iHome = new Intent();
		iHome.setAction(AppConstants.HOME_CLICK);
		sendBroadcast(iHome);
		if (!(BaseActivity.this instanceof HomeActivity))
			finish();

		// Need to change, If Crash will come
		//
		//
		//
		//
		//
		//
		AppConstants.allAirportsDO = null;
		AppConstants.allAirportNamesDO = null;
		AppConstants.arrListCurrencies = null;

	}

	public void clickLanguageChange() {
		Intent iHome = new Intent();
		iHome.setAction(AppConstants.LANGUAGE_CHANGE);
		sendBroadcast(iHome);
		// finish();
	}

	public void bookComplete() {
		Intent iHome = new Intent();
		iHome.setAction(AppConstants.BOOK_SUCCESS);
		sendBroadcast(iHome);
	}

	/**
	 * Email Validation.
	 * 
	 * @param email
	 * @return
	 */
	public Matcher emailValidate(String email) {
		Matcher matcher;
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(inputStr);
		return matcher;
	}

	protected boolean updateAirportNameFromCode(String strAirportCode, Vector<AirportNamesDO> vecAirport,
			TextView textSelected) {
		String strCountryName = strAirportCode;
		boolean hasBus=false;
		for (int i = 0; i < vecAirport.size(); i++) {
			AirportNamesDO airportNamesDO = vecAirport.get(i);
			if (strAirportCode.equalsIgnoreCase(airportNamesDO.code)) {
				if (checkLangArabic())
					strCountryName = airportNamesDO.ar;
				else if (checkLangFrench())
					strCountryName = airportNamesDO.fr;
				else
				{
					strCountryName = airportNamesDO.en;
					int j;
					for(j=0; j<strCountryName.length()&&strCountryName.charAt(j)!='('; j++);
					if(j<strCountryName.length())
						hasBus=true;
					else
						hasBus=false;
				}
				if (strCountryName.equalsIgnoreCase(""))
					textSelected.setText(strAirportCode.trim());
				else
					textSelected.setText(strCountryName.trim());
				break;
			}
		}
		return hasBus;
	}

	protected String updateStrCountryName(String strCountryCode) {
		String strCountryName = strCountryCode;
		for (int i = 0; i < AppConstants.allAirportNamesDO.vecAirport.size(); i++) {
			AirportNamesDO airportNamesDO = AppConstants.allAirportNamesDO.vecAirport.get(i);
			if (strCountryCode.equalsIgnoreCase(airportNamesDO.code)) {
				if (checkLangArabic())
					strCountryName = airportNamesDO.ar;
				else if (checkLangFrench())
					strCountryName = airportNamesDO.fr;
				else
					strCountryName = airportNamesDO.en;
				break;
			}
		}
		if (strCountryName.equalsIgnoreCase(""))
			return strCountryCode;
		else
			return strCountryName;
	}

	public Vector<OfficeLocationDO> sortVector(Vector<OfficeLocationDO> vector) {
		Vector<OfficeLocationDO> vec;
		vec = vector;

		for (int i = 0; i < vec.size(); i++) {
			vec.get(0).country.toString();
		}

		return vec;
	}

	public Vector<OfficeLocationDO> sortVectorByDate(Vector<OfficeLocationDO> vector) {
		Vector<OfficeLocationDO> vec;
		vec = vector;

		for (int i = 0; i < vec.size(); i++) {
			vec.get(0).country.toString();
		}

		return vec;
	}

	public void showCallOnTouchPopup(List<String> listOfNumbers) {
		LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.popup_titles, null);
		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
		dialog.setContentView(ll);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		LinearLayout llPopupTitleMain = (LinearLayout) ll.findViewById(R.id.llPopupTitleMain);
		TextView tvTitleHeader = (TextView) ll.findViewById(R.id.tvTitleHeader);
		tvTitleHeader.setVisibility(View.VISIBLE);
		tvTitleHeader.setText("Choose Number to Dial");

		for (int i = 0; i < listOfNumbers.size(); i++) {
			LinearLayout llTitle = (LinearLayout) layoutInflater.inflate(R.layout.popup_title_item, null);

			final TextView tvTitleItem = (TextView) llTitle.findViewById(R.id.tvTitleItem);
			llPopupTitleMain.addView(llTitle);
			tvTitleItem.setText(listOfNumbers.get(i));

			tvTitleItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selectedNumberToCall = tvTitleItem.getText().toString();
					Intent dial = new Intent();
					dial.setAction("android.intent.action.DIAL");
					dial.setData(Uri.parse("tel:" + selectedNumberToCall));
					startActivity(dial);
					dialog.dismiss();
				}
			});
		}
		dialog.show();
	}

	public void lockDrawer(final String from) {
		LogUtils.errorLog("Menu Drawer Locked in ", from);
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	}

	public void toggleDrawer() {
		if (drawerLayout.isDrawerOpen(flMenu))
			drawerLayout.closeDrawer(flMenu);
		else
			drawerLayout.openDrawer(flMenu);
	}

	public void closeDrawer() {
		drawerLayout.closeDrawer(flMenu);
	}

	public void openDrawer() {
		if (drawerLayout.isDrawerOpen(Gravity.END)) {
			drawerLayout.closeDrawer(Gravity.END);
		}

	}

	@Override
	protected void onResume() {

		super.onResume();
		CheckInactivity();
		// updateLang();
		if ((SharedPrefUtils.getKeyValue(BaseActivity.this, SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.isLoggedIn)).equalsIgnoreCase("1")) {

			isUserLoggedIn = true;
		} else {
			isUserLoggedIn = false;
		}

		if (isUserLoggedIn) {
			getMenuListUser();
		} else
			getMenuListGuest();

		if (isUserLoggedIn) {
			userLoginDo = new LoginDO();
			userLoginDo = (LoginDO) gettingObjecFromSharedPrerence("LoginDO");
			if (userLoginDo != null && !userLoginDo.firstName.equalsIgnoreCase("")
					&& !userLoginDo.lastName.equalsIgnoreCase("")) {
				tvUserNameLoggedIn.setText(userLoginDo.firstName);
			} else
				tvUserNameLoggedIn.setText("Welcome");
		} else
			tvUserNameLoggedIn.setText(getString(R.string.guest_user));
	}

	private void updateLang() {
		selectedLanguageCode = SharedPrefUtils.getKeyValue(BaseActivity.this, SharedPreferenceStrings.APP_PREFERENCES,
				SharedPreferenceStrings.USER_LANGUAGE);
		configuration = getBaseContext().getResources().getConfiguration();

		if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)
				&& !configuration.locale.getLanguage().toString().equalsIgnoreCase(AppConstants.LANG_LOCAL_AR)) {
			locale2 = new Locale(AppConstants.LANG_LOCAL_AR);
			Locale.setDefault(locale2);
			configuration.locale = locale2;
			resources.updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)
				&& !configuration.locale.getLanguage().toString().equalsIgnoreCase(AppConstants.LANG_LOCAL_FR)) {
			locale2 = new Locale(AppConstants.LANG_LOCAL_FR);
			Locale.setDefault(locale2);
			configuration.locale = locale2;
			resources.updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)
				&& !configuration.locale.getLanguage().toString().equalsIgnoreCase(AppConstants.LANG_LOCAL_EN)) {
			locale2 = new Locale(AppConstants.LANG_LOCAL_EN);
			Locale.setDefault(locale2);
			configuration.locale = locale2;
			resources.updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
		}else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_RU)
				&& !configuration.locale.getLanguage().toString().equalsIgnoreCase(AppConstants.LANG_LOCAL_RU)) {
			locale2 = new Locale(AppConstants.LANG_LOCAL_RU);
			Locale.setDefault(locale2);
			configuration.locale = locale2;
			resources.updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
		}
	}

	/// ================================ Menu Drawer Adapter Class
	/// ===================================================///
	class DrawerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (arrMenu != null && arrMenu.size() > 0) {
				return arrMenu.size();
			} else
				return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arrMenu.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			ViewHolder viewHolder;
			if (convertView == null) {

				// inflate the layout
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.drawer_menu_item, null);

				// well set up the ViewHolder
				viewHolder = new ViewHolder();
				viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.tvDrawerListItem);

				// store the holder with the view.
				convertView.setTag(viewHolder);
				viewHolder.textViewItem.setTypeface(typefaceOpenSansRegular);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.textViewItem.setText(arrMenu.get(pos) + "");
			return convertView;
		}
	}

	class ViewHolder {
		TextView textViewItem;
	}
	// ===========================================================================================================//

	/// ===================================For Setting Status Bar Color
	/// ===========================================//
	private void setStatusBarColor(View statusBar, int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window w = getWindow();
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			int statusBarHeight = getStatusBarHeight();
			statusBar.getLayoutParams().height = statusBarHeight;
			statusBar.setBackgroundColor(color);
		}
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	// ===============================================================================================================///

	// =========================================Saving/Getting Login Object from
	// Shared Preference through GSON ==========================//

	public void savingObjecInSharedPrerence(LoginDO userLoginDoTemp, String PreferenceName) {
		try {
			Editor prefsEditor = mPrefs.edit();
			Gson gson = new Gson();
			String json = gson.toJson(userLoginDoTemp);
			prefsEditor.putString(PreferenceName, json);
			// prefsEditor.putString("Email_LoggedInPrevious",
			// AppConstants.currentUserEmail);
			prefsEditor.commit();
		} catch (Exception e) {
			LogUtils.errorLog("Exception", e.getMessage());
		}
	}

	public interface Predicate<T> {
		boolean apply(T type);
	}

	public LoginDO gettingObjecFromSharedPrerence(String PreferenceName) {
		LoginDO userLoginDoTemp = null;
		try {
			Gson gson = new Gson();
			String json = mPrefs.getString(PreferenceName, "");
			userLoginDoTemp = gson.fromJson(json, LoginDO.class);
		} catch (Exception e) {
			LogUtils.errorLog("Exception", e.getMessage());
		}
		return userLoginDoTemp;
	}

	public void savingObjecInSharedPrerence(OtherPassengerDo userLoginDoTemp, String PreferenceName) {
		try {
			Editor prefsEditor = mPrefs.edit();
			Gson gson = new Gson();
			String json = gson.toJson(userLoginDoTemp);
			prefsEditor.putString(PreferenceName, json);
			// prefsEditor.putString("Email_LoggedInPrevious",
			// AppConstants.currentUserEmail);
			prefsEditor.commit();
		} catch (Exception e) {
			LogUtils.errorLog("Exception", e.getMessage());
		}
	}

	public OtherPassengerDo gettingObjecFromSharedPrerenceOtherPassengerDo(String PreferenceName) {
		OtherPassengerDo userLoginDoTemp = null;
		try {
			Gson gson = new Gson();
			String json = mPrefs.getString(PreferenceName, "");
			userLoginDoTemp = gson.fromJson(json, OtherPassengerDo.class);
		} catch (Exception e) {
			LogUtils.errorLog("Exception", e.getMessage());
		}
		return userLoginDoTemp;
	}

	public String updateCurrencyByFactor(String currentPrice, int decimalUpto) {
		
		Double tempPrice = MathUtils.round(StringUtils.getDouble(currentPrice) * AppConstants.currencyConversionFactor,
				decimalUpto);

		if (decimalUpto == 0)
			return roundOfAmountForZeroAfterDecimal(tempPrice) + "";
		else
			return tempPrice + "";
	}

	private String roundOfAmountForZeroAfterDecimal(Double amount) {
		String tempAmount = amount + "";
		if ((amount * 100) % 10 == 0) {
			if (amount.toString().contains(".00"))
				tempAmount = amount.toString().replace(".00", "");
			else
				tempAmount = amount.toString().replace(".0", "");
		}
		return tempAmount;
	}

	public static void setTypeFaceSemiBold(ViewGroup group) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button || v instanceof EditText/* etc. */)
				((TextView) v).setTypeface(typefaceOpenSansSemiBold);
			else if (v instanceof ViewGroup)
				setTypeFaceSemiBold((ViewGroup) v);
		}
	}

	public static void setTypeFaceSansRegular(ViewGroup group) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button || v instanceof EditText/* etc. */)
				((TextView) v).setTypeface(typefaceOpenSansRegular);
			else if (v instanceof ViewGroup)
				setTypeFaceSansRegular((ViewGroup) v);
		}
	}

	public static void setTypeFaceOpenSansLight(ViewGroup group) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button || v instanceof EditText/* etc. */)
				((TextView) v).setTypeface(typeFaceOpenSansLight);
			else if (v instanceof ViewGroup)
				setTypeFaceOpenSansLight((ViewGroup) v);
		}
	}

	public static void setTypefaceOpenSansRegular(ViewGroup group) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button || v instanceof EditText/* etc. */)
				((TextView) v).setTypeface(typefaceOpenSansRegular);
			else if (v instanceof ViewGroup)
				setTypefaceOpenSansRegular((ViewGroup) v);
		}
	}

	public static void setTypefaceHelveticaLight(ViewGroup group) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button || v instanceof EditText/* etc. */)
				((TextView) v).setTypeface(typefaceHelveticaLight);
			else if (v instanceof ViewGroup)
				setTypefaceHelveticaLight((ViewGroup) v);
		}
	}

	public void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		showCustomDialog(getApplicationContext(), getString(R.string.Alert),
				getString(R.string.please_enable_your_location_for_closest_airport), getString(R.string.skip),
				getString(R.string.enable_location), "GPS_splash");
	}

	public void findLocation() {
		GPSTracker gps;
		List<Address> addresses = null;
		gps = new GPSTracker(BaseActivity.this);
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			Geocoder geocoder = new Geocoder(BaseActivity.this, new Locale(AppConstants.LANG_EN));

			try {
				while (addresses == null) {
					addresses = geocoder.getFromLocation(latitude, longitude, 1);
				}
				Address result;
				if (addresses != null && !addresses.isEmpty()) {
					if (checkLangArabic()) {
						AppConstants.country = addresses.get(0).getCountryName() + "";
					} else
						AppConstants.country = addresses.get(0).getCountryName() + "";
					//AppConstants.countryCode = addresses.get(0).getCountryCode() + "";
					
				}

			} catch (IOException ignored) {
			}

		} else {

			gps.showSettingsAlert();
		}
	}
	
	public void refreshLocation() {
		final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			if (AppConstants.isCameFromBookFlightForLocation) {
				if (!checkLangArabic())
					showCustomDialog(BaseActivity.this, getString(R.string.Alert),
							getString(R.string.please_enable_your_location_for_closest_airport),
							getString(R.string.skip), getString(R.string.enable_location), "GPS_REQ");
				else
					showCustomDialog(BaseActivity.this, getString(R.string.Alert),
							getString(R.string.please_enable_your_location_for_closest_airport),
							getString(R.string.skip), getString(R.string.enable_location), "GPS_REQ");
			}
		} else {
			findLocation();
		}
	}

	public String getFirstLetterCapital(String str) {
		if (!Locale.getDefault().equals(new Locale(AppConstants.LANG_LOCAL_AR))) {
			char c = str.charAt(0);
			char[] tmp = str.toCharArray();
			c = Character.toUpperCase(c);
			tmp[0] = c;
			return String.valueOf(tmp);
		} else
			return str;

	}

	public static String toTitleCase(String givenString) {
		String[] arr = givenString.split(" ");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < arr.length; i++) {
			sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
		}
		return sb.toString().trim();
	}

	public static String getCurrencyCode(String srvUrlType) {
		if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_G9))
			return ServiceURLs.GET_G9_CURRENCY;
		else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_3O))
			return ServiceURLs.GET_3O_CURRENCY;
		else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_E5))
			return ServiceURLs.GET_E5_CURRENCY;
		else if (srvUrlType.equalsIgnoreCase(AppConstants.SERVICE_URL_TYPE_9P))
			return ServiceURLs.GET_9P_CURRENCY;
		else
			return "AED";
	}

//=============	Added on 28Feb2018 for session Implementation in the app
// i.e. when the user inactive for the given duration the app will be restarted.============================
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onUserInteraction() {
		CheckInactivity();
	}

	public void CheckInactivity() {
		previousInteraction = (currentInteraction == null ? new Date() : currentInteraction);
		currentInteraction = new Date();
		long diff = currentInteraction.getTime() - previousInteraction.getTime();
		long seconds = diff / 1000;
		long minutes = seconds / 60;
		if (minutes >= SessionTimeOut) {
			RestartApp();
		}
	}

	protected void RestartApp() {
		showCustomDialog(getApplicationContext(), getString(R.string.Alert), "Your session has been expired.",
				getString(R.string.Ok), "", "SESSION");
	}
// ==============================================================================================================================//
}

// ==============================================================================================================================//

/// =========================================Date Comparator
/// Class=============================================================////
class DateComparator implements Comparator<OriginDestinationOptionDO> {
	@Override
	public int compare(OriginDestinationOptionDO a, OriginDestinationOptionDO b) {
		return (a.vecFlightSegmentDOs.get(0).departureDateTimeInMillies > b.vecFlightSegmentDOs
				.get(0).departureDateTimeInMillies) ? 1 : 0;
	}
}

//// =========================================================================================================================///
/// ======================================Office Location Comparator
//// =======================================================///
class OfficeLocationComparator implements Comparator<OfficeLocationDO> {
	@Override
	public int compare(OfficeLocationDO a, OfficeLocationDO b) {
		return a.country.compareToIgnoreCase(b.country);
	}
}
//// =========================================================================================================================////

// =================================This comparator class is for sorting Vector
// of OriginDestinationOptionDo ====================///

class vecOriginDestOptDo implements Comparator<OriginDestinationOptionDO> {
	@Override
	public int compare(OriginDestinationOptionDO lhs, OriginDestinationOptionDO rhs) {

		return (lhs.vecFlightSegmentDOs.get(0).departureDateTimeInMillies > rhs.vecFlightSegmentDOs
				.get(0).departureDateTimeInMillies) ? 1 : 0;
	}
}
// ==========================================================================================================================//

// =================================== This class is for Sorting Vector
// FlightSegmentDo ===================================//
class VecSortFlightSegmentDo implements Comparator<FlightSegmentDO> {

	@Override
	public int compare(FlightSegmentDO lhs, FlightSegmentDO rhs) {
		return (lhs.departureDateTimeInMillies > rhs.departureDateTimeInMillies) ? 1 : 0;
	}
}
// ========================================================================================================================//
