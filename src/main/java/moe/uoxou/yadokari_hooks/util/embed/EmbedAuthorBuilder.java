package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

public class EmbedAuthorBuilder implements IEmbedAuthorBuilder {
	@Nullable private String name = null;
	@Nullable private String url = null;
	@Nullable private String iconUrl = null;
	@Nullable private String proxyIconUrl = null;

	public EmbedAuthorBuilder() {}

	@Override
	public IEmbedAuthorBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public IEmbedAuthorBuilder setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public IEmbedAuthorBuilder setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
		return this;
	}

	@Override
	public IEmbedAuthorBuilder setProxyIconUrl(String proxyIconUrl) {
		this.proxyIconUrl = proxyIconUrl;
		return this;
	}

	@Override
	public JsonElement build() {
		JsonObject obj = new JsonObject();
		if (this.name != null) obj.addProperty("name", this.name);
		if (this.url != null) obj.addProperty("url", this.url);
		if (this.iconUrl != null) obj.addProperty("icon_url", this.iconUrl);
		if (this.proxyIconUrl != null) obj.addProperty("proxy_icon_url", this.proxyIconUrl);
		return obj;
	}
}
