package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

public class EmbedImageBuilder implements IEmbedImageBuilder {
	@Nullable private String url = null;
	@Nullable private String proxyUrl = null;
	private int height = 0;
	private int width = 0;

	public EmbedImageBuilder() {}

	@Override
	public IEmbedImageBuilder setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public IEmbedImageBuilder setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
		return this;
	}

	@Override
	public IEmbedImageBuilder setHeight(int height) {
		this.height = height;
		return this;
	}

	@Override
	public IEmbedImageBuilder setWidth(int width) {
		this.width = width;
		return this;
	}

	@Override
	public JsonElement build() {
		JsonObject obj = new JsonObject();
		if (this.url != null) obj.addProperty("url", this.url);
		if (this.proxyUrl != null) obj.addProperty("proxy_url", this.proxyUrl);
		obj.addProperty("height", this.height);
		obj.addProperty("width", this.width);
		return obj;
	}
}
