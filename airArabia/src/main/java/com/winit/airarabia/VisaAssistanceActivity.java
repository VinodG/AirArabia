package com.winit.airarabia;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.airarabia.common.AppConstants;

public class VisaAssistanceActivity extends BaseActivity implements
OnClickListener {

	private LinearLayout llVisaAssistance;
	private TextView tvVisaRequirements, tvVisaRegulation,
	tvVisaAssistanceApplynow;
	private VisaAssistanceActivity.BCR bcr;

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
		llVisaAssistance = (LinearLayout) layoutInflater.inflate(
				R.layout.visaassistance, null);
		tvVisaRequirements = (TextView) llVisaAssistance
				.findViewById(R.id.tvVisaRequirements);
		tvVisaRegulation = (TextView) llVisaAssistance
				.findViewById(R.id.tvVisaRegulation);
		tvVisaAssistanceApplynow = (TextView) llVisaAssistance
				.findViewById(R.id.tvVisaAssistanceApplynow);
		llMiddleBase.addView(llVisaAssistance, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

	@Override
	public void bindingControl() {
		tvHeaderTitle.setText(getString(R.string.VisaAssistance));
		tvVisaRequirements.setOnClickListener(this);
		tvVisaRegulation.setOnClickListener(this);
		tvVisaAssistanceApplynow.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvVisaRequirements)
			visarequirements();
		else if (v.getId() == R.id.tvVisaRegulation)
			visaregulation();
		else if (v.getId() == R.id.tvVisaAssistanceApplynow)
			applynow();
	}

	private void applynow() {
		Intent in = new Intent(VisaAssistanceActivity.this,
				ApplyNowActivity.class);
		startActivity(in);
	}

	private void visaregulation() {
		Intent in = new Intent(VisaAssistanceActivity.this,
				VisaRegulationsActivity.class);
		startActivity(in);
	}

	private void visarequirements() {
		Intent in = new Intent(VisaAssistanceActivity.this,
				VisaRequirementActivity.class);
		startActivity(in);
	}
}