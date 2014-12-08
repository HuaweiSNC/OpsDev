package com.huawei.network.opsdev.core;

public class RetRpc {

	private int statusCode = 200;
	private String status ="";
	private String content = "";
	private String msg = "";
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("[\"").append(this.statusCode).append("\"");
		buf.append(",\"").append(this.msg).append("\"");
		buf.append(",\"").append(this.content).append("\"]");
		
		return buf.toString();
	}
	
	
	
}
