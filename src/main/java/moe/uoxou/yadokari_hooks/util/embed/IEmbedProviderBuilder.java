package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonElement;

public interface IEmbedProviderBuilder {
	IEmbedProviderBuilder setName(String name);

	IEmbedProviderBuilder setUrl(String url);

	JsonElement build();
}
