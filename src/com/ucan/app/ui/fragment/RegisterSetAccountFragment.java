package com.ucan.app.ui.fragment;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import cn.my7g.qjlink.sdk.QJLinkManager;
import cn.my7g.qjlink.sdk.http.OnLoadDataListener;

import com.ucan.app.R;
import com.ucan.app.common.http.HttpRequestManager;
import com.ucan.app.common.utils.LogUtil;
import com.ucan.app.common.utils.ToastUtil;
import com.ucan.app.common.utils.VeryUtils;
import com.ucan.app.common.view.DefineInputView;

public class RegisterSetAccountFragment extends Fragment implements
		View.OnClickListener {

	private TimeCount time;
	private Button nextBtn, getVcodeBtn;
	private String account, vcode;
	private EditText vcodeInput;
	private DefineInputView mobileInput;
	private View view;
	private OnSyncDataListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnSyncDataListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "must implement OnPushDataListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_register_setaccount,
				container, false);
		mobileInput = (DefineInputView) view.findViewById(R.id.register_mobile);
		vcodeInput = (EditText) view.findViewById(R.id.register_verifycode);
		getVcodeBtn = (Button) view.findViewById(R.id.register_get_verifycode);
		getVcodeBtn.setOnClickListener(this);
		nextBtn = (Button) view.findViewById(R.id.acc_next_btn);
		nextBtn.setOnClickListener(this);
		time = new TimeCount(60000, 1000);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		if (vcodeInput != null)
			vcodeInput.setText("");

	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void requestVerifyCode(String account) {
		QJLinkManager.getInstance(getActivity().getApplicationContext())
				.requestPassword(account, new OnLoadDataListener() {
					@Override
					public void onError(String result) {
						LogUtil.e(result);
						ToastUtil.showMessage(R.string.http_error_2);
					}

					@Override
					public void onSuccess(String result) {
						LogUtil.e(result);
						try {
							JSONObject rs = new JSONObject(result);
							if ((Integer) rs.get("code") != 0) {
								ToastUtil.showMessage(new JSONObject(result)
										.get("msg").toString());
								return;
							}
							time.start();
						} catch (JSONException e) {
							ToastUtil.showMessage(R.string.http_error_2);
							e.printStackTrace();
						}
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_get_verifycode:
			account = mobileInput.getText().toString().trim();
			if (TextUtils.isEmpty(account)) {
				ToastUtil.showMessage("请输入手机号");
				return;
			}
			if (!VeryUtils.validPhoneNumber(account)) {
				ToastUtil.showMessage("请输入正确的手机号码");
				return;
			}
			HttpRequestManager.getInstance().isEffectiveUser(
					new HashMap<String, String>() {
						private static final long serialVersionUID = -7130671072591476015L;
						{
							put("account", account);
						}
					}, new HttpRequestManager.OnAsyncResponseListener() {
						@Override
						public void onSuccess(int code,
								List<HashMap<String, String>> data, int length) {
							if (code == 200) {
								ToastUtil.showMessage("手机号已经注册");
								return;
							}
							requestVerifyCode(account);
						}

						@Override
						public void onError(int statu, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							arg3.printStackTrace();
						}
					});
			break;
		case R.id.acc_next_btn:
			account = mobileInput.getText().toString().trim();
			if (TextUtils.isEmpty(account)) {
				ToastUtil.showMessage("请输入手机号");
				return;
			}
			vcode = vcodeInput.getText().toString().trim();
			if (TextUtils.isEmpty(vcode)) {
				ToastUtil.showMessage("验证码不能为空");
				return;
			}
			mListener.OnPushData(new HashMap<String, String>() {
				private static final long serialVersionUID = -6261061978150358762L;
				{
					put("account", account);
				}
			});
			  //requestLogin(mListener);
			break;
		}
	}

	private void requestLogin(OnSyncDataListener onPushDataListener) {
		QJLinkManager.getInstance(getActivity()).requestLogin(account, vcode,
				new OnLoadDataListener() {
					@Override
					public void onError(String result) {
						LogUtil.e(result);
						ToastUtil.showMessage(R.string.http_error_2);
					}

					@Override
					public void onSuccess(String result) {
						LogUtil.e(result);
						try {
							JSONObject rs = new JSONObject(result);
							if ((Integer) rs.get("code") != 0) {
								ToastUtil.showMessage(new JSONObject(result)
										.get("msg").toString());
								return;
							}
							mListener.OnPushData(new HashMap<String, String>() {
								private static final long serialVersionUID = -6261061978150358762L;
								{
									put("account", account);
								}
							});
						} catch (JSONException e) {
							ToastUtil.showMessage(R.string.http_error_2);
							e.printStackTrace();
						}
					}
				});
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			getVcodeBtn.setText("重新验证");
			getVcodeBtn.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			getVcodeBtn.setClickable(false);
			getVcodeBtn.setText(millisUntilFinished / 1000 + "秒");
		}
	}

}
