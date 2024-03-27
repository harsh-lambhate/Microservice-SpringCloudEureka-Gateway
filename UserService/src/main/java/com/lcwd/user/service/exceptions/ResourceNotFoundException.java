package com.lcwd.user.service.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceNotFoundException extends RuntimeException {
	
	private static final Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);
	
    public ResourceNotFoundException() {
        super("Resource not found on server !!");
    }

    public ResourceNotFoundException(String message) {
        super(message);
        logger.error("ResourceNotFoundException: || ", message, this);
    }

}

