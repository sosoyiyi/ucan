package com.ucan.app.ui.activity;

import java.io.InvalidClassException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.ucan.app.R;
import com.ucan.app.base.manager.UCAppManager;
import com.ucan.app.base.storage.ContactSqlManager;
import com.ucan.app.common.contacts.ContactLogic;
import com.ucan.app.common.contacts.UCContacts;
import com.ucan.app.common.dialog.UCProgressDialog;
import com.ucan.app.common.enums.UCPreferenceSettings;
import com.ucan.app.common.http.HttpRequestManager;
import com.ucan.app.common.model.ClientUser;
import com.ucan.app.common.utils.ToastUtil;
import com.ucan.app.common.utils.UCPreferences;
import com.ucan.app.common.utils.VeryUtils;
import com.ucan.app.common.view.CircularImage;
import com.ucan.app.common.view.DefineInputView;
import com.ucan.app.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity implements TextWatcher,
		View.OnClickListener {
	private CircularImage cover_user_photo;
	private DefineInputView mobileInput, pwdInput;
	private Button signInBtn, signUpBtn;
	private String account, password;
	private UCProgressDialog mPostingdialog;
	private Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		initRes();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (!TextUtils.isEmpty(mobileInput.getText())
				&& !TextUtils.isEmpty(pwdInput.getText())) {
			signInBtn.setEnabled(true);
		} else {
			signInBtn.setEnabled(false);
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	private void initRes() {
		setContentView(R.layout.activity_login);
		setTranslucentStatus();
		cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
		cover_user_photo.setBorderWidth(1);
		cover_user_photo.setBorderColor(getResources().getColor(
				R.color.user_level3_Gold));
		cover_user_photo.setImageResource(R.drawable.user_photo);

		mobileInput = (DefineInputView) findViewById(R.id.login_mobile);
		mobileInput.addTextChangedListener(this);
		pwdInput = (DefineInputView) findViewById(R.id.login_password);
		pwdInput.addTextChangedListener(this);

		signInBtn = (Button) findViewById(R.id.login_signin);
		signInBtn.setOnClickListener(this);
		signUpBtn = (Button) findViewById(R.id.login_signup);
		signUpBtn.setOnClickListener(this);
	}

	HttpRequestManager.OnAsyncResponseListener cb = new HttpRequestManager.OnAsyncResponseListener() {

		@Override
		public void onSuccess(int code, List<HashMap<String, String>> data,
				int length) {
			if (code == 200) {
				try {
					saveAccount();
				} catch (InvalidClassException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(ctx, MainActivity.class);
				intent.putExtra("launch_from", 0x06);
				// 注册成功跳转
				startActivity(intent);
				finish();
			} else {
				mPostingdialog.dismiss();
				ToastUtil.showMessage("用户名或密码错误");
			}

		}

		@Override
		public void onError(int statu, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			ToastUtil.showMessage("无法连接服务器");
			mPostingdialog.dismiss();
			arg3.printStackTrace();

		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_signin:
			hideSoftKeyboard();
			account = mobileInput.getText().toString().trim();
			password = pwdInput.getText().toString().trim();
			mPostingdialog = new UCProgressDialog(ctx, R.string.login_posting);
			mPostingdialog.show();
			HashMap<String, String> params = new HashMap<String, String>();
			try {
				params.put("account", account);
				params.put("password",
						URLEncoder.encode(VeryUtils.md5(password), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpRequestManager.getInstance().isEffectiveUser(params, cb);
			break;
		case R.id.login_signup:
			Intent intent = new Intent(ctx, RegisterActivity.class);
			startActivityForResult(intent, 0x2a);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0x2a) {

		}
	}

	private void saveAccount() throws InvalidClassException {
		ClientUser user = new ClientUser(account);
		UCAppManager.setClientUser(user);
		UCPreferences.savePreference(UCPreferenceSettings.SETTINGS_REGIST_AUTO,
				user.toString(), true);
		// 初始化了在线客服
		ArrayList<UCContacts> objects = ContactLogic.initContacts();
		objects = ContactLogic.converContacts(objects);
		ContactSqlManager.insertContacts(objects);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
