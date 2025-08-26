package moe.uoxou.yadokari_hooks.send;

import com.google.gson.JsonObject;
import org.spongepowered.api.entity.living.player.Player;

public final class PayloadHelpers {
	public static JsonObject createPlayerPayload(Player player) {
		JsonObject payload = new JsonObject();
		payload.addProperty("uuid", player.uniqueId().toString());
		payload.addProperty("name", player.name());

		return payload;
	}
}
