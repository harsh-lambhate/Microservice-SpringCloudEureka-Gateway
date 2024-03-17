package com.lcwd.rating.repository;

import com.lcwd.rating.entities.Rating;
//import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating,String>
{
    //custom finder methods
    List<Rating> findByUserId(String userId);
    List<Rating> findByHotelId(String hotelId);

}
