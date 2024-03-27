package com.lcwd.rating.repository;

import com.lcwd.rating.entities.Rating;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating,String>
{
    //custom finder methods
	@Query("SELECT r FROM Rating r WHERE r.userId = :userId")
    List<Rating> findByUserId(String userId);
	
	@Query("SELECT r FROM Rating r WHERE r.hotelId = :hotelId")
    List<Rating> findByHotelId(String hotelId);

}
