package com.phonegap.placemarker;

import android.app.Activity;
import android.os.Bundle;
import com.phonegap.*;

public class placemarker extends DroidGap {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.loadUrl("file:///android_asset/www/index.html");
	}
}
