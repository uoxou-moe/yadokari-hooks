package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EmbedBuilder implements IEmbedBuilder {
	@Nullable private String title = null;
	@Nullable private String type = null;
	@Nullable private String description = null;
	@Nullable private String url = null;
	@Nullable private String timestamp = null;
	@Nullable private Integer color = null;
	@Nullable private IEmbedFooterBuilder footer = null;
	@Nullable private IEmbedImageBuilder image = null;
	@Nullable private IEmbedImageBuilder thumbnail = null;
	@Nullable private IEmbedVideoBuilder video = null;
	@Nullable private IEmbedProviderBuilder provider = null;
	@Nullable private IEmbedAuthorBuilder author = null;
	private final List<IEmbedFieldBuilder> fields = new ArrayList<>();

	public EmbedBuilder() {}

	@Override
	public IEmbedBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	@Override
	public IEmbedBuilder setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public IEmbedBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	@Override
	public IEmbedBuilder setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public IEmbedBuilder setTimestamp(String timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	@Override
	public IEmbedBuilder setColor(int color) {
		this.color = color;
		return this;
	}

	@Override
	public IEmbedBuilder setFooter(IEmbedFooterBuilder footer) {
		this.footer = footer;
		return this;
	}

	@Override
	public IEmbedBuilder setImage(IEmbedImageBuilder image) {
		this.image = image;
		return this;
	}

	@Override
	public IEmbedBuilder setThumbnail(IEmbedImageBuilder thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	@Override
	public IEmbedBuilder setVideo(IEmbedVideoBuilder video) {
		this.video = video;
		return this;
	}

	@Override
	public IEmbedBuilder setProvider(IEmbedProviderBuilder provider) {
		this.provider = provider;
		return this;
	}

	@Override
	public IEmbedBuilder setAuthor(IEmbedAuthorBuilder author) {
		this.author = author;
		return this;
	}

	@Override
	public IEmbedBuilder addField(IEmbedFieldBuilder field) {
		this.fields.add(field);
		return this;
	}

	@Override
	public JsonElement build() {
		JsonObject json = new JsonObject();

		if (this.title != null) json.addProperty("title", this.title);
		if (this.type != null) json.addProperty("type", this.type);
		if (this.description != null) json.addProperty("description", this.description);
		if (this.url != null) json.addProperty("url", this.url);
		if (this.timestamp != null) json.addProperty("timestamp", this.timestamp);
		if (this.color != null) json.addProperty("color", this.color);
		if (this.footer != null) json.add("footer", this.footer.build());
		if (this.image != null) json.add("image", this.image.build());
		if (this.thumbnail != null) json.add("thumbnail", this.thumbnail.build());
		if (this.video != null) json.add("video", this.video.build());
		if (this.provider != null) json.add("provider", this.provider.build());
		if (this.author != null) json.add("author", this.author.build());
		JsonArray fieldsArray = new JsonArray();
		this.fields.forEach(field -> fieldsArray.add(field.build()));
		json.add("fields", fieldsArray);

		return json;
	}
}
