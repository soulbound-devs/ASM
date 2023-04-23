package net.vakror.asm.seal;

public interface ISeal {

    public default String getId() {
        return "error";
    }

    public default boolean canBeActivated() {
        return false;
    }
}
