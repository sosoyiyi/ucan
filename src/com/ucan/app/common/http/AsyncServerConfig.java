package com.ucan.app.common.http;

public class AsyncServerConfig {
	static {
		System.loadLibrary("base");
	}

	public static native String asyncServer();

	public static native String asyncDesKey();
}
