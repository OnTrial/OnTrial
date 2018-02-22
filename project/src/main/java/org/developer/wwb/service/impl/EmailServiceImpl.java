package org.developer.wwb.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.developer.wwb.core.auth.Email;
import org.developer.wwb.core.constants.AuthConstants;
import org.developer.wwb.core.constants.EmailConstants;
import org.developer.wwb.core.constants.ErrorCodeConstants;
import org.developer.wwb.core.utils.DesUtils;
import org.developer.wwb.core.utils.EmailUtils;
import org.developer.wwb.core.utils.PlatException;
import org.developer.wwb.entity.Customization;
import org.developer.wwb.service.EmailService;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailServiceImpl implements EmailService{


	@SuppressWarnings("unused")
	@Override
	public void sendAsynEmailService(String receiver, String content, String title) throws Exception {
		Email sysEmail = Email.getInstance();
		if (StringUtils.isBlank(sysEmail.getSysHost()) || StringUtils.isBlank(sysEmail.getSysPassword())
				|| StringUtils.isBlank(sysEmail.getSysSender())) {
			sysEmail.setSysHost(EmailConstants.DEFAULT_EMAIL_HOST);
			;
			sysEmail.setSysSender(EmailConstants.DEFAULT_EMAIL_SENDER);
			sysEmail.setSysPassword(EmailConstants.DEFAULT_EMAIL_PASSWORD);

			Customization customEamil=null;

//			customEamil = customizationDao.getCustomizationByName(EmailConstants.SENDER_EMAIL_NAME);

			if (customEamil != null && StringUtils.isNotBlank(customEamil.getValue())) {
				sysEmail.setSysSender(customEamil.getValue());
			}
			if (customEamil != null && StringUtils.isNotBlank(customEamil.getExtend1())) {
				sysEmail.setSysHost(customEamil.getExtend1());
			}
			DesUtils des = null;
			try {
				des = new DesUtils(AuthConstants.DES_KEY);
			} catch (Exception e) {

			}
			if (des == null) {
				throw new PlatException(ErrorCodeConstants.Email.EMAIL_SEND_FAILED, "系统邮箱密码错误");
			}
			if (customEamil != null && StringUtils.isNotBlank(customEamil.getExtend2())) {
				try {
					sysEmail.setSysPassword(des.decrypt(customEamil.getExtend2()));
				} catch (Exception e) {

				}
			}
		}

		EmailUtils.sendAsynEmail(sysEmail, content, title, receiver);

	}

	@SuppressWarnings("unused")
	@Override
	public void sendEmailService(String receiver, String content, String title)throws Exception {
		Email sysEmail = Email.getInstance();
		if (StringUtils.isBlank(sysEmail.getSysHost()) || StringUtils.isBlank(sysEmail.getSysPassword())
				|| StringUtils.isBlank(sysEmail.getSysSender())) {
			sysEmail.setSysHost(EmailConstants.DEFAULT_EMAIL_HOST);
			;
			sysEmail.setSysSender(EmailConstants.DEFAULT_EMAIL_SENDER);
			sysEmail.setSysPassword(EmailConstants.DEFAULT_EMAIL_PASSWORD);

			Customization customEamil=null;

//			customEamil = customizationDao.getCustomizationByName(EmailConstants.SENDER_EMAIL_NAME);

			if (customEamil != null && StringUtils.isNotBlank(customEamil.getValue())) {
				sysEmail.setSysSender(customEamil.getValue());
			}
			if (customEamil != null && StringUtils.isNotBlank(customEamil.getExtend1())) {
				sysEmail.setSysHost(customEamil.getExtend1());
			}
			DesUtils des = null;
			try {
				des = new DesUtils(AuthConstants.DES_KEY);
			} catch (Exception e) {

			}
			if (des == null) {
				throw new PlatException(ErrorCodeConstants.Email.EMAIL_SEND_FAILED, "系统邮箱密码错误");
			}
			if (customEamil != null && StringUtils.isNotBlank(customEamil.getExtend2())) {
				try {
					sysEmail.setSysPassword(des.decrypt(customEamil.getExtend2()));
				} catch (Exception e) {

				}
			}
		}
		EmailUtils.sendEmail(sysEmail, content, title, receiver);
	}

}

