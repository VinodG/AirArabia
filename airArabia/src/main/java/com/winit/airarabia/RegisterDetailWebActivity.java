package com.winit.airarabia;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.common.AppConstants;

public class RegisterDetailWebActivity extends BaseActivity {
	WebView wvAirewards;
	TextView tvRegistrationHeader;
	LinearLayout llAirewardsWebview;
	private final long TIME_LIMIT = 8 * 1000;
	private BCR bcr;

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
		showLoader("");
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		registerReceiver(bcr, intentFilter);
		llAirewardsWebview = (LinearLayout) layoutInflater.inflate(
				R.layout.activity_airewards_webviw, null);
		llMiddleBase.addView(llAirewardsWebview, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		String actionNameText=getIntent().getExtras().getString("Action_Name");
		tvRegistrationHeader=(TextView) llAirewardsWebview.findViewById(R.id.tvRegistrationHeader);
		
		tvRegistrationHeader.setTypeface(typefaceOpenSansSemiBold);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		
		if(getIntent().hasExtra("groupName")){
			String groupname=getIntent().getExtras().getString("groupName");
			tvRegistrationHeader.setText(groupname+"");
		}
		tvHeaderTitle.setText(actionNameText+"");
		if(checkLangArabic())
		{
			tvHeaderTitle.setTextSize(15);
		}
		
		tvHeaderTitle.setGravity(Gravity.CENTER);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		wvAirewards = (WebView) llAirewardsWebview
				.findViewById(R.id.webView);
	
		tvRegistrationHeader.setVisibility(View.GONE);
		
		
		final Activity MyActivity = this;
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
				Window.PROGRESS_VISIBILITY_ON);
		wvAirewards.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				/** Make the bar disappear after URL is loaded, and changes
				 string to Loading...*/
				MyActivity.setTitle(getString(R.string.Loading));
				MyActivity.setProgress(progress * 100); // Make the bar disappear after URL is loaded

				/** Return the app name after finish loading*/
				if (progress == 100)
					MyActivity.setTitle(R.string.app_name);
			}
		});
		/** to open the all urls in web view only.*/
		wvAirewards.setWebViewClient(new WebViewClient());
		wvAirewards.getSettings().setJavaScriptEnabled(true);
		wvAirewards.getSettings().setUseWideViewPort(true);
		wvAirewards.setInitialScale(10);
		wvAirewards.getSettings().setBuiltInZoomControls(true);
		
		String url=getIntent().getExtras().getString("url");
		wvAirewards
				.loadUrl(url);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				hideLoader();
			}
		}, TIME_LIMIT);
	}

	/** To handle the back press in webview*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (wvAirewards.canGoBack() == true) {
					wvAirewards.goBack();
				} else {
					finish();
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void bindingControl() {
	}
	
	

}

  