package com.winit.airarabia.controls;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.winit.airarabia.R;

/** class to create the Custom Dialog **/
public class CustomDialog extends Dialog
{
	//initializations
	boolean isCancellable = false;
	static AlertDialog.Builder builder;
	
	/**
	 * Constructor 
	 * @param context
	 * @param view
	 */
	public CustomDialog(Context context, View view) 
	{
		super(context,R.style.Theme_Dialog_Translucent);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
	}
	/**
	 * Constructor 
	 * @param context
	 * @param view
	 * @param lpW
	 * @param lpH
	 */
	public CustomDialog(Context context,View view, int lpW, int lpH) 
	{
		this(context, view, lpW, lpH, true);
	}
	/**
	 * Constructor 
	 * @param context
	 * @param view
	 * @param lpW
	 * @param lpH
	 * @param isCancellable
	 */
	public CustomDialog(Context context,View view, int lpW, int lpH, boolean isCancellable) 
	{
		super(context,R.style.Theme_Dialog_Translucent);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view, new LayoutParams(lpW, lpH));
		this.isCancellable = isCancellable;
	}
	
	@Override
	public void onBackPressed()
	{
		if(isCancellable)
			super.onBackPressed();
	}
	public static void myAlertDialog(final Context context, String title, String msg, final String posBtn, String NegBtn, final String from)
	{
		try {
			builder = new AlertDialog.Builder(context);
	    	builder.setTitle(title);
	    	builder.setMessage(msg);
	    	builder.setPositiveButton(posBtn, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();
					btnPosClick(from);
				}
			});
	    	builder.setNegativeButton(NegBtn, new OnClickListener() {

	    		@Override
	    		public void onClick(DialogInterface dialog, int which) {
	    			
	    			dialog.dismiss();
	    			btnNegClick(from);
	    		}
	    	});
	    	
	    	if(builder != null)
	    		builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void btnPosClick(String from)
	{
	}
	
	public static void btnNegClick(String from)
	{
	}
}