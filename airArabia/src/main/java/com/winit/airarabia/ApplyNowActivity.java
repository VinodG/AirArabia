package com.winit.airarabia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.winit.airarabia.common.AppConstants;

public class ApplyNowActivity extends BaseActivity {

	private ScrollView llVisaApplyNow;
	private WebView wvvisa_applynow;
	private ApplyNowActivity.BCR bcr;

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
		tvHeaderTitle.setText(getString(R.string.ApplyNow));
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		registerReceiver(bcr, intentFilter);

		llVisaApplyNow = (ScrollView) layoutInflater.inflate(
				R.layout.visarequirement, null);
		wvvisa_applynow = (WebView) llVisaApplyNow
				.findViewById(R.id.wvvisarequirement);
		llMiddleBase.addView(llVisaApplyNow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

	@Override
	public void bindingControl() {
		wvvisa_applynow.setWebViewClient(new WebViewClient());
		wvvisa_applynow.getSettings().setJavaScriptEnabled(true);
		wvvisa_applynow.getSettings().setUseWideViewPort(true);
		wvvisa_applynow.getSettings().setBuiltInZoomControls(true);
		wvvisa_applynow.loadUrl("file:///android_asset/ApplyNow_en.html");
	}
}