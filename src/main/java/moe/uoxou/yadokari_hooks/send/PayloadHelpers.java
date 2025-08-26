package moe.uoxou.yadokari_hooks.send;

import com.google.gson.JsonObject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.plugin.PluginContainer;

public final class PayloadHelpers {
	public static JsonObject createPlayerPayload(Player player) {
		JsonObject payload = new JsonObject();
		payload.addProperty("uuid", player.uniqueId().toString());
		payload.addProperty("name", player.name());

		return payload;
	}

	public static JsonObject createPluginPayload(PluginContainer plugin) {
		JsonObject payload = new JsonObject();
		payload.addProperty("id", plugin.metadata().id());
		plugin.metadata().name().ifPresent(name -> payload.addProperty("name", name));
		payload.addProperty("version", plugin.metadata().version().toString());

		return payload;
	}
}
