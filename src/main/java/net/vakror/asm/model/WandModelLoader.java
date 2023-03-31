package net.vakror.asm.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

/* Used in read json of meals */
public enum WandModelLoader implements IGeometryLoader<WandModel> {
	INSTANCE;

	public static final List<ResourceLocation> textures = new ArrayList<ResourceLocation>();


	@Override
	public WandModel read(JsonObject modelContents, JsonDeserializationContext deserializationContext){
		List<TypedTextures> typedTexturesList = new ArrayList<>();
		ResourceLocation wandLocation = new ResourceLocation("");
		
		if(modelContents.has("wand")){
			JsonObject wandJsonObject = modelContents.getAsJsonObject("wand");
			wandLocation = new ResourceLocation(wandJsonObject.get("wand").getAsString());
			WandModelLoader.textures.add(wandLocation);
			
			JsonArray parts = wandJsonObject.get("seals").getAsJsonArray();
			for(JsonElement element : parts){
				TypedTextures typedTextures = new TypedTextures((JsonObject) element);
				typedTexturesList.add(typedTextures);
				
				for(Entry<String, ResourceLocation> entry : typedTextures.getTextures().entrySet()){
					WandModelLoader.textures.add(entry.getValue());
				}
			}
		}
		
		return new WandModel(wandLocation, ImmutableList.copyOf(typedTexturesList));
	}
	
	public static class TypedTextures{
		private final ImmutableMap<String, ResourceLocation> textures;
		
		private TypedTextures(JsonObject elementIn){

			Map<String, ResourceLocation> map = new HashMap<>();
			for(Entry<String, JsonElement> entry : elementIn.entrySet()){
				ResourceLocation location = new ResourceLocation(entry.getValue().getAsString());
				map.put(entry.getKey(), location);
			}
			
			this.textures = ImmutableMap.copyOf(map);
		}

		public ImmutableMap<String, ResourceLocation> getTextures(){
			return textures;
		}

		@Nullable
		public TextureAtlasSprite getSprite(String name, Function<Material, TextureAtlasSprite> spriteGetter){
			ResourceLocation location = this.textures.get(name);
			@SuppressWarnings("deprecation")
			Material material = new Material(TextureAtlas.LOCATION_BLOCKS, location);
            TextureAtlasSprite sprite = material != null ? spriteGetter.apply(material) : null;
            if(sprite != null && !sprite.getName().equals(MissingTextureAtlasSprite.getLocation())){
                return sprite;
            }
			return null;
		}
	}
}