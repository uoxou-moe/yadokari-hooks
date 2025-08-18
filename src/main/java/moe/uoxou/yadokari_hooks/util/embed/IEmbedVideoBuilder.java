package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonElement;

public interface IEmbedVideoBuilder {
	IEmbedVideoBuilder setUrl(String url);

	IEmbedVideoBuilder setHeight(int height);

	IEmbedVideoBuilder setWidth(int width);

	JsonElement build();
}
