package fr.polytech.caspapicoserver.controllers;

import fr.polytech.caspapicoserver.database.documents.Device;
import fr.polytech.caspapicoserver.database.repositories.DeviceRepository;
import fr.polytech.caspapicoserver.database.repositories.RawDataRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller
public class WebController {

	final DeviceRepository deviceRepository;
	final RawDataRepository rawDataRepository;

	public WebController(DeviceRepository deviceRepository, RawDataRepository rawDataRepository) {
		this.deviceRepository = deviceRepository;
		this.rawDataRepository = rawDataRepository;
	}

	@GetMapping("/")
	public Mono<String> homePage(){
		return Mono.just("CASPA-PICO_ACCUEIL.html");
	}

	@GetMapping("/appareils")
	public Mono<String> listDevices(Model map){
		if(!map.containsAttribute("device")){
			map.addAttribute("device", new Device());
		}

		return deviceRepository.findByIdNotNull(PageRequest.of(0, 10)).collectList().flatMap(devices -> {
			map.addAttribute("devices", devices);
			return Mono.just("CASPA-PICO_APPAREIL.html");
		}).onErrorReturn("CASPA-PICO_APPAREIL.html");
	}

	@PostMapping("/appareils")
	public Mono<String> createDevice(@Valid Device device, BindingResult bindingResult, Model map){
		map.addAttribute("showAdd", true);
		if(device.getActivationKey() != null && !device.getActivationKey().isBlank()){
			if(device.getActivationKey().length() != 6){
				bindingResult.rejectValue("activationKey", "device.error.activationKey_not_valid", "Clé d'activation invalide !");
			}
		}

		if(!bindingResult.hasErrors()){
			map.addAttribute("device", new Device());
		}
		return listDevices(map);
	}

	@GetMapping("/donnees")
	public Mono<String> allDatas(){
		return Mono.just("CASPA-PICO_DONNEES.html");
	}

	@GetMapping("/a_propos")
	public Mono<String> about(){
		return Mono.just("CASPA-PICO_APROPOS.html");
	}
}
