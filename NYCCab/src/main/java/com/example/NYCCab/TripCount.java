package com.example.NYCCab;

public class TripCount {
	private final long medallion;
	private final long date;
	private final long numTrips;
	
	public TripCount(long medallion, long date, long numTrips) {
		this.medallion = medallion;
		this.date = date;
		this.numTrips = numTrips;
	}
	
	public long getMedallion() {
		return medallion;
	}
	
	public long getDate() {
		return date;
	}
	
	public long numTrips() {
		return numTrips;
	}

}

