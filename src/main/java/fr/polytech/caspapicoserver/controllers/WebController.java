package fr.polytech.caspapicoserver.controllers;

import fr.polytech.caspapicoserver.database.documents.Account;
import fr.polytech.caspapicoserver.database.documents.Device;
import fr.polytech.caspapicoserver.database.repositories.AccountRepository;
import fr.polytech.caspapicoserver.database.repositories.DeviceRepository;
import fr.polytech.caspapicoserver.database.repositories.RawDataRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Locale;
import java.util.TreeMap;

@Controller
public class WebController {

	final DeviceRepository deviceRepository;
	final RawDataRepository rawDataRepository;
	final AccountRepository accountRepository;

	public WebController(DeviceRepository deviceRepository, RawDataRepository rawDataRepository, AccountRepository accountRepository) {
		this.deviceRepository = deviceRepository;
		this.rawDataRepository = rawDataRepository;
		this.accountRepository = accountRepository;
	}

	@GetMapping("/")
	public Mono<String> homePage(){
		return Mono.just("CASPA-PICO_ACCUEIL.html");
	}

	@GetMapping("/appareils")
	public Mono<String> listDevices(Model map, ServerWebExchange swe){
		addCountryListToModel(map);
		return deviceRepository.findByPublicDevice(true, PageRequest.of(0, 10)).collectList().flatMap(devices -> {
			map.addAttribute("devices", devices);
			return Mono.just("devices/CASPA-PICO_APPAREILS.html");
		}).onErrorReturn("devices/CASPA-PICO_APPAREILS.html")
				.switchIfEmpty(Mono.just("devices/CASPA-PICO_APPAREILS.html"));
	}

	@GetMapping("/mes-appareils")
	public Mono<String> listMyDevices(Authentication authentication, Model map, ServerWebExchange swe){
		Mono<String> mono = deviceRepository.findByOwner(((Account)authentication.getPrincipal()).getId(), PageRequest.of(0, 10)).collectList().flatMap(devices -> {
			map.addAttribute("devices", devices);
			return Mono.just("devices/CASPA-PICO_MES_APPAREILS.html");
		}).onErrorReturn("devices/CASPA-PICO_MES_APPAREILS.html");

		var idStr = swe.getRequest().getQueryParams().getFirst("deviceID");
		if(idStr != null && ObjectId.isValid(idStr)){
			return deviceRepository.findById(new ObjectId(idStr)).flatMap(device -> {
				map.addAttribute("selectDeviceId", device.getId());
				return mono;
			});
		}
		return mono;
	}

	@GetMapping("/ajout-appareil")
	public Mono<String> addDevice(Model map, ServerWebExchange swe){
		if(!map.containsAttribute("device")){
			map.addAttribute("device", new Device());
		}
		addCountryListToModel(map);
		return Mono.just("devices/CASPA-PICO_ADD_APPAREIL.html");
	}

	@PostMapping("/ajout-appareil")
	public Mono<String> createDevice(@Valid Device device, BindingResult bindingResult, Authentication authentication, Model map, ServerWebExchange swe){
		if(device.getActivationKey() != null && !device.getActivationKey().isBlank() && device.getActivationKey().length() != 6){
			bindingResult.rejectValue("activationKey", "device.error.activationKey_not_valid", "clé d'activation invalide !");
		}

		if(!bindingResult.hasErrors()){
			Device newDevice = new Device(device.getDisplayName(), device.isPublicDevice(), device.getDescription(), ((Account)authentication.getPrincipal()).getId());
			if(device.getActivationKey() != null && !device.getActivationKey().isBlank()){
				newDevice.setActivationKey(device.getActivationKey());
			}
			return deviceRepository.save(newDevice).flatMap(device1 -> {
				map.addAttribute("createdDevice", device1.getId());
				return Mono.just("devices/CASPA-PICO_ADD_APPAREIL_SUCCESS.html");
			}).onErrorResume(throwable -> {
				bindingResult.rejectValue("displayName", "server.error.database", "erreur interne lors de l'ajout de l'appareil");
				return addDevice(map, swe);
			});
		}

		return addDevice(map, swe);
	}

	@GetMapping("/donnees")
	public Mono<String> allDatas(){
		//return Mono.just("CASPA-PICO_DONNEES.html");
		return Mono.just("redirect:/grafana/");
	}

	@GetMapping("/a_propos")
	public Mono<String> about(){
		return Mono.just("CASPA-PICO_APROPOS.html");
	}

	private void addCountryListToModel(Model map){
		TreeMap<String, String> countryList = new TreeMap<>();
		for(Locale locale : Locale.getAvailableLocales()){
			if(!locale.getCountry().isBlank() && !locale.getDisplayCountry(Locale.FRANCE).isBlank()) {
				countryList.put(locale.getDisplayCountry(Locale.FRANCE), locale.getCountry());
			}
		}
		map.addAttribute("countryList", countryList);
	}
}
