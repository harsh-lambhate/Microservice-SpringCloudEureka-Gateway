package com.lcwd.rating.controllers;


import com.lcwd.rating.entities.Rating;
import com.lcwd.rating.services.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Rating", description = "Rating Service APIs")
@CrossOrigin(origins = "http://localhost:9081")
@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;
    
    private Logger logger = LoggerFactory.getLogger(RatingController.class);

    //create rating
   //@PreAuthorize("hasAuthority('Admin')")
    @Operation(summary = "Create a new rating", tags = { "Rating", "Post" })
    @ApiResponses({
    	 @ApiResponse(responseCode = "200", content = {
    	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "500", content = {
	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
    @PostMapping
    public ResponseEntity<Rating> create(@RequestBody Rating rating) {
    	logger.info("Creating Rating data-->RatingController");
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.create(rating));
    }

    //get all
    @Operation(summary = "get rating by ratingId", tags = { "Rating", "Get"})
    @ApiResponses({
    	 @ApiResponse(responseCode = "200", content = {
    	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "500", content = {
	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public ResponseEntity<List<Rating>> getRatings() {
    	logger.info("fetching all ratings--> RatingController");
        return ResponseEntity.ok(ratingService.getRatings());
    }

    //get all of user
    //@PreAuthorize("hasAuthority('SCOPE_internal') || hasAuthority('Admin')")
    @Operation(summary = "get all hotel", tags = { "Rating", "Get" })
    @ApiResponses({
    	 @ApiResponse(responseCode = "200", content = {
    	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "500", content = {
	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Rating>> getRatingsByUserId(@PathVariable String userId) {
    	logger.info("fetching rating data for specific user ---> RatingController"+userId);
        return ResponseEntity.ok(ratingService.getRatingByUserId(userId));
    }

    //get all of hotels
    @Operation(summary = "get rating by hotelId", tags = { "Rating", "Get"})
    @ApiResponses({
    	 @ApiResponse(responseCode = "200", content = {
    	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "500", content = {
	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<List<Rating>> getRatingsByHotelId(@PathVariable String hotelId) {
    	logger.info("fetching hotelratings data for specific hotel ---> RatingController"+hotelId);
        return ResponseEntity.ok(ratingService.getRatingByHotelId(hotelId));
    }

    //delete rating by ratingId
    @Operation(summary = "get rating by ratingId", tags = { "Rating", "Delete"})
    @ApiResponses({
    	 @ApiResponse(responseCode = "200", content = {
    	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "500", content = {
	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{ratingId}")
	public ResponseEntity<Rating> deleteRatingById(@PathVariable String ratingId){
		Rating rating = ratingService.deleteRatingByRatingId(ratingId);
		return ResponseEntity.status(HttpStatus.OK).body(rating);
	}

  //update rating by ratingId
    @Operation(summary = "get rating by ratingId", tags = { "Rating", "Put"})
    @ApiResponses({
    	 @ApiResponse(responseCode = "200", content = {
    	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "500", content = {
	            @Content(schema = @Schema(implementation = Rating.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
    @PutMapping("/{ratingId}")
	public ResponseEntity<Rating> updateHotelByHotelId(@PathVariable String ratingId,@RequestBody Rating rating){
    	Rating ratingResponse = ratingService.updateRatingByRatingId(ratingId,rating);
		return ResponseEntity.status(HttpStatus.OK).body(ratingResponse);
	}
}
