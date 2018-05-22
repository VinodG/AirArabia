package com.winit.airarabia;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insider.android.insider.Insider;
import com.pushio.manager.PushIOManager;
import com.winit.airarabia.busynesslayer.CommonBL;
import com.winit.airarabia.busynesslayer.DataListener;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.insider.GcmBroadcastReceiver;
import com.winit.airarabia.objects.KeyValue;
import com.winit.airarabia.objects.LoginDO;
import com.winit.airarabia.objects.OtherPassengerDo;
import com.winit.airarabia.objects.PosterImagesDO;
import com.winit.airarabia.utils.LogUtils;
import com.winit.airarabia.utils.SharedPrefUtils;
import com.winit.airarabia.webaccess.Response;


public class LoginActivityScreen extends BaseActivity implements DataListener {
	private ImageView imgRememberMe, ivmenu_login;
	private LinearLayout llLogin,llRememberMe;
	private Boolean isRemember = false;
	EditText etUserName, etPassword;
	private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView rememberMeTag;
    private final String LOGIN_ACTIVITY_SUCCESSFUL = "LoginActivity_Succesful";
    private String cameFrom;
    private Bundle bundle ;
	private LoginDO loginDO;
    private  PosterImagesDO posterImagesDO;
    private String lastExecutedServiceForLogin = "";
    private int count = 0;
    private Button btn_back_login;

	@Override
	public void initilize() {
		llLogin = (LinearLayout) layoutInflater.inflate(R.layout.login_screen, null);
		lltop.setVisibility(View.GONE);
		lockDrawer("LoginActivity");
		llMiddleBase.addView(llLogin, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		initClassMembers();
		setTypeFaceSemiBold(llLogin);
		btn_back_login.setTypeface(typefaceOpenSansSemiBold);
		
		Insider insider = new Insider();
		insider.openSession(this, AppConstants.ProjectName);
		insider.registerInBackground(this, AppConstants.GoogleProjectNumber);
		Insider.setLandingActivity(GcmBroadcastReceiver.class, this);
		Insider.setNotificationIcon(R.drawable.ic_launcher, this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		Insider.start(this, intent);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
			Insider.stop(this);
	}
	
	private void initClassMembers()
	{
		imgRememberMe = (ImageView) findViewById(R.id.imgRememberMe);
		btn_back_login= (Button) findViewById(R.id.btn_back_login);
		ivmenu_login = (ImageView) findViewById(R.id.ivmenu_login);
		llRememberMe    = (LinearLayout) findViewById(R.id.llRememberMe);
        etUserName        = (EditText) findViewById(R.id.etUserName);
        etPassword        = (EditText) findViewById(R.id.etPassword);
        rememberMeTag   = (TextView) findViewById(R.id.rememberMeTag);
        pref = getApplicationContext().getSharedPreferences(SharedPreferenceStrings.APP_PREFERENCES, MODE_PRIVATE );
        editor = pref.edit();
        if (pref.getBoolean(SharedPreferenceStrings.isRemember,false))
        {
        	isRemember = true;
        	String user_name = pref.getString(SharedPreferenceStrings.USERNAME,"");
        	String pass      = pref.getString(SharedPreferenceStrings.PASSWORD,"");
        	if ((!user_name.isEmpty()) && (!pass.isEmpty()))
        	{
        		etUserName.setText(user_name.toString());
        		etPassword.setText(pass.toString());
        		LogUtils.errorLog("UserName,Pass", "UserName - "+user_name.toString()+", Pass- "+pass.toString());
        		imgRememberMe.setImageResource(R.drawable.btn_on_pasgr);
        	}
        }
        
        btnSubmitNext.setVisibility(View.VISIBLE);
        btnSubmitNext.setText(getString(R.string.login));
        btn_back_login.setTypeface(typefaceOpenSansSemiBold);
        btn_back_login.setTypeface(typefaceOpenSansSemiBold);
	}

	@Override
	public void bindingControl() {
		
		llRememberMe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isRemember) {
					isRemember = false;
                    SharedPrefUtils.getKeyValue(LoginActivityScreen.this, SharedPreferenceStrings.isRemember, isRemember + "");
					imgRememberMe.setImageResource(R.drawable.btn_off_pasgr);
				} else {
					Insider.tagEvent("Remember_button_Clicked", LoginActivityScreen.this);
					//Insider.Instance.tagEvent(LoginActivityScreen.this,"Remember_button_Clicked");
					trackEvent("Login Screen","Remember_button_Clicked","");
					isRemember = true;
					imgRememberMe.setImageResource(R.drawable.btn_on_pasgr);
				}

                editor.putBoolean(SharedPreferenceStrings.isRemember,isRemember);
                editor.commit();

			}
		});
		btn_back_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
								Insider.tagEvent("Back_button_LoginClicked", LoginActivityScreen.this);
				//Insider.Instance.tagEvent(LoginActivityScreen.this,"Back_button_LoginClicked");
				trackEvent("Login Screen","Back_button_LoginClicked","");
				btnBack.performClick();
			}
		});
		ivmenu_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ivmenu.performClick();
			}
		});
		btnSubmitNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				count = 0;
				String errorCode = "";
				errorCode = isValidated(etUserName.getText().toString(),etPassword.getText().toString());
				if(errorCode.equalsIgnoreCase(""))
				{
										Insider.tagEvent("Login_button_clicked", LoginActivityScreen.this);
					//Insider.Instance.tagEvent(LoginActivityScreen.this,"Login_button_clicked");
					trackEvent("Login Screen","Login_button_clicked","");
					showLoader("Please wait..!!");
					if (callloginService(AppConstants.LoginServiceEndPoint_G9)) 
					{
						count++;
						if (isRemember)
		                {
							LogUtils.errorLog("UserName Shared", "UserName - "+etUserName.getText().toString()+", Pass- "+etPassword.getText().toString());							
		                    editor.putString(SharedPreferenceStrings.USERNAME, etUserName.getText().toString());
		                    editor.putString(SharedPreferenceStrings.PASSWORD,etPassword.getText().toString());
		                    editor.commit();
		                }
					}
					else
					{
						hideLoader();
						showCustomDialog(getApplicationContext(),
								"",
								getString(R.string.InternetProblem),
								getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
					}
	            
				}
				else
					showCustomDialog(getApplicationContext(),
							"",
							errorCode,
							getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
			}
		});
		
		if(!isRemember)
			llRememberMe.performClick();

	}

	@Override
	public void dataRetreived(Response data) {
		if(data != null /*&& (!data.isError && (!(count >= 4)))*/)
		{
			switch (data.method) {
			case WS_LOGIN:
				if(data.data instanceof LoginDO && !data.isError)
				{
					loginDO = (LoginDO) data.data;
               
					if(loginDO != null)
					{
//						countryCode
						if (isRemember)
		                {
		                    editor.putString(SharedPreferenceStrings.USERNAME, etUserName.getText().toString());
		                    editor.putString(SharedPreferenceStrings.PASSWORD,etPassword.getText().toString());
		                    editor.commit();
		                }
						
						KeyValue keyValue = new KeyValue(SharedPreferenceStrings.isLoggedIn, 1+"");
						KeyValue keyValueOldEmail = new KeyValue(SharedPreferenceStrings.Email_LoggedInPrevious, etUserName.getText().toString().trim());
						
						SharedPrefUtils.setValue(LoginActivityScreen.this, SharedPreferenceStrings.APP_PREFERENCES, keyValue);
						AppConstants.currentUserEmail = etUserName.getText().toString();
						
						savingObjecInSharedPrerence(loginDO, SharedPreferenceStrings.loginDo);
						savingObjecInSharedPrerence(loginDO, "LoginDO");
						if(!AppConstants.currentUserEmail.trim().equalsIgnoreCase(mPrefs.getString(SharedPreferenceStrings.Email_LoggedInPrevious, "")))
						{
							deleteOldProfile();
							SharedPrefUtils.setValue(LoginActivityScreen.this, SharedPreferenceStrings.APP_PREFERENCES, keyValueOldEmail);
						}
						hideLoader();	
		                showCustomDialog(LoginActivityScreen.this, getString(R.string.success), getString(R.string.logged_in_succesfully)+" "+loginDO.firstName+".", getString(R.string.Ok), null, LOGIN_ACTIVITY_SUCCESSFUL);
						PushIOManager.getInstance(this).registerUserId(AppConstants.currentUserEmail);
					}
				}
				else if(data.data instanceof String ||(data.isError && count <= 3))
				{
					if(!lastExecutedServiceForLogin.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_3O) && count==1)
					{
						count++;
						if (callloginService(AppConstants.LoginServiceEndPoint_3O)) 
						{
						}
						else
						{
							hideLoader();
							showCustomDialog(getApplicationContext(),
									"",
									getString(R.string.InternetProblem),
									getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
						}
					}
					else if(!lastExecutedServiceForLogin.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_E5) && count==2)
					{
						count++;
						if (callloginService(AppConstants.LoginServiceEndPoint_E5)) 
						{
						}
						else
						{
							hideLoader();
							showCustomDialog(getApplicationContext(),
									"",
									getString(R.string.InternetProblem),
									getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
						}
					}
					else if(!lastExecutedServiceForLogin.equalsIgnoreCase(AppConstants.LoginServiceEndPoint_9P) && count==3)
					{
						count++;
						if (callloginService(AppConstants.LoginServiceEndPoint_9P)) 
						{
						}
						else
						{
							hideLoader();
							showCustomDialog(getApplicationContext(),
									getString(R.string.Alert),
									getString(R.string.InternetProblem),
									getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
						}
					}
					else
					{
						hideLoader();
						showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.login_error), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
					}
				}
				else
				{
					hideLoader();
					showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
				}

				break;

			default:
				break;
			}
		}
		else
		{
			hideLoader();
			if(data.data instanceof String)
			{
				if(((String)data.data).equalsIgnoreCase(getString(R.string.ConnenectivityTimeOutExpMsg)))
					showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.ConnenectivityTimeOutExpMsg), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
				else showCustomDialog(getApplicationContext(), getString(R.string.Alert), getString(R.string.TechProblem), getString(R.string.Ok), "", AppConstants.INTERNET_PROBLEM);
			}
		}	
	}
	
	@Override
	public void onButtonYesClick(String from) {
	
		super.onButtonYesClick(from);
		if (from.equals(LOGIN_ACTIVITY_SUCCESSFUL)) {
			
			bundle = getIntent().getExtras();
			if (bundle != null && bundle.getString(AppConstants.PASS_INFO)!=null && bundle.getString(AppConstants.PASS_INFO).equalsIgnoreCase("passInfo")) {
				setResult(RESULT_OK);
				finish();
			} else {

				Intent in = new Intent(LoginActivityScreen.this,MyProfileActivity.class);
				in.putExtra("CameFromLogin", "Yes");
				in.putExtra("Email_LoggedIn", etUserName.getText().toString());
				finish();
				startActivity(in);
			}
		}
	}
	
	public boolean callloginService(String requestURLType)
	{
		lastExecutedServiceForLogin = requestURLType;
		SharedPrefUtils spu = new SharedPrefUtils(LoginActivityScreen.this);
		String str = spu.getStringFromPreference(SharedPrefUtils.gcmId, "");
		return new CommonBL(LoginActivityScreen.this, LoginActivityScreen.this).getLogin(etUserName.getText().toString(), etPassword.getText().toString(), requestURLType,str);
	}
	
	public String isValidated(String userName, String Pass)
	{
		if(userName.isEmpty() && userName.equalsIgnoreCase(""))
		{
			return getString(R.string.EmailIdError);
		}
		else if(Pass.isEmpty() && Pass.equalsIgnoreCase(""))
		{
			return getString(R.string.PassError);
		}
		else if(!emailValidate(userName).matches())
		{
			return getString(R.string.invaild_email);
		}
		else
			return "";
	}
	
	
	private void deleteOldProfile()
	{
		savingObjecInSharedPrerence(new LoginDO(), "LoginDO");
		if(loginDO != null)
			savingObjecInSharedPrerence(loginDO, "LoginDO");
		savingObjecInSharedPrerence(new OtherPassengerDo(), SharedPreferenceStrings.otherPassengerDo);
		SharedPrefUtils.saveArrayInPreference(new ArrayList<String>(),SharedPreferenceStrings.FAVLIST,LoginActivityScreen.this);
	}
}
