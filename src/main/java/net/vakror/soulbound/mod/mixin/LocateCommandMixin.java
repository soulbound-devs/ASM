package net.vakror.soulbound.mod.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.github.L_Ender.cataclysm.init.ModStructures.*;

@Mixin(LocateCommand.class)
public class LocateCommandMixin {
    @Redirect(method = "locateStructure", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;findNearestMapStructure(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/HolderSet;Lnet/minecraft/core/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;"))
    private static Pair<BlockPos, Holder<Structure>> onTryLocate(ChunkGenerator instance, ServerLevel holder, HolderSet<Structure> blockpos, BlockPos d0, int concentricringsstructureplacement, boolean pair) {
        Holder<Structure> structureHolder = blockpos.get(0);
        if (!(structureHolder.get().type().equals(SOUL_BLACK_SMITH.get()) || structureHolder.get().type().equals(RUINED_CITADEL.get()) || structureHolder.get().type().equals(BURNING_ARENA.get()) || structureHolder.get().type().equals(ANCIENT_FACTORY.get()) || structureHolder.get().type().equals(SUNKEN_CITY.get()))) {
            return instance.findNearestMapStructure(holder, blockpos, d0, concentricringsstructureplacement, pair);
        } else {
            return null;
        }
    }
}