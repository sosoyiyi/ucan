package com.ucan.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.ucan.app.R;
import com.ucan.app.common.UCAppManager;
import com.ucan.app.common.model.ClientUser;
import com.ucan.app.common.utils.ToastUtil;
import com.ucan.app.ui.base.BaseActivity;
import com.ucan.app.ui.launcher.LauncherActivity;
import com.ucan.app.ui.launcher.LoginActivity;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

public class Splash extends BaseActivity implements View.OnClickListener {
	private ImageView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		ECHandlerHelper.postDelayedRunnOnUI(initRunnable, 3000);
		view = (ImageView) findViewById(R.id.welcome_logo_iv_dest);
		view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.welcome_logo_iv_dest:
			ToastUtil.showMessage("你好");
			break;
		default:
			break;
		}
	}

	private Runnable initRunnable = new Runnable() {
		@Override
		public void run() {
			init();
		}
	};

	public void init() {
		String account = getAutoRegistAccount();
		if (!TextUtils.isEmpty(account)) {
			startActivity(new Intent(this, LauncherActivity.class));
			finish();
			return;
		} else {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return;

		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
