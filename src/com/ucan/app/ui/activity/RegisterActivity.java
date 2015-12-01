package com.ucan.app.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.my7g.qjlink.sdk.QJLinkManager;
import cn.my7g.qjlink.sdk.http.OnLoadDataListener;

import com.ucan.app.R;
import com.ucan.app.common.dialog.UCProgressDialog;
import com.ucan.app.common.utils.LogUtil;
import com.ucan.app.common.utils.ToastUtil;
import com.ucan.app.common.view.DefineInputView;
import com.ucan.app.ui.base.BaseActivity;

public class RegisterActivity extends BaseActivity implements TextWatcher,
		View.OnClickListener {
	private DefineInputView mobileInput, pwdInput;
	private EditText vcodeInput;
	private Button signUpBtn, getVcodeBtn;
	private String account, password, verifyCode;
	private UCProgressDialog mPostingdialog;
	private Context ctx;
	private TimeCount time;
	private Boolean isCountDown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		initRes();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (!TextUtils.isEmpty(mobileInput.getText())
				&& !TextUtils.isEmpty(pwdInput.getText())
				&& !TextUtils.isEmpty(vcodeInput.getText())) {
			signUpBtn.setClickable(true);
		} else {
			signUpBtn.setClickable(false);
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
		setContentView(R.layout.activity_register);
		setTranslucentStatus();
		time = new TimeCount(60000, 1000);
		mobileInput = (DefineInputView) findViewById(R.id.regist_mobile);
		pwdInput = (DefineInputView) findViewById(R.id.regist_password);
		vcodeInput = (EditText) findViewById(R.id.regist_verifycode);
		getVcodeBtn = (Button) findViewById(R.id.regist_get_verifycode);
		getVcodeBtn.setOnClickListener(this);
		signUpBtn = (Button) findViewById(R.id.regist_signup);
		signUpBtn.setOnClickListener(this);
	}

	OnLoadDataListener onLoadDataListener = new OnLoadDataListener() {
		@Override
		public void onError(String result) {
			LogUtil.e(result);
			try {
				ToastUtil.showMessage(new JSONObject(result).get("msg")
						.toString());
			} catch (JSONException e) {
				ToastUtil.showMessage("网络连接异常");
				e.printStackTrace();
			}
		}

		@Override
		public void onSuccess(String result) {
			LogUtil.e(result);
			try {
				JSONObject rs = new JSONObject(result);
				if (rs.get("msg").equals("请60秒后再重发验证码")) {
					ToastUtil.showMessage(new JSONObject(result).get("msg")
							.toString());
				} else {
					time.start();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_signin:
			hideSoftKeyboard();

			account = mobileInput.getText().toString().trim();
			password = pwdInput.getText().toString().trim();
			verifyCode = vcodeInput.getText().toString().trim();
			QJLinkManager.getInstance(getApplicationContext()).requestLogin(
					account, verifyCode, onLoadDataListener);
			mPostingdialog = new UCProgressDialog(this,
					R.string.register_posting);
			mPostingdialog.show();
			break;
		case R.id.login_signup:
			break;
		case R.id.regist_get_verifycode:
			account = mobileInput.getText().toString().trim();
			if (TextUtils.isEmpty(account)) {
				ToastUtil.showMessage("请先输入手机号码");
			} else {
				QJLinkManager.getInstance(getApplicationContext())
						.requestPassword(account, onLoadDataListener);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			getVcodeBtn.setText("重新验证");
			getVcodeBtn.setClickable(true);
			isCountDown = false;
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			isCountDown = true;
			getVcodeBtn.setClickable(false);
			getVcodeBtn.setText(millisUntilFinished / 1000 + "秒");
		}
	}

}
