package net.vakror.asm.seal.seals.amplifying.wand.haste;

import net.vakror.asm.seal.ISeal;
import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.SealRegistry;
import net.vakror.asm.seal.tier.seal.IntegerTiered;
import net.vakror.asm.seal.type.amplifying.AmplifyingSeal;

import java.util.List;

public abstract class HasteSeal extends AmplifyingSeal implements IntegerTiered {
    public HasteSeal(int tier) {
        super("mining_speed_tier_" + tier);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("haste"));
        properties.add(new SealProperty("tier_one"));
        properties.add(new SealProperty("amplifying"));
        return super.properties();
    }

    @Override
    public String getTierId() {
        return "haste";
    }

    public static class HasteSealTierOne extends HasteSeal{

        public HasteSealTierOne() {
            super(1);
        }

        @Override
        public int getAmount() {
            return 8;
        }

        @Override
        public ISeal getNextSeal() {
            return SealRegistry.amplifyingSeals.get("mining_speed_tier_two");
        }

        @Override
        public int getTier() {
            return 1;
        }
    }

    public static class HasteSealTierTwo extends HasteSeal{

        public HasteSealTierTwo() {
            super(2);
        }

        @Override
        public int getAmount() {
            return 16;
        }

        @Override
        public ISeal getNextSeal() {
            return SealRegistry.amplifyingSeals.get("mining_speed_tier_three");
        }

        @Override
        public int getTier() {
            return 2;
        }
    }

    public static class HasteSealTierThree extends HasteSeal{

        public HasteSealTierThree() {
            super(3);
        }

        @Override
        public int getAmount() {
            return 24;
        }

        @Override
        public ISeal getNextSeal() {
            return null;
        }

        @Override
        public int getTier() {
            return 3;
        }
    }
}
