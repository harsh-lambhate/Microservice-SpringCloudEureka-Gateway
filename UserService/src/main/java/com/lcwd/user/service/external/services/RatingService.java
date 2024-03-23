package com.lcwd.user.service.external.services;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.lcwd.user.service.entities.Rating;

@Service
@FeignClient(name = "RATING-SERVICE")
public interface RatingService {

	/**
	 * GET 
	 * This method is use to create the Rating
	 * 
	 * @return ResponseEntity containing list of rating
	 */
	@GetMapping("/ratings")
	public ResponseEntity<List<Rating>> getRatings();

	/**
	 * POST 
	 * This method is use to create the Rating
	 * 
	 * @param values
	 * @return ResponseEntity containing rating
	 */
	@PostMapping("/ratings")
	public ResponseEntity<Rating> createRating(Rating values);

	/**
	 * PUT 
	 * This method is use to update the Rating using ratingId
	 * 
	 * @param ratingId
	 * @param rating
	 * @return ResponseEntity containing rating
	 */
	@PutMapping("/ratings/{ratingId}")
	public ResponseEntity<Rating> updateRating(@PathVariable("ratingId") String ratingId, Rating rating);

	/**
	 * DELETE 
	 * This method used to delete the rating by ratingId
	 * 
	 * @param ResponseEntity containing ratingId
	 */
	@DeleteMapping("/ratings/{ratingId}")
	public void deleteRating(@PathVariable String ratingId);
	
	/**
	 * DELETE 
	 * This method used to delete the rating
	 * 
	 * @param ResponseEntity containing ratingId
	 */
	@DeleteMapping("/ratings")
	public void deleteAllRating();
    
    
}
