package com.assess.docservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class DocserviceApplication {

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.out.println("FORCED JVM TZ = " + TimeZone.getDefault());
	}

	public static void main(String[] args) {

		System.out.println("VM OPTION TZ = " + System.getProperty("user.timezone"));

		SpringApplication.run(DocserviceApplication.class, args);
	}

}
