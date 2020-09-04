package com.example.NYCCab;

/**
 * Exception for bad medallions in GET request.
 */
public class MedallionException extends Exception {
	public MedallionException(String errorMessage) {
		super(errorMessage);
	}
}