package fr.polytech.caspapicoserver.controllers;

import fr.polytech.caspapicoserver.database.repositories.DeviceRepository;
import fr.polytech.caspapicoserver.database.repositories.RawDataRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;

@Controller
public class WebController {

	final DeviceRepository deviceRepository;
	final RawDataRepository rawDataRepository;

	public WebController(DeviceRepository deviceRepository, RawDataRepository rawDataRepository) {
		this.deviceRepository = deviceRepository;
		this.rawDataRepository = rawDataRepository;
	}

	@GetMapping("/devices")
	public Mono<String> listDevices(Model map){
		return deviceRepository.findByIdNotNull((Pageable) PageRequest.of(0, 10)).collectList().flatMap(devices -> {
			map.addAttribute("devices", devices);
			return Mono.just("deviceList");
		});
	}
}
