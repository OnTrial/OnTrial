package org.developer.wwb.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.developer.wwb.core.auth.TokenManager;
import org.developer.wwb.core.auth.TokenManager.TokenStatus;
import org.developer.wwb.core.utils.CommonUtil;

import net.sf.json.JSONObject;

public class UserTokenFilter implements Filter {
	protected Log logger = LogFactory.getLog(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.reset();
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset=utf-8");
		String requestUrl = req.getRequestURI().replace(req.getContextPath(), "");

		System.out.println(requestUrl);
		if (requestUrl.startsWith("/static/")) {
			chain.doFilter(request, response);
			return;
		} else {
			if (checkToken(req)) {
				chain.doFilter(request, response);
				return;
			} else {
				PrintWriter pw = null;
				try {
					pw = resp.getWriter();
				} catch (IOException e) {
					logger.error("httpsValidFilter: response.getWriter() error");
					return;
				}
				JSONObject ret = new JSONObject();
				ret.put("status", "fail");
				ret.put("msg", "token 校验失败");
				pw.print(ret.toString());
				pw.flush();
				pw.close();
				return;
			}

		}

	}

	private boolean checkToken(HttpServletRequest req) {

		Cookie[] cookies = req.getCookies();
		if (cookies == null || cookies.length < 1) {
			return false;
		}
		String token = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("tokenAuthCode")) {
				token = cookie.getValue();
			}
		}
		JSONObject tokenRet = null;
		if (StringUtils.isBlank(token)) {
			return false;
		}
		try {
			tokenRet = CommonUtil.decrypToken(token);
		} catch (Exception e) {

			return false;
		}
		if (tokenRet == null || !tokenRet.containsKey("status") || !tokenRet.get("status").equals("ok")
				|| !tokenRet.containsKey("data") || StringUtils.isBlank(tokenRet.getString("data"))) {
			return false;
		}
		String tokens = tokenRet.getString("data");
		String[] usernameAndToken = tokens.split(":");
		TokenStatus tokenStatus = null;
		if (usernameAndToken == null || usernameAndToken.length != 2) {
			return false;
		}

		tokenStatus = TokenManager.checkToken(usernameAndToken[0], usernameAndToken[1],token);

		if (tokenStatus != TokenStatus.TOKEN_VALID) {
			return false;
		}
		TokenManager.refreshExpirationDate(usernameAndToken[0], usernameAndToken[1]);
		return true;
	}

	@Override
	public void destroy() {

	}

}
