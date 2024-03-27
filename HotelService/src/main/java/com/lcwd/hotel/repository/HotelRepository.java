package com.lcwd.hotel.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.hotel.entities.Hotel;
import org.springframework.data.jpa.repository.Query;


public interface HotelRepository extends JpaRepository<Hotel, String>{

	 @Query("SELECT h FROM Hotel h WHERE h.id = :id")
	 Optional<Hotel> findById(String id);
}
