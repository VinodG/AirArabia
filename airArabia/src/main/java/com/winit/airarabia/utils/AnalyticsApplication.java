/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.winit.airarabia.utils;

import android.app.Application;

import java.util.HashMap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.winit.airarabia.R;

public class AnalyticsApplication extends Application {
	private static final String PROPERTY_ID = "UA-86404821-1";
	public static final String LOCK = "Lock" ;
	private Tracker mTracker;
	public static int GENERAL_TRACKER = 0;

	public enum TrackerName {
		APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
	}

	public AnalyticsApplication() {
		super();
	}

	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {

			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
			Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
					: (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.app_tracker)
							: analytics.newTracker(R.xml.ecommerce_tracker);
			t.enableAdvertisingIdCollection(true);
			mTrackers.put(trackerId, t);
		}
		return mTrackers.get(trackerId);
	}
	/*
	 * HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName,
	 * Tracker>();
	 * 
	 *//**
		 * Gets the default {@link Tracker} for this {@link Application}.
		 * 
		 * @return tracker
		 *//*
		 * synchronized public Tracker getDefaultTracker() { if (mTracker ==
		 * null) { GoogleAnalytics analytics =
		 * GoogleAnalytics.getInstance(this); // To enable debug logging use:
		 * adb shell setprop log.tag.GAv4 DEBUG // mTracker =
		 * analytics.newTracker(R.xml.global_tracker); mTracker =
		 * analytics.newTracker(R.xml.app_tracker);
		 * 
		 * 
		 * } return mTracker; }
		 */
}
