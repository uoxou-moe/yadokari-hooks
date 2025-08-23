package moe.uoxou.yadokari_hooks;

import com.google.inject.Inject;
import moe.uoxou.yadokari_hooks.config.YadokariHooksConfig;
import moe.uoxou.yadokari_hooks.send.WebhookSender;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.message.PlayerChatEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.nio.file.Path;

@Plugin("yadokari_hooks")
public class YadokariHooks {
	private final PluginContainer container;
	private final Path configPath;
	private final Logger logger;
	private final YadokariHooksConfig config;
	private final WebhookSender webhookSender;

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
		this.webhookSender = new WebhookSender(() -> this.config, () -> this.logger);
	}

	@Listener
	public void onServerStarted(final StartedEngineEvent<Server> event) {
		this.webhookSender.onServerStarted(event);
	}

	@Listener
	public void onServerStopped(final StoppingEngineEvent<Server> event) {
		this.webhookSender.onServerStopping(event);
	}

	@Listener
	public void onPlayerJoin(final ServerSideConnectionEvent.Join event) {
		this.webhookSender.onPlayerJoin(event);
	}

	@Listener
	public void onPlayerLeave(final ServerSideConnectionEvent.Leave event) {
		this.webhookSender.onPlayerLeave(event);
	}

	@Listener
	public void onPlayerChat(PlayerChatEvent.Submit event) {
		this.webhookSender.onPlayerChat(event);
	}

}
