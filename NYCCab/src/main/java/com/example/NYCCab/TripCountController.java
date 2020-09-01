package com.example.NYCCab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import java.time.LocalDateTime;

@RestController
public class TripCountController {
	
	@GetMapping("/tripCount")
	public TripCount tripCount(
			@RequestParam(value = "medallion", defaultValue = "") long medallion,
			@RequestParam(value = "date", defaultValue = "") long date
	) {
		return new TripCount(0L, 0L, 0L);
	}
}