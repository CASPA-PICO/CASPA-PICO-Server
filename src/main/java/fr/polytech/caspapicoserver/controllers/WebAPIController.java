package fr.polytech.caspapicoserver.controllers;

import fr.polytech.caspapicoserver.database.documents.Account;
import fr.polytech.caspapicoserver.database.repositories.DeviceRepository;
import fr.polytech.caspapicoserver.database.repositories.RawDataRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/webapi")
public class WebAPIController {

	final DeviceRepository deviceRepository;
	final RawDataRepository rawDataRepository;

	public WebAPIController(DeviceRepository deviceRepository, RawDataRepository rawDataRepository) {
		this.deviceRepository = deviceRepository;
		this.rawDataRepository = rawDataRepository;
	}

	@GetMapping("/device/info")
	public Mono<String> webapiDeviceInfo(Model map, @RequestParam("deviceKey") String deviceKey){
		return deviceRepository.findByKey(deviceKey).flatMap(device -> {
			map.addAttribute("device", device);
			return Mono.just("htmx/device/deviceInfo.html");
		}).switchIfEmpty(Mono.empty()).onErrorReturn("");
	}

	@GetMapping("/device/activate")
	public Mono<String> webapiGetDeviceActivate(Model map, @RequestParam("deviceKey") String deviceKey){
		map.addAttribute("deviceKey", deviceKey);
		return Mono.just("htmx/device/deviceActivate.html");
	}

	@PostMapping("/device/activate")
	public Mono<String> webapiPostDeviceActivate(Authentication authentication, Model map, ServerWebExchange swe){
		return swe.getFormData().flatMap(formData -> {
			if(formData.containsKey("deviceKey") && formData.containsKey("activationKey")){
				String deviceKey = formData.getFirst("deviceKey");
				map.addAttribute("deviceKey", deviceKey);
				String activationKey = formData.getFirst("activationKey");
				if(formData.getFirst("activationKey").length() == 6){
					return deviceRepository.findByKey(deviceKey).flatMap(device -> {
						if(device.getOwner().equals(((Account)authentication.getPrincipal()).getId())){
							device.setActivationKey(activationKey);
							return deviceRepository.save(device).flatMap(device1 -> {
								map.addAttribute("success", true);
								return Mono.just("htmx/device/deviceActivate.html");
							}).onErrorResume(throwable -> {
								map.addAttribute("activationError", "Erreur interne au serveur");
								return Mono.just("htmx/device/deviceActivate.html");
							});
						}
						else{
							map.addAttribute("activationError", "Vous n'êtes pas propriétaire de l'appareil");
							return Mono.just("htmx/device/deviceActivate.html");
						}
					}).switchIfEmpty(Mono.defer(() -> {
						map.addAttribute("activationError", "Appareil non trouvé");
						return Mono.just("htmx/device/deviceActivate.html");
					}))
					.onErrorResume(throwable -> {
						map.addAttribute("activationError", "Erreur interne au serveur");
						return Mono.just("htmx/device/deviceActivate.html");
					});
				}
				else{
					map.addAttribute("activationError", "La clé d'activation doit faire 6 caractères de long");
					return Mono.just("htmx/device/deviceActivate.html");
				}
			}
			return Mono.empty();
		});
		/*String activationKey = "", deviceKey = "";
		if(activationKey.length() != 6){
			map.addAttribute("activationKeyError", "La clé d'activation faire 6 caractères de long");
			map.addAttribute("deviceKey", deviceKey);
			return Mono.just("htmx/device/deviceActivate.html");
		}

		return deviceRepository.findByKey(deviceKey).flatMap(device -> {
			device.setActivationKey(activationKey);
			return deviceRepository.save(device).flatMap( device1 -> Mono.just(""));
		});*/
	}

}
