package net.vakror.asm.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.vakror.asm.capability.wand.ItemSealProvider;
import net.vakror.asm.seal.seals.amplifying.sack.ColumnUpgradeSeal;
import net.vakror.asm.seal.seals.amplifying.sack.RowUpgradeSeal;
import net.vakror.asm.seal.seals.amplifying.sack.StackSizeUpgradeSeal;
import net.vakror.asm.seal.tier.sealable.ISealableTier;
import net.vakror.asm.screen.SackMenu;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SackItem extends SealableItem {
    private int width = 9;
    private int height = 3;
    private int stackLimit = 64;

    public SackItem(Properties properties, ISealableTier tier) {
        super(properties, tier);
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

        NetworkHooks.openScreen(user, provider, (buf) -> {
            buf.writeInt(width);
            buf.writeInt(height);
            buf.writeInt(stackLimit);
            buf.writeUUID(uuid);
        });

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
        stack.getCapability(ItemSealProvider.SEAL).ifPresent((itemSeal -> {
            itemSeal.getAmplifyingSeals().forEach((seal -> {
                if (seal instanceof ColumnUpgradeSeal upgradeSeal) {
                    switch (upgradeSeal.actionType) {
                        case ADD -> width[0] += upgradeSeal.amount;
                        case SUBTRACT -> width[0] -= upgradeSeal.amount;
                        case MULTIPLY -> width[0] *= upgradeSeal.amount;
                        case DIVIDE -> width[0] /= upgradeSeal.amount;
                        case POW -> width[0] = (int) Math.pow(width[0], upgradeSeal.amount);
                    }
                }
            }));
        }));
        return width[0];
    }

    public static int getSackHeight(ItemStack stack){
        final int[] height = new int[]{((SackItem) stack.getItem()).height};
        stack.getCapability(ItemSealProvider.SEAL).ifPresent((itemSeal -> {
            itemSeal.getAmplifyingSeals().forEach((seal -> {
                if (seal instanceof RowUpgradeSeal upgradeSeal) {
                    switch (upgradeSeal.actionType) {
                        case ADD -> height[0] += upgradeSeal.amount;
                        case SUBTRACT -> height[0] -= upgradeSeal.amount;
                        case MULTIPLY -> height[0] *= upgradeSeal.amount;
                        case DIVIDE -> height[0] /= upgradeSeal.amount;
                        case POW -> height[0] = (int) Math.pow(height[0], upgradeSeal.amount);
                    }
                }
            }));
        }));
        return height[0];
    }

    public static int getSackStackSize(ItemStack stack){
        final int[] stackSize = new int[]{((SackItem) stack.getItem()).stackLimit};
        stack.getCapability(ItemSealProvider.SEAL).ifPresent((itemSeal -> {
            itemSeal.getAmplifyingSeals().forEach((seal -> {
                if (seal instanceof StackSizeUpgradeSeal upgradeSeal) {
                    switch (upgradeSeal.actionType) {
                        case ADD -> stackSize[0] += upgradeSeal.amount;
                        case SUBTRACT -> stackSize[0] -= upgradeSeal.amount;
                        case MULTIPLY -> stackSize[0] *= upgradeSeal.amount;
                        case DIVIDE -> stackSize[0] /= upgradeSeal.amount;
                        case POW -> stackSize[0] = (int) Math.pow(stackSize[0], upgradeSeal.amount);
                    }
                }
            }));
        }));
        return stackSize[0];
    }

    public static int getBPInvSize(ItemStack stack){
        return getSackHeight(stack) * getSackWidth(stack);
    }
}