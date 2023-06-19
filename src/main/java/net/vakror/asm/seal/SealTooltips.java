package net.vakror.asm.seal;

import static net.vakror.asm.seal.Tooltip.*;
import static net.vakror.asm.seal.Tooltip.TooltipComponentBuilder.ColorCode.*;

public class SealTooltips {
    public static Tooltip AXING = new TooltipBuilder()
            .addPart(TooltipComponent.offensive())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(1)
                    .addPartWithNewline("Max Tier: 4")
                    .addPartWithNewline("When Activated: ")
                    .addPartWithNewline("   • Strips Wood", GREEN)
                    .addPartWithNewline("   • Mines Wood Blocks Faster", GREEN)
                    .addPartWithNewline("       + Mines With Diamond Tier", GREEN)
                    .addPartWithNewline("   • Damage + 9", GREEN)
                    .addPartWithNewline("   • Swing Speed -4", RED)
                    .addPartWithNewline("")
                    .addPartWithNewline("Compatible With:")
                    .addPartWithNewline("   • Damage Seal | Tier 1", GOLD)
                    .addPartWithNewline("   • Haste Seal | Tiers 1&2", GOLD).build())
            .build();

    public static Tooltip SWORDING = new TooltipBuilder()
            .addPart(TooltipComponent.offensive())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(1)
                    .addPartWithNewline("Max Tier: 4")
                    .addPartWithNewline("When Activated: ")
                    .addPartWithNewline("   • Damage + 7", GREEN)
                    .addPartWithNewline("   • Swing Speed -3", RED)
                    .addPartWithNewline("")
                    .addPartWithNewline("Compatible With:")
                    .addPartWithNewline("   • Damage Seal | Tier 1", GOLD)
                    .addPartWithNewline("   • Looting Seal | Tier 1", GOLD).build())
            .build();
    public static Tooltip PICKING = new TooltipBuilder()
            .addPart(TooltipComponent.passive())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(1)
                    .addPartWithNewline("Max Tier: 3")
                    .addPartWithNewline("When Activated: ")
                    .addPartWithNewline("   • Mines Stone Blocks Faster", GREEN)
                    .addPartWithNewline("       + Mines With Diamond Tier", GREEN)
                    .addPartWithNewline("   • Damage +4.5", GREEN)
                    .addPartWithNewline("   • Swing Speed -2", RED)
                    .addPartWithNewline("")
                    .addPartWithNewline("Compatible With:")
                    .addPartWithNewline("   • Haste Seal | Tiers 1&2", GOLD)
                    .addPartWithNewline("   • Smelting Seal", GOLD)
                    .addPartWithNewline("   • Vein Mine Seal | All Tiers", GOLD)
                    .addPartWithNewline("   • Fortune Seal | Tiers 1-3", GOLD)
                    .addPartWithNewline("   • Silk Touch Seal", GOLD)
                    .build())
            .build();
    public static Tooltip HOEING = new TooltipBuilder()
            .addPart(TooltipComponent.passive())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(1)
                    .addPartWithNewline("Max Tier: 3")
                    .addPartWithNewline("When Activated: ")
                    .addPartWithNewline("   • Tills Dirt", GREEN)
                    .addPartWithNewline("   • Mines Natural (Leaf & Hay...) Blocks Faster", GREEN)
                    .addPartWithNewline("       + Mines With Diamond Tier", GREEN)
                    .addPartWithNewline("   • Damage +1", GREEN)
                    .addPartWithNewline("   • Swing Speed -2", RED)
                    .addPartWithNewline("")
                    .addPartWithNewline("Compatible With:")
                    .addPartWithNewline("   • Haste Seal | Tiers 1&2", GOLD)
                    .addPartWithNewline("   • Replant Seal", GOLD)
                    .addPartWithNewline("   • Harvest AOE Seal | Tier 1", GOLD)
                    .addPartWithNewline("   • Farm AOE Seal | Tiers 1&2", GOLD).build())
            .build();
    public static Tooltip HASTE = new TooltipBuilder()
            .addPart(TooltipComponent.amplifying())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(1)
                    .addPartWithNewline("Max Tier: 3")
                    .addPartWithNewline("When On Wand: ")
                    .addPartWithNewline("   • Mining Speed +8", GREEN).build())
            .build();
    public static Tooltip HASTE_TIER_2 = new TooltipBuilder()
            .addPart(TooltipComponent.amplifying())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(2)
                    .addPartWithNewline("Max Tier: 3")
                    .addPartWithNewline("When On Wand: ")
                    .addPartWithNewline("   • Mining Speed +24", GREEN).build())
            .build();
    public static Tooltip HASTE_TIER_3 = new TooltipBuilder()
            .addPart(TooltipComponent.amplifying())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(3, true)
                    .addPartWithNewline("When On Wand: ")
                    .addPartWithNewline("   • Mining Speed +36", GREEN).build())
            .build();
    public static Tooltip STACK_SIZE = new TooltipBuilder()
            .addPart(TooltipComponent.amplifying())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(1)
                    .addPartWithNewline("Max Tier: 6")
                    .addPartWithNewline("When On Sack: ")
                    .addPartWithNewline("   • Increases Stack Size In Sack By 2x", GREEN).build())
            .build();
    public static Tooltip PICKUP = new TooltipBuilder()
            .addPart(TooltipComponent.amplifying())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(1, true)
                    .addPartWithNewline("When On Sack: ")
                    .addPartWithNewline("   • Items that are picked up automatically transferred to sack", GREEN)
                    .addPartWithNewline("       • Three Pickup Modes:", GREEN)
                    .addPartWithNewline("           • ALL = All items are transferred to sack", LIGHT_GREEN)
                    .addPartWithNewline("           • VOID (junk mode) = items that are already in the sack are transferred, up to the stack limit (then will be deleted)", DARK_RED)
                    .addPartWithNewline("           • FILTERED = Items that are already in sack will be transferred", GREEN)
                    .addPartWithNewline("           • NONE = No items are transferred", YELLOW)
                    .addPartWithNewline("       • Use Backtick/Tilde key to switch between modes", WHITE)
                    .addPartWithNewline("           • Items that are picked up automatically transferred to sack", GREEN).build())
            .build();
    public static Tooltip WIDTH = new TooltipBuilder()
            .addPart(TooltipComponent.amplifying())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(1)
                    .addPartWithNewline("Max Tier: 4")
                    .addPartWithNewline("When On Sack: ")
                    .addPartWithNewline("   • Adds 1 Column To Sack", GREEN).build())
            .build();
    public static Tooltip HEIGHT = new TooltipBuilder()
            .addPart(TooltipComponent.amplifying())
            .addPart(new TooltipComponentBuilder()
                    .addPartWithNewline("")
                    .addTierWithNewline(1)
                    .addPartWithNewline("Max Tier: 4")
                    .addPartWithNewline("When On Sack: ")
                    .addPartWithNewline("   • Adds 1 Row To Sack", GREEN).build())
            .build();
}
