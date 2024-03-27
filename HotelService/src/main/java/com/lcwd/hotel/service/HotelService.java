package com.lcwd.hotel.service;

import java.util.List;

import com.lcwd.hotel.entities.Hotel;

public interface HotelService {
	
	//create
	Hotel create(Hotel hotel);
	//getAll
	List<Hotel> getAll();
	//get Single
    Hotel get(String id);
    
    //delete hotel by hotelId
    Hotel deleteHotelByHotelId(String hotelId);
    
    
    //update hotel by hotelId
    Hotel updateHotelByHotelId(String hotelId,Hotel hotel);
}
