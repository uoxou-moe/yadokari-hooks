package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonElement;

public interface IEmbedFooterBuilder {
	IEmbedFooterBuilder setText(String text);

	IEmbedFooterBuilder setIconUrl(String iconUrl);

	IEmbedFooterBuilder setProxyIconUrl(String proxyIconUrl);

	JsonElement build();
}
