package com.lcwd.hotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.service.HotelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Tag(name = "Hotel", description = "Hotel Service APIs")
@CrossOrigin(origins = "http://localhost:9081")
@RestController
@RequestMapping("/hotels")
public class HotelController {
	
	@Autowired
	private HotelService service;
	
	//create
	 @Operation(summary = "Create a new Hotel", tags = { "Hotel", "Post" })
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
		Hotel create = service.create(hotel);
		return ResponseEntity.status(HttpStatus.CREATED).body(create);
	}
	
	//getSingle
	 @Operation(summary = "get Hotel by HotelId", tags = { "Hotel", "Get" , "Filter"})
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
		Hotel hotel = service.get(hotelId);
		return ResponseEntity.status(HttpStatus.OK).body(hotel);
	}
	
	//getAll
	 @Operation(summary = "get all Hotel", tags = { "Hotel", "Get" })
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
		List<Hotel> hotel = service.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(hotel);
	}

}
