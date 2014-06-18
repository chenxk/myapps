package com.yuguan.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class TimeOutThread implements Runnable {

	private long startTime;
	private long timeout;
	private Handler handler;
	private boolean loading = true;

	public TimeOutThread(long timeout, Handler handler) {
		this.timeout = timeout;
		this.handler = handler;
		startTime = System.currentTimeMillis();
	}

	@Override
	public void run() {

		while (loading) {
			try {
				Thread.sleep(100);
				startTime += 100;
				if (System.currentTimeMillis() - startTime >= timeout) {
					Message message = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("TIMEOUT", "TIMEOUT");
					message.setData(bundle);
					handler.sendMessage(message);
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}
	
	

}
