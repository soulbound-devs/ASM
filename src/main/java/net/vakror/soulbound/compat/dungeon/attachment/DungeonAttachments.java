package net.vakror.soulbound.compat.dungeon.attachment;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vakror.soulbound.attachment.ModAttachments;

public class DungeonAttachments {
    public static DeferredHolder<AttachmentType<?>, AttachmentType<DungeonAttachment>> DUNGEON_ATTACHMENT;

    public static void register() {
        DUNGEON_ATTACHMENT = ModAttachments.ATTACHMENT_TYPES.register("dungeon", () -> AttachmentType.serializable(DungeonAttachment::new).build());
    }
}
