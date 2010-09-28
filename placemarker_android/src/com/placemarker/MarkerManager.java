package com.placemarker;

import java.lang.reflect.Method;

import org.json.JSONArray;

import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;

import com.phonegap.DroidGap;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

public class MarkerManager implements Plugin {
	private DroidGap ctx;										// DroidGap object
	private WebView webView;									// Webview object

	public MarkerManager() {
		// TODO Auto-generated constructor stub
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

}
