package com.lcwd.hotel.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.exception.ResourceNotFoundException;
import com.lcwd.hotel.repository.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService{

	@Autowired
	private HotelRepository repository;
	
	private Logger logger = LoggerFactory.getLogger(HotelService.class);
	
	@Override
	public Hotel create(Hotel hotel) {
		 logger.info("creating Hotel data--->HotelServiceImpl ");
		String random = UUID.randomUUID().toString();
		hotel.setId(random);
		return repository.save(hotel);
	}

	@Override
	public List<Hotel> getAll() {
		logger.info("fetching all hotel data -> HotelServiceImpl");
		return repository.findAll();
	}

	@Override
	public Hotel get(String hotelId) {
		 logger.info("fetching all hotel data with id -> HotelServiceImpl");
		return repository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("hotel with given id "+hotelId+" is not found"));
	}
	
	
	@Override
	public Hotel deleteHotelByHotelId(String hotelId) {
	    return repository.findById(hotelId)
	            .map(hotel -> {
	                repository.delete(hotel);
	                return hotel;
	            })
	            .orElseThrow(() -> new ResourceNotFoundException("Hotel with the given id " + hotelId + " is not found"));

	}

	
	@Override
	public Hotel updateHotelByHotelId(String hotelId, Hotel hotel) {
	    Hotel hotelDetail = repository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel with given id " + hotelId + " is not found"));
	    Optional.ofNullable(hotel.getId()).ifPresent(hotelDetail::setId);
	    Optional.ofNullable(hotel.getName()).ifPresent(hotelDetail::setName);
	    Optional.ofNullable(hotel.getLocation()).ifPresent(hotelDetail::setLocation);
	    Optional.ofNullable(hotel.getAbout()).ifPresent(hotelDetail::setAbout);
	    return repository.save(hotelDetail);
	}


}
