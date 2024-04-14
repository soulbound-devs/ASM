package net.vakror.soulbound.model.wand;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.vakror.soulbound.model.ModelUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/* Used in read json of meals */
public enum WandModelLoader implements IGeometryLoader<WandModel> {
	INSTANCE;

	public static final List<ResourceLocation> textures = new ArrayList<ResourceLocation>();

	@Override
	public @NotNull WandModel read(@NotNull JsonObject modelContents, @NotNull JsonDeserializationContext deserializationContext) {
		return new WandModel(modelContents);
	}
}