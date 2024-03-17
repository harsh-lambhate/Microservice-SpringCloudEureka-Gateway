package com.lcwd.hotel.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.exception.ResourceNotFoundException;
import com.lcwd.hotel.repository.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService{

	@Autowired
	private HotelRepository repository;
	
	@Override
	public Hotel create(Hotel hotel) {
		String random = UUID.randomUUID().toString();
		hotel.setId(random);
		return repository.save(hotel);
	}

	@Override
	public List<Hotel> getAll() {
		return repository.findAll();
	}

	@Override
	public Hotel get(String id) {
		return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("hotel with given id "+id+" is not found"));
	}

}
