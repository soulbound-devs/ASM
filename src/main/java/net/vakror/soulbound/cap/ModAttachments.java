package net.vakror.soulbound.cap;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.vakror.soulbound.SoulboundMod;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, SoulboundMod.MOD_ID);

    public static final Supplier<AttachmentType<WandSealAttachment>> SEAL_ATTACHMENT
            = ATTACHMENT_TYPES.register("seals", () -> AttachmentType.serializable(WandSealAttachment::new).comparator(WandSealAttachment::areCompatible).build());

    public static final Supplier<AttachmentType<PlayerSoulAttachment>> SOUL_ATTACHMENT
            = ATTACHMENT_TYPES.register("soul", () -> AttachmentType.serializable(PlayerSoulAttachment::new).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
