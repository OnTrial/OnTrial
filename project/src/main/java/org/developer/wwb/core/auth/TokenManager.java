package org.developer.wwb.core.auth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.developer.wwb.core.constants.AuthConstants;
import org.developer.wwb.entity.User;

public class TokenManager {

	private static ConcurrentMap<String, List<TokenInfo>> tokens = new ConcurrentHashMap<String, List<TokenInfo>>();
	public static ConcurrentMap<String, User> users = new ConcurrentHashMap<String, User>();

	public enum TokenStatus {
		TOKEN_VALID, TOKEN_EXPIRED, TOKEN_INVALID
	}

	public static void putToken(User user, String token, String loginToken) {
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setToken(token);
		tokenInfo.setOriginToken(loginToken);
		Date date = new Date();
		tokenInfo.setLastSignInDate(date);
		Date date2 = new Date();
		date2.setTime(date.getTime() + AuthConstants.TOKEN_EXPIRE_TIME);
		tokenInfo.setExpirationDate(date2);
		List<TokenInfo> tokenInfos = tokens.get(user.getName());
		if (tokenInfos == null) {
			tokenInfos = new ArrayList<TokenInfo>();
			tokenInfos.add(tokenInfo);
			tokens.put(user.getMobile(), tokenInfos);
		} else {
			tokenInfos.add(tokenInfo);
		}
		users.put(loginToken, user);
	}

	public static List<TokenInfo> getTokens(String username) {
		return tokens.get(username);
	}

	/**
	 * remove token by username
	 * 
	 * @param username
	 * @param token
	 */
	public static void removeToken(String username, String token, String loginToken) {
		if (username == null)
			return;
		List<TokenInfo> tokenInfos = tokens.get(username);
		if (tokenInfos == null || token == null)
			return;
		Iterator<TokenInfo> it = tokenInfos.iterator();
		while (it.hasNext()) {
			TokenInfo tokenInfo = it.next();
			if (tokenInfo.getToken().equals(token)) {
				it.remove();
				break;
			}
		}
		if (tokenInfos.isEmpty())
			tokens.remove(username);
		if (users.containsKey(loginToken)) {
			users.remove(loginToken);

		}
	}

	public static void refreshExpirationDate(String username, String token) {
		List<TokenInfo> tokenInfos = tokens.get(username);
		if (tokenInfos == null || token == null)
			return;
		for (TokenInfo tokenInfo : tokenInfos) {
			if (tokenInfo.getToken().equals(token)) {
				Date date = new Date();
				date.setTime(date.getTime() + AuthConstants.TOKEN_EXPIRE_TIME);
				tokenInfo.setExpirationDate(date);
				break;
			}
		}
	}

	/**
	 * check the token status
	 * 
	 * @param username
	 * @param token
	 * @return TokenStatus, TOKEN_VALID, TOKEN_EXPIRED or TOKEN_INVALID
	 */
	public static TokenStatus checkToken(String username, String token, String loginToken) {
		List<TokenInfo> tokenInfos = tokens.get(username);
		if (tokenInfos == null || token == null)
			return TokenStatus.TOKEN_INVALID;
		for (TokenInfo tokenInfo : tokenInfos) {
			if (tokenInfo.getToken().equals(token)) {
				Date expirationDate = tokenInfo.getExpirationDate();
				Date nowDate = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(tokenInfo.getLastSignInDate());
				calendar.add(Calendar.DATE, 1);
				Date nextDateAfterSignIn = calendar.getTime();
				if (nowDate.before(expirationDate) && nowDate.before(nextDateAfterSignIn)) {
					refreshExpirationDate(username, token);
					return TokenStatus.TOKEN_VALID;
				} else {
					if (users.containsKey(loginToken)) {
						users.remove(loginToken);
					}
					cleanUpTokens();
					return TokenStatus.TOKEN_EXPIRED;
				}
			}
		}
		if (users.containsKey(loginToken)) {
			users.remove(loginToken);
		}
		cleanUpTokens() ;
		return TokenStatus.TOKEN_INVALID;
	}

	public static void cleanUpTokens() {
		Iterator<Entry<String, List<TokenInfo>>> iterator = tokens.entrySet().iterator();
		Date nowdate = new Date();
		while (iterator.hasNext()) {
			Entry<String, List<TokenInfo>> entry = iterator.next();
			List<TokenInfo> tokenInfos = entry.getValue();
			if (tokenInfos != null) {
				Iterator<TokenInfo> it = tokenInfos.iterator();
				while (it.hasNext()) {
					TokenInfo tokenInfo = it.next();
					if (nowdate.after(tokenInfo.getExpirationDate())) {
						it.remove();
					}
				}
				if (tokenInfos.isEmpty())
					iterator.remove();
			} else
				iterator.remove();
		}
	}

}
