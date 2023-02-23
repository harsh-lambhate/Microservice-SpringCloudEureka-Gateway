package com.lcwd.hotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.service.HotelService;

@RestController
@RequestMapping("/hotels")
public class HotelController {
	
	@Autowired
	private HotelService service;
	
	//create
	@PostMapping()
	public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel){
		Hotel create = service.create(hotel);
		return ResponseEntity.status(HttpStatus.CREATED).body(create);
	}
	
	//getSingle
	@GetMapping("/{hotelId}")
	public ResponseEntity<Hotel> getHotelById(@PathVariable String hotelId){
		Hotel hotel = service.get(hotelId);
		return ResponseEntity.status(HttpStatus.OK).body(hotel);
	}
	
	//getAll
	@GetMapping()
	public ResponseEntity<List<Hotel>> getAllHotel(){
		List<Hotel> hotel = service.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(hotel);
	}

}
