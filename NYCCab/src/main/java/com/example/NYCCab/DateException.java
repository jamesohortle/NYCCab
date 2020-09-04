package com.example.NYCCab;

/**
 * Exception for bad dates in GET request.
 */
public class DateException extends Exception {
	public DateException(String errorMessage) {
		super(errorMessage);
	}
}
