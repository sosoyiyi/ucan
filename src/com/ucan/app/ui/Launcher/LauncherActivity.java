package com.ucan.app.ui.Launcher;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.ucan.app.R;
import com.ucan.app.common.dialog.UCAlertDialog;
import com.ucan.app.common.utils.LogUtil;
import com.ucan.app.common.utils.UCNotificationManager;
import com.ucan.app.ui.base.BaseActivity;
import com.yuntongxun.ecsdk.ECDevice;

public class LauncherActivity extends BaseActivity {
	public static LauncherActivity mLauncherUI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mLauncherUI != null) {
			LogUtil.i(LogUtil.getLogUtilsTag(LauncherActivity.class),
					"finish last LauncherUI");
			mLauncherUI.finish();
		}
		setContentView(R.layout.activity_main);
	}

	public void handlerKickOff(String kickoffText) {
		if (isFinishing()) {
			return;
		}
		UCAlertDialog buildAlert = UCAlertDialog.buildAlert(this, kickoffText,
				getString(R.string.dialog_btn_confim),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						UCNotificationManager.getInstance()
								.forceCancelNotification();
						restartAPP();
					}
				});
		buildAlert.setTitle("异地登陆");
		buildAlert.setCanceledOnTouchOutside(false);
		buildAlert.setCancelable(false);
		buildAlert.show();
	}

	public void restartAPP() {

		ECDevice.unInitial();
		Intent intent = new Intent(this, LauncherActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
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

}
