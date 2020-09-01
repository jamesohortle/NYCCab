package com.example.NYCCab;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@RestController
public class TripCountController {
	
	@GetMapping("/tripCount")
	public TripCount tripCount(
			@RequestParam(value = "medallion", required = true) String medallion,
			@RequestParam(value = "date", required = false) String date
	) {
		// Validate medallion.
		// Medallion is a hexadecimal value with 32 digits.
		// Therefore, in range 0 to 0xFFF...FFF (32 long).
		// Split in half since 0xF...F (16 long) <= 2^64 -1 = MAX_INT 
		// Try parsing first to see if valid.
		assert(medallion.length() == 32);
		try {
			Long.parseUnsignedLong(medallion.substring(0, 16), 16);
			Long.parseUnsignedLong(medallion.substring(16), 16);	
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return new TripCount("", "", 0L);
		}
		
		// Validate date.
		// For now, accept only yyyy-mm-dd, like database.
		assert(date.length() == 10);
		Pattern datePattern = Pattern.compile("\\d{4}-[01]\\d-[0-3]\\d");
		Matcher dateMatch = datePattern.matcher(date);
		if (!dateMatch.matches()) {
			return new TripCount("", "", 0L);
		}
		
		
		// Query database.
		String url = "jdbc:mysql://localhost:3306/nyccab?serverTimezone=Australia/Sydney&useLegacyDatetimeCode=false";
		String username = "root";
		String password = "rootroot";
		
		System.out.println("Connecting database...");
		
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
		    System.out.println("Database connected!");
		    Statement stmt = connection.createStatement();
			String query = String.format(
					"SELECT COUNT(*) FROM cab_trip_data WHERE medallion = '%s' AND DATE(pickup_datetime) = '%s';",
					medallion,
					date);
			ResultSet result = stmt.executeQuery(query);
			if (result.next()) {
				long count = result.getInt("COUNT(*)");
				return new TripCount(medallion, date, count);
			} else {
				
			}
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
		
		
		return new TripCount("", "", 0L);
	}
}