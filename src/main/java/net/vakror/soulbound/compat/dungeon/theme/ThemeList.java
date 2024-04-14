package net.vakror.soulbound.compat.dungeon.theme;

import java.util.ArrayList;
import java.util.List;

public record ThemeList(List<DungeonTheme> themeList) {
    public ThemeList() {
        this(new ArrayList<>());
    }

    public ThemeList addTheme(DungeonTheme theme) {
        themeList.add(theme);
        return this;
    }
}
