package org.developer.wwb.service;

import org.developer.wwb.core.auth.TokenWrapper;
import org.developer.wwb.entity.User;

public interface IUserService {

	

	User loadUserByUsername(String username)throws Exception;

	
	public User getUserInfo(Long userId)throws Exception ;

	public boolean isUserExisted(String username)throws Exception ;

	public TokenWrapper signIn(String username, String password)throws Exception;

	public User getUserByName(String username) throws Exception;

	public boolean isEmailExisted(String email)throws Exception ;

	public void addUser(User user) throws Exception;

	public boolean signOut(String token) throws Exception;

}
