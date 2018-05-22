package com.winit.airarabia;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Feedback extends BaseActivity implements TextWatcher {

	private LinearLayout llFeedback;
	private EditText etSubject, etMessage,etCC;
	private String  emailAddress="", emailSubject="",
			emailMessage="", CC="";
	private TextView tvEmail,tvEmailTag, tvSubjectTag, tvMessageTag,tvCCTag;
	@Override
	public void initilize() {
		llFeedback = (LinearLayout) layoutInflater.inflate(R.layout.feedback_user, null);
		lltop.setVisibility(View.VISIBLE);
		tvHeaderTitle.setText(getString(R.string.email_us));
		llMiddleBase.addView(llFeedback, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
		tvEmail				= (TextView) llFeedback.findViewById(R.id.tvEmailTo);
		etCC				= (EditText) llFeedback.findViewById(R.id.etCc_bcc);
		etSubject			= (EditText) llFeedback.findViewById(R.id.etSubjectFeedback);
		etMessage			= (EditText) llFeedback.findViewById(R.id.etMessage);
		
		tvEmailTag			= (TextView) llFeedback.findViewById(R.id.tvEmailToTag);
		tvCCTag				= (TextView) llFeedback.findViewById(R.id.tvCc_bccTag);
		tvSubjectTag		= (TextView) llFeedback.findViewById(R.id.tvSubjectFeedbackTag);
		tvMessageTag		= (TextView) llFeedback.findViewById(R.id.tvMessageTag);
		
		btnSubmitNext.setVisibility(View.VISIBLE);
		btnSubmitNext.setText(getString(R.string.Send));
		setTypeFaceSemiBold(llFeedback);
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		bindingControl();
		
	}

	@Override
	public void bindingControl() {
		// TODO Auto-generated method stub
		etCC.addTextChangedListener(this);
		etSubject.addTextChangedListener(this);
		etMessage.addTextChangedListener(this);
		
		btnSubmitNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(!TextUtils.isEmpty(tvEmail.getText().toString()))
					emailAddress = tvEmail.getText().toString();
				
				if(!TextUtils.isEmpty(etSubject.getText().toString()))
					emailSubject = etSubject.getText().toString();
				
				if(!TextUtils.isEmpty(etMessage.getText().toString()))
					emailMessage = etMessage.getText().toString();
				
				if(!TextUtils.isEmpty(etCC.getText().toString()))
					CC = etCC.getText().toString();
				  
				  String validationMessage =validate() ;
				  if(validationMessage.equalsIgnoreCase(""))
				  {
					  sendMail();
				  }
				  else if(validationMessage.equalsIgnoreCase(getString(R.string.Subject)))
					  showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.Please_enter)+" "+validationMessage,  getString(R.string.Cancel), getString(R.string.Ok), "fromFeedback");
				  else 
					  showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.Please_enter)+" "+validationMessage, getString(R.string.Ok), null, "fromFeedback");
				}
				
			
		});
		
	}
	
	private String validate() {
		String returningValue = "";
		if (!CC.equalsIgnoreCase("") && emailValidate(CC).matches()) 
		{
		}
		else if(CC.equalsIgnoreCase(""))
		{
			if(!emailSubject.equalsIgnoreCase(""))
			{
				if (!emailMessage.equalsIgnoreCase("")) {

				}
				else
					returningValue = getString(R.string.message);
			}
			else
				returningValue = getString(R.string.Subject);
		}
		else
		{
			returningValue=getString(R.string.Cc_Bcc);
		}



		return returningValue;
	}
	
	private void sendMail()
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
		intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
		intent.putExtra(Intent.EXTRA_TEXT, emailMessage);
		intent.putExtra(Intent.EXTRA_CC, new String[]{CC});
		Intent mailer = Intent.createChooser(intent, null);
		startActivity(mailer);
		finish();
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

		if (etCC.getText().hashCode() == s.hashCode())
		{
			if(!(etCC.getText().toString()).equalsIgnoreCase("")){

				tvCCTag.setVisibility(View.VISIBLE);
			}else {
				tvCCTag.setVisibility(View.GONE);
			}
		}
		if (etSubject.getText().hashCode() == s.hashCode())
		{
			if(!(etSubject.getText().toString()).equalsIgnoreCase("")){

				tvSubjectTag.setVisibility(View.VISIBLE);
			}else {
				tvSubjectTag.setVisibility(View.GONE);
			}
		}
		if (etMessage.getText().hashCode() == s.hashCode())
		{
			if(!(etMessage.getText().toString()).equalsIgnoreCase("")){

				tvMessageTag.setVisibility(View.VISIBLE);
			}else {
				tvMessageTag.setVisibility(View.GONE);
			}
		}


	}
	

	@Override
	public void onButtonNoClick(String from) {
		// TODO Auto-generated method stub
		super.onButtonNoClick(from);
		if(from.equalsIgnoreCase("fromFeedback"))
			sendMail();
	}
}
