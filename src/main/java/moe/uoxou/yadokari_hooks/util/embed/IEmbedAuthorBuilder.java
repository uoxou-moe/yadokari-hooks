package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonElement;

public interface IEmbedAuthorBuilder {
	IEmbedAuthorBuilder setName(String name);

	IEmbedAuthorBuilder setUrl(String url);

	IEmbedAuthorBuilder setIconUrl(String iconUrl);

	IEmbedAuthorBuilder setProxyIconUrl(String proxyIconUrl);

	JsonElement build();
}
