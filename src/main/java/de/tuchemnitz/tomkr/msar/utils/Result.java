package de.tuchemnitz.tomkr.msar.utils;

public class Result {

	private String msg;
	private boolean success;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Result(boolean success, String msg) {
		this.msg = msg;
		this.success = success;
	}
}
