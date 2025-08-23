package moe.uoxou.yadokari_hooks.send;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import moe.uoxou.yadokari_hooks.config.YadokariHooksConfig;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class WebhookSender {
	private final Supplier<YadokariHooksConfig> config;
	private final Supplier<Logger> logger;

	public WebhookSender(Supplier<YadokariHooksConfig> config, Supplier<Logger> logger) {
		this.config = config;
		this.logger = logger;
	}

	public HttpResponse<String> sendWebhook(URL url, JsonObject payload) {
		try (HttpClient client = HttpClient.newHttpClient()) {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(url.toURI())
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
					.build();

			return client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public void onServerStart(StartingEngineEvent<Server> event) {
		this.config.get().getHooks().stream()
				.filter(c -> c.onServerStart().enabled())
				.forEach(hook -> {
					JsonObject payload = createPayload(event);
					HttpResponse<String> response = sendWebhook(hook.url(), payload);
					this.logger.get().info("Sent webhook to {}: {} - {}", hook.url(), response.statusCode(), response.body());
				});
	}

	public static JsonObject createPayload(StartingEngineEvent<Server> event) {
		JsonObject data = new JsonObject();
		Map<String, JsonElement> forDiscord = new HashMap<>();
		forDiscord.put("content", new JsonPrimitive("サーバーが起動しました"));

		return createPayload(Event.SERVER_START, new Date(), data, forDiscord);
	}

	public static JsonObject createPayload(Event event, Date timestamp, JsonElement data, @Nullable Map<String, JsonElement> extra) {
		JsonObject payload = new JsonObject();

		payload.addProperty("event", event.getValue());
		payload.addProperty("timestamp", timestamp.toString());
		payload.add("data", data);
		if (extra != null) {
			extra.forEach(payload::add);
		}

		return payload;
	}
}
