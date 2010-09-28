package com.phonegap.placemarker;

import android.os.Bundle;

import com.phonegap.DroidGap;

public class PlaceMarkerApp extends DroidGap {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.loadUrl("file:///android_asset/www/index.html");
	}
}
