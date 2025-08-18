package moe.uoxou.yadokari_hooks.util.embed;

import com.google.gson.JsonElement;

public interface IEmbedBuilder {
	IEmbedBuilder setTitle(String title);

	IEmbedBuilder setType(String type);

	IEmbedBuilder setDescription(String description);

	IEmbedBuilder setUrl(String url);

	IEmbedBuilder setTimestamp(String timestamp);

	IEmbedBuilder setColor(int color);

	IEmbedBuilder setFooter(IEmbedFooterBuilder footer);

	IEmbedBuilder setImage(IEmbedImageBuilder image);

	IEmbedBuilder setThumbnail(IEmbedImageBuilder thumbnail);

	IEmbedBuilder setVideo(IEmbedVideoBuilder video);

	IEmbedBuilder setProvider(IEmbedProviderBuilder provider);

	IEmbedBuilder setAuthor(IEmbedAuthorBuilder author);

	IEmbedBuilder addField(IEmbedFieldBuilder field);

	JsonElement build();
}
