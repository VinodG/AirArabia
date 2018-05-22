package com.winit.airarabia;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.winit.airarabia.common.AppConstants;

public class ReachUsActivity extends BaseActivity implements OnClickListener {

	private LinearLayout llReachus;
	private Button btnCallCenters, btnOfficelocation, btnEmailUs;
	private ReachUsActivity.BCR bcr;

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
		llReachus = (LinearLayout) layoutInflater.inflate(R.layout.reachus,
				null);
		btnCallCenters = (Button) llReachus.findViewById(R.id.btn_callcenters);
		btnOfficelocation = (Button) llReachus
				.findViewById(R.id.btn_officelocation);
		btnEmailUs		= (Button) llReachus.findViewById(R.id.btnEmailUs);
		llMiddleBase.addView(llReachus, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setTypeFaceOpenSansLight(llReachus);
		btnCallCenters.setTypeface(typefaceOpenSansSemiBold);
		btnOfficelocation.setTypeface(typefaceOpenSansSemiBold);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		btnEmailUs.setTypeface(typefaceOpenSansSemiBold);
	}

	@Override
	public void bindingControl() {
		tvHeaderTitle.setText(getString(R.string.ContactUs));
		btnCallCenters.setOnClickListener(this);
		btnOfficelocation.setOnClickListener(this);
		btnEmailUs.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_callcenters) {
//			Intent in = new Intent(ReachusActivity.this,CallcentersActivity.class);
			trackEvent("ContactUs Screen",AppConstants.CallCenterButton,"");
			Intent in = new Intent(ReachUsActivity.this,CallcentersActivityNew.class);
			startActivity(in);
		} else if (v.getId() == R.id.btn_officelocation) {
			trackEvent("ContactUs Screen",AppConstants.OfficeLocationButton,"");
//			Intent in = new Intent(ReachusActivity.this,OfficeLocationActivity.class);
			Intent in = new Intent(ReachUsActivity.this,OfficeLocationActivityNew.class);
			startActivity(in);
		}
		else if (v.getId() == R.id.btnEmailUs) {
			trackEvent("ContactUs Screen",AppConstants.Feedback_Button,"");
			Intent in = new Intent(ReachUsActivity.this,
					EmailUs.class);
			startActivity(in);
		}
	}
}