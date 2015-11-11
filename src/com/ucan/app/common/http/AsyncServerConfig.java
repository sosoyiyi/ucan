package com.ucan.app.common.http;

public class AsyncServerConfig {
	static {
		System.loadLibrary("config");
	}

	public static native String asyncServer();

	public static native String asyncDesKey();
}
