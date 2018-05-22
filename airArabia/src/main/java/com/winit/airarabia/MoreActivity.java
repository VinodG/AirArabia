package com.winit.airarabia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.winit.airarabia.common.AppConstants;

public class MoreActivity extends BaseActivity {

	private LinearLayout llMore;
	private Button btnVissaAssistance, btnReachus;
	private MoreActivity.BCR bcr;
	

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
		llMore = (LinearLayout) layoutInflater.inflate(R.layout.more, null);
		btnVissaAssistance = (Button) llMore
				.findViewById(R.id.btn_visaassistance);
		btnReachus = (Button) llMore.findViewById(R.id.btn_reachusinmore);
		llMiddleBase.addView(llMore, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		tvHeaderTitle.setText(getString(R.string.More));
		btnVissaAssistance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(MoreActivity.this,
						VisaAssistanceActivity.class);
				startActivity(in);
			}
		});

		btnReachus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(MoreActivity.this, ReachUsActivity.class);
				startActivity(in);

			}
		});
	}

	@Override
	public void bindingControl() {

	}
}
