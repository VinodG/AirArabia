package com.winit.airarabia;

import org.apache.http.util.LangUtils;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.webaccess.ServiceURLs;

public class Register extends BaseActivity implements OnClickListener{

	private LinearLayout llSignUpOnline;
	private WebView webviewRegisterOnline;
	private final long TIME_LIMIT = 7 * 1000;
	private Register.BCR bcr;
	private Button btnUAE, btnMorocco, btnEgypt, btnJordan;

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
		llSignUpOnline = (LinearLayout) layoutInflater.inflate(
				R.layout.activity_signup, null);
		llMiddleBase.addView(llSignUpOnline, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		tvHeaderTitle.setText(getString(R.string.registerNow));
		btnUAE=(Button) llSignUpOnline.findViewById(R.id.btnUAE);
		btnMorocco=(Button) llSignUpOnline.findViewById(R.id.btnMorocco);
		btnEgypt=(Button) llSignUpOnline.findViewById(R.id.btnEgypt);
		btnJordan=(Button) llSignUpOnline.findViewById(R.id.btnJordan);
		setTypeFaceSansRegular(llSignUpOnline);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		
//=======================newly added for registernow=======================================================================		
		
		btnUAE.setOnClickListener(this);
		btnMorocco.setOnClickListener(this);
		btnEgypt.setOnClickListener(this);
		btnJordan.setOnClickListener(this);
		
		
	}
	
	
@Override
	public void bindingControl() {
	}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch(v.getId())
	{
	case R.id.btnUAE:
	{
		trackEvent("Register Screen", AppConstants.RegisterG9Button, "");
		String url =ServiceURLs.URL_New_Registration_UAE_G9;
		Intent intent1= new Intent(Register.this,RegisterDetailWebActivity.class);
		intent1.putExtra("url", url);
		if(checkLangArabic())
			intent1.putExtra("Action_Name", getString(R.string.i_travel_mainly_from_to_via_UAE));
		else
			intent1.putExtra("Action_Name", getString(R.string.registerNow));
		intent1.putExtra("groupName", "UAE");
		startActivity(intent1);
		break;
		

	}
		
	case R.id.btnMorocco:
	{
		trackEvent("Register Screen", AppConstants.Register3OButton, "");
		String url =ServiceURLs.URL_New_Registration_MOROCCO_3O;
		Intent intent1= new Intent(Register.this,RegisterDetailWebActivity.class);
		intent1.putExtra("url", url);
		if(checkLangArabic())
			intent1.putExtra("Action_Name", getString(R.string.i_travel_mainly_from_to_via_Morocco));
		else
			intent1.putExtra("Action_Name", getString(R.string.registerNow));
			
		intent1.putExtra("groupName", "MOROCCO");
		startActivity(intent1);

		break;
	}
	case R.id.btnEgypt:
	{
//		Log.d("test","in the onItemClick"+position); 
		trackEvent("Register Screen", AppConstants.RegisterE5Button, "");
		String url =ServiceURLs.URL_New_Registration_Egypt_E5;
		Intent intent1= new Intent(Register.this,RegisterDetailWebActivity.class);
		intent1.putExtra("url", url);
		if(checkLangArabic())
			intent1.putExtra("Action_Name", getString(R.string.i_travel_mainly_from_to_via_Egypt));
		else
			intent1.putExtra("Action_Name", getString(R.string.registerNow));
		intent1.putExtra("groupName", "Egypt");
		startActivity(intent1);
		break;

	}
	case R.id.btnJordan:
	{
		trackEvent("Register Screen", AppConstants.Register9PButton, "");
		String url =ServiceURLs.URL_New_Registration_Jorden_9P;
		Intent intent1= new Intent(Register.this,RegisterDetailWebActivity.class);
		intent1.putExtra("url", url);
		if (checkLangArabic())
			intent1.putExtra("Action_Name", getString(R.string.i_travel_mainly_from_to_via_Jordan));
		else
			intent1.putExtra("Action_Name", getString(R.string.registerNow));
		intent1.putExtra("groupName", "JORDAN");
		startActivity(intent1);
		break;

	}

	}
}
}
