package com.winit.airarabia;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.winit.airarabia.common.AppConstants;

public class VisaRegulationsActivity extends BaseActivity{

	private ScrollView llVisaRegulations;
	private WebView webviewVisaRegulation;
	private VisaRegulationsActivity.BCR bcr;

	private class BCR extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equalsIgnoreCase(AppConstants.HOME_CLICK))
				finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(bcr);
	}

	@Override
	public void initilize() 
	{
		tvHeaderTitle.setText(getString(R.string.VisaRegulations));
		bcr = new BCR();
		intentFilter.addAction(AppConstants.HOME_CLICK);
		registerReceiver(bcr, intentFilter);

		llVisaRegulations		= (ScrollView) layoutInflater.inflate(R.layout.visarequirement, null);
		webviewVisaRegulation 	= (WebView) llVisaRegulations.findViewById(R.id.wvvisarequirement);
		llMiddleBase.addView(llVisaRegulations, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	}

	@Override
	public void bindingControl() 
	{
		webviewVisaRegulation.setWebViewClient(new WebViewClient());
		webviewVisaRegulation.getSettings().setJavaScriptEnabled(true);
		webviewVisaRegulation.getSettings().setUseWideViewPort(true);
		webviewVisaRegulation.getSettings().setBuiltInZoomControls(true);
		webviewVisaRegulation.loadUrl("file:///android_asset/VisaRegulation_en.html");
	}
}