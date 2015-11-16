package com.ucan.app.ui.Launcher;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.ucan.app.R;
import com.ucan.app.common.view.CircularImage;
import com.ucan.app.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity implements TextWatcher {
	private CircularImage cover_user_photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTranslucentStatus();
		initRes();
	}

	private void initRes() {
		cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
		cover_user_photo.setBorderWidth(4);
		cover_user_photo.setBorderColor(getResources().getColor(
				R.color.user_level3_Gold));
		cover_user_photo.setImageResource(R.drawable.user_photo);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

}
