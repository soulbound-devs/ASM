package net.vakror.soulbound.mod.util;

public record SackStats(int rows, int columns, int maxStackLimit) {
    public int inventorySize() {
        return rows * columns;
    }
}
