package com.oraclejava.demodogs;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthEndpointTests {

	@LocalServerPort
	int port;

	private final HttpClient client = HttpClient.newHttpClient();

	private HttpResponse<String> get(String path) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build();
		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	@Test
	void healthIsUp() throws Exception {
		HttpResponse<String> response = get("/actuator/health");
		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).contains("\"status\":\"UP\"");
	}

	@Test
	void livenessAndReadinessProbesAreUp() throws Exception {
		assertThat(get("/actuator/health/liveness").body()).contains("\"status\":\"UP\"");
		assertThat(get("/actuator/health/readiness").body()).contains("\"status\":\"UP\"");
	}

}
