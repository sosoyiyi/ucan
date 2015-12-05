package com.ucan.app.ui.fragment;

import java.util.Arrays;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.ucan.app.R;
import com.ucan.app.common.utils.LogUtil;
import com.ucan.app.ui.dpizarro.uipicker.PickerUI;
import com.ucan.app.ui.dpizarro.uipicker.PickerUISettings;

public class RegisterSetBasicInfoFragment extends Fragment implements
		View.OnClickListener {
	private View view;
	private Button fromBtn;
	private PickerUI mProvince;
	private String nickNameString;
	private String sexString;
	private String fromString;
	private List<String> province;
	private int currentPosition;
	private List<String> city;
	private int[] cities = { R.array.ah_city, R.array.bj_city, R.array.cq_city,
			R.array.fj_city, R.array.gs_city, R.array.gx_city, R.array.gz_city,
			R.array.hb_city, R.array.hn_city, R.array.hlj_city,
			R.array.hub_city, R.array.hun_city, R.array.hain_city,
			R.array.jl_city, R.array.jx_city, R.array.js_city, R.array.ln_city,
			R.array.nx_city, R.array.nmg_city, R.array.qh_city,
			R.array.sh_city, R.array.sx_city, R.array.sd_city,
			R.array.ssx_city, R.array.sc_city, R.array.tj_city,
			R.array.xj_city, R.array.xz_city, R.array.yn_city, R.array.zj_city,
			R.array.xg_city, R.array.am_city, R.array.tw_city,
			R.array.countries };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_register_setbasicinfo,
				container, false);
		fromBtn = (Button) view.findViewById(R.id.register_fromBtn);
		fromBtn.setOnClickListener(this);
		mProvince = (PickerUI) view.findViewById(R.id.picker_province);
		// Populate list
		province = Arrays.asList(getActivity().getResources().getStringArray(
				R.array.province));
		// Populate list
		mProvince.setColorTextCenter(getActivity().getResources().getColor(
				R.color.background_picker));
		mProvince.setColorTextNoCenter(getActivity().getResources().getColor(
				R.color.background_picker));
		mProvince.setBackgroundColorPanel(getActivity().getResources()
				.getColor(R.color.background_picker));
		mProvince.setLinesColor(getActivity().getResources().getColor(
				R.color.background_picker));
		mProvince
				.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
					@Override
					public void onItemClickPickerUI(int which, int position,
							String valueResult) {
						LogUtil.e("press", String.valueOf(mProvince.isClickable()));
						fromBtn.setText("我来自 " + valueResult);
						LogUtil.e("position", String.valueOf(position));
					}
				});
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
		if (mProvince!=null&&mProvince.isPanelShown()) {
			mProvince.slide(0);
		}
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_fromBtn:
			hideSoftKeyboard();
			PickerUISettings pickerUISettings = new PickerUISettings.Builder()
					.withItems(province).withAutoDismiss(false)
					.withItemsClickables(true).withUseBlur(false).build();
			mProvince.setSettings(pickerUISettings);
			mProvince.slide(0);
			fromBtn.setText("我来自 安徽");
			break;

		}
	}

	public void hideSoftKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			View localView = getActivity().getCurrentFocus();
			if (localView != null && localView.getWindowToken() != null) {
				IBinder windowToken = localView.getWindowToken();
				inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
			}
		}
	}

}
