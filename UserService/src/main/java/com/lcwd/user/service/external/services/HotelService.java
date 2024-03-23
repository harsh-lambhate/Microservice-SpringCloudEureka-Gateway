package com.lcwd.user.service.external.services;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lcwd.user.service.entities.Hotel;


/**
 * The feign is a declarative HTTP web client developed by Netflix
 * If you want to use feign create an interface and annotate it. 
 */
@FeignClient(name = "HOTEL-SERVICE")
public interface HotelService {

	@GetMapping("/hotels")
	public ResponseEntity<List<Hotel>> getAllHotel();
	  
    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<Hotel> getHotel(@PathVariable("hotelId") String hotelId);
    
    /**
	 * DELETE 
	 * This method used to delete hotel by hotelId
	 * 
	 * @param ResponseEntity containing hotel
	 */
    @DeleteMapping("/hotels/{hotelId}")
    public void deleteHotelById(@PathVariable("hotelId") String hotelId);
    
    /**
	 * DELETE 
	 * This method used to delete hotel 
	 * 
	 * @param ResponseEntity containing hotel
	 */
    @DeleteMapping("/hotels")
    public void deleteAllHotel();

   
}


