package org.developer.wwb.service;

import java.util.Map;

public interface VerifyExtendService {
	boolean sendValifyCode(String mobile);

	Map<String, Object> verifySMSCode(String mobile, String code);
}
