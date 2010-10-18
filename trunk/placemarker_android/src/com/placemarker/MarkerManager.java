package com.placemarker;

import java.lang.reflect.Method;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.phonegap.DroidGap;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

public class MarkerManager implements Plugin, LocationListener {
	private DroidGap ctx;
	private WebView webView;
	private LocationManager lm;

	public MarkerManager() {
		Log.i("#### MarkerManager","init");
	//	lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
	//	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1l,1l, this);
	}

	public PluginResult execute(String action, JSONArray args) {
		Log.i("#### MarkerManager", action);
		Log.i("### MarkerManager", args.toString());
		try {
			Method method = this.getClass().getDeclaredMethod(action, String.class);
			Object result = method.invoke(this, args.getString(0));
			webView.loadUrl("javascript: alert('" + result + "!');");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String sayHi(String name) {
		Log.i("#### sayHi: ", name);
		return "Hi, " + name;
	}

	public boolean isSynch(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
	}

	public void onDestroy() {
		// TODO Auto-generated method stub
	}

	public void onPause() {
		// TODO Auto-generated method stub
	}

	public void onResume() {
		// TODO Auto-generated method stub
	}

	public void setContext(DroidGap droidGap) {
		ctx = droidGap;
	}

	public void setView(WebView webView) {
		this.webView = webView;
	}

	public void onLocationChanged(Location loc) {
		String lat = String.valueOf(loc.getLatitude());
		String lon = String.valueOf(loc.getLongitude());
		Log.e("GPS", "location changed: lat="+lat+", lon="+lon);
		webView.loadUrl("javascript: onLocationChanged(" + loc.getLatitude() + "," + loc.getLongitude() + ");");
	}

	public void onProviderDisabled(String provider) {
		Log.e("GPS", "provider disabled " + provider);
		
	}

	public void onProviderEnabled(String provider) {
		Log.e("GPS", "provider enabled " + provider);
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.e("GPS", "status changed to " + provider + " [" + status + "]");
		
	}

}