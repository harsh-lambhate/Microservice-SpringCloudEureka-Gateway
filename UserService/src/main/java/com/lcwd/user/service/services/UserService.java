package com.lcwd.user.service.services;

import com.lcwd.user.service.entities.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

	/**
	 * create
	 * 
	 * @param user
	 * @return User
	 */
	User saveUser(User user);

	/**
	 * get all user
	 * 
	 * @return List of User
	 */
	Page<User> getAllUser(Pageable pageable);

	/**
	 * get single user of given userId
	 * 
	 * @param userId
	 * @return User
	 */
	User getUser(String userId);

	/**
	 * To delete user by userId
	 */
	void deleteUserByUserId(String userId);
	
	/**
	 * To delete all users
	 */
	void deleteAllUsers();
	
 
	/**
	 * To update the user by userId
	 * @return User
	 */
	User getUserById(String userId, User user);

}
