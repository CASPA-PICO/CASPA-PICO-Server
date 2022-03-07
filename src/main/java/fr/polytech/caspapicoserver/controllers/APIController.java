package fr.polytech.caspapicoserver.controllers;

import fr.polytech.caspapicoserver.database.documents.RawData;
import fr.polytech.caspapicoserver.database.repositories.DeviceRepository;
import fr.polytech.caspapicoserver.database.repositories.RawDataRepository;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@RestController
@RequestMapping("/api")
public class APIController {

	final DeviceRepository deviceRepository;
	final RawDataRepository rawDataRepository;

	public APIController(DeviceRepository deviceRepository, RawDataRepository rawDataRepository) {
		this.deviceRepository = deviceRepository;
		this.rawDataRepository = rawDataRepository;
	}

	@GetMapping("/time")
	public String getTime(){
		return String.valueOf(Instant.now().toEpochMilli());
	}

	@GetMapping("/devices/check")
	public Mono<ResponseEntity<String>> checkDevice(final ServerWebExchange swe){
		if(!swe.getRequest().getQueryParams().containsKey("key")){
			return Mono.just(new ResponseEntity<>("no key parameter", HttpStatus.BAD_REQUEST));
		}

		String key = swe.getRequest().getQueryParams().getFirst("key");

		return deviceRepository.findByKey(key).map(device ->new ResponseEntity<>("ok", HttpStatus.OK))
				.switchIfEmpty(Mono.just(new ResponseEntity<>("no device", HttpStatus.FORBIDDEN)))
				.onErrorReturn(new ResponseEntity<>("Internal server error !", HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@PostMapping("/devices/uploadData")
	public Mono<ResponseEntity<String>> uploadData(final ServerWebExchange swe){
		if(!swe.getRequest().getHeaders().containsKey("X-API-Key")){
			return Mono.just(new ResponseEntity<>("missing X-API-Key header", HttpStatus.UNAUTHORIZED));
		}

		String key = swe.getRequest().getHeaders().getFirst("X-API-Key");
		return deviceRepository.findByKey(key).flatMap(device -> DataBufferUtils.join(swe.getRequest().getBody()).flatMap(dataBuffer -> {
			byte[] bytes = new byte[dataBuffer.readableByteCount()];
			dataBuffer.read(bytes);
			DataBufferUtils.release(dataBuffer);
			RawData rawData;
			try {
				rawData = new RawData(bytes, device.getId());
			} catch (NoSuchAlgorithmException e) {
				return Mono.just(new ResponseEntity<>("Internal server error : " + e, HttpStatus.INTERNAL_SERVER_ERROR));
			}
			return rawDataRepository.save(rawData).flatMap(rawData1 -> Mono.just(new ResponseEntity<>("ok", HttpStatus.OK)))
					.onErrorReturn(new ResponseEntity<>("Internal server error !", HttpStatus.INTERNAL_SERVER_ERROR));
		}))
		.switchIfEmpty(Mono.just(new ResponseEntity<>("no device", HttpStatus.FORBIDDEN)))
		.onErrorReturn(new ResponseEntity<>("Internal server error !", HttpStatus.INTERNAL_SERVER_ERROR));
	}
}
