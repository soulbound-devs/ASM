package net.vakror.soulbound.api.context;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.mod.model.models.ActiveSealModels;
import net.vakror.soulbound.mod.model.models.WandModels;

public class ModelRegistrationContext implements IRegistrationContext {
    /**
     * Registers a CUSTOM model for a wand. Base models are registered in your model JSON
     *
     * @param name the name of your wand model
     * @param location the location for the OBJ file for your wand, must include the .obj file extension. Example: "soulbound:models/obj/wand/ancient_oak/base.obj"
     */
    public void registerWandModel(String name, ResourceLocation location) {
        WandModels.registerModel(name, location);
    }

    /**
     * Registers a model for an active seal (spell)
     *
     * @param name the name of your active seal model (if for a seal and not custom model, must match seal registry name <b>EXACTLY</b>
     * @param location the location for the OBJ file for your seal, must include the .obj file extension and have another file named the same, with "_outline" at the end in the same directory. Example: "soulbound:models/obj/seal/pickaxing/base.obj"
     */
    public void registerSpellModel(String name, ResourceLocation location) {
        ActiveSealModels.registerModel(name, location);
    }

    /**
     * @return the name of all default soulbound contexts will always be "default"
     */
    @Override
    public String getName() {
        return "default";
    }
}
