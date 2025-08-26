package moe.uoxou.yadokari_hooks.send;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import moe.uoxou.yadokari_hooks.config.YadokariHooksConfig;
import moe.uoxou.yadokari_hooks.util.embed.*;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.message.PlayerChatEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.util.Color;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

public final class WebhookSender {
	private final Supplier<YadokariHooksConfig> config;
	@SuppressWarnings("unused") private final Supplier<Logger> logger;

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

	public void onServerStarted(StartedEngineEvent<Server> event) {
		this.config.get().getHooks().stream()
				.filter(c -> c.onServerStart().enabled())
				.forEach(hook -> {
					JsonObject payload = createPayload(event, hook.format());
					sendWebhook(hook.url(), payload);
				});
	}

	public void onServerStopping(StoppingEngineEvent<Server> event) {
		this.config.get().getHooks().stream()
				.filter(c -> c.onServerStop().enabled())
				.forEach(hook -> {
					JsonObject payload = createPayload(event, hook.format());
					sendWebhook(hook.url(), payload);
				});
	}

	public void onPlayerJoin(ServerSideConnectionEvent.Join event) {
		this.config.get().getHooks().stream()
				.filter(c -> c.onPlayerJoin().enabled())
				.forEach(hook -> {
					JsonObject payload = createPayload(event, hook.format());
					sendWebhook(hook.url(), payload);
				});
	}

	public void onPlayerLeave(ServerSideConnectionEvent.Leave event) {
		this.config.get().getHooks().stream()
				.filter(c -> c.onPlayerLeave().enabled())
				.forEach(hook -> {
					JsonObject payload = createPayload(event, hook.format());
					sendWebhook(hook.url(), payload);
				});
	}

	public void onPlayerChat(PlayerChatEvent.Submit event) {
		this.config.get().getHooks().stream()
				.filter(c -> c.onPlayerChat().enabled())
				.forEach(hook -> {
					JsonObject payload = createPayload(event, hook.format());
					sendWebhook(hook.url(), payload);
				});
	}

	public static JsonObject createPayload(StartedEngineEvent<Server> event, YadokariHooksConfig.Format format) {
		Optional<String> host = event.engine().boundAddress().map(InetSocketAddress::getHostString);
		Optional<Integer> port = event.engine().boundAddress().map(InetSocketAddress::getPort);

		switch (format) {
			case GENERIC: {
				JsonObject data = new JsonObject();
				data.addProperty("version", event.game().platform().minecraftVersion().name());
				data.addProperty("host", host.orElse("Unknown"));
				data.addProperty("port", port.map(String::valueOf).orElse("Unknown"));
				data.add("implementation", PayloadHelpers.createPluginPayload(event.game().platform().container(Platform.Component.IMPLEMENTATION)));

				return createPayload(Event.SERVER_START, new Date(), data);
			}
			case DISCORD: {
				IEmbedBuilder embed = new EmbedBuilder().setTitle("✅ サーバーが起動しました")
						.setColor(0x008000)
						.setTimestamp(Instant.now().toString())
						.addField(new EmbedFieldBuilder()
								.setName("Version")
								.setValue(event.game().platform().minecraftVersion().name()))
						.addField(new EmbedFieldBuilder()
								.setName("Host")
								.setValue(host.orElse("Unknown")))
						.addField(new EmbedFieldBuilder()
								.setName("Port")
								.setValue(port.map(String::valueOf).orElse("Unknown")))
						.addField(new EmbedFieldBuilder()
								.setName("Impl Name")
								.setValue(event.game().platform().container(Platform.Component.IMPLEMENTATION).metadata().name().orElse("Unknown"))
								.setInline(true))
						.addField(new EmbedFieldBuilder()
								.setName("Impl Version")
								.setValue(event.game().platform().container(Platform.Component.IMPLEMENTATION).metadata().version().toString())
								.setInline(true));

				return createDiscordPayload(embed);
			}
			default:
				throw new IllegalArgumentException("Unknown format: " + format);
		}
	}

	public static JsonObject createPayload(StoppingEngineEvent<Server> event, YadokariHooksConfig.Format format) {
		switch (format) {
			case GENERIC: {
				JsonObject data = new JsonObject();
				// 必要な情報を data に追加
				return createPayload(Event.SERVER_STOP, new Date(), data);
			}
			case DISCORD: {
				IEmbedBuilder embed = new EmbedBuilder()
						.setTitle("❌ サーバーが停止しました")
						.setColor(0x008000)
						.setTimestamp(Instant.now().toString());
				return createDiscordPayload(embed);
			}
			default:
				throw new IllegalArgumentException("Unknown format: " + format);
		}
	}

	public static JsonObject createPayload(ServerSideConnectionEvent.Join event, YadokariHooksConfig.Format format) {
		switch (format) {
			case GENERIC: {
				JsonObject data = new JsonObject();
				data.add("player", PayloadHelpers.createPlayerPayload(event.player()));
				return createPayload(Event.PLAYER_JOIN, new Date(), data);
			}
			case DISCORD: {
				IEmbedBuilder embed = new EmbedBuilder()
						.setTitle("🙌 " + event.player().name() + " さんがサーバーに参加しました")
						.setColor(Color.CYAN.rgb())
						.setThumbnail(new EmbedImageBuilder()
								.setUrl("https://minotar.net/helm/" + event.player().uniqueId().toString() + "/20.png")
								.setHeight(50)
								.setWidth(50))
						.setTimestamp(Instant.now().toString());
				return createDiscordPayload(embed);
			}
			default:
				throw new IllegalArgumentException("Unknown format: " + format);
		}
	}

	public static JsonObject createPayload(ServerSideConnectionEvent.Leave event, YadokariHooksConfig.Format format) {
		switch (format) {
			case GENERIC: {
				JsonObject data = new JsonObject();
				data.add("player", PayloadHelpers.createPlayerPayload(event.player()));
				return createPayload(Event.PLAYER_LEAVE, new Date(), data);
			}
			case DISCORD: {
				IEmbedBuilder embed = new EmbedBuilder()
						.setTitle("👋 " + event.player().name() + " さんがサーバーから退出しました")
						.setColor(Color.CYAN.rgb())
						.setThumbnail(new EmbedImageBuilder()
								.setUrl("https://minotar.net/helm/" + event.player().uniqueId().toString() + "/20.png")
								.setHeight(50)
								.setWidth(50))
						.setTimestamp(Instant.now().toString());
				return createDiscordPayload(embed);
			}
			default:
				throw new IllegalArgumentException("Unknown format: " + format);
		}
	}

	public static JsonObject createPayload(PlayerChatEvent.Submit event, YadokariHooksConfig.Format format) {
		switch (format) {
			case GENERIC: {
				JsonObject data = new JsonObject();
				event.player().ifPresent(player -> data.add("player", PayloadHelpers.createPlayerPayload(player)));
				data.addProperty("message", PlainTextComponentSerializer.plainText().serialize(event.message()));
				return createPayload(Event.PLAYER_CHAT, new Date(), data);
			}
			case DISCORD: {
				IEmbedBuilder embed = new EmbedBuilder()
						.setDescription(PlainTextComponentSerializer.plainText().serialize(event.message()))
						.setTimestamp(Instant.now().toString());
				event.player().ifPresent(player -> embed.setFooter(new EmbedFooterBuilder()
						.setText(player.name())
						.setIconUrl("https://minotar.net/helm/" + player.uniqueId().toString())));
				return createDiscordPayload(embed);
			}
			default:
				throw new IllegalArgumentException("Unknown format: " + format);
		}
	}

	public static JsonObject createPayload(Event event, Date timestamp, JsonElement data) {
		JsonObject payload = new JsonObject();

		payload.addProperty("event", event.getValue());
		payload.addProperty("timestamp", timestamp.toString());
		payload.add("data", data);

		return payload;
	}

	public static JsonObject createDiscordPayload(IEmbedBuilder... embeds) {
		JsonObject payload = new JsonObject();
		Arrays.stream(embeds).forEach(embed -> embed.setTimestamp(Instant.now().toString()));
		payload.add("embeds", Arrays.stream(embeds)
				.map(IEmbedBuilder::build)
				.collect(com.google.gson.JsonArray::new, com.google.gson.JsonArray::add, com.google.gson.JsonArray::addAll));

		return payload;
	}
}
