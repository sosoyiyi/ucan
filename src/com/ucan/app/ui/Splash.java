package com.ucan.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.ucan.app.R;
import com.ucan.app.base.manager.UCAppManager;
import com.ucan.app.common.model.ClientUser;
import com.ucan.app.core.SDKCoreHelper;
import com.ucan.app.ui.activity.LoginActivity;
import com.ucan.app.ui.activity.MainActivity;
import com.ucan.app.ui.base.BaseActivity;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

public class Splash extends BaseActivity implements View.OnClickListener {
	private ImageView view;
	private String account;
	private Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		ctx = this;
		account = getAutoRegistAccount();
		if (!TextUtils.isEmpty(account)) {
			ClientUser user = new ClientUser("").from(account);
			UCAppManager.setClientUser(user);
			SDKCoreHelper.init(ctx);
		}
		ECHandlerHelper.postDelayedRunnOnUI(initRunnable, 3000);
		view = (ImageView) findViewById(R.id.welcome_logo_iv_dest);
		view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.welcome_logo_iv_dest:
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
		if (!TextUtils.isEmpty(account)) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("launch_from", 0x28);
			startActivity(intent);
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
