package dev.pablito.dots.api.discogs;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.pablito.dots.entity.MessageManager;

@Component
public class DiscogsClient {
	private static final String DISCOGS_API_URL = "https://api.discogs.com";
	// INFO: Relevant information
	@Value("${discogs.token}")
	private String TOKEN;

	// Makes sure no request is lost for the error 429 "Too many requests"
	public HttpResponse<String> requestHandler(HttpClient client, HttpRequest request)
			throws IOException, InterruptedException {
		HttpResponse<String> response;

		response = client.send(request, HttpResponse.BodyHandlers.ofString());
		int waitingTime = 1000;
		while (response.statusCode() == 429) {
			System.out.println("En el requestHandler ha fallado la peticion, esperamos " + waitingTime);
			Thread.sleep(waitingTime);
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			waitingTime += 10000;
		}
		return response;

	}

	// Function: Get orders from Discogs

	public DiscogsOrder getDiscogsOrder(String id) throws IOException, InterruptedException {
		// Cliente HTTP
		HttpClient client = HttpClient.newHttpClient();
		// Peticion HTTP
		HttpRequest request = HttpRequest.newBuilder().GET().header("Authorization", "Discogs token=" + TOKEN)
				.uri(URI.create(DISCOGS_API_URL + "/marketplace/orders/" + id)).build();
		// Envia la Peticion HTTP
		HttpResponse<String> response = requestHandler(client, request);
		// Comprueba si ha sido correcta la Peticion HTTP
		if (response.statusCode() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			// Configuramos para que los campos que no aparecen en Order se ignoren
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// Lee los valores y los guarda en la variable "order"
			return mapper.readValue(response.body(), new TypeReference<DiscogsOrder>() {
			});
		} else {
			System.out.println("[ERROR " + response.statusCode() + "]: Trying to get order " + id);
			System.out.println(response.body());
			return null;
		}
	}

	public List<DiscogsOrder> getDiscogsOrdersCreatedAfter(String createdAfter)
			throws IOException, InterruptedException {
		List<DiscogsOrder> orders = new ArrayList<DiscogsOrder>();
		int page = 1;
		DiscogsApiResponse apiResponse;
		do {
			apiResponse = getDiscogsOrdersCreatedAfterWithPagination(createdAfter, page, 100);
			orders.addAll(apiResponse.getOrders());
			page++;
		} while (apiResponse.getPagination().getUrls().getNext() != null);
		return orders;
	}

	public DiscogsApiResponse getDiscogsOrdersCreatedAfterWithPagination(String createdAfter, int page, int perPage)
			throws IOException, InterruptedException {
		// Cliente HTTP
		HttpClient client = HttpClient.newHttpClient();
		// Petición HTTP
		String url = DISCOGS_API_URL + "/marketplace/orders?sort_order=desc&created_after=" + createdAfter + "&page="
				+ page + "&per_page=" + perPage;
		HttpRequest request = HttpRequest.newBuilder().GET().header("Authorization", "Discogs token=" + TOKEN)
				.uri(URI.create(url)).build();
		// Envía la Petición HTTP
		HttpResponse<String> response = requestHandler(client, request);
		// Comprueba si ha sido correcta la Petición HTTP
		if (response.statusCode() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			// Configuramos para que los campos que no aparecen en Order se ignoren
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// Lee los valores y los guarda en la variable "orders"
			return mapper.readValue(response.body(), new TypeReference<DiscogsApiResponse>() {
			});
		} else {

			System.out.println("[ERROR " + response.statusCode() + "]: Trying to get all  orders");
			System.out.println(response.body());
			return null;
		}
	}

	public DiscogsRelease getRelease(Long id) throws IOException, InterruptedException {
		// Cliente HTTP
		HttpClient client = HttpClient.newHttpClient();
		// Peticion HTTP
		HttpRequest request = HttpRequest.newBuilder().GET().header("Authorization", "Discogs token=" + TOKEN)
				.uri(URI.create(DISCOGS_API_URL + "/releases/" + id)).build();
		// Envia la Peticion HTTP
		HttpResponse<String> response = requestHandler(client, request);
		// Comprueba si ha sido correcta la Peticion HTTP
		if (response.statusCode() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			// Configuramos para que los campos que no aparecen en Order se ignoren
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// Lee los valores y los guarda en la variable "order"
			return mapper.readValue(response.body(), new TypeReference<DiscogsRelease>() {
			});
		} else {
			System.out.println("[ERROR " + response.statusCode() + "]: Trying to get release " + id);
			System.out.println(response.body());
			return null;
		}
	}

	public ListDiscogsOrders getNewDiscogsOrders() throws IOException, InterruptedException {
		// Cliente HTTP
		HttpClient client = HttpClient.newHttpClient();
		// Petición HTTP
		HttpRequest request = HttpRequest.newBuilder().GET().header("Authorization", "Discogs token=" + TOKEN)
				.uri(URI.create(DISCOGS_API_URL
						+ "/marketplace/orders?archived=false&sort_order=desc&status=Payment%20Received"))
				.build();
		// Envía la Petición HTTP
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		// Comprueba si ha sido correcta la Petición HTTP
		if (response.statusCode() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			// Configuramos para que los campos que no aparecen en Order se ignoren
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// Lee los valores y los guarda en la variable "orders"
			return mapper.readValue(response.body(), new TypeReference<ListDiscogsOrders>() {
			});
		} else {
			System.out.println("[ERROR " + response.statusCode() + "]: Trying to get all  orders");
			System.out.println(response.body());
			return null;
		}
	}

	// TODO: Pagination parameters
	public MessageManager getDiscogsMessages(String id) throws IOException, InterruptedException {
		// Cliente HTTP

		HttpClient client = HttpClient.newHttpClient();

		// Petición HTTP
		HttpRequest request = HttpRequest.newBuilder().GET().header("Authorization", "Discogs token=" + TOKEN)
				.uri(URI.create(DISCOGS_API_URL + "/marketplace/orders/" + id + "/messages")).build();
		// Envía la Petición HTTP

		HttpResponse<String> response = requestHandler(client, request);

		// Comprueba si ha sido correcta la Petición HTTP
		if (response.statusCode() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			// Configuramos para que los campos que no aparecen en Order se ignoren
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// Lee los valores y los guarda en la variable "orders"

			return mapper.readValue(response.body(), new TypeReference<MessageManager>() {
			});

		} else {
			System.out.println("[ERROR " + response.statusCode() + "]: Trying to get messages of order :" + id);
			System.out.println(response.body());

			return null;

		}
	}

	public static String normalizeDiscogsCondition(String condition) {
		if (condition == null || condition.isBlank()) {
			throw new IllegalArgumentException("La condición no puede ser null o vacía");
		}

		String c = condition.trim().toUpperCase();

		return switch (c) {
		case "M", "MINT" -> "Mint (M)";

		case "NM", "M-", "NEAR MINT" -> "Near Mint (NM or M-)";

		case "VG+", "VERY GOOD PLUS" -> "Very Good Plus (VG+)";

		case "VG", "VERY GOOD" -> "Very Good (VG)";

		case "G+", "GOOD PLUS" -> "Good Plus (G+)";

		case "G", "GOOD" -> "Good (G)";

		case "F", "FAIR" -> "Fair (F)";

		case "P", "POOR" -> "Poor (P)";

		default -> throw new IllegalArgumentException("Condición no válida para Discogs: " + condition);
		};
	}

	// Mirar el request handler
	public String createListing(long releaseId, double price, String discCondition, String sleeveCondition,
			String comments) throws IOException, InterruptedException {
		if (releaseId <= 0) {
			throw new IllegalArgumentException("El release_id es obligatorio y debe ser mayor a 0.");
		}
		if (price <= 0) {
			throw new IllegalArgumentException("El precio (price) debe ser mayor a 0.");
		}

		String status = "For Sale";

		// Crear el cuerpo de la solicitud como JSON
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode payload = mapper.createObjectNode();
		payload.put("release_id", releaseId);
		payload.put("price", price);
		payload.put("condition", normalizeDiscogsCondition(discCondition));
		payload.put("sleeve_condition", normalizeDiscogsCondition(sleeveCondition));
		payload.put("comments", comments);
		payload.put("status", status);

		// Crear la solicitud HTTP
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(mapper.writeValueAsString(payload)))
				.header("Content-Type", "application/json").header("Authorization", "Discogs token=" + TOKEN)
				.uri(URI.create(DISCOGS_API_URL + "/marketplace/listings")).build();

		HttpResponse<String> response = requestHandler(client, request);
		// Enviar la solicitud y manejar la respuesta
		if (response.statusCode() == 201) { // 201 Created
			return response.body(); // Devuelve la respuesta en caso de éxito
		} else {
			System.out.println("[ERROR " + response.statusCode() + "]: No se pudo crear el listado.");
			System.out.println(response.body());
			return null;
		}
	}

	public DiscogsOrder updateOrderStatus(String id, String newStatus) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();

		String jsonPayload = String.format("{\"status\": \"%s\"}", newStatus);
		HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(jsonPayload))
				.header("Content-Type", "application/json").header("Authorization", "Discogs token=" + TOKEN)
				.uri(URI.create(DISCOGS_API_URL + "/marketplace/orders/" + id)).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			// Configuramos para que los campos que no aparecen en Order se ignoren
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// Lee los valores y los guarda en la variable "order"
			return mapper.readValue(response.body(), new TypeReference<DiscogsOrder>() {
			});
		} else {
			System.out.println("[ERROR " + response.statusCode() + "]: Trying to update order " + id);
			System.out.println(response.body());
			return null;
		}
	}

	public void sendMessage(String id, String message) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();

		Map<String, String> payload = new HashMap<>();
		if (message != null && !message.isEmpty()) {
			payload.put("message", message); // Agregar el mensaje si está presente
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonPayload = objectMapper.writeValueAsString(payload);
		HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(jsonPayload))
				.header("Content-Type", "application/json").header("Authorization", "Discogs token=" + TOKEN)
				.uri(URI.create(DISCOGS_API_URL + "/marketplace/orders/" + id + "/messages")).build();

		HttpResponse<String> response = requestHandler(client, request);

		if (response.statusCode() == 201) {
		} else {
			System.out.println("[ERROR " + response.statusCode() + "]: Trying to send message to " + id);
			System.out.println(response.body());
		}
	}

}
