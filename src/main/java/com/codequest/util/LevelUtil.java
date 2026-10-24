package com.codequest.util;

public final class LevelUtil {
    private LevelUtil() {}
    // Level 1 at 0–99 XP, Level 2 at 100–199 XP, etc.
    public static int levelForXp(int xp) { return Math.max(1, (xp / 100) + 1); }
    public static int progressInLevel(int xp) { return xp % 100; }
}
