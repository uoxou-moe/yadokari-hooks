package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonElement;

public interface IEmbedFieldBuilder {
	IEmbedFieldBuilder setName(String name);

	IEmbedFieldBuilder setValue(String value);

	IEmbedFieldBuilder setInline(boolean inline);

	JsonElement build();
}
