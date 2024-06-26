package com.lcwd.user.service.services.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.external.services.RatingService;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

//    @Autowired
//    private RestTemplate restTemplate;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private RatingService ratingService;

	@Autowired
	private CacheManager cacheManager;

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    
	@PostConstruct
	public void preloadCache() {
		cacheManager.getCache("user");
		LocalDateTime currentDateTime = LocalDateTime.now();
		logger.info("****** Initializing Cache ****** :"+currentDateTime);
	}

	@Scheduled(fixedRate = 60000, initialDelay = 15000)
	public void clearCache() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		logger.info("****** Clearing the Cache ****** :"+currentDateTime);
		cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
	}
	

    /**
     * This method is used to save userId which is created by Random Number UUID 
     * * @return user
     */
    @Override
    public User saveUser(User user) {
        //generate  unique userId
    	logger.info("creating user data--> UserServiceImpl");
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }
    
    /**
     * This method is used to get all user
     * @return list of all user
     */
    @Override
    public Page<User> getAllUser(Pageable pageable) {
        //List<User> users = 
    	logger.info("Fetching all users data--> UserServiceImpl");
        Page<User> users = userRepository.findAll(pageable);
        ResponseEntity<List<Rating>> ratingsResponse = ratingService.getRatings();
        ResponseEntity<List<Hotel>> hotelsResponse = hotelService.getAllHotel();

        // Process ratings
        if (ratingsResponse != null && ratingsResponse.getBody() != null) {
            Map<String, List<Rating>> userRatingsMap = ratingsResponse.getBody().stream()
                    .collect(Collectors.groupingBy(Rating::getUserId));
            users.forEach(user -> user.setRatings(userRatingsMap.get(user.getUserId())));
        }

        // Process hotels
        if (hotelsResponse != null && hotelsResponse.getBody() != null) {
            Map<String, Hotel> hotelMap = hotelsResponse.getBody().stream()
                    .collect(Collectors.toMap(Hotel::getId, Function.identity()));
            users.forEach(user -> {
                if (user.getRatings() != null) {
                    user.getRatings().forEach(rating -> rating.setHotel(hotelMap.get(rating.getHotelId())));
                }
            });
        }


        return users;
    }


    /**
     * This method is used to get single user by userId
     * @param String userId
     * @return user
     */
    //get single user
	@Override
	public User getUserById(String userId) {
		// get user from database with the help of user repository
		logger.info("Fetching single users data--> UserServiceImpl");
		User user = userRepository.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User with given id is not found on server !! : " + userId));
		
		 List<Rating> ratingList = Optional.ofNullable(ratingService.getRatingsByUserId(userId).getBody()).orElse(Collections.emptyList());		 
		
		  List<Rating> ratings = ratingList.stream().map(rating -> {
			// api call to hotel service to get the hotel
			Hotel hotel = hotelService.getHotel(rating.getHotelId()).getBody();
			// set the hotel to rating
			rating.setHotel(hotel);
			// return the rating
			return rating;
		}).collect(Collectors.toList());

		user.setRatings(ratings);

		return user;
	}
   
    /**
     * This method is used to delete user by userId
     * @param String userId
     */
	@Override
	public void deleteUserByUserId(String userId) {
		logger.info("Deleting single user's data--> UserServiceImpl");
	    userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User with the given id " + userId + " is not found"));
	  //Data has updated in databases for both rating and hotel tables 
	   Optional.ofNullable(ratingService.getRatingsByUserId(userId).getBody())
	       .orElse(Collections.emptyList())
	       .forEach(rating -> hotelService.deleteHotelById(rating.getHotelId()));
	    
	    Optional.ofNullable(ratingService.getRatingsByUserId(userId).getBody())
            .orElse(Collections.emptyList())
            .forEach(rating -> ratingService.deleteRating(rating.getRatingId()));
	    
	   userRepository.deleteById(userId);
	}

	
	/**
	 * This method is used to delete all users
	 * @param user
	 */
	@Override
	public void deleteAllUsers() {
			logger.info("Deleting users data--> UserServiceImpl");
			//Optional.ofNullable(userRepository).ifPresent(UserRepository::deleteAll);
	}


	/**
	 * This method is used to update the user by userId
	 *  @param String userId , User updatedUser
	 *  @return user
	 */
	@Override
	public User updateUserById(String userId, User updatedUser) {
		logger.info("update users data by userId--> UserServiceImpl");
		User existingUser = userRepository.findById(userId).orElse(null);
		if (existingUser != null) {

			// Update existing user fields
			existingUser.setName(updatedUser.getName());
			existingUser.setEmail(updatedUser.getEmail());
			existingUser.setAbout(updatedUser.getAbout());
			
			List<Rating> ratings = Optional.ofNullable(updatedUser.getRatings()).orElse(Collections.emptyList());
			//Data has updated in databases for both rating and hotel tables 
			ratings.stream().forEach(rating->hotelService.updateHotelByHotelId(rating.getHotelId(), rating.getHotel()));
			ratings.stream().forEach(rating->ratingService.updateRatingByRatingId(rating.getRatingId(), rating));
			existingUser.setRatings(updatedUser.getRatings());
			return userRepository.save(existingUser);
		} else {
			throw new ResourceNotFoundException("User with given id is not found on server !! : " + userId);
		}
    }
	 

	/**
	 * This method is used to fetch the total count of user
	 * @return total count of user
	 */
	public long getTotalRecords() {
		return userRepository.count();
	}

    /**
     * This method used to create a dummy User Details and it's will shows when service is down
     * @return user
     */
    public User getUserDetails() {
		User user = User.builder().userId("1213").name("John").email("john@email.com")
				.about("This user is created dummy because some service is down")
				.ratings(Arrays.asList(Rating.builder().ratingId("22131").userId("1213").hotelId("31131").rating(2)
						.feedback("This rating is created dummy because some service is down")
						.hotel(Hotel.builder().id("31131").name("Taj Palace").location("Mumbai")
								.about("This hotel is created dummy beacause some service is down").build())
						.build()))
				.build();
		return user;
	}
	
}
