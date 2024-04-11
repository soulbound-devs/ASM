package net.vakror.soulbound.seal.function.use.custom;

import com.google.gson.JsonObject;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.soulbound.seal.function.use.UseFunction;

import java.nio.file.Path;

public class ChangeBlockUseFunction extends UseFunction {
    public TagKey<Block> blockTag;
    public Block block;

    public ChangeBlockUseFunction(JsonObject functionObject, Path path) {
        super(functionObject, path);
    }

    @Override
    public void readFromJson(JsonObject function, Path path) {
    }

    @Override
    public void executeUseOn(UseOnContext context) {
        super.executeUseOn(context);
        BlockState clickedBlockState = context.getLevel().getBlockState(context.getClickedPos());
        if ((block != null && clickedBlockState.getBlock().equals(block)) || (blockTag != null && clickedBlockState.is(blockTag))) {
            context.getLevel().setBlock(context.getClickedPos(), block.defaultBlockState(), 3);
        }
    }
}