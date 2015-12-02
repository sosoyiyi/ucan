package com.ucan.app.common.http;

import java.util.HashMap;

import com.squareup.okhttp.OkHttpClient;

public class HttpHelper {

	private OkHttpClient mOkHttpClient = new OkHttpClient();

	private String server = AsyncServerConfig.asyncServer();

	private static HttpHelper instance;

	public static HttpHelper getInstanc() {

		if (instance == null) {

			instance = new HttpHelper();
		}
		return instance;
	}

	public void getUser(HashMap<String, String> params, String method) {
		
		String url=server+method;
		

	}
}