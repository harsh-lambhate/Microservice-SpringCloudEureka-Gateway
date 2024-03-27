package com.lcwd.user.service.repositories;

import com.lcwd.user.service.entities.User;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User,String>
{
	 
	 @Query("SELECT u FROM User u WHERE u.userId = :userId")
	 Optional<User> findById(String userId);
	 
	 
	 @Modifying
	 @Transactional
	 @Query("DELETE FROM User u WHERE u.id = :userId")
	 void deleteById(String userId);
}
