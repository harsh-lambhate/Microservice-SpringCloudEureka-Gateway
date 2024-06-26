package com.lcwd.hotel.controller;

import java.util.List;

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

import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.service.HotelService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Hotel", description = "Hotel Service APIs")
@CrossOrigin(origins = "http://localhost:9081")
@RestController
@RequestMapping("/hotels")
public class HotelController {
	
	@Autowired
	private HotelService service;
	
	private Logger logger = LoggerFactory.getLogger(HotelController.class);
	
	//create
	 @Operation(summary = "Create a new Hotel", tags = { "Hotel","Post" })
	    @ApiResponses({
	    	 @ApiResponse(responseCode = "200", content = {
	    	            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "201", content = {
	            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "500", content = {
		            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
	 
	@PostMapping()
	public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel){
		 logger.info("Creating Hotel data-->HotelController ");
		Hotel create = service.create(hotel);
		return ResponseEntity.status(HttpStatus.CREATED).body(create);
	}
	
	//getSingle
	 @Operation(summary = "get Hotel by HotelId", tags = { "Hotel","Get"})
	    @ApiResponses({
	    	 @ApiResponse(responseCode = "200", content = {
	    	            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "201", content = {
	            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "500", content = {
		            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
	@GetMapping("/{hotelId}")
	public ResponseEntity<Hotel> getHotelById(@PathVariable String hotelId){
		 logger.info("fetch hotel data by hotelId--> HotelController"+ hotelId);
		Hotel hotel = service.get(hotelId);
		return ResponseEntity.status(HttpStatus.OK).body(hotel);
	}
	
	//getAll
	 @Operation(summary = "get all Hotel", tags = { "Hotel","Get" })
	    @ApiResponses({
	    	 @ApiResponse(responseCode = "200", content = {
	    	            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "201", content = {
	            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "500", content = {
		            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
	@GetMapping()
	public ResponseEntity<List<Hotel>> getAllHotel(){
		 logger.info("fetching all hotel data list--> HotelController");
		List<Hotel> hotel = service.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(hotel);
	}
	 
	 
	 
	// delete employee rest api
	 @Operation(summary = "delete hotel by hotel Id", tags = { "Hotel","Delete" })
	    @ApiResponses({
	    	 @ApiResponse(responseCode = "200", content = {
	    	            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "201", content = {
	            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "500", content = {
		            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
		@DeleteMapping("/{hotelId}")
		public ResponseEntity<Hotel> deleteHotelByHotelId(@PathVariable String hotelId){
			Hotel hotel = service.deleteHotelByHotelId(hotelId);
			return ResponseEntity.status(HttpStatus.OK).body(hotel);
		}
	 
	// update employee rest api
	 @Operation(summary = "update hotel by hotel Id", tags = { "Hotel","Put" })
	    @ApiResponses({
	    	 @ApiResponse(responseCode = "200", content = {
	    	            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "201", content = {
	            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "500", content = {
		            @Content(schema = @Schema(implementation = Hotel.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
	 @PutMapping("/{hotelId}")
		public ResponseEntity<Hotel> updateHotelByHotelId(@PathVariable String hotelId,@RequestBody Hotel hotel){
			Hotel hotelResponse = service.updateHotelById(hotelId,hotel);
			return ResponseEntity.status(HttpStatus.OK).body(hotelResponse);
		}

}
