package com.jsp.hello.exception;

public class ErrorStructure {
	private int status;
	private String message;
	private String rootCasue;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRootCasue() {
		return rootCasue;
	}
	public void setRootCasue(String rootCasue) {
		this.rootCasue = rootCasue;
	}
}
