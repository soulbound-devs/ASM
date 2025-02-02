package net.vakror.soulbound.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.vakror.soulbound.attachment.ModAttachments;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class BroomEntity extends Entity {
    private static final EntityDataAccessor<ItemStack> STACK = SynchedEntityData.defineId(BroomEntity.class, EntityDataSerializers.ITEM_STACK);
    public boolean left;
    public boolean right;
    public boolean front;
    public boolean back;

    public BroomEntity(EntityType<BroomEntity> type, Level level) {
        super(ModEntities.BROOM.get(), level);
    }

    public <T extends BroomEntity> BroomEntity(ItemStack stack, Level level) {
        this(ModEntities.BROOM.get(), level);
        this.entityData.set(STACK, stack);
    }
    public ItemStack getItem() {
        return this.entityData.get(STACK);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(STACK, ItemStack.EMPTY);
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    protected Vector3f getPassengerAttachmentPoint(Entity pEntity, EntityDimensions pDimensions, float pScale) {
        return super.getPassengerAttachmentPoint(pEntity, pDimensions, pScale).add(0, 0, 0.2F);
    }

    @Override
    public boolean hurt(DamageSource source, float p_19947_) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.level().isClientSide && !this.isRemoved()) {
            this.discard();
        }
        return true;
    }

    @Override
    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(new Vec3(0D, -0.008D, 0D)));
        }
        if (this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
            this.setRot(player.getYRot(), player.getXRot());
            player.getExistingData(ModAttachments.SOUL_ATTACHMENT).ifPresent((soul ->  {
                if (soul.getSoul() != soul.MIN_SOUL) {
                    soul.addSoul(-1);
                } else {
                    player.setItemInHand(InteractionHand.MAIN_HAND, getItem());
                    this.discard();
                }
            }));
        }
        if (this.isControlledByLocalInstance()) {
            if (this.level().isClientSide()) {
                LivingEntity controller = this.getControllingPassenger();
                assert controller != null;
                this.handleMove(new Vec3(controller.xxa, controller.yya, controller.zza), 0.3f, this.getYRot());
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    public ItemStack getPickResult() {
        return this.entityData.get(STACK);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        else {
            if (!this.level().isClientSide) {
                return player.startRiding(this) ? InteractionResult.CONSUME: InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void checkFallDamage(double p_19911_, boolean p_19912_, BlockState p_19913_, BlockPos p_19914_) {
        this.fallDistance = 0;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.getFirstPassenger() instanceof LivingEntity entity) {
            return entity;
        } else return null;
    }

    private void handleMove(Vec3 movement, float speed, float angle) {
        float horizontalSpeed = speed * 2.5f;
        float verticalSpeed = speed * 11f;
        if (this.isVehicle()) {
            Vec3 inputVec;
            double length = movement.lengthSqr();
            if (length < 1.0E-7D) {
                inputVec = Vec3.ZERO;
            } else {
                Vec3 vec = (length > 1 ? movement.normalize() : movement).scale(horizontalSpeed);
                float f = Mth.sin(angle * ((float) Math.PI/180));
                float alsoF = Mth.cos(angle * ((float) Math.PI/180));
                inputVec = new Vec3(vec.x * (double) alsoF - vec.z * (double) f, (-this.getXRot() / 360) * verticalSpeed, vec.z * (double) alsoF + vec.x * (double) f);
            }

            if (this.hasInput()) {
                this.setDeltaMovement(this.getDeltaMovement().add(inputVec));
            }
        }
    }

    private boolean hasInput() {
        return left || right || front || back;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {

    }

    public void setInput(boolean left, boolean right, boolean front, boolean back) {
        this.left = left;
        this.right = right;
        this.front = front;
        this.back = back;
    }
}
