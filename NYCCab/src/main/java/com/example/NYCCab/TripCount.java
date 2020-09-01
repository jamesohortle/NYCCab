package com.example.NYCCab;

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

