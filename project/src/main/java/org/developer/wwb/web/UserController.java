package org.developer.wwb.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.developer.wwb.core.RetInfo;
import org.developer.wwb.core.auth.EmailAndToken;
import org.developer.wwb.core.auth.TokenWrapper;
import org.developer.wwb.core.constants.AuthConstants;
import org.developer.wwb.core.constants.ReturnCodeConstant;
import org.developer.wwb.entity.User;
import org.developer.wwb.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserService userService;

	@RequestMapping(value = "/getUserInfo")
	@ResponseBody
	public RetInfo getUserInfo(Long userId) {
		RetInfo ret = new RetInfo();
		List<User> ls = new ArrayList<User>();
		try {
			ls.add(userService.getUserInfo(userId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret.setData(ls);
		return ret;
	}

	@SuppressWarnings({ "unused" })
	@RequestMapping(value = "/userlogin", method = RequestMethod.POST)
	@ResponseBody
	public RetInfo userlogin(HttpServletRequest request, HttpServletResponse response) {
		long current = System.currentTimeMillis();
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		RetInfo ret = new RetInfo();
		if (StringUtils.isBlank(username)) {
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("用户名为空");
			return ret;
		}

		if (StringUtils.isBlank(password)) {
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("用户名为空");
			return ret;
		}

		TokenWrapper tokenWrapper = null;
		try {
			if (!userService.isUserExisted(username)) {
				ret.setCode(ReturnCodeConstant.FAILED);
				ret.setStatus("fail");
				ret.setInfo("当前用户不存在");
				return ret;
			}
			tokenWrapper = userService.signIn(username, password);
			if (tokenWrapper == null) {
				ret.setCode(ReturnCodeConstant.FAILED);
				ret.setInfo("用户名或者密码错误");
				ret.setStatus("fail");
				return ret;
			}
		} catch (Exception e) {
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("登录失败");
			return ret;
		}

		EmailAndToken emailAndToken = new EmailAndToken(tokenWrapper.getEmail(), tokenWrapper.getEmail(),
				tokenWrapper.getToken());
		Cookie tokenCookie = new Cookie("tokenAuthCode", tokenWrapper.getToken());
		tokenCookie.setMaxAge(AuthConstants.TOKEN_EXPIRE_TIME);
		response.addCookie(tokenCookie);
		ret.setCode(ReturnCodeConstant.SUCCESS);
		ret.setInfo("登录成功");
		User obj = null;
		try {
			obj = userService.getUserByName(username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret.setData(obj);
		logger.debug("login is executed: " + (System.currentTimeMillis() - current) + " ms");
		return ret;
	}

	@RequestMapping(value = "/userRegister", method = RequestMethod.POST)
	@ResponseBody
	public RetInfo registerUser(User user) {

		long current = System.currentTimeMillis();

		RetInfo ret = new RetInfo();
		if (user == null || StringUtils.isBlank(user.getName())) {
			String message = "username is missing";
			logger.error(message);
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("用户名不能为空");
			return ret;
		}
		if (StringUtils.isBlank(user.getEmail())) {
			String message = "email is missing";
			logger.error(message);
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("邮箱不能为空");
			return ret;
		}
		if (StringUtils.isBlank(user.getPassword())) {
			String message = "password is missing";
			logger.error(message);
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("密码不能为空");
			return ret;
		}
		if (user.getName().length() < AuthConstants.MIN_USERNAME_LENGTH
				|| user.getName().length() > AuthConstants.MAX_USERNAME_LENGTH) {
			String message = "Username length should be in " + AuthConstants.MIN_USERNAME_LENGTH + "-"
					+ AuthConstants.MAX_USERNAME_LENGTH;
			logger.error(message);
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("用户名不符合规则");
			return ret;
		}
		if (user.getPassword().length() < AuthConstants.MIN_PASSWORD_LENGTH
				|| user.getPassword().length() > AuthConstants.MAX_PASSWORD_LENGTH) {
			String message = "Password length should be in " + AuthConstants.MIN_PASSWORD_LENGTH + "-"
					+ AuthConstants.MAX_PASSWORD_LENGTH;
			logger.error(message);
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("密码不符合规则");
			return ret;
		}
		try {
			if (userService.isUserExisted(user.getName())) {
				String message = "username is existed";
				logger.error(message);
				ret.setCode(ReturnCodeConstant.FAILED);
				ret.setStatus("fail");
				ret.setInfo("用户名已存在");
				return ret;
			}
			if (userService.isEmailExisted(user.getEmail())) {
				String message = "email is existed";
				logger.error(message);
				ret.setCode(ReturnCodeConstant.FAILED);
				ret.setStatus("fail");
				ret.setInfo("邮箱被注册，请核实");
				return ret;
			}

			userService.addUser(user);
		} catch (Exception e) {
			String message = "Signup failed";
			logger.error(message);
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("注册失败");

			return ret;
		}
		ret.setCode(ReturnCodeConstant.SUCCESS);
		ret.setStatus("ok");
		User obj = null;
		try {
			obj = userService.getUserByName(user.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret.setData(obj);
		logger.debug("register is executed: " + (System.currentTimeMillis() - current) + " ms");
		return ret;
	}

	@RequestMapping(value = "/userLogout", method = RequestMethod.GET)
	@ResponseBody
	public RetInfo userLogout(HttpServletRequest request, HttpServletResponse response) {
		long current = System.currentTimeMillis();
		String token = request.getParameter("tokenAuthCode");
		RetInfo ret = new RetInfo();
		if (token == null) {
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("token is null");
			return ret;
		}
		try {
			if (userService.signOut(token)) {
				logger.debug("signOut is executed: " + (System.currentTimeMillis() - current) + " ms");
				Cookie tokenCookie = new Cookie("tokenAuthCode", "");
				tokenCookie.setMaxAge(AuthConstants.TOKEN_EXPIRE_TIME);
				response.addCookie(tokenCookie);
				ret.setCode(ReturnCodeConstant.SUCCESS);
				ret.setStatus("ok");
				return ret;
			} else {
				ret.setCode(ReturnCodeConstant.FAILED);
				ret.setStatus("fail");
				ret.setInfo("logout fail");
				return ret;
			}
		} catch (Exception e) {
			ret.setCode(ReturnCodeConstant.FAILED);
			ret.setStatus("fail");
			ret.setInfo("logout fail");
			return ret;
		}
	}

}