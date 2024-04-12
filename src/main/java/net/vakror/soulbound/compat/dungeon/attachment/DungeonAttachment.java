package net.vakror.soulbound.compat.dungeon.attachment;

import net.minecraft.nbt.*;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.vakror.soulbound.compat.dungeon.Dungeon;
import net.vakror.soulbound.compat.dungeon.DungeonTicker;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistry;
import net.vakror.soulbound.compat.dungeon.setup.DungeonSetup;
import org.jetbrains.annotations.NotNull;

public class DungeonAttachment implements INBTSerializable<CompoundTag> {
        Vec3 returnPos;
        CompoundTag extraData;
        Dungeon dungeon;

        @Override
        public @NotNull CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            if (returnPos != null) {
                nbt.putDouble("returnPosX", returnPos.x);
                nbt.putDouble("returnPosY", returnPos.y);
                nbt.putDouble("returnPosZ", returnPos.y);
            }

            if (extraData != null) {
                nbt.put("extraData", extraData);
            }
            if (dungeon != null && dungeon.serializeNBT() != null) {
                nbt.put("data", dungeon.serializeNBT());
            }
            return nbt;
        }


        @Override
        public void deserializeNBT(CompoundTag nbt) {
            if (nbt.contains("returnPosX")) {
                returnPos = new Vec3(nbt.getDouble("returnPosX"), nbt.getDouble("returnPosY"), nbt.getDouble("returnPosZ"));
            }
            if (nbt.get("extraData") != null) {
                extraData = nbt.getCompound("extraData");
            }

            dungeon = Dungeon.deserialize(nbt.getCompound("data"));
        }

        public Vec3 getReturnPos() {
            return returnPos;
        }

        public void setReturnPos(Vec3 returnPos) {
            this.returnPos = returnPos;
        }

        public CompoundTag getExtraData() {
            return extraData;
        }

        public void setExtraData(CompoundTag extraData) {
            this.extraData = extraData;
        }

        public Dungeon getDungeon() {
            return dungeon;
        }

        public void setDungeon(Dungeon dungeon) {
            this.dungeon = dungeon;
        }

        public DungeonSetup getSetup() {
            return DungeonRegistry.dungeons.get(dungeon.getType()).placer();
        }

        public DungeonTicker getTicker() {
            return DungeonRegistry.dungeons.get(dungeon.getType()).ticker();
        }
    }