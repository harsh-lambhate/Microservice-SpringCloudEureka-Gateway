package com.lcwd.rating.services.impl;


import com.lcwd.rating.entities.Rating;
import com.lcwd.rating.exception.ResourceNotFoundException;
import com.lcwd.rating.repository.RatingRepository;
import com.lcwd.rating.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RatingServiceImpl implements RatingService {


    @Autowired
    private RatingRepository repository;

    @Override
    public Rating create(Rating rating) {
    	String random = UUID.randomUUID().toString();
    	rating.setRatingId(random);
        return repository.save(rating);
    }

    @Override
    public List<Rating> getRatings() {
        return repository.findAll();
    }

    @Override
    public List<Rating> getRatingByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<Rating> getRatingByHotelId(String hotelId) {
        return repository.findByHotelId(hotelId);
    }

	@Override
	public Rating deleteRatingByRatingId(String ratingId) {
		 return repository.findById(ratingId)
		            .map(rating -> {
		                repository.delete(rating);
		                return rating;
		            })
		            .orElseThrow(() -> new ResourceNotFoundException("Rating with the given id " + ratingId + " is not found"));
	}

	@Override
	public Rating updateRatingByRatingId(String ratingId, Rating rating) {
	    Rating ratingDetail = repository.findById(ratingId).orElseThrow(() -> new ResourceNotFoundException("Rating with given id " + ratingId + " is not found"));

	    Optional.ofNullable(rating.getRatingId()).ifPresent(ratingDetail::setRatingId);
	    Optional.ofNullable(rating.getHotelId()).ifPresent(ratingDetail::setHotelId);
	    Optional.ofNullable(rating.getUserId()).ifPresent(ratingDetail::setUserId);
	    Optional.ofNullable(rating.getFeedback()).ifPresent(ratingDetail::setFeedback);
	    Optional.ofNullable(rating.getRating()).ifPresent(ratingDetail::setRating);
	    return repository.save(ratingDetail);
	}

    
    
    
}
