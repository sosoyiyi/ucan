package com.ucan.app.base.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import com.ucan.app.common.http.AsyncServerConfig;
import com.ucan.app.common.http.HttpRequestManager;
import com.ucan.app.common.http.NetCallBack;
import com.ucan.app.common.utils.Decrypt;

public class UCAccountManager {
	private static final int MAX_RETRIES = 2;
	private static final int MAX_TIMEOUT = 10;

	public UCAccountManager() {
	}

	public static void isEffectiveUser(String account, String password,
			NetCallBack cb) {
		HashMap<String, String> params = new HashMap<String, String>();
		HttpRequestManager hm = new HttpRequestManager(MAX_RETRIES, MAX_TIMEOUT);
		StringBuilder sb = new StringBuilder();
		sb.append(AsyncServerConfig.asyncServer());
		sb.append("/login");
		sb.append("/get");

		try {
			params.put("username", account);
			params.put("password", URLEncoder.encode(
					Decrypt.encrypt(AsyncServerConfig.asyncDesKey(), password),
					"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		hm.clientGet(sb.toString(), params, cb);
	}

	private String convertParam(HashMap<String, String> params) {
		return null;

	}

}
