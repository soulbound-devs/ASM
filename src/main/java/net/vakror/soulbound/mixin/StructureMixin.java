package net.vakror.soulbound.mixin;

import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

import static com.github.L_Ender.cataclysm.init.ModStructures.*;

@Mixin(Structure.class)
public abstract class StructureMixin {
    @Shadow public abstract StructureType<?> type();

    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"))
    public boolean isPresent(Optional<Structure.GenerationStub> instance) {
        if (type().equals(SOUL_BLACK_SMITH.get()) || type().equals(RUINED_CITADEL.get()) || type().equals(BURNING_ARENA.get()) || type().equals(ANCIENT_FACTORY.get()) || type().equals(SUNKEN_CITY.get())) {
            return false;
        }
        return instance.isPresent();
    }
}
