package net.vakror.soulbound.compat.dungeon.theme;

import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

import java.util.ArrayList;
import java.util.List;

public class RuleDungeonTheme implements DungeonTheme {
    List<ProcessorRule> ruleProcessors;

    private RuleDungeonTheme(List<ProcessorRule> ruleProcessors) {
        this.ruleProcessors = ruleProcessors;
    }

    @Override
    public StructureProcessor getProcessor() {
        return new RuleProcessor(ruleProcessors);
    }

    public static class Builder {
        List<ProcessorRule> ruleProcessors = new ArrayList<>();

        public Builder() {}

        public Builder addRule(ProcessorRule ruleProcessor) {
            ruleProcessors.add(ruleProcessor);
            return this;
        }

        public RuleDungeonTheme build() {
            return new RuleDungeonTheme(ruleProcessors);
        }
    }
}
