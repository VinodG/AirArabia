package com.winit.airarabia;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.winit.airarabia.common.AppConstants;

public class CheckinOnlineActivity extends BaseActivity {

	private LinearLayout llcheckinonline;
	private WebView webviewCheckonline;
	private final long TIME_LIMIT = 7 * 1000;
	private CheckinOnlineActivity.BCR bcr;

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
		llcheckinonline = (LinearLayout) layoutInflater.inflate(
				R.layout.checkinonline, null);
		llMiddleBase.addView(llcheckinonline, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		tvHeaderTitle.setText(getString(R.string.CheckInOnline));
		webviewCheckonline = (WebView) llcheckinonline
				.findViewById(R.id.checkinonlinewebview);
		
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		
		final Activity MyActivity = this;
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
				Window.PROGRESS_VISIBILITY_ON);
		webviewCheckonline.setWebChromeClient(new WebChromeClient() {
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
//		webviewCheckonline.setWebViewClient(new WebViewClient());
		webviewCheckonline.setWebViewClient(new WebViewClient(){
			 public void onPageFinished(WebView view, String url) {
			        // do your stuff here
					hideLoader();
			    }
		});
		webviewCheckonline.getSettings().setJavaScriptEnabled(true);
		webviewCheckonline.getSettings().setUseWideViewPort(true);
		webviewCheckonline.setInitialScale(10);
		webviewCheckonline.getSettings().setBuiltInZoomControls(true);
		/*webviewCheckonline
				.loadUrl("https://fastcheck.sita.aero/cce-presentation-web-g9/entryUpdate.do");*/
		webviewCheckonline
				.loadUrl("http://webcheckin.airarabia.com/sitawebapp");
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				hideLoader();
//			}
//		}, TIME_LIMIT);
		
	}

	/** To handle the back press in webview*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (webviewCheckonline.canGoBack() == true) {
					webviewCheckonline.goBack();
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