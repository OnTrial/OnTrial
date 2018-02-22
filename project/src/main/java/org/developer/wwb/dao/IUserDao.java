package org.developer.wwb.dao;

import java.util.List;

import org.developer.wwb.core.exception.AppDaoException;
import org.developer.wwb.entity.User;

public interface IUserDao extends IGeneralDao {

	public boolean isUserExisted(String userName) throws AppDaoException;

	public boolean isEmailExisted(String email) throws AppDaoException;

	public User getUserByName(String userName) throws AppDaoException;

	public User getUserByEmail(String email) throws AppDaoException;

	public List<User> getAllUserByParam(String keyword, String role, int pageNum, int pageSize, String orderby)
			throws AppDaoException;

	public long getAllUserCount(String keyword, String role) throws AppDaoException;

	public List<User> getUserByUserRole(String userFinanceRole) throws AppDaoException;

	public List<User> getUsernamesByRole(String userRole) throws AppDaoException;
}
