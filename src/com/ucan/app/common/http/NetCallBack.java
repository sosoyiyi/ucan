package com.ucan.app.common.http;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class NetCallBack extends AsyncHttpResponseHandler {

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		httpFailure(arg0, arg1, arg2, arg3);
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		httpSuccess(arg0, arg1, arg2);
	}
   

	public abstract void httpSuccess(int arg0, Header[] arg1, byte[] arg2);

	public abstract void httpFailure(int arg0, Header[] arg1, byte[] arg2,
			Throwable arg3);

}
