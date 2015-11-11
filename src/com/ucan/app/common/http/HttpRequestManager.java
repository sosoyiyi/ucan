package com.ucan.app.common.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.loopj.android.http.AsyncHttpClient;

public class HttpRequestManager {

	private static AsyncHttpClient httpClient;
	private static ObjectMapper mapper = new ObjectMapper();
	private static final int DEFAULT_MAX_RETRIES = 2;
	private static final int DEFAULT_MAX_TIMEOUT = 10;

	public HttpRequestManager() {
		httpClient = new AsyncHttpClient();
		httpClient.setMaxRetriesAndTimeout(DEFAULT_MAX_RETRIES,
				DEFAULT_MAX_TIMEOUT);
	}
	/**
	 * @param r
	 * @param t
	 */
	public HttpRequestManager(int r, int t) {
		httpClient = new AsyncHttpClient();
		httpClient.setMaxRetriesAndTimeout(r, t);
	}

	public void clientGet(String url, HashMap<String, String> params,
			NetCallBack cb) {
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
		httpClient.get(url, cb);

	}

}
