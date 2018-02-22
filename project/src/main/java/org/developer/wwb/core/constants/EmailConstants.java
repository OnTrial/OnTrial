package org.developer.wwb.core.constants;

public class EmailConstants {
	
	public static final String USER_NAME = "{#user_name}";
	public static final String NEW_PWD = "{#new_password}";
	public static final String CONFIRM_URL = "{#confirm_url}";
	public static final String ERROR_CONTENT = "{#error_content}";
	
	public static final String DEFAULT_EMAIL_SENDER = "noreply@internetware.cn";
	public static final String DEFAULT_EMAIL_HOST = "mail.internetware.cn";
	public static final String DEFAULT_EMAIL_PASSWORD = "noreply@2016";
	public static final String SENDER_EMAIL_NAME = "senderMail";
	
	public static final String SIGNUP_SUCCESS_TITLE = "注册成功";

	
	public static final String RESET_PWD_TITLE = "密码重置确认";
	
	public static final String RESET_PASSWORD_CONTENT = "尊敬的 {user_name} ，您好：\n\n"
			+ "您已申请重置密码，新密码为{new_password}，如您确认重置，请在浏览器中复制并打开如"
			+ "下链接（该链接24小时后失效）。\n\n"
			+ "{confirm_url}\n\n如您不需要重置密码，请忽略此邮件\n\n Store\n\n";
}
