package com.ucan.app.ui.launcher;

import java.io.InvalidClassException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.ucan.app.R;
import com.ucan.app.base.manager.UCAppManager;
import com.ucan.app.base.storage.IMessageSqlManager;
import com.ucan.app.chat.chatting.IMChattingHelper;
import com.ucan.app.common.UCContentObservers;
import com.ucan.app.common.adapter.OverflowAdapter;
import com.ucan.app.common.adapter.OverflowAdapter.OverflowItem;
import com.ucan.app.common.dialog.UCAlertDialog;
import com.ucan.app.common.dialog.UCProgressDialog;
import com.ucan.app.common.enums.UCPreferenceSettings;
import com.ucan.app.common.model.ClientUser;
import com.ucan.app.common.utils.CrashHandler;
import com.ucan.app.common.utils.LogUtil;
import com.ucan.app.common.utils.ToastUtil;
import com.ucan.app.common.utils.UCNotificationManager;
import com.ucan.app.common.utils.UCPreferences;
import com.ucan.app.common.utils.VeryUtils;
import com.ucan.app.core.SDKCoreHelper;
import com.ucan.app.ui.base.BaseActivity;
import com.ucan.app.ui.base.OverflowHelper;
import com.umeng.analytics.MobclickAgent;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

public class LauncherActivity extends BaseActivity {
	public static final String TAG = "UCAN.LauncherActivity";
	public static LauncherActivity mLauncherUI;
	private final static int TAB_INDEX_CHATROOM = 0;
	private final static int TAB_INDEX_PRACTISE = 1;
	private final static int TAB_INDEX_GROUP = 2;
	private final static int TAB_INDEX_SETTING = 3;
	private int tabBtnIndex, topBtnIndex;
	private int cTabIndex, cTopIndex;
	private Button mTabBtn[], mTopBtn[];
	private OverflowAdapter.OverflowItem[] mItems;
	private OverflowHelper mOverflowHelper;
	private UCProgressDialog mPostingdialog;
	private boolean mInitActionFlag;
	private InternalReceiver internalReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (mLauncherUI != null) {
			LogUtil.i(LogUtil.getLogUtilsTag(LauncherActivity.class),
					"finish last LauncherUI");
			mLauncherUI.finish();
		}
		super.onCreate(savedInstanceState);
		mLauncherUI = this;
		intRes();
		mOverflowHelper = new OverflowHelper(this);
		MobclickAgent.updateOnlineConfig(this);
		MobclickAgent.setDebugMode(true);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		UCContentObservers.getInstance().initContentObserver();
	}

	private void intRes() {
		setContentView(R.layout.activity_main);
		setSatutsBarTint(this, R.color.default_color);
		initOverflowItems();
		mTabBtn = new Button[4];
		mTabBtn[0] = (Button) findViewById(R.id.tab_btn_chatroom);
		mTabBtn[1] = (Button) findViewById(R.id.tab_btn_activity);
		mTabBtn[2] = (Button) findViewById(R.id.tab_btn_group);
		mTabBtn[3] = (Button) findViewById(R.id.tab_btn_setting);
		mTabBtn[0].setSelected(true);
		mTopBtn = new Button[2];
		mTopBtn[0] = (Button) findViewById(R.id.top_btn_chatroom_left);
		mTopBtn[1] = (Button) findViewById(R.id.top_btn_chatroom_right);
		mTopBtn[0].setSelected(true);
	}

	private void controlPlusSubMenu() {
		if (mOverflowHelper == null) {
			return;
		}

		if (mOverflowHelper.isOverflowShowing()) {
			mOverflowHelper.dismiss();
			return;
		}

		mOverflowHelper.setOverflowItems(mItems);
		mOverflowHelper
				.setOnOverflowItemClickListener(mOverflowItemCliclListener);
		mOverflowHelper.showAsDropDown(findViewById(R.id.btn_menu));
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		LogUtil.d(LogUtil.getLogUtilsTag(LauncherActivity.class), " onKeyDown");
		if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
				&& event.getAction() == KeyEvent.ACTION_UP) {
			// dismiss PlusSubMenuHelper
			if (mOverflowHelper != null && mOverflowHelper.isOverflowShowing()) {
				mOverflowHelper.dismiss();
				return true;
			}
		}

		// 这里可以进行设置全局性的menu菜单的判断
		if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			doTaskToBackEvent();
		}

		try {

			return super.dispatchKeyEvent(event);
		} catch (Exception e) {
			LogUtil.e(LogUtil.getLogUtilsTag(LauncherActivity.class),
					"dispatch key event catch exception " + e.getMessage());
		}

		return false;
	}

	public void doTaskToBackEvent() {
		moveTaskToBack(true);

	}

	public void onTopBtnClick(View view) {
		switch (view.getId()) {
		case R.id.top_btn_chatroom_left:
			topBtnIndex = 0;
			break;
		case R.id.top_btn_chatroom_right:
			topBtnIndex = 1;
			break;
		case R.id.btn_menu:
			controlPlusSubMenu();
			break;
		}
		if (cTopIndex != topBtnIndex) {
		}
		mTopBtn[cTopIndex].setSelected(false);
		// 把当前btn设为选中状态
		mTopBtn[topBtnIndex].setSelected(true);
		cTopIndex = topBtnIndex;
	}

	public void onTabBtnClick(View view) {
		switch (view.getId()) {
		case R.id.tab_btn_chatroom:
			tabBtnIndex = TAB_INDEX_CHATROOM;
			break;
		case R.id.tab_btn_activity:
			tabBtnIndex = TAB_INDEX_PRACTISE;
			break;
		case R.id.tab_btn_group:
			tabBtnIndex = TAB_INDEX_GROUP;
			break;
		case R.id.tab_btn_setting:
			tabBtnIndex = TAB_INDEX_SETTING;
			break;
		}
		if (cTabIndex != tabBtnIndex) {
			/*
			 * FragmentTransaction trx = getSupportFragmentManager()
			 * .beginTransaction(); trx.hide(fragments[currentTabIndex]); if
			 * (!fragments[index].isAdded()) { trx.add(R.id.fragment_container,
			 * fragments[index]); } trx.show(fragments[index]).commit();
			 */
		}
		mTabBtn[cTabIndex].setSelected(false);
		// 把当前btn设为选中状态
		mTabBtn[tabBtnIndex].setSelected(true);
		cTabIndex = tabBtnIndex;
	}

	private final void registerReceiver(String[] actionArray) {
		if (actionArray == null) {
			return;
		}
		IntentFilter intentfilter = new IntentFilter();
		for (String action : actionArray) {
			intentfilter.addAction(action);
		}
		if (internalReceiver == null) {
			internalReceiver = new InternalReceiver();
		}
		registerReceiver(internalReceiver, intentfilter);
	}

	private void reTryConnect() {
		ECDevice.ECConnectState connectState = SDKCoreHelper.getConnectState();
		if (connectState == null
				|| connectState == ECDevice.ECConnectState.CONNECT_FAILED) {

			if (!TextUtils.isEmpty(getAutoRegistAccount())) {
				SDKCoreHelper.init(this);
			}
		}
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

	private class InternalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null || TextUtils.isEmpty(intent.getAction())) {
				return;
			}
			LogUtil.d(TAG, "[onReceive] action:" + intent.getAction());
			if (SDKCoreHelper.ACTION_SDK_CONNECT.equals(intent.getAction())) {
				doInitAction();
				updateConnectState();
			} else if (SDKCoreHelper.ACTION_KICK_OFF.equals(intent.getAction())) {
				String kickoffText = intent.getStringExtra("kickoffText");
				handlerKickOff(kickoffText);
			}
		}
	}

	public void updateConnectState() {
		ECDevice.ECConnectState connect = SDKCoreHelper.getConnectState();
		if (connect == ECDevice.ECConnectState.CONNECTING) {
		} else if (connect == ECDevice.ECConnectState.CONNECT_FAILED) {
			ToastUtil.showMessage("连接失败");
			reTryConnect();
		} else if (connect == ECDevice.ECConnectState.CONNECT_SUCCESS) {
			ToastUtil.showMessage("连接成功");
		}
	}

	/**
	 * 处理一些初始化操作
	 */
	private void doInitAction() {
		if (SDKCoreHelper.getConnectState() == ECDevice.ECConnectState.CONNECT_SUCCESS
				&& !mInitActionFlag) {

			// 检测当前的版本
			SDKCoreHelper.SoftUpdate mSoftUpdate = SDKCoreHelper.mSoftUpdate;
			if (mSoftUpdate != null) {
				if (VeryUtils.checkUpdater(mSoftUpdate.version)) {
					boolean force = mSoftUpdate.mode == 2;
					showUpdaterTips(force);
					if (force) {
						return;
					}
				}
			}

			IMChattingHelper.getInstance().getPersonInfo();
			// 检测离线消息
			checkOffineMessage();
			mInitActionFlag = true;
		}
	}

	void showProcessDialog() {
		mPostingdialog = new UCProgressDialog(LauncherActivity.this,
				R.string.login_posting_submit);
		mPostingdialog.show();
	}

	public void onNetWorkNotify(ECDevice.ECConnectState connect) {
		updateConnectState();

	}

	private void checkOffineMessage() {
		if (SDKCoreHelper.getConnectState() != ECDevice.ECConnectState.CONNECT_SUCCESS) {
			return;
		}
		ECHandlerHelper handlerHelper = new ECHandlerHelper();
		handlerHelper.postDelayedRunnOnThead(new Runnable() {
			@Override
			public void run() {
				boolean result = IMChattingHelper.isSyncOffline();
				if (!result) {
					ECHandlerHelper.postRunnOnUI(new Runnable() {
						@Override
						public void run() {
							disPostingLoading();
						}
					});
					IMChattingHelper.checkDownFailMsg();
				}
			}
		}, 1000);
	}

	UCAlertDialog showUpdaterTipsDialog = null;

	private void showUpdaterTips(final boolean force) {
		if (showUpdaterTipsDialog != null) {
			return;
		}
		String negativeText = getString(force ? R.string.settings_logout
				: R.string.update_next);
		String msg = getString(R.string.new_update_version);
		showUpdaterTipsDialog = UCAlertDialog.buildAlert(this, msg,
				negativeText, getString(R.string.app_update),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						showUpdaterTipsDialog = null;
						if (force) {
							try {
								UCPreferences
										.savePreference(
												UCPreferenceSettings.SETTINGS_FULLY_EXIT,
												true, true);
							} catch (InvalidClassException e) {
								e.printStackTrace();
							}
							restartAPP();
						}
					}
				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						UCAppManager.startUpdater(LauncherActivity.this);
						// restartAPP();
						showUpdaterTipsDialog = null;
					}
				});

		showUpdaterTipsDialog.setTitle(R.string.app_tip);
		showUpdaterTipsDialog.setDismissFalse();
		showUpdaterTipsDialog.setCanceledOnTouchOutside(false);
		showUpdaterTipsDialog.setCancelable(false);
		showUpdaterTipsDialog.show();
	}

	private void disPostingLoading() {
		if (mPostingdialog != null && mPostingdialog.isShowing()) {
			mPostingdialog.dismiss();
		}
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
		if (internalReceiver != null) {
			unregisterReceiver(internalReceiver);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		CrashHandler.getInstance().setContext(this);
		// 统计时长
		MobclickAgent.onResume(this);
		registerReceiver(new String[] {
				IMChattingHelper.INTENT_ACTION_SYNC_MESSAGE,
				SDKCoreHelper.ACTION_SDK_CONNECT });
		Intent intent = getIntent();
		if (intent != null && intent.getIntExtra("launcher_from", -1) == 0x06) {
			// 从Login过来,注册SDK,SDK登陆
			SDKCoreHelper.init(this);
		}
		OnUpdateMsgUnreadCounts();
	}

	public void OnUpdateMsgUnreadCounts() {
		int unreadCount = IMessageSqlManager.qureyAllSessionUnreadCount();
		int notifyUnreadCount = IMessageSqlManager.getUnNotifyUnreadCount();
		int count = unreadCount;
		if (unreadCount >= notifyUnreadCount) {
			count = unreadCount - notifyUnreadCount;
		}
		if (count > 0) {
			findViewById(R.id.btn_unread).setBackgroundResource(
					R.drawable.icon_topbar_message_new);
		} else {
			findViewById(R.id.btn_unread).setBackgroundResource(
					R.drawable.icon_topbar_message);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	void initOverflowItems() {
		if (mItems == null) {
			if (SDKCoreHelper.getInstance().isSupportMedia()) {
				mItems = new OverflowAdapter.OverflowItem[3];
				mItems[0] = new OverflowAdapter.OverflowItem(
						getString(R.string.main_plus_meeting_voice));
				mItems[0].setIcon(R.drawable.headset);
				mItems[1] = new OverflowAdapter.OverflowItem(
						getString(R.string.main_plus_meeting_video));
				mItems[1].setIcon(R.drawable.camera);
				mItems[2] = new OverflowAdapter.OverflowItem(
						getString(R.string.main_plus_search));
				mItems[2].setIcon(R.drawable.search);

			} else {
				mItems = new OverflowAdapter.OverflowItem[1];
				mItems[0] = new OverflowAdapter.OverflowItem(
						getString(R.string.main_plus_search));
				mItems[0].setIcon(R.drawable.search);
			}
		}

	}

	private final AdapterView.OnItemClickListener mOverflowItemCliclListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			controlPlusSubMenu();

			OverflowItem overflowItem = mItems[position];
			String title = overflowItem.getTitle();

			if (getString(R.string.main_plus_meeting_voice).equals(title)) {
				// 语音房间

			} else if (getString(R.string.main_plus_meeting_video)
					.equals(title)) {

				// 视频房间

			} else if (getString(R.string.main_plus_search).equals(title)) {

				startActivity(new Intent(LauncherActivity.this,
						SearchActivity.class));
				// 搜索
			}
		}

	};
}
