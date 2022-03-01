package fr.polytech.caspapicoserver.controllers;

import fr.polytech.caspapicoserver.database.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class APIController {

	@Autowired
	DeviceRepository deviceRepository;

	@GetMapping("/time")
	public String getTime(){
		return String.valueOf(Instant.now().toEpochMilli());
	}

	@GetMapping("/devices/check")
	public ResponseEntity<String> checkDevice(final ServerWebExchange swe){
		if(!swe.getRequest().getQueryParams().containsKey("key")){
			return new ResponseEntity<>("no key parameter", HttpStatus.BAD_REQUEST);
		}
		else if(deviceRepository.findByKey(swe.getRequest().getQueryParams().getFirst("key")) == null){
			return new ResponseEntity<>("no device", HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
}
