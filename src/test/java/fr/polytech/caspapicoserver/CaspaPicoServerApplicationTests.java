package fr.polytech.caspapicoserver;

import fr.polytech.caspapicoserver.controllers.APIController;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

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

}
