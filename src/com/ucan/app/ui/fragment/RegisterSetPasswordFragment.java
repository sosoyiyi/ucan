package com.ucan.app.ui.fragment;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ucan.app.R;
import com.ucan.app.common.utils.ToastUtil;
import com.ucan.app.common.utils.VeryUtils;
import com.ucan.app.common.view.DefineInputView;

public class RegisterSetPasswordFragment extends Fragment implements
		View.OnClickListener {
	private OnSyncDataListener mListener;
	private View view;
	private Button nextBtn;
	private DefineInputView pwdInput, pwdagInput;
	private String password, passwordag;

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
		view = inflater.inflate(R.layout.fragment_register_setpassword,
				container, false);
		pwdInput = (DefineInputView) view.findViewById(R.id.register_password);
		pwdagInput = (DefineInputView) view
				.findViewById(R.id.register_password_again);
		nextBtn = (Button) view.findViewById(R.id.pwd_next_btn);
		nextBtn.setOnClickListener(this);
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
		if (pwdInput != null) {
			pwdInput.setText("");
			pwdagInput.setText("");
		}

	}

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pwd_next_btn:
			password = pwdInput.getText().toString();
			passwordag = pwdagInput.getText().toString();
			if (TextUtils.isEmpty(password)) {
				ToastUtil.showMessage("请输入密码");
				return;
			}
			if (!VeryUtils.validPassword(password)) {
				ToastUtil.showMessage("请输入由8至16位数字和字母组成的密码");
				return;
			}
			if (!password.equals(passwordag)) {
				ToastUtil.showMessage("两次输入的密码不一致");
				return;
			}
			mListener.OnPushData(new HashMap<String, String>() {
				private static final long serialVersionUID = 6793681013466248605L;
				{
					put("password", VeryUtils.md5(password));
				}
			});
			break;

		}

	}
}
