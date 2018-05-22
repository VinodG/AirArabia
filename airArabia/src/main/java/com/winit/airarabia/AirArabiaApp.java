package com.winit.airarabia;

import android.app.Application;
import android.content.Context;

public class AirArabiaApp extends Application {
	
	public static Context mContext;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = AirArabiaApp.this;
	}
}
