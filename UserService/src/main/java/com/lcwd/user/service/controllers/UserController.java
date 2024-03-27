package com.lcwd.user.service.controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.payload.UserApiResponse;
import com.lcwd.user.service.services.UserService;
import com.lcwd.user.service.services.impl.UserServiceImpl;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "User Service APIs")
@CrossOrigin(origins = "http://localhost:9081")
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserServiceImpl userServiceImpl;

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	/**
	 *  create user 
	 * @param user
	 * @return ResponseEntity containing user
	 */
	 @Operation(summary = "Create a new user", tags = { "User", "Post" })
	    @ApiResponses({
	    	 @ApiResponse(responseCode = "200", content = {
	    	            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "201", content = {
	            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "500", content = {
		            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User userResponse = userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
	}
	 
	 
	 
	 /**
		 *  create user 
		 * @param user
		 * @return ResponseEntity containing user
		 */
		 @Operation(summary = "update user", tags = { "User", "Put" })
		    @ApiResponses({
		    	 @ApiResponse(responseCode = "200", content = {
		    	            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
		        @ApiResponse(responseCode = "201", content = {
		            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
		        @ApiResponse(responseCode = "500", content = {
			            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
		        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
		@PutMapping("/{userId}")
		@CachePut(cacheNames = "user" , key = "#userId")
		public ResponseEntity<User> updateUserByUserId(@PathVariable String userId,@RequestBody User user) {
			User userResponse = userService.getUserById(userId,user);
			return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
		}

	/**
	 * single user get
	 * @param userId
	 * @return ResponseEntity containing user
	 */
	 @Operation(summary = "get all user by userId", tags = { "User", "Filter" })
	    @ApiResponses({
	    	 @ApiResponse(responseCode = "200", content = {
	    	            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "201", content = {
	            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "500", content = {
		            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
	@GetMapping("/{userId}")
	@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
	//@Retry(name = "ratingHotelService", fallbackMethod = "callingfallbackWithId")
	@Cacheable(cacheNames ="user",key="#userId")
		public ResponseEntity<UserApiResponse> getSingleUser(@PathVariable String userId) {
			ResponseEntity<UserApiResponse> userResponse = null;
			try {
				logger.info("Get Single User Handler: UserController");
				User user = Optional.ofNullable(userService.getUser(userId)).orElse(null);
				UserApiResponse userApiResponse = UserApiResponse.builder()
						.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)).message("User Found SuccessFully !!")
						.exception(null).success(true).status(HttpStatus.OK)
						.user(user != null ? Arrays.asList(user) : Collections.emptyList()).build();
				userResponse = ResponseEntity.status(HttpStatus.OK).body(userApiResponse);
			}

			catch (ResourceNotFoundException ex) {
				UserApiResponse userApiResponse = UserApiResponse.builder()
						.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)).message("User not found")
						.exception(ex.getMessage()).success(false).status(HttpStatus.NOT_FOUND).user(null).build();
				userResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(userApiResponse);

			}
			return userResponse;
		}

	/**
	 * all user get
	 * @return ResponseEntity containing allUser
	 */
	 @Operation(summary = "get all users", tags = { "User", "Get" })
	    @ApiResponses({
	    	 @ApiResponse(responseCode = "200", content = {
	    	            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "201", content = {
	            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "500", content = {
		            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
	        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
	@GetMapping
	@CircuitBreaker(name="ratingHotelService",fallbackMethod = "ratingHotelFallback")
	//@Retry(name="ratingHotelService",fallbackMethod = "ratingHotelFallback")
	// URL for testing : localhost:8081/users?pageNo=0&pageSize=10
		public ResponseEntity<UserApiResponse> getAllUser(@PageableDefault(page = 0, size = 10) Pageable pageable) {
			ResponseEntity<UserApiResponse> userResponse = null;
			long totalRecords = 0;
			totalRecords = userServiceImpl.getTotalRecords();

			try {
				Page<User> getAllUser = userService.getAllUser(pageable);
				if (getAllUser.hasContent()) {
					UserApiResponse userApiResponse = UserApiResponse.builder()
							.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
							.message("User fetch SuccessFully !!").exception(null)
							.success(true).status(HttpStatus.OK).user(getAllUser.getContent())
							.pageNo(pageable.getPageNumber()).pageSize(pageable.getPageSize())	
							.totalRecords(totalRecords).build();
					userResponse = ResponseEntity.status(HttpStatus.OK).body(userApiResponse);
				}
				else {
					UserApiResponse userApiResponse = UserApiResponse.builder()
							.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
							.message("No records found !!").success(false).status(HttpStatus.NOT_FOUND)
							.user(null).pageNo(pageable.getPageNumber())
							.pageSize(pageable.getPageSize()).totalRecords(totalRecords).build();
					userResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(userApiResponse);
				}
			}
			catch (ResourceNotFoundException ex) {
				UserApiResponse userApiResponse = UserApiResponse.builder()
						.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
						.message("User not found").exception(ex.getMessage())
						.success(false).status(HttpStatus.OK).user(null).pageNo(pageable.getPageNumber())
						.pageSize(pageable.getPageSize()).totalRecords(totalRecords).build();
				userResponse = ResponseEntity.status(HttpStatus.OK).body(userApiResponse);
				
			}
			return userResponse;
		}
	 
	 /**
		 *  delete user 
		 * @param user
		 * @return ResponseEntity containing user
		 */
		 @Operation(summary = "delete user", tags = { "User", "Delete" })
		    @ApiResponses({
		    	 @ApiResponse(responseCode = "200", content = {
		    	            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
		        @ApiResponse(responseCode = "201", content = {
		            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
		        @ApiResponse(responseCode = "500", content = {
			            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
		        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
		@DeleteMapping
		public ResponseEntity<UserApiResponse> deleteUsers() {
			 ResponseEntity<UserApiResponse> userResponse;
			 try {
				 userService.deleteAllUsers();
					userResponse = ResponseEntity.status(HttpStatus.OK)
							.body(UserApiResponse.builder()
									.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
									.message("User Deleted SuccessFully !!").success(true)
									.status(HttpStatus.OK).user(null).build());
			} catch (Exception ex) {
				 userResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(UserApiResponse.builder()
									.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
									.message("An error occurred while deleting users.")
									.exception(ex.getMessage()).success(false)
									.status(HttpStatus.INTERNAL_SERVER_ERROR).user(null).build());
			}	 
				 return userResponse;
			}		 
	
		  /**
			 *  delete user by userId
			 * @param user
			 * @return ResponseEntity containing user
			 */
			 @Operation(summary = "delete user", tags = { "User", "Delete", "Filter" })
			    @ApiResponses({
			    	 @ApiResponse(responseCode = "200", content = {
			    	            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
			        @ApiResponse(responseCode = "201", content = {
			            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
			        @ApiResponse(responseCode = "500", content = {
				            @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
			        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }) })
			@DeleteMapping("/{userId}")
			@CircuitBreaker(name="ratingHotelService",fallbackMethod = "callingfallbackWithId")
			@CacheEvict(cacheNames ="user",key="#userId")
			public ResponseEntity<UserApiResponse> deleteUserByUserId(@PathVariable String userId) {
				 logger.info("user deleted");
				 ResponseEntity<UserApiResponse> userResponse;
				 long totalRecords = 0;
				 totalRecords = userServiceImpl.getTotalRecords();
				 try {
					 userService.deleteUserByUserId(userId);
						userResponse = ResponseEntity.status(HttpStatus.OK)
								.body(UserApiResponse.builder()
										.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
										.totalRecords(totalRecords)
										.message("User Deleted SuccessFully by userId : "+ userId).success(true)
										.status(HttpStatus.OK).user(null).build());
				}
				 catch (EmptyResultDataAccessException ex) {
						UserApiResponse userApiResponse = UserApiResponse.builder()
								.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
								.message("User not found").exception(ex.getMessage())
								.success(false).status(HttpStatus.OK).user(null)
								.totalRecords(totalRecords).build();
						userResponse = ResponseEntity.status(HttpStatus.OK).body(userApiResponse);	
					}
				 catch (Exception ex) {
					 userResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								.body(UserApiResponse.builder()
										.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
										.message("An error occurred while deleting user")
										.totalRecords(totalRecords)
										.exception(ex.getMessage()).success(false)
										.status(HttpStatus.INTERNAL_SERVER_ERROR).user(null).build());
				}	 
					 return userResponse;
				}	

	/**
	 * Fallback method for handling exceptions in ratingHotel service call.
	 * @param userId The user ID.
	 * @param ex The exception.
	 * @return ResponseEntity containing fallback response.
	 */
	public ResponseEntity<UserApiResponse> callingfallbackWithId(String userId, Exception ex) {
		 logger.info("Fallback is executed because server is down", ex.getMessage());
		    User user = userServiceImpl.getUserDetails();
		    UserApiResponse userApiResponse = UserApiResponse.builder()
		        .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
		        .message("Service is not avaiable.").exception(ex.getMessage())
		        .success(false).status(HttpStatus.SERVICE_UNAVAILABLE)
		        .user(List.of(user)).pageNo(0).pageSize(10).totalRecords(0).build();
		    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(userApiResponse);
	}
	/**
	 * Fallback method for handling exceptions in ratingHotel service call.
	 * @param ex The exception.
	 * @return ResponseEntity containing fallback response.
	 */
	public ResponseEntity<UserApiResponse> ratingHotelFallback(Exception ex) {
	    logger.info("Fallback is executed because server is down", ex.getMessage());
	    User user = userServiceImpl.getUserDetails();
	    UserApiResponse userApiResponse = UserApiResponse.builder()
	        .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
	        .message("Service is not avaiable.").exception(ex.getMessage())
	        .success(false).status(HttpStatus.SERVICE_UNAVAILABLE)
	        .user(Arrays.asList(user)).pageNo(0).pageSize(10).totalRecords(0).build();
	    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(userApiResponse);
	}

	
}
