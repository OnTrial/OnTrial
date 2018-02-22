package org.developer.wwb.core.auth;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmailAndToken {
	private String email;
	private String notificationEmail;
	private String token;
	public EmailAndToken(String email, String notificationEmail, String token) {
		this.email = email;
		this.notificationEmail = notificationEmail;
		this.token = token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNotificationEmail() {
		return notificationEmail;
	}
	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
