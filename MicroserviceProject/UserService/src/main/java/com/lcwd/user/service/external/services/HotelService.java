package com.lcwd.user.service.external.services;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


//The feign is a declarative HTTP web client developed by Netflix
//If you want to use feign create an interface and annotate it. 

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelService {

    @GetMapping("/hotels/{hotelId}")
    Hotel getHotel(@PathVariable("hotelId") String hotelId);

   
}


