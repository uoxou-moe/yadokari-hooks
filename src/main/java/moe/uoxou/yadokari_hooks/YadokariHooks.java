package moe.uoxou.yadokari_hooks;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
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
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Plugin("yadokari_hooks")
public class YadokariHooks {
	private final PluginContainer container;
	private final Logger logger;

	@Inject
	public YadokariHooks(final PluginContainer container, final Logger logger) {
		this.container = container;
		this.logger = logger;
	}

	@Listener
	public void onConstructPlugin(final ConstructPluginEvent event) {
		// Perform any one-time setup
		this.logger.info("Constructing yadokari-hooks");
	}

	public static final String WEBHOOK_URL = System.getenv("WEBHOOK_URL");

	@Listener
	public void onServerStarting(final StartingEngineEvent<Server> event) {
		String jsonPayload = """
            {
              "content": "Hello from Java!"
            }
        """;

		// HTTPクライアント
		HttpResponse<String> response;
		try (HttpClient client = HttpClient.newHttpClient()) {

			// リクエスト構築
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(WEBHOOK_URL))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
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
}
