package fr.polytech.caspapicoserver;

import fr.polytech.caspapicoserver.controllers.APIController;
import fr.polytech.caspapicoserver.database.documents.Device;
import fr.polytech.caspapicoserver.database.documents.RawData;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CaspaPicoServerApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private APIController apiController;

    @Test
    public void contextLoads() {
        assertThat(apiController).isNotNull();
    }

    @Test
    public void checkTimeAPI(){
        assertThat(this.restTemplate.getForObject("http://localhost:" + port+ "/api/time", Long.class)).isCloseTo(Instant.now().toEpochMilli(), Percentage.withPercentage(0.001));
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

}
