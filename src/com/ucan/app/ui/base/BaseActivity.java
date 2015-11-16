package com.ucan.app.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.ucan.app.common.view.SystemBarTintManager;
import com.ucan.app.core.SDKCoreHelper;

public class BaseActivity extends Activity {
	private final static String TAG = "UCAN_BaseActivity";
	private InternalReceiver internalReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
								| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
	}
	  /**
     * hide inputMethod
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null ) {
            View localView = this.getCurrentFocus();
            if(localView != null && localView.getWindowToken() != null ) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(internalReceiver);
		} catch (Exception e) {
		}
	}

	protected final void registerReceiver(String[] actionArray) {
		if (actionArray == null) {
			return;
		}
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction(SDKCoreHelper.ACTION_KICK_OFF);
		for (String action : actionArray) {
			intentfilter.addAction(action);
		}
		if (internalReceiver == null) {
			internalReceiver = new InternalReceiver();
		}
		registerReceiver(internalReceiver, intentfilter);
	}

	private class InternalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null || intent.getAction() == null) {
				return;
			}
			handleReceiver(context, intent);
		}
	}

	protected void handleReceiver(Context context, Intent intent) {
		// 广播处理
		if (intent == null) {
			return;
		}
		if (SDKCoreHelper.ACTION_KICK_OFF.equals(intent.getAction())) {
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	protected void setSatutsBarTint(Activity activity, int color) {
		setTranslucentStatus();
		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		tintManager.setStatusBarTintResource(color);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarDarkMode(true, activity);
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
	protected void setTranslucentStatus() {
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
