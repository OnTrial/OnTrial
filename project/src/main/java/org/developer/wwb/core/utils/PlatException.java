package org.developer.wwb.core.utils;

public class PlatException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String errorMessage;

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public PlatException(int errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

}
