package net.vakror.asm.data.seal.function.use.custom;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.asm.data.seal.function.use.UseFunction;

import java.nio.file.Path;

public class ChangeBlockUseFunction extends UseFunction {
    public TagKey<Block> blockTag;
    public Block block;

    public ChangeBlockUseFunction(JsonObject functionObject, Path path) {
        super(functionObject, path);
    }

    @Override
    public void readFromJson(JsonObject function, Path path) {
        super.readFromJson(function, path);
        try {
            JsonObject action = function.getAsJsonObject("updateBlock");
            String block = action.get("block").getAsString();
            if (block.startsWith("#")) {
                String tagLocation = block.replace("#", "");
                blockTag = TagKey.create(Registries.BLOCK, new ResourceLocation(tagLocation.split(":")[0], tagLocation.split(":")[1]));
            } else {
                this.block = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY).lookup(Registries.BLOCK).get().getOrThrow(ResourceKey.create(Registries.BLOCK, new ResourceLocation(block.split(":")[0], block.split(":")[1]))).get();
            }
        } catch (IllegalStateException | ClassCastException e) {
            if (e instanceof IllegalStateException && e.getMessage().startsWith("Missing element")) {
                throw new IllegalStateException("Block: " + block + ", mentioned in file: " + path.toString() + "Is not found in registry");
            } else if (e instanceof ClassCastException) {
                throw new IllegalStateException("updateBlock Action of " + this.id + " in file: " + path.toString() + " IS NOT A JSON OBJECT!");
            }
        }
    }

    @Override
    public void executeUse(UseOnContext context) {
        super.executeUse(context);
        BlockState clickedBlockState = context.getLevel().getBlockState(context.getClickedPos());
        if ((block != null && clickedBlockState.getBlock().equals(block)) || (blockTag != null && clickedBlockState.is(blockTag))) {
            context.getLevel().setBlock(context.getClickedPos(), block.defaultBlockState(), 3);
        }
    }
}