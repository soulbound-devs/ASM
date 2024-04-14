package net.vakror.soulbound.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.vakror.soulbound.entity.BroomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow public Input input;

    @Shadow public abstract void tick();

    @Shadow private boolean handsBusy;

    public LocalPlayerMixin(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }

    @Inject(at = @At("TAIL"), method = "rideTick")
    private void rideTick(CallbackInfo ci) {
        if (this.getVehicle() instanceof BroomEntity broom) {
            broom.setInput(this.input.left, this.input.right, this.input.up, this.input.down);
            this.handsBusy |= this.input.left || this.input.right || this.input.up || this.input.down;
        }
    }
}
