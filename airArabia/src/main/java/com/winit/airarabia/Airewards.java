package com.winit.airarabia;

import com.winit.airarabia.common.AppConstants;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Airewards extends BaseActivity implements OnClickListener{
	private ImageView iv2;
	private Button btnEarning,btnRedeeming,btnFaqs,btnJoinNow;
	private LinearLayout llAirewards;
	private TextView tv1;

	@Override
	public void initilize() {
		// TODO Auto-generated method stub

		llAirewards = (LinearLayout) layoutInflater.inflate(R.layout.activity_airewards, null);
		lltop.setVisibility(View.VISIBLE);
		tvHeaderTitle.setText(getString(R.string.airewards));
		llMiddleBase.addView(llAirewards, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

		btnEarning=(Button)llAirewards.findViewById(R.id.btnEarning);
		btnRedeeming=(Button)llAirewards.findViewById(R.id.btnRedeeming);
		btnFaqs=(Button)llAirewards.findViewById(R.id.btnFaqs);
		btnJoinNow=(Button)llAirewards.findViewById(R.id.btnJoinNow);
		tv1=(TextView)llAirewards.findViewById(R.id.tv1);

		btnEarning.setOnClickListener(this);
		btnRedeeming.setOnClickListener(this);
		btnFaqs.setOnClickListener(this);
		btnJoinNow.setOnClickListener(this);

		setTypeFaceSansRegular(llAirewards);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);

	}
//onMessageRecieved

	@Override
	public void bindingControl() {
		// TODO Auto-generated method stub

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btnEarning:
		{
			trackEvent("AireWards Screen",AppConstants.EarningButton,"");
			String url ="";
			if (checkLangArabic()) {
				url ="https://airewards.airarabia.com/portal/Index.aspx?MenuId=16&LanguageId=1";
//				url ="http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/earning.aspx";
			}
			else if(checkLangFrench())
			{
//				url ="http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/earningfr.aspx";
				url ="https://airewards.airarabia.com/portal/Index.aspx?MenuId=16&LanguageId=3";
			}
			else
				url ="https://airewards.airarabia.com/portal/Index.aspx?MenuId=16&LanguageId=1";
//				url ="http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/earning.aspx";

			Intent intent1= new Intent(Airewards.this,AirewardsWebActivity.class);
			intent1.putExtra("url", url);
			intent1.putExtra("Action_Name", getString(R.string.earning));
			startActivity(intent1);
			break;


		}

		case R.id.btnRedeeming:
		{
			trackEvent("AireWards Screen",AppConstants.RedeemingButton,"");
			String url ="";
			if (checkLangArabic()) {
				url ="https://airewards.airarabia.com/portal/Index.aspx?MenuId=18&LanguageId=1";
//				url ="http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/Redeem.aspx";
			}
			else if(checkLangFrench())
			{
				url ="https://airewards.airarabia.com/portal/Index.aspx?MenuId=18&LanguageId=3";
//				url ="http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/Redeemfr.aspx";
			}
			else
				url ="https://airewards.airarabia.com/portal/Index.aspx?MenuId=18&LanguageId=1";
//				url ="http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/Redeem.aspx";


			Intent intent1= new Intent(Airewards.this,AirewardsWebActivity.class);
			intent1.putExtra("url", url);
			intent1.putExtra("Action_Name", getString(R.string.redeeming));
			startActivity(intent1);

			break;
		}
		case R.id.btnFaqs:
		{
			trackEvent("AireWards Screen",AppConstants.FAQButton,"");
			String url ="";
			//			Log.d("test","in the onItemClick"+position); 
			if (checkLangArabic()) {
				url ="https://airewards.airarabia.com/portal/Index.aspx?MenuId=22&LanguageId=1";
//				url ="http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/Faqs.aspx";
			}
			else if(checkLangFrench())
			{
				url ="https://airewards.airarabia.com/portal/Index.aspx?MenuId=22&LanguageId=3";
//				url ="http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/Faqsfr.aspx";
			}
			else
				url ="https://airewards.airarabia.com/portal/Index.aspx?MenuId=22&LanguageId=1";
//				url ="http://dev4.winitsoftware.com/airarabiadynamic/StaticXML/Faqs.aspx";


			Intent intent1= new Intent(Airewards.this,AirewardsWebActivity.class);
			intent1.putExtra("url", url);
			intent1.putExtra("Action_Name", getString(R.string.faq));
			startActivity(intent1);
			break;

		}
		case R.id.btnJoinNow:
		{
			//			String url ="https://airewards.airarabia.com/portal/Index.aspx?MenuId=12";
			//			Intent intent1= new Intent(Airewards.this,AirewardsWebActivity.class);
			//			intent1.putExtra("url", url);
			//			intent1.putExtra("Action_Name", "JOIN NOW");
			//			startActivity(intent1);
			//			break;
			trackEvent("AireWards Screen",AppConstants.JoinNowButton,"");
			Intent intent1= new Intent(Airewards.this,Register.class);
			startActivity(intent1);
			break;

		}

		}

	}







}
