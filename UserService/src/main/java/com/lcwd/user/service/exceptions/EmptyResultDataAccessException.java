package com.lcwd.user.service.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptyResultDataAccessException extends RuntimeException {
	
	private static final Logger logger = LoggerFactory.getLogger(EmptyResultDataAccessException.class);
	
    public EmptyResultDataAccessException() {
        super("Resource not found on server !!");
    }

    public EmptyResultDataAccessException(String message) {
        super(message);
        logger.error("EmptyResultDataAccessException: || ", message, this);
    }

}
