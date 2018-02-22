package org.developer.wwb.core.auth;

public class Email {
	private static Email sysEmail;

	private Email() {
	}

	public String sysHost;
	public String sysSender;
	public String sysPassword;

	public String getSysSender() {
		return sysSender;
	}

	public void setSysSender(String sysSender) {
		this.sysSender = sysSender;
	}

	public String getSysPassword() {
		return sysPassword;
	}

	public void setSysPassword(String sysPassword) {
		this.sysPassword = sysPassword;
	}

	public static Email getInstance() {
		if (sysEmail == null) {
			synchronized (Email.class) {
				if (sysEmail == null) {
					sysEmail = new Email();
				}
			}

		}
		return sysEmail;
	}

	public String getSysHost() {
		return sysHost;
	}

	public void setSysHost(String sysHost) {
		this.sysHost = sysHost;
	}
}
