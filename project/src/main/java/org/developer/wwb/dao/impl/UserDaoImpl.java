package org.developer.wwb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.developer.wwb.core.exception.AppDaoException;
import org.developer.wwb.dao.IUserDao;
import org.developer.wwb.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class UserDaoImpl extends GeneralDaoImpl implements IUserDao {

	private final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	@Override
	public boolean isUserExisted(String userName) throws AppDaoException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("username", userName);
			long count = this.checkisExisted(User.class, params);
			return count > 0 ? true : false;
		} catch (Exception e) {
			LOGGER.error("Check username ({}) error {}", userName, e.getMessage());
			throw new AppDaoException(e);
		}
	}

	@Override
	public boolean isEmailExisted(String email) throws AppDaoException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("email", email);
			long count = this.checkisExisted(User.class, params);
			return count > 0 ? true : false;
		} catch (Exception e) {
			LOGGER.error("Check email ({}) error {}", email, e.getMessage());
			throw new AppDaoException(e);
		}
	}

	@Override
	public User getUserByName(String userName) throws AppDaoException {
		User user = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("username", userName);
			user = (User) this.getEntity(User.class, params);
		} catch (Exception e) {
			LOGGER.error("Get User by userName ({}) error {}", userName, e.getMessage());
			throw new AppDaoException(e);
		}
		return user;

	}

	@Override
	public User getUserByEmail(String email) throws AppDaoException {
		User user = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("email", email);
			user = (User) this.getEntity(User.class, params);
		} catch (Exception e) {
			LOGGER.error("Get User by email ({}) error {}", email, e.getMessage());
			throw new AppDaoException(e);
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUserByParam(String keyword, String role, int pageNum, int pageSize, String orderby)
			throws AppDaoException {
		StringBuffer sql = new StringBuffer();
		sql.append("select o from User o where 1=1  ");
		StringBuffer params = new StringBuffer();
		if (keyword != null && !keyword.trim().equals("")) {
			params.append(" and username like '%");
			params.append(keyword);
			// params.append("%' or nickName like '%");
			// params.append(keyword);
			// params.append("%' or email like '%");
			// params.append(keyword);
			params.append("%'");
		}
		if (!StringUtils.isEmpty(role)) {
			params.append(" and userRole like '%").append(role).append("%' ");
		}
		sql.append(params);
		if (orderby != null && !orderby.trim().equals("")) {
			sql.append(" ORDER BY ");
			sql.append(orderby);
			sql.append("desc");
		}
		Query query = em.createQuery(sql.toString(), User.class).setMaxResults(pageSize)
				.setFirstResult(pageNum * pageSize);
		List<User> entity = query.getResultList();
		return entity;

	}

	@Override
	public long getAllUserCount(String keyword, String role) throws AppDaoException {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(id) from User o  where 1=1 ");
		StringBuffer params = new StringBuffer();
		if (keyword != null && !keyword.trim().equals("")) {
			params.append(" and username like '%");
			params.append(keyword);
			// params.append("%' or nickName like '%");
			// params.append(keyword);
			// params.append("%' or email like '%");
			// params.append(keyword);
			params.append("%' and username !='admin' ");
		}

		if (!StringUtils.isEmpty(role)) {
			params.append(" and userRole like '%").append(role).append("%' ");
		}

		sql.append(params);

		Query query = em.createQuery(sql.toString());
		Long count = (Long) query.getSingleResult();

		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserByUserRole(String userFinanceRole) throws AppDaoException {
		StringBuffer sql = new StringBuffer();
		sql.append("select o from User o ");
		StringBuffer params = new StringBuffer();
		if (userFinanceRole != null && !userFinanceRole.trim().equals("")) {
			params.append(" where userRole like '%");
			params.append(userFinanceRole);
			params.append("%'");
		}
		sql.append(params);
		Query query = em.createQuery(sql.toString(), User.class);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsernamesByRole(String userRole) throws AppDaoException {
		StringBuffer sql = new StringBuffer();
		sql.append("select o.username,o.nickName from User o  where o.verified=1  and ");
		String[] roles = userRole.split(",");
		for (String role : roles) {
			sql.append(" userRole like '%").append(role).append("%'  or   ");
		}
		Query query = em.createQuery(sql.substring(0, sql.length() - 5));
		return query.getResultList();
	}

}
