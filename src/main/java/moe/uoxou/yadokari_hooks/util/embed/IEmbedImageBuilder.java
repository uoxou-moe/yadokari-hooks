package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonElement;

public interface IEmbedImageBuilder {
	IEmbedImageBuilder setUrl(String url);

	IEmbedImageBuilder setProxyUrl(String proxyUrl);

	IEmbedImageBuilder setHeight(int height);

	IEmbedImageBuilder setWidth(int width);

	JsonElement build();
}
