package com.example.NYCCab;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@EnableEncryptableProperties
public class NycCabApplication {

	public static void main(String[] args) {
		SpringApplication.run(NycCabApplication.class, args);
	}

}
