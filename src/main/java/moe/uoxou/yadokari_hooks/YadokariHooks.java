package moe.uoxou.yadokari_hooks;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import moe.uoxou.yadokari_hooks.config.YadokariHooksConfig;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.LinearComponents;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.message.PlayerChatEvent;
import org.spongepowered.api.event.message.SystemMessageEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

@Plugin("yadokari_hooks")
public class YadokariHooks {
	private final PluginContainer container;
	private final Path configPath;
	private final Logger logger;
	private final YadokariHooksConfig config;

	@Inject
	public YadokariHooks(
			final PluginContainer container,
			@DefaultConfig(sharedRoot = true) final Path configPath,
			final Logger logger
	) {
		this.container = container;
		this.configPath = configPath;
		this.logger = logger;
		this.config = new YadokariHooksConfig(configPath, logger);
	}

	@Listener
	public void onServerStarting(final StartingEngineEvent<Server> event) {
		this.config.getHooks().stream().filter(c -> c.onServerStart().enabled()).forEach(hook -> {
			JsonObject payload = new JsonObject();
			payload.addProperty("content", "サーバーが起動しました");

			try {
				sendWebhookRequest(hook.url(), payload);
			} catch (Exception e) {
				this.logger.error("Failed to send webhook request: {}", e.getMessage(), e);
			}
		});
	}

	@Listener
	public void onServerStopping(final StoppingEngineEvent<Server> event) {
		// Any tear down per-game instance. This can run multiple times when
		// using the integrated (singleplayer) server.
	}

	@Listener
	public void onServerSideConnectionLogin(ServerSideConnectionEvent.Login event) {
		JsonObject payload = new JsonObject();
		payload.addProperty("content", "おしりからこんにちは: " + event.user().name());

		// HTTPクライアント
		HttpResponse<String> response;
		try (HttpClient client = HttpClient.newHttpClient()) {

			// リクエスト構築
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(WEBHOOK_URL))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
					.build();

			// リクエスト送信
			try {
				response = client.send(request, HttpResponse.BodyHandlers.ofString());
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		// レスポンス確認
		this.logger.info("Webhook response status code: {}", response.statusCode());
		this.logger.info("Webhook response body: {}", response.body());
	}

	@Listener
	public void onPlayerChat(PlayerChatEvent.Submit event) {
//		JsonObject payload = new JsonObject();
//		JsonElement embed = new EmbedBuilder()
//				.setDescription(PlainTextComponentSerializer.plainText().serialize(event.message()))
//				.setColor(0x00FF00) // 緑色
//				.setFooter(new EmbedFooterBuilder()
//						.setText(event.player().map(Nameable::name).orElse(null))
//						.setIconUrl(event.player().map(p -> "https://minotar.net/helm/" + p.uniqueId().toString().replace("-", "")).orElse(""))
//				)
//				.build();
//		JsonArray embeds = new JsonArray();
//		embeds.add(embed);
//		payload.add("embeds", embeds);
//
//		sendWebhookRequest(payload);
	}

	@Listener
	public void onSystemMessage(SystemMessageEvent event) {
//		JsonObject payload = new JsonObject();
//		JsonElement embed = new EmbedBuilder()
//				.setDescription(PlainTextComponentSerializer.plainText().serialize(event.message()))
//				.setColor(0x0000FF) // 青色
//				.build();
//		JsonArray embeds = new JsonArray();
//		embeds.add(embed);
//		payload.add("embeds", embeds);
//
//		sendWebhookRequest(payload);
	}


	@Listener
	public void onRegisterCommands(final RegisterCommandEvent<Command.Parameterized> event) {
		// Register a simple command
		// When possible, all commands should be registered within a command register event
		final Parameter.Value<String> nameParam = Parameter.string().key("name").build();
		event.register(this.container, Command.builder().addParameter(nameParam).permission("yadokari-hooks.command.greet").executor(ctx -> {
			final String name = ctx.requireOne(nameParam);
			ctx.sendMessage(Identity.nil(), LinearComponents.linear(NamedTextColor.AQUA, Component.text("Hello "), Component.text(name, Style.style(TextDecoration.BOLD)), Component.text("!")));

			return CommandResult.success();
		}).build(), "greet", "wave");
	}

	public static final String WEBHOOK_URL = System.getenv("WEBHOOK_URL");

	@SuppressWarnings("UnusedReturnValue")
	private static HttpResponse<String> sendWebhookRequest(URL url, JsonObject payload) {
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
}
