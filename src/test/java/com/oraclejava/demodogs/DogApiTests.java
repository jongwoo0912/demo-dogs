package com.oraclejava.demodogs;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DogApiTests {

	private static final String DOG_JSON = """
			{"kind":"진돗개","price":500000,"image":"jindo.jpg","country":"대한민국",
			 "height":50.5,"weight":18.0,"content":"충성심이 강한 한국 토종견"}
			""";

	@LocalServerPort
	int port;

	private final HttpClient client = HttpClient.newHttpClient();

	private HttpResponse<String> send(String method, String path, String body) throws Exception {
		HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
			.header("Content-Type", "application/json")
			.method(method, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
		return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
	}

	@Test
	void crudFlow() throws Exception {
		// Create
		HttpResponse<String> created = send("POST", "/api/dogs", DOG_JSON);
		assertThat(created.statusCode()).isEqualTo(201);
		assertThat(created.body()).contains("\"kind\":\"진돗개\"", "\"readcount\":0");
		Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(created.body());
		assertThat(m.find()).isTrue();
		String path = "/api/dogs/" + m.group(1);
		assertThat(created.headers().firstValue("Location")).hasValue(path);

		// Read (조회수 증가)
		assertThat(send("GET", path, null).body()).contains("\"readcount\":1");
		assertThat(send("GET", path, null).body()).contains("\"readcount\":2");

		// List
		HttpResponse<String> list = send("GET", "/api/dogs", null);
		assertThat(list.statusCode()).isEqualTo(200);
		assertThat(list.body()).startsWith("[").contains("진돗개");

		// Update (조회수 유지)
		HttpResponse<String> updated = send("PUT", path, DOG_JSON.replace("500000", "700000"));
		assertThat(updated.statusCode()).isEqualTo(200);
		assertThat(updated.body()).contains("\"price\":700000", "\"readcount\":2");

		// Delete
		assertThat(send("DELETE", path, null).statusCode()).isEqualTo(204);
		assertThat(send("GET", path, null).statusCode()).isEqualTo(404);
	}

	@Test
	void invalidRequestIsRejected() throws Exception {
		HttpResponse<String> response = send("POST", "/api/dogs", "{\"kind\":\"\",\"price\":-1}");
		assertThat(response.statusCode()).isEqualTo(400);
	}

	@Test
	void missingDogReturns404() throws Exception {
		assertThat(send("GET", "/api/dogs/999999", null).statusCode()).isEqualTo(404);
		assertThat(send("PUT", "/api/dogs/999999", DOG_JSON).statusCode()).isEqualTo(404);
		assertThat(send("DELETE", "/api/dogs/999999", null).statusCode()).isEqualTo(404);
	}

}
