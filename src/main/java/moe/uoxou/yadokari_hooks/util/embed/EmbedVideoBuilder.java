package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

public class EmbedVideoBuilder implements IEmbedVideoBuilder {
	@Nullable private String url = null;
	private int height = 0;
	private int width = 0;

	public EmbedVideoBuilder() {}

	@Override
	public IEmbedVideoBuilder setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public IEmbedVideoBuilder setHeight(int height) {
		this.height = height;
		return this;
	}

	@Override
	public IEmbedVideoBuilder setWidth(int width) {
		this.width = width;
		return this;
	}

	@Override
	public JsonElement build() {
		JsonObject obj = new JsonObject();
		if (this.url != null) obj.addProperty("url", this.url);
		obj.addProperty("height", this.height);
		obj.addProperty("width", this.width);
		return obj;
	}
}
