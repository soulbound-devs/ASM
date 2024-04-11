package net.vakror.soulbound.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vakror.soulbound.cap.ModAttachments;
import net.vakror.soulbound.util.SackInventory;
import net.vakror.soulbound.screen.SackMenu;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.seal.seals.amplifying.sack.ColumnUpgradeSeal;
import net.vakror.soulbound.seal.seals.amplifying.sack.RowUpgradeSeal;
import net.vakror.soulbound.seal.seals.amplifying.sack.StackSizeUpgradeSeal;
import net.vakror.soulbound.seal.tier.sealable.ISealableTier;
import net.vakror.soulbound.util.PickupUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class SackItem extends SealableItem {
    private int width = 9;
    private int height = 3;
    private int stackLimit = 64;

    private PickupUtil.PickupMode pickupMode = PickupUtil.PickupMode.NONE;

    public SackItem(Properties properties, ISealableTier tier) {
        super(properties, tier);
    }

    public static SackInventory getInv(ItemStack sack) {
        return new SackInventory(sack, getSackSize(sack), getSackStackSize(sack));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        SackHelper.openSackGui(player, player.getItemInHand(hand));
        if (!level.isClientSide) {
            openScreen((ServerPlayer) player, hand);
        }
        return super.use(level, player, hand);
    }

    public static void openScreen(ServerPlayer user, InteractionHand hand) {
        final var stack = user.getItemInHand(hand);
        // Getting existing UUID or generated new one
        var uuid = getOrBindUUID(stack);

        var width = getSackWidth(stack);
        var height = getSackHeight(stack);
        var stackLimit = getSackStackSize(stack);

        MenuProvider provider = new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return stack.hasCustomHoverName() ? stack.getHoverName(): Component.literal("Sack");
            }

            @Override
            public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                return new SackMenu(syncId, inv, width, height, stackLimit, uuid, stack);
            }
        };

        user.openMenu(provider, (buf) -> {
            buf.writeInt(width);
            buf.writeInt(height);
            buf.writeInt(stackLimit);
            buf.writeUUID(uuid);
        });

    }

    public PickupUtil.PickupMode getPickupMode() {
        return pickupMode;
    }

    public void setPickupMode(PickupUtil.PickupMode pickupMode) {
        this.pickupMode = pickupMode;
    }

    @Override
    @SuppressWarnings("all")
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int p_41407_, boolean p_41408_) {
        super.inventoryTick(pStack, pLevel, pEntity, p_41407_, p_41408_);
    }

    private static int getStackLimitFromSealList(ItemStack stack) {
        return 64;
    }

    private static int getHeightFromSealList(ItemStack stack) {
        return 3;
    }

    private static int getWidthFromSealList(ItemStack stack) {
        return 9;
    }

    public static UUID bindUid(ItemStack stack) {
        var uuid = UUID.randomUUID();
        stack.getOrCreateTag().putUUID("SackUUID", uuid);

        return uuid;
    }

    public static PickupUtil.PickupMode getPickupMode(ItemStack stack) {
        AtomicReference<PickupUtil.PickupMode> mode = new AtomicReference<>(PickupUtil.PickupMode.NONE);
        stack.getExistingData(ModAttachments.SEAL_ATTACHMENT).ifPresent((itemSeal -> {
            boolean hasPickup = itemSeal.getAmplifyingSeals().contains(SealRegistry.amplifyingSeals.get("pickup"));
            if (hasPickup) {
                mode.set(((SackItem) stack.getItem()).pickupMode);
            } else {
                mode.set(PickupUtil.PickupMode.NONE);
            }
        }));
        return mode.get();
    }

    public static UUID getOrBindUUID(ItemStack stack) {
        var foundUid = getUUID(stack);

        if (foundUid == null) {
            return bindUid(stack);
        }

        return foundUid;
    }

    public static UUID getUUID(ItemStack stack) {
        try {
            var uuid = stack.getOrCreateTag().getUUID("SackUUID");

            return uuid;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isUUIDMatch(ItemStack stack, UUID uid) {
        var uuid = getUUID(stack);
        return uuid != null && uuid.equals(uid);
    }

    public static int getSackWidth(ItemStack stack){
        final int[] width = new int[]{((SackItem) stack.getItem()).width};
        stack.getExistingData(ModAttachments.SEAL_ATTACHMENT).ifPresent((itemSeal -> {
            itemSeal.getAmplifyingSeals().forEach((seal -> {
                if (seal instanceof ColumnUpgradeSeal upgradeSeal) {
                    if (Objects.requireNonNull(upgradeSeal.actionType) == AttributeModifier.Operation.ADDITION) {
                        width[0] += upgradeSeal.amount;
                    } else {
                        width[0] *= upgradeSeal.amount;
                    }
                }
            }));
        }));
        return width[0];
    }

    public static int getSackHeight(ItemStack stack){
        final int[] height = new int[]{((SackItem) stack.getItem()).height};
        stack.getExistingData(ModAttachments.SEAL_ATTACHMENT).ifPresent((itemSeal -> {
            itemSeal.getAmplifyingSeals().forEach((seal -> {
                if (seal instanceof RowUpgradeSeal upgradeSeal) {
                    if (Objects.requireNonNull(upgradeSeal.actionType) == AttributeModifier.Operation.ADDITION) {
                        height[0] += upgradeSeal.amount;
                    } else {
                        height[0] *= upgradeSeal.amount;
                    }
                }
            }));
        }));
        return height[0];
    }

    public static int getSackStackSize(ItemStack stack){
        final int[] stackSize = new int[]{((SackItem) stack.getItem()).stackLimit};
        stack.getExistingData(ModAttachments.SEAL_ATTACHMENT).ifPresent((itemSeal -> {
            itemSeal.getAmplifyingSeals().forEach((seal -> {
                if (seal instanceof StackSizeUpgradeSeal upgradeSeal) {
                    if (Objects.requireNonNull(upgradeSeal.actionType) == AttributeModifier.Operation.ADDITION) {
                        stackSize[0] += upgradeSeal.amount;
                    } else {
                        stackSize[0] *= upgradeSeal.amount;
                    }
                }
            }));
        }));
        return stackSize[0];
    }

    public static int getSackSize(ItemStack stack){
        return getSackHeight(stack) * getSackWidth(stack);
    }

    public boolean canPickup(ItemStack stack) {
        return hasSeal("pickup", stack);
    }
}