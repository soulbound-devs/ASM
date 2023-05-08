package net.vakror.asm.data.seal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.loading.FMLPaths;
import net.vakror.asm.data.seal.function.use.UseFunction;
import net.vakror.asm.seal.SealType;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SealJsonReader {

    public static List<DataSeal> readSeals() {
        List<DataSeal> seals = new ArrayList<>();
        File asmSealConfigDir = FMLPaths.CONFIGDIR.get().resolve("asm/seals").toFile();;
        if (!asmSealConfigDir.exists() && asmSealConfigDir.mkdirs()) {
            List<File> sealFiles = List.of(Objects.requireNonNull(asmSealConfigDir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"))));
            for (File sealFile : sealFiles) {
                try {
                    Reader reader = new InputStreamReader(new FileInputStream(sealFile), StandardCharsets.UTF_8);
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    reader.close();
                    seals.add(parseSeal(json));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return seals;
    }

    private static DataSeal parseSeal(JsonObject seal) throws IOException {
        String id = seal.get("id").getAsString();
        SealType type = SealType.valueOf(seal.get("type").getAsString().toUpperCase());
        ResourceLocation itemTexture = getResourceLocation(seal.get("texture").getAsString());
        if (type != SealType.AMPLIFYING) {
            ResourceLocation activeTexture = getResourceLocation(seal.get("activeTexture").getAsString());
            float damage = 0.0f;
            float aoeDamage = 0.0f;
            int aoeRange = 0;
            int maxAoeMobs = 0;
            int miningSpeed = 0;
            TagKey<Block> mineableBlocks = null;
            Tiers miningLevel = Tiers.DIAMOND;
            int veinMine = 0;
            int areaMine = 0;
            AreaMineShape areaMineShape = AreaMineShape.NONE;
            boolean silkTouch = false;
            int fortune = 0;
            List<UseFunction> useFunctions;

            if (type == SealType.OFFENSIVE && seal.get("attackDamage") != null) {
                damage = seal.get("attackDamage").getAsFloat();
                if (seal.get("aoe").isJsonObject()) {
                    JsonObject aoeObject = seal.getAsJsonObject("aoe");
                    aoeDamage = aoeObject.get("aoeDamageMultiplier").getAsFloat();
                    aoeRange = aoeObject.get("range").getAsInt();
                    maxAoeMobs = aoeObject.get("maxMobCount").getAsInt();
                }
            } if (seal.get("mining") instanceof JsonObject miningObject) {
                miningSpeed = miningObject.get("miningSpeed").getAsInt();
                mineableBlocks = TagKey.create(Registries.BLOCK, getResourceLocation(miningObject.get("miningLevel").getAsString()));
                miningSpeed = miningObject.get("miningSpeed").getAsInt();
                JsonElement veinMineElement = miningObject.get("veinMine");
                veinMine = (veinMineElement == null? 0: veinMineElement.getAsInt());
                JsonElement areaMineElement = miningObject.get("areaMine");
                areaMine = (areaMineElement == null? 0: areaMineElement.getAsInt());
                if (areaMine > 0) {
                    areaMineShape = AreaMineShape.valueOf(miningObject.get("shape").getAsString().toUpperCase());
                }
                JsonElement fortuneElement = miningObject.get("fortune");
                fortune = (fortuneElement == null? 0: fortuneElement.getAsInt());
                silkTouch = miningObject.get("silkTouch").getAsBoolean();
                if (silkTouch && fortune > 0) {
                    throw new IllegalStateException("ASM ERROR: Fortune and Silk touch cannot both be on a Seal!");
                }
            }
        }
        return new DataSeal();
    }

    private static ResourceLocation getResourceLocation(String location) {
        String[] splitString = location.split(":");
        return new ResourceLocation(splitString[0], splitString[1]);
    }
}
