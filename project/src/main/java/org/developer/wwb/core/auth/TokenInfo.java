package org.developer.wwb.core.auth;

import java.util.Date;

public class TokenInfo {
	private String token;
	private String originToken;
	private Date expirationDate;
	private Date lastSignInDate;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Date getLastSignInDate() {
		return lastSignInDate;
	}
	public void setLastSignInDate(Date lastSignInDate) {
		this.lastSignInDate = lastSignInDate;
	}
	public String getOriginToken() {
		return originToken;
	}
	public void setOriginToken(String originToken) {
		this.originToken = originToken;
	}
}
