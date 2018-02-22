package org.developer.wwb.core.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;
import org.developer.wwb.core.auth.Email;
import org.developer.wwb.core.constants.AuthConstants;
import org.developer.wwb.core.constants.EmailConstants;

public class EmailUtils {
	private EmailUtils() {
	}


	public static boolean sendForgetPasswordEmail(String username, String newpwd, String randomString,
			String senderEmail, String receiverEmail, String baseConfirmURL, String mailHost, String senderPassword)
			throws Exception {
		boolean flag = false;
		DesUtils desUtils = new DesUtils(AuthConstants.DES_KEY);
		String token = new String(Base64.encodeBase64(desUtils.encrypt(
				(username + ":" + newpwd + ":" + randomString + ":" + new Date().getTime()).getBytes("utf-8"))));
		String confirmURL;
		if (baseConfirmURL.endsWith("/"))
			confirmURL = baseConfirmURL + "confirm?token=" + token;
		else
			confirmURL = baseConfirmURL + "/confirm?token=" + token;
		String content = EmailConstants.RESET_PASSWORD_CONTENT.replace(EmailConstants.USER_NAME, username)
				.replace(EmailConstants.NEW_PWD, newpwd).replace(EmailConstants.CONFIRM_URL, confirmURL);

		flag = sendMail(content, EmailConstants.RESET_PWD_TITLE, senderEmail, receiverEmail, mailHost, senderPassword);
		return flag;
	}

	private static boolean sendMail(String content, String title, String sender, String receiver, String mailHost,
			String mailPassword) throws Exception {
		Properties prop = new Properties();
		// prop.setProperty("mail.host", "smtp.sohu.com");//mail.internetware.cn
		prop.setProperty("mail.host", mailHost);// mail.internetware.cn
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.auth", "true");
		// 1、创建session
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(true);
		// 2、通过session得到transport对象
		Transport transport = session.getTransport();
		// 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
		transport.connect(mailHost, sender, mailPassword);
		// 4、创建邮件
		Message message = createSimpleMail(session, sender, receiver, content, title);
		// 5、发送邮件
		transport.sendMessage(message, message.getAllRecipients());

		transport.close();

		return true;
	}

	/**
	 * @param mailPassword
	 *            未经解密的密码串
	 * 
	 */
	private static void sendAsynMail(final String content, final String title, final String sender,
			final String receiver, final String mailHost, final String mailPassword) {
		new Thread() {
			public void run() {
				try {
					sendMail(content, title, sender, receiver, mailHost, mailPassword);
				} catch (Exception e) {

				}
			};
		}.start();
	}

	private static MimeMessage createSimpleMail(Session session, String sender, String receiver, String content,
			String title) throws Exception {
		// 创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 指明邮件的发件人
		message.setFrom(new InternetAddress(sender));
		// 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
		// 邮件的标题
		message.setSubject(title);
		// 邮件的文本内容
		message.setContent(content, "text/html;charset=UTF-8");
		// 返回创建好的邮件对象
		return message;
	}

	public static void sendAsynEmail(Email sysEmail, String content, String title, String receiver) {

		sendAsynMail(content, title, sysEmail.getSysSender(), receiver, sysEmail.getSysHost(),
				sysEmail.getSysPassword());
	}

	public static void sendEmail(Email sysEmail, String content, String title, String receiver) throws Exception {

		sendMail(content, title, sysEmail.getSysSender(), receiver, sysEmail.getSysHost(), sysEmail.getSysPassword());
	}

	public static void main(String[] args) throws Exception {
		EmailUtils.sendAsynMail("您好", "测试", "415525338wwba@sina.com", "80502100@qq.com", "smtp.sina.com",
				"022d6731e578cbead261f0716ff90518");

	}
}
