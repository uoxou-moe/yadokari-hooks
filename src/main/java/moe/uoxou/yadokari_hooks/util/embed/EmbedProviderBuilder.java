package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

public class EmbedProviderBuilder implements IEmbedProviderBuilder {
	@Nullable private String name = null;
	@Nullable private String url = null;

	public EmbedProviderBuilder() {}

	@Override
	public IEmbedProviderBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public IEmbedProviderBuilder setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public JsonElement build() {
		JsonObject obj = new JsonObject();
		if (this.name != null) obj.addProperty("name", this.name);
		if (this.url != null) obj.addProperty("url", this.url);
		return obj;
	}
}
