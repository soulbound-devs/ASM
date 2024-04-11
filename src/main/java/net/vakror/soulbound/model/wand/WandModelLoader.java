package net.vakror.soulbound.model.wand;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

import java.util.ArrayList;
import java.util.List;

public enum WandModelLoader implements IGeometryLoader<WandModel> {
	INSTANCE;

	public static final List<ResourceLocation> textures = new ArrayList<ResourceLocation>();
	@Override
	public WandModel read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
		ResourceLocation wandLocation = new ResourceLocation("");
		if (modelContents.has("wand")) {
			JsonObject wandJsonObject = modelContents.getAsJsonObject("wand");
			wandLocation = new ResourceLocation(wandJsonObject.get("wand").getAsString());
		}
		return new WandModel(wandLocation);
	}
}