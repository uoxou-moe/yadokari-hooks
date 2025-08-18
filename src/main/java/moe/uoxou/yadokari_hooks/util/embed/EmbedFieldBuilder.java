package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

public class EmbedFieldBuilder implements IEmbedFieldBuilder {
	@Nullable private String name = null;
	@Nullable private String value = null;
	private boolean inline = false;

	public EmbedFieldBuilder() {}

	@Override
	public IEmbedFieldBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public IEmbedFieldBuilder setValue(String value) {
		this.value = value;
		return this;
	}

	@Override
	public IEmbedFieldBuilder setInline(boolean inline) {
		this.inline = inline;
		return this;
	}

	@Override
	public JsonElement build() {
		JsonObject obj = new JsonObject();
		if (this.name != null) obj.addProperty("name", this.name);
		if (this.value != null) obj.addProperty("value", this.value);
		obj.addProperty("inline", this.inline);
		return obj;
	}
}
