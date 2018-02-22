package org.developer.wwb.core.exception;

import java.io.Serializable;

public class AppDaoException extends Exception implements Serializable {

	private static final long serialVersionUID = 231171529082754072L;

	public AppDaoException(Throwable throwable) {
		super(throwable);
	}
}
