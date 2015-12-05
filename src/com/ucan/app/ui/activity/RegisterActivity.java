package com.ucan.app.ui.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.ucan.app.R;
import com.ucan.app.common.dialog.UCProgressDialog;
import com.ucan.app.common.utils.LogUtil;
import com.ucan.app.ui.base.BaseActivity;
import com.ucan.app.ui.fragment.OnSyncDataListener;
import com.ucan.app.ui.fragment.RegisterSetAccountFragment;
import com.ucan.app.ui.fragment.RegisterSetBasicInfoFragment;
import com.ucan.app.ui.fragment.RegisterSetPasswordFragment;

public class RegisterActivity extends BaseActivity implements
		OnSyncDataListener {

	private UCProgressDialog mPostingdialog;
	private Context ctx;

	private Fragment[] fragments;
	HashMap<String, String> params = new HashMap<String, String>();
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		initRes();
	}

	public void doRegister() {
		mPostingdialog = new UCProgressDialog(this, R.string.register_posting);
		mPostingdialog.show();
	}

	private void initRes() {
		setContentView(R.layout.activity_register);
		setTranslucentStatus();
//		fragments = new Fragment[] { new RegisterSetAccountFragment(),
//				new RegisterSetPasswordFragment(),
//				new RegisterSetBasicInfoFragment() };
		
		fragments = new Fragment[] { new RegisterSetBasicInfoFragment(),
				new RegisterSetPasswordFragment(),
				new RegisterSetAccountFragment() };
		getFragmentManager().beginTransaction()
				.add(R.id.fragment_contain, fragments[0])
				.add(R.id.fragment_contain, fragments[1])
				.add(R.id.fragment_contain, fragments[2]).hide(fragments[1])
				.hide(fragments[2]).commit();
		currentIndex = 0;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (currentIndex <= 0) {
				return super.dispatchKeyEvent(event);
			}
			currentIndex--;
			getFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.anim.fragment_slide_left_in,
							R.anim.fragment_slide_right_out)
					.hide(fragments[currentIndex + 1])
					.show(fragments[currentIndex]).commit();
			return false;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void OnPushData(HashMap<String, String> param) {
		if (currentIndex >= 2) {
			doRegister();
			return;
		}
		currentIndex++;
		Iterator<Entry<String, String>> iter = param.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
					.next();
			String key = entry.getKey();
			String val = entry.getValue();
			params.put(key, val);
		}
		LogUtil.e(params.toString());
		getFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.fragment_slide_right_in,
						R.anim.fragment_slide_left_out)
				.hide(fragments[currentIndex - 1])
				.show(fragments[currentIndex]).commit();
	}

}
