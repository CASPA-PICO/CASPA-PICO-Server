package fr.polytech.caspapicoserver;

import fr.polytech.caspapicoserver.controllers.APIController;
import fr.polytech.caspapicoserver.database.documents.Device;
import fr.polytech.caspapicoserver.database.documents.RawData;
import fr.polytech.caspapicoserver.database.repositories.DeviceRepository;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
class CaspaPicoServerApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private APIController apiController;

	@Autowired
	private DeviceRepository deviceRepository;

	@Test
	public void contextLoads() {
		assertThat(apiController).isNotNull();
	}

	@Test
	public void checkTimeAPI(){
		assertThat(restTemplate.getForObject("http://localhost:" + port+ "/api/time", Long.class)).isCloseTo(Instant.now().toEpochMilli(), Percentage.withPercentage(0.001));
	}

	@Test
	public void checkDocumentDevice(){
		Device device1 = new Device("device1");
		Device device2 = new Device("device2");

		assertThat(device1.getDisplayName()).isEqualTo("device1");
		assertThat(device2.getDisplayName()).isEqualTo("device2");

		assertThat(device1.getId()).isNotEqualTo(device2.getId());
		assertThat(device1.isAllowed()).isTrue();
		assertThat(device2.isAllowed()).isTrue();

		assertThat(device1.getKey()).isNotEqualTo(device2.getKey());
		assertThat(device1.getKey().length()).isEqualTo(64);
		assertThat(device2.getKey().length()).isEqualTo(64);
	}

	@Test
	public void checkDocumentRawData() throws NoSuchAlgorithmException {
		Device device = new Device("device");
		RawData data = new RawData("test".getBytes(StandardCharsets.UTF_8), device.getId());
		assertThat(data.getDataHash()).isEqualToIgnoringCase("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff");
		assertThat(data.getData()).isEqualTo("test".getBytes(StandardCharsets.UTF_8));
		assertThat(data.getDeviceID()).isEqualTo(device.getId());
	}

	@Test
	public void checkDeviceCheck(){
		Device device = new Device("device");
		deviceRepository.save(device).block();
		assertThat(deviceRepository.findById(device.getId()).block()).usingRecursiveComparison().isEqualTo(device);
		assertThat(restTemplate.getForEntity("http://localhost:" + port + "/api/devices/check", String.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(restTemplate.getForEntity("http://localhost:" + port + "/api/devices/check?key=DefinitlyNotAKey", String.class).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(restTemplate.getForEntity("http://localhost:" + port + "/api/devices/check?key="+device.getKey(), String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void checkUploadData(){
		Device device = new Device("device");
		deviceRepository.save(device).block();
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-API-Key", "DefinitlyNotAKey");
		HttpEntity<String> request = new HttpEntity<>("", headers);
		assertThat(restTemplate.postForEntity("http://localhost:" + port + "/api/devices/uploadData", request, String.class).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		headers.clear();
		headers.add("X-API-Key", device.getKey());
		request = new HttpEntity<>("test", headers);
		assertThat(restTemplate.postForEntity("http://localhost:" + port + "/api/devices/uploadData", request, String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void checkActivateDevice(){
		Device device = new Device("device", "123456");
		deviceRepository.save(device).block();
		assertThat(restTemplate.getForEntity("http://localhost:" + port + "/api/devices/activate?activationKey=", String.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(restTemplate.getForEntity("http://localhost:" + port + "/api/devices/activate?activationKey=NotAKey", String.class).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(restTemplate.getForEntity("http://localhost:" + port + "/api/devices/activate?activationKey="+device.getActivationKey(), String.class).getBody()).isEqualTo(device.getKey());
		assertThat(Objects.requireNonNull(deviceRepository.findById(device.getId()).block()).getActivationKey()).isNull();
		assertThat(restTemplate.getForEntity("http://localhost:" + port + "/api/devices/activate?activationKey="+device.getActivationKey(), String.class).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
}
