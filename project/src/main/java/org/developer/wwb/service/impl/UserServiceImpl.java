package org.developer.wwb.service.impl;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;
import org.developer.wwb.core.auth.TokenManager;
import org.developer.wwb.core.auth.TokenWrapper;
import org.developer.wwb.core.constants.AuthConstants;
import org.developer.wwb.core.utils.DesUtils;
import org.developer.wwb.core.utils.PasswordHash;
import org.developer.wwb.dao.IUserDao;
import org.developer.wwb.entity.User;
import org.developer.wwb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;

	@Transactional
	@Override
	public User loadUserByUsername(String username) throws Exception {

		return userDao.getUserByName(username);
	}

	@Override
	public User getUserInfo(Long userId) throws Exception {
		return userDao.getEntityByID(User.class, userId);
	}

	@Override
	public boolean isUserExisted(String username) throws Exception {
		User user = userDao.getUserByName(username);

		return user == null ? false : true;
	}

	@Override
	public TokenWrapper signIn(String username, String password) throws Exception {

		TokenWrapper tokenWrapper = null;

		User enUser = userDao.getUserByName(username);

		if (enUser != null) {

			String pwdHash;
			try {
				pwdHash = PasswordHash.createHash(password, enUser.getSalt());
			} catch (Exception e) {
				return null;
			}
			String email = enUser.getEmail();
			if (enUser.getPassword().equals(pwdHash)) {
				SecureRandom random = new SecureRandom();
				byte[] originTokenByte = new byte[AuthConstants.ORIGIN_TOKEN_SIZE];
				random.nextBytes(originTokenByte);
				String originToken = PasswordHash.toHex(originTokenByte);

				DesUtils desUtils;
				try {
					desUtils = new DesUtils(AuthConstants.DES_KEY);
				} catch (Exception e) {
					return null;
				}
				String token = null;
				try {
					token = new String(
							Base64.decodeBase64(desUtils.encrypt((username + ":" + originToken).getBytes("utf-8"))));
				} catch (Exception e) {
					return null;
				}
				TokenManager.putToken(enUser, originToken, token);
				tokenWrapper = new TokenWrapper(0, email, token);
			}
		}
		return tokenWrapper;
	}

	@Override
	public User getUserByName(String username) throws Exception {

		return userDao.getUserByName(username);
	}

	@Override
	public boolean isEmailExisted(String email) throws Exception {
		User user = userDao.getUserByEmail(email);

		return user == null ? false : true;
	}

	@Override
	public void addUser(User user) throws Exception {
		userDao.save(user);

	}

	@SuppressWarnings("unused")
	@Override
	public boolean signOut(String token) throws Exception {

		DesUtils desUtils = null;
		try {
			desUtils = new DesUtils(AuthConstants.DES_KEY);
		} catch (Exception e1) {
			return false;
		}
		String userAndToken = null;
		try {
			userAndToken = new String(desUtils.decrypt(Base64.decodeBase64(token.getBytes())), "utf-8");
		} catch (Exception e) {
			return false;
		}
		if (userAndToken == null) {
			return false;
		}
		String[] tmp = userAndToken.split(":");
		if (tmp.length != 2)
			return false;
		String username = tmp[0], originToken = tmp[1];

		TokenManager.TokenStatus tokenStatus = TokenManager.checkToken(username, originToken, token);
		if (tokenStatus == TokenManager.TokenStatus.TOKEN_INVALID)
			return false;
		TokenManager.removeToken(username, originToken, token);
		return true;
	}

}
