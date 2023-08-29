package net.vakror.soulbound.mod.seal;

public enum SealType {
    PASSIVE,
    OFFENSIVE,
    AMPLIFYING;

    public int getId() {
        return switch (this) {
            case PASSIVE -> 0;
            case OFFENSIVE -> 1;
            case AMPLIFYING -> 2;
        };
    }
}
