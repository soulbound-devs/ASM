package net.vakror.asm.seal;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;

public class SealTooltip {

    private final List<Component> tooltip;

    public SealTooltip(List<Component> tooltip) {
        this.tooltip = tooltip;
    }

    public List<Component> getTooltip() {
        return tooltip;
    }

    public static SealTooltip empty() {
        return new SealTooltip(new ArrayList<>());
    }

    public static class SealTooltipBuilder {
        private final List<SealTooltipComponent> tooltips = new ArrayList<>();

        public SealTooltipBuilder addPart(SealTooltipComponent part) {
            tooltips.add(part);
            return this;
        }

        public SealTooltip build() {
            List<Component> tooltip = new ArrayList<>();
            tooltips.forEach((component) -> {
                tooltip.add(component.getTooltip());
            });
            return new SealTooltip(tooltip);
        }
    }
    public static class SealTooltipComponent {

        private final Component tooltip;

        public SealTooltipComponent(MutableComponent tooltip, Style style) {
            this.tooltip = tooltip.withStyle(style);
        }

        public Component getTooltip() {
            return tooltip;
        }

        public static SealTooltipComponent passive() {
            return new SealTooltipComponentBuilder().addPartWithNewline("").addPart("Passive Seal").setStyle(ChatFormatting.AQUA).build();
        }

        public static SealTooltipComponent offensive() {
            return new SealTooltipComponentBuilder().addPartWithNewline("").addPart("Offensive Seal").setStyle(ChatFormatting.RED).build();
        }

        public static SealTooltipComponent amplifying() {
            return new SealTooltipComponentBuilder().addPartWithNewline("").addPart("Amplifying Seal").setStyle(ChatFormatting.GOLD).build();
        }

        public static SealTooltipComponent empty() {
            return new SealTooltipComponent(Component.empty(), Style.EMPTY);
        }
    }

    public static class SealTooltipComponentBuilder {
        private final MutableComponent tooltip = Component.literal("");
        private Style style = Style.EMPTY;

        public SealTooltipComponentBuilder addPartWithNewline(String part) {
            tooltip.append(part);
            tooltip.append("\n");
            return this;
        }

        public SealTooltipComponentBuilder addPartWithNewline(String part, ColorCode color) {
            tooltip.append("§" + color.id + part);
            tooltip.append("\n");
            return this;
        }

        public SealTooltipComponentBuilder addTierWithNewline(int tier) {
            tooltip.append("§" + getColorFromTier(tier).id + "Current Tier: " + tier);
            tooltip.append("\n");
            return this;
        }

        private ColorCode getColorFromTier(int tier) {
            return switch (tier) {
                case 2 -> ColorCode.PINK;
                case 3 -> ColorCode.RED;
                case 4 -> ColorCode.DARK_RED;
                case 5 -> ColorCode.PURPLE;
                case 6 -> ColorCode.DARK_BLUE;
                case 7 -> ColorCode.BLUE;
                case 8 -> ColorCode.LIGHT_BLUE;
                case 9 -> ColorCode.CYAN;
                case 10 -> ColorCode.LIGHT_GRAY;
                case 11 -> ColorCode.GRAY;
                default -> ColorCode.WHITE;
            };
        }

        public SealTooltipComponentBuilder addPart(String part) {
            tooltip.append(part);
            return this;
        }

        public SealTooltipComponentBuilder addPart(String part, ColorCode color) {
            tooltip.append("§" + color.id + part);
            return this;
        }

        public SealTooltipComponentBuilder setStyle(Style style) {
            this.style = style;
            return this;
        }

        public SealTooltipComponentBuilder setStyle(ChatFormatting style) {
            this.style = Style.EMPTY.applyFormat(style);
            return this;
        }

        public SealTooltipComponent build() {
            return new SealTooltipComponent(tooltip, style);
        }

        public static enum ColorCode {
            BLACK(0),
            DARK_BLUE(1),
            GREEN(2),
            CYAN(3),
            DARK_RED(4),
            PURPLE(5),
            GOLD(6),
            LIGHT_GRAY(7),
            GRAY(8),
            BLUE(9),
            LIGHT_GREEN("A"),
            LIGHT_BLUE("B"),
            RED("C"),
            PINK("D"),
            YELLOW("E"),
            WHITE("F");

            final String id;
            ColorCode(String id) {
                this.id = id;
            }
            ColorCode(int id) {
                this.id = String.valueOf(id);
            }

            public String getId() {
                return id;
            }
        }
    }
}
