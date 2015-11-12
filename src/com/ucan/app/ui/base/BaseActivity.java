package com.ucan.app.ui.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends Activity {
	private final static String TAG = "UCAN_BaseActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTranslucentStatus();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void setTranslucentStatus() {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			winParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		} else {
			winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		}
		win.setAttributes(winParams);
	}

}
