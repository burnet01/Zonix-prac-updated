package us.zonix.practice.util;

import org.bukkit.ChatColor;

public final class CC {

    public static final String S = "\u00BB";
    public static final String PRIMARY = ChatColor.RED.toString();
    public static final String WHITE = ChatColor.WHITE.toString();
    public static final String GRAY = ChatColor.GRAY.toString();
    public static final String RED = ChatColor.RED.toString();
    public static final String GREEN = ChatColor.GREEN.toString();
    public static final String GOLD = ChatColor.GOLD.toString();
    public static final String DARK_RED = ChatColor.DARK_RED.toString();
    public static final String BOLD = ChatColor.BOLD.toString();
    public static final String ITALIC = ChatColor.ITALIC.toString();

    private CC() {
        throw new RuntimeException("Cannot instantiate a utility class.");
    }
}
