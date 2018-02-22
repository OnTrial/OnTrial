package org.developer.wwb.core.auth;

public class TokenWrapper {

	private Integer code;
	private String email;
	private String token;

	public TokenWrapper() {
	}

	public TokenWrapper(Integer code, String email, String token) {
		this.code = code;
		this.email = email;
		this.token = token;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
