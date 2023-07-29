package net.vakror.soulbound.mixin;

import net.minecraft.world.level.chunk.storage.IOWorker;
import net.minecraft.world.level.chunk.storage.RegionFileStorage;
import net.vakror.soulbound.compat.hammerspace.dimension.DungeonRegionFileStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.function.Consumer;

@Mixin(IOWorker.class)
public abstract class IOWorkerMixin implements Consumer<RegionFileStorage> {

    @Mutable
    @Accessor
    public abstract void setStorage(RegionFileStorage cache);

    @Override
    public void accept(RegionFileStorage cache) {
        this.setStorage(cache);
    }

    @Inject(method="<init>", at=@At("RETURN"))
    private void onConstruction(Path path, boolean sync, String threadName, CallbackInfo info) {
        String s = path.toString();
        if (s.contains("dimensions/soulbound") || s.contains("dimensions\\soulbound")) {
            accept(new DungeonRegionFileStorage(path, sync));
        }
    }
}
