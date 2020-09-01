package com.example.NYCCab;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@EnableCaching
@RestController
public class TripCountController {
	
	@Cacheable("tripcounts")
	@GetMapping("/tripCount")
	public ArrayList<TripCount> tripCount(
			@RequestParam(value = "medallion", required = true) String[] medallions,
			@RequestParam(value = "date", required = false) String date
	) throws Exception {
		// Validate medallions.
		// A medallion is a hexadecimal value with 32 digits.
		// Therefore, in range 0 to 0xFFF...FFF (32 long).
		// Split in half since 0xF...F (16 long) <= 2^64 -1 = MAX_INT 
		// Try parsing first to see if valid.
		ArrayList<String> validMedallions = new ArrayList<String>();
		for (String medallion : medallions) {
			assert(medallion.length() == 32);
			try {
				if (medallion.length() != 32) {
					throw new MedallionException("Medallion wrong length.");
				}
				Long.parseUnsignedLong(medallion.substring(0, 16), 16);
				Long.parseUnsignedLong(medallion.substring(16), 16);
				
				// Add to validMedallions.
				validMedallions.add(medallion);
			} catch (NumberFormatException ex) {
				continue;
			}catch (MedallionException ex) {
				continue;
			}
		}
		
		System.out.println("Valid medallions are:");
		for (String medallion : validMedallions) {
			System.out.println(medallion);
		}
		
		// Validate date.
		// For now, accept only yyyy-mm-dd, like database.
		// Accept only one date.
		Pattern datePattern = Pattern.compile("\\d{4}-[01]\\d-[0-3]\\d");
		Matcher dateMatch = datePattern.matcher(date);
		if (!dateMatch.matches()) {
			throw new DateException("Not a valid date.");
		}
		System.out.println(String.format("Date to query: %s.", date));
		
		
		// Query database and append counts for each medallion.
		// If medallion not found, count = 0.
		System.out.println("Connecting database...");
		ArrayList<TripCount> tripCounts = new ArrayList<TripCount>();
		for (String medallion : medallions) {
			tripCounts.add(countTrips(medallion, date));
		}
		
		
		return tripCounts;
	}
	
	private TripCount countTrips(String medallion, String date) {
		
		String url = "jdbc:mysql://localhost:3306/***REMOVED***?serverTimezone=Australia/Sydney&useLegacyDatetimeCode=false";
		String username = "***REMOVED***";
		String password = "***REMOVED******REMOVED***";
		String query = String.format(
				"SELECT COUNT(*) FROM cab_trip_data WHERE medallion = '%s' AND DATE(pickup_datetime) = '%s';",
				medallion,
				date);
		TripCount tc;
		try (
			// conn, stmt, result will autoclose.
			Connection conn = DriverManager.getConnection(url, username, password);
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
				
		) {
			System.out.println("Database connected!");
			if (result.next()) {
				long count = result.getInt("COUNT(*)");
				tc = new TripCount(medallion, date, count);
			} else {
				tc = new TripCount(medallion, date, 0L);
			}
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect to database!", e);
		}
		return tc;
	}
	
}