package net.vakror.asm.util;

public record SackStats(int rows, int columns, int maxStackLimit) {
    public int inventorySize() {
        return rows * columns;
    }
}
