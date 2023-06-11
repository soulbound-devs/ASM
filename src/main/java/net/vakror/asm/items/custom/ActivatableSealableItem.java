package net.vakror.asm.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vakror.asm.seal.tier.ISealableTier;
import net.vakror.asm.capability.wand.ItemSealProvider;

public class ActivatableSealableItem extends SealableItem {
    public ActivatableSealableItem(Properties properties, ISealableTier tier) {
        super(properties, tier);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            if (pPlayer.isShiftKeyDown()) {
                switchSeal(pPlayer, pPlayer.getItemInHand(pUsedHand));
            }
            else {
                activateSeal(pPlayer.getItemInHand(pUsedHand));
            };
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    private void activateSeal(ItemStack stack) {
        stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            if (wand.getActiveSeal() != null) {
                wand.setActiveSeal(null, stack);
            } else if (wand.getActiveSeal() == null && wand.isSelectedIsAttack()) {
                int attackSelectedSlot = wand.getSelectedSealSlot() - tier.getPassiveSlots();
                if (attackSelectedSlot >= 0) {
                    if (wand.getAttackSeals().size() > 0 && wand.getAttackSeals().get(attackSelectedSlot) != null) {
                        wand.setActiveSeal(wand.getAttackSeals().get(attackSelectedSlot), stack);
                        System.err.println(wand.getAttackSeals().get(attackSelectedSlot).getId() + "IS ACTIVE!");
                    }
                }
            } else if (wand.getActiveSeal() == null && !wand.isSelectedIsAttack()) {
                if (wand.getPassiveSeals().size() != 0 && wand.getPassiveSeals().get(wand.getSelectedSealSlot() - 1) != null) {
                    wand.setActiveSeal(wand.getPassiveSeals().get(wand.getSelectedSealSlot() - 1), stack);
                }
            }
        });
    }

    private void switchSeal(Player player, ItemStack wand) {
        wand.getCapability(ItemSealProvider.SEAL).ifPresent(itemWand -> {
            itemWand.setSelectedSealSlot(itemWand.getSelectedSealSlot() + 1);
            if (itemWand.getSelectedSealSlot() > tier.getActivatableSlots()) {
                itemWand.setSelectedSealSlot(1);
                itemWand.setSelectedIsAttack(itemWand.getAllActivatableSeals().get(0).isAttack());
            } else if (itemWand.getSelectedSealSlot() > tier.getPassiveSlots() && itemWand.getSelectedSealSlot() != 0) {
                itemWand.setSelectedIsAttack(itemWand.getAllActivatableSeals().get(itemWand.getSelectedSealSlot() - 2).isAttack());
            }
            itemWand.setActiveSeal(null, wand);
            String mode = itemWand.isSelectedIsAttack() ? "Offensive/Defensive" : "Passive";
            int readableSlot = itemWand.isSelectedIsAttack() ? itemWand.getSelectedSealSlot() - tier.getPassiveSlots() : itemWand.getSelectedSealSlot();
            String selectedSealName;
            if (itemWand.getAllActivatableSeals().size() > itemWand.getSelectedSealSlot() - 1) {
                selectedSealName = capitalizeString(itemWand.getAllActivatableSeals().get(itemWand.getSelectedSealSlot() - 1).getId());
                ((ServerPlayer) player).connection.send(new ClientboundSetActionBarTextPacket(Component.literal("Selected " + mode + " Slot: " + readableSlot + " (" + selectedSealName + ")")));
            }
        });
    }
}
