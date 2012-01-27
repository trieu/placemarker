package com.placemarker;

import java.lang.reflect.Method;

import org.json.JSONArray;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.phonegap.DroidGap;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

public class MarkerManager extends Plugin {
	private DroidGap ctx;
	private WebView webView;
	private LocationManager lm;

	public MarkerManager() {
		Log.i("#### MarkerManager","init");
		//	lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		//	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1l,1l, this);
	}

	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		Log.i("#### MarkerManager", action);
		Log.i("### MarkerManager", args.toString());
		try {
			Method method = this.getClass().getDeclaredMethod(action, String.class);
			Object result = method.invoke(this, args.getString(0));
			webView.loadUrl("javascript: navigator.notification.alert('" + result + "!');");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String sayHi(String name) {
		Log.i("#### sayHi: ", name);
		return "Hi, " + name;
	}



	public void setContext(DroidGap droidGap) {
		ctx = droidGap;
	}

	@Override
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