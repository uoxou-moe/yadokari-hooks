package moe.uoxou.yadokari_hooks.config;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class YadokariHooksConfig {
	private final HoconConfigurationLoader loader;
	private final Logger logger;

	@Nullable private CommentedConfigurationNode rootNode;

	public YadokariHooksConfig(Path path, Logger logger) {
		this.loader = HoconConfigurationLoader.builder()
				.path(path)
				.build();
		this.logger = logger;

		this.load();
	}

	public List<Hook> getHooks() {
		return Optional.ofNullable(this.rootNode)
				.map(n -> {
					try {
						return n.get(Config.class);
					} catch (SerializationException e) {
						throw new RuntimeException(e);
					}
				})
				.map(Config::hooks)
				.orElse(List.of());
	}

	public void load() {
		try {
			this.rootNode = this.loader.load();
		} catch (ConfigurateException e) {
			this.rootNode = null;
			this.logger.error("Failed to load configuration file: {}", e.getMessage(), e);
		}
	}

	@ConfigSerializable
	private static class Config {
		@Setting("hooks") private List<Hook> hooks = List.of();

		public List<Hook> hooks() {
			return hooks;
		}
	}

	@ConfigSerializable
	public static class Hook {
		@Setting("url") @Required private URL url;
		@Setting("on-server-start") private OnServerStart onServerStart = new OnServerStart();

		public URL url() {
			return url;
		}

		public OnServerStart onServerStart() {
			return onServerStart;
		}
	}

	@ConfigSerializable
	public static class OnServerStart {
		@Setting("enabled") private boolean enabled = true;

		public boolean enabled() {
			return enabled;
		}
	}
}
