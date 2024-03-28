package com.lcwd.rating.services;

import com.lcwd.rating.entities.Rating;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RatingService {

    //create
    Rating create(Rating rating);


    //get all ratings
    List<Rating> getRatings();

    //get all by UserId
    List<Rating> getRatingByUserId(String userId);

    //get all by hotel
    List<Rating> getRatingByHotelId(String hotelId);

    //delete rating by rating
    Rating deleteRatingByRatingId(String hotelId);
    
    
    //update rating by ratingId
    Rating updateRatingById(String ratingId,Rating rating);


}
