package com.lcwd.hotel.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.hotel.controller.HotelController;
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
	public Hotel get(String id) {
		 logger.info("fetching all hotel data with id -> HotelServiceImpl");
		return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("hotel with given id "+id+" is not found"));
	}

}
