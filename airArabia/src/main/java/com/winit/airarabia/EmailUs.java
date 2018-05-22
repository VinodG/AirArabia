package com.winit.airarabia;
import android.app.ActionBar.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EmailUs extends BaseActivity {

	private LinearLayout llEmailUs;
	private EditText etName, etEmail, etSubject, etMobile,etMessage;
	private TextView tvTitleFeedack;
	@Override
	public void initilize() {
		llEmailUs = (LinearLayout) layoutInflater.inflate(R.layout.email_us, null);
		lltop.setVisibility(View.VISIBLE);
		tvHeaderTitle.setText(getString(R.string.email_us));
		llMiddleBase.addView(llEmailUs, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
		etName			= (EditText) llEmailUs.findViewById(R.id.etName);
		etEmail			= (EditText) llEmailUs.findViewById(R.id.etEmail);
		tvTitleFeedack	= (TextView) llEmailUs.findViewById(R.id.tvTitleFeedack);
		etSubject			= (EditText) llEmailUs.findViewById(R.id.etSubject);
		etMobile			= (EditText) llEmailUs.findViewById(R.id.etMobile);
		etMessage			= (EditText) llEmailUs.findViewById(R.id.etMessage);
		
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Send));
		setTypeFaceOpenSansLight(llEmailUs);
		tvTitleFeedack.setTypeface(typefaceOpenSansSemiBold);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		bindingControl();
		
	}

	@Override
	public void bindingControl() {
		// TODO Auto-generated method stub
		
		btnSubmitNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}

	
}
