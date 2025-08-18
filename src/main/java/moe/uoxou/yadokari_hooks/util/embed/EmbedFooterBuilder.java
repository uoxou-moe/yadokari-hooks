package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

public class EmbedFooterBuilder implements IEmbedFooterBuilder {
	@Nullable private String text = null;
	@Nullable private String iconUrl = null;
	@Nullable private String proxyIconUrl = null;

	public EmbedFooterBuilder() {}

	@Override
	public IEmbedFooterBuilder setText(String text) {
		this.text = text;
		return this;
	}

	@Override
	public IEmbedFooterBuilder setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
		return this;
	}

	@Override
	public IEmbedFooterBuilder setProxyIconUrl(String proxyIconUrl) {
		this.proxyIconUrl = proxyIconUrl;
		return this;
	}

	@Override
	public JsonElement build() {
		JsonObject obj = new JsonObject();
		if (this.text != null) obj.addProperty("text", this.text);
		if (this.iconUrl != null) obj.addProperty("icon_url", this.iconUrl);
		if (this.proxyIconUrl != null) obj.addProperty("proxy_icon_url", this.proxyIconUrl);
		return obj;
	}
}
