package com.winit.airarabia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.winit.airarabia.common.AppConstants;

public class VisaRequirementActivity extends BaseActivity {

	private ScrollView llVisaRequirement;
	private WebView wvvisarequirements;
	private VisaRequirementActivity.BCR bcr;

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
		tvHeaderTitle.setText(getString(R.string.VisaRequirements));
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		registerReceiver(bcr, intentFilter);

		llVisaRequirement = (ScrollView) layoutInflater.inflate(
				R.layout.visarequirement, null);
		wvvisarequirements = (WebView) llVisaRequirement
				.findViewById(R.id.wvvisarequirement);
		llMiddleBase.addView(llVisaRequirement, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

	@Override
	public void bindingControl() {
		wvvisarequirements.setWebViewClient(new WebViewClient());
		wvvisarequirements.getSettings().setJavaScriptEnabled(true);
		wvvisarequirements.getSettings().setUseWideViewPort(true);
		wvvisarequirements.getSettings().setBuiltInZoomControls(true);
		wvvisarequirements.loadUrl("file:///android_asset/VisaRequirements_en.html");
	}
}