package com.example.NYCCab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class TripCalculator {

	@Value("${spring.data.mysql.database.url}")
	private String url;
	@Value("${spring.data.mysql.database.user}")
	private String user;
	@Value("${spring.data.mysql.database.password}")
	private String password;
	@Value("${spring.data.mysql.database.table}")
	private String table;
	@Value("${spring.data.mysql.database.table.medallion}")
	private String medallionColumn;
	@Value("${spring.data.mysql.database.table.pickupdatetime}")
	private String pickupDatetimeColumn;

	public TripCalculator() {
	}

	/**
	 * Count trips made by medallion on date. Parameter keepCache = false will
	 * return new data from DB, but cache will not update.
	 */
	@Cacheable(value = "tripcounts", key = "{#medallion,#date}", condition = "#keepCache")
	public TripCount countTrips(String medallion, String date, Boolean keepCache) {

		// This user may request fresh data with keepCache = false,
		// but the old value is still stored in cache for other users.
		// Evict cache here?
		// evictTripCountsCache(medallion, date);

		String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = '%s' AND DATE(%s) = '%s';", this.table,
				this.medallionColumn, medallion, this.pickupDatetimeColumn, date);
		TripCount tc;
		System.out.println("Connecting to DB...");
		try (
				// conn, stmt, result will autoclose.
				Connection conn = DriverManager.getConnection(url, user, password);
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
		} catch (SQLException ex) {
			throw new IllegalStateException("Cannot connect to database!", ex);
		}
		return tc;
	}

	@CacheEvict(value = "tripcounts", allEntries = true)
	public void evictAllTripCountsCache() {
		System.out.println("Entire cache evicted!");
	}

	@CacheEvict(value = "tripcounts", key = "{#medallion, #date}")
	public void evictTripCountsCache(String medallion, String date) {
		System.out.println(String.format("Cache item (%s, %s) evicted!", medallion, date));
	}

}
