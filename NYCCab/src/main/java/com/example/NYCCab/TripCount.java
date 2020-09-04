package com.example.NYCCab;

/**
 * Represents the trip count for a taxi on a given date. No set methods since DB
 * is read-only.
 */
public class TripCount {
	private final String medallion;
	private final String date;
	private final long numTrips;

	public TripCount(String medallion, String date, long numTrips) {
		this.medallion = medallion;
		this.date = date;
		this.numTrips = numTrips;
	}

	public String getMedallion() {
		return medallion;
	}

	public String getDate() {
		return date;
	}

	public long getNumTrips() {
		return numTrips;
	}

}
