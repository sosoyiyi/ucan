package com.ucan.app.common.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ucan.app.common.utils.LogUtil;

public class HttpRequestManager {

	private static AsyncHttpClient httpClient;
	private static ObjectMapper mapper = new ObjectMapper();
	private static HttpRequestManager instance;
	private static final int MAX_RETRIES = 2;
	private static final int MAX_TIMEOUT = 10;

	public static HttpRequestManager getInstance() {
		if (instance == null) {
			instance = new HttpRequestManager();
		}

		return instance;

	}

	public HttpRequestManager() {
		httpClient = new AsyncHttpClient();
		httpClient.setMaxRetriesAndTimeout(MAX_RETRIES, MAX_TIMEOUT);
	}

	public void registerUser(HashMap<String, String> params,
			OnAsyncResponseListener onAsyncResponseListener) {
		StringBuilder sb = new StringBuilder();
		sb.append(AsyncServerConfig.asyncServer());
		sb.append("/userlogin");
		sb.append("/putInfo");
		clientGet(sb.toString(), params, onAsyncResponseListener);
	}

	public void isEffectiveUser(HashMap<String, String> params,
			OnAsyncResponseListener onAsyncResponseListener) {
		StringBuilder sb = new StringBuilder();
		sb.append(AsyncServerConfig.asyncServer());
		sb.append("/userlogin");
		sb.append("/getInfo");
		clientGet(sb.toString(), params, onAsyncResponseListener);
	}

	private void clientGet(String url, HashMap<String, String> params,
			OnAsyncResponseListener onAsyncResponseListener) {
		try {
			httpClient.addHeader("params", URLEncoder.encode(
					mapper.writeValueAsString(params), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpClient.get(url, onAsyncResponseListener);
	}

	public static abstract class OnAsyncResponseListener extends
			AsyncHttpResponseHandler {
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			onError(arg0, arg1, arg2, arg3);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			try {
				HashMap<String, Object> result = mapper.readValue(new String(
						arg2), HashMap.class);
				int code = (Integer) result.get("code");
				if (result.get("items") != null) {
					list = (List<HashMap<String, String>>) result.get("items");
				}
				int length = list.size();
				onSuccess(code, list, length);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public abstract void onSuccess(int code,
				List<HashMap<String, String>> data, int length);

		public abstract void onError(int statu, Header[] arg1, byte[] arg2,
				Throwable arg3);
	}

}
