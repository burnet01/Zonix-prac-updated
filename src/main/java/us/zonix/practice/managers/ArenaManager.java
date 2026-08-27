package us.zonix.practice.managers;

import java.util.Collection;
import org.bukkit.configuration.ConfigurationSection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import us.zonix.practice.kit.Kit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import us.zonix.practice.util.ItemUtil;
import org.bukkit.Material;
import us.zonix.practice.util.inventory.InventoryUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.Iterator;
import org.bukkit.configuration.file.FileConfiguration;
import us.zonix.practice.CustomLocation;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;
import us.zonix.practice.arena.StandaloneArena;
import us.zonix.practice.arena.Arena;
import java.util.Map;
import us.zonix.practice.file.ConfigFile;
import us.zonix.practice.Practice;

public class ArenaManager
{
    private final Practice plugin;
    private final ConfigFile arenasFile;
    private final Map<String, Arena> arenas;
    private final Map<StandaloneArena, UUID> arenaMatchUUIDs;
    private int generatingArenaRunnables;

    public ArenaManager() {
        this.plugin = Practice.getInstance();
        this.arenas = new HashMap<String, Arena>();
        this.arenaMatchUUIDs = new HashMap<StandaloneArena, UUID>();
        this.arenasFile = new ConfigFile(this.plugin, "arenas");
        this.loadArenas();
    }

    private void loadArenas() {
        final FileConfiguration fileConfig = this.arenasFile.getConfiguration();
        final ConfigurationSection arenaSection = fileConfig.getConfigurationSection("arenas");

        if (arenaSection == null) {
            return;
        }

        arenaSection.getKeys(false).forEach(arenaName -> {
            final String arenaRoot = "arenas." + arenaName;

            // Load basic arena data
            final String aString = fileConfig.getString(arenaRoot + ".a");
            final String bString = fileConfig.getString(arenaRoot + ".b");
            final String minString = fileConfig.getString(arenaRoot + ".min");
            final String maxString = fileConfig.getString(arenaRoot + ".max");
            final boolean enabled = fileConfig.getBoolean(arenaRoot + ".enabled", true);

            if (aString == null || bString == null || minString == null || maxString == null) {
                return;
            }

            final CustomLocation a = CustomLocation.stringToLocation(aString);
            final CustomLocation b = CustomLocation.stringToLocation(bString);
            final CustomLocation min = CustomLocation.stringToLocation(minString);
            final CustomLocation max = CustomLocation.stringToLocation(maxString);

            final List<StandaloneArena> standaloneArenas = new ArrayList<StandaloneArena>();
            final List<StandaloneArena> availableArenas = new ArrayList<StandaloneArena>();

            final ConfigurationSection standaloneSection = fileConfig.getConfigurationSection(arenaRoot + ".standaloneArenas");
            if (standaloneSection != null) {
                standaloneSection.getKeys(false).forEach(standaloneKey -> {
                    final String standaloneRoot = arenaRoot + ".standaloneArenas." + standaloneKey;

                    final String saAString = fileConfig.getString(standaloneRoot + ".a");
                    final String saBString = fileConfig.getString(standaloneRoot + ".b");
                    final String saMinString = fileConfig.getString(standaloneRoot + ".min");
                    final String saMaxString = fileConfig.getString(standaloneRoot + ".max");

                    if (saAString != null && saBString != null && saMinString != null && saMaxString != null) {
                        final CustomLocation saA = CustomLocation.stringToLocation(saAString);
                        final CustomLocation saB = CustomLocation.stringToLocation(saBString);
                        final CustomLocation saMin = CustomLocation.stringToLocation(saMinString);
                        final CustomLocation saMax = CustomLocation.stringToLocation(saMaxString);

                        final StandaloneArena standaloneArena = new StandaloneArena(saA, saB, saMin, saMax);
                        standaloneArenas.add(standaloneArena);
                        availableArenas.add(standaloneArena);
                    }
                });
            }

            final Arena arena = new Arena(arenaName, standaloneArenas, availableArenas, a, b, min, max, enabled);
            this.arenas.put(arenaName, arena);
        });
    }

    public void saveArenas() {
        final FileConfiguration fileConfig = (FileConfiguration)this.arenasFile.getConfiguration();
        fileConfig.set("arenas", (Object)null);
        this.arenas.forEach((arenaName, arena) -> {
            if (arena.getA() == null || arena.getB() == null) {
                return;
            }
            final String a = CustomLocation.locationToString(arena.getA());
            final String b = CustomLocation.locationToString(arena.getB());
            final String min = CustomLocation.locationToString(arena.getMin());
            final String max = CustomLocation.locationToString(arena.getMax());
            final String arenaRoot = "arenas." + arenaName;
            fileConfig.set(arenaRoot + ".a", (Object)a);
            fileConfig.set(arenaRoot + ".b", (Object)b);
            fileConfig.set(arenaRoot + ".min", (Object)min);
            fileConfig.set(arenaRoot + ".max", (Object)max);
            fileConfig.set(arenaRoot + ".enabled", (Object)arena.isEnabled());
            fileConfig.set(arenaRoot + ".standaloneArenas", (Object)null);
            int i = 0;
            if (arena.getStandaloneArenas() != null) {
                for (final StandaloneArena saArena : arena.getStandaloneArenas()) {
                    final String saA = CustomLocation.locationToString(saArena.getA());
                    final String saB = CustomLocation.locationToString(saArena.getB());
                    final String saMin = CustomLocation.locationToString(saArena.getMin());
                    final String saMax = CustomLocation.locationToString(saArena.getMax());
                    final String standAloneRoot = arenaRoot + ".standaloneArenas." + i;
                    fileConfig.set(standAloneRoot + ".a", (Object)saA);
                    fileConfig.set(standAloneRoot + ".b", (Object)saB);
                    fileConfig.set(standAloneRoot + ".min", (Object)saMin);
                    fileConfig.set(standAloneRoot + ".max", (Object)saMax);
                    ++i;
                }
            }
            return;
        });
        this.arenasFile.save();
    }

    public void reloadArenas() {
        this.saveArenas();
        this.arenas.clear();
        this.loadArenas();
    }

    public void openArenaSystemUI(final Player player) {
        if (this.arenas.size() == 0) {
            player.sendMessage(ChatColor.RED + "There's no arenas.");
            return;
        }
        final InventoryUI inventory = new InventoryUI("Arena System", true, 6);
        for (final Arena arena : this.arenas.values()) {
            final ItemStack item = ItemUtil.createItem(Material.PAPER, ChatColor.YELLOW + arena.getName() + ChatColor.GRAY + " (" + (arena.isEnabled() ? (ChatColor.GREEN.toString() + ChatColor.BOLD + "ENABLED") : (ChatColor.RED.toString() + ChatColor.BOLD + "DISABLED")) + ChatColor.GRAY + ")");
            ItemUtil.reloreItem(item, ChatColor.GRAY + "Arenas: " + ChatColor.GREEN + ((arena.getStandaloneArenas().size() == 0) ? "Single Arena (Invisible Players)" : (arena.getStandaloneArenas().size() + " Arenas")), ChatColor.GRAY + "Standalone Arenas: " + ChatColor.GREEN + ((arena.getAvailableArenas().size() == 0) ? "None" : (arena.getAvailableArenas().size() + " Arenas Available")), "", ChatColor.YELLOW.toString() + ChatColor.BOLD + "LEFT CLICK " + ChatColor.GRAY + "Teleport to Arena", ChatColor.YELLOW.toString() + ChatColor.BOLD + "RIGHT CLICK " + ChatColor.GRAY + "Generate Standalone Arenas");
            inventory.addItem(new InventoryUI.AbstractClickableItem(item) {
                @Override
                public void onClick(final InventoryClickEvent event) {
                    final Player player = (Player)event.getWhoClicked();
                    if (event.getClick() == ClickType.LEFT) {
                        player.teleport(arena.getA().toBukkitLocation());
                    }
                    else {
                        final InventoryUI generateInventory = new InventoryUI("Generate Arenas", true, 1);
                        final int[] array;
                        final int[] batches = array = new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150 };
                        for (final int batch : array) {
                            final ItemStack item = ItemUtil.createItem(Material.PAPER, ChatColor.RED.toString() + ChatColor.BOLD + batch + " ARENAS");
                            generateInventory.addItem(new InventoryUI.AbstractClickableItem(item) {
                                @Override
                                public void onClick(final InventoryClickEvent event) {
                                    final Player player = (Player)event.getWhoClicked();
                                    player.performCommand("arena generate " + arena.getName() + " " + batch);
                                    player.sendMessage(ChatColor.GREEN + "Generating " + batch + " arenas, please check console for progress.");
                                    player.closeInventory();
                                }
                            });
                        }
                        player.openInventory(generateInventory.getCurrentPage());
                    }
                }
            });
        }
        player.openInventory(inventory.getCurrentPage());
    }

    public void createArena(final String name) {
        this.arenas.put(name, new Arena(name));
    }

    public void deleteArena(final String name) {
        this.arenas.remove(name);
    }

    public Arena getArena(final String name) {
        return this.arenas.get(name);
    }

    public Arena getRandomArena(final Kit kit) {
        final List<Arena> enabledArenas = new ArrayList<Arena>();
        for (final Arena arena : this.arenas.values()) {
            if (!arena.isEnabled()) {
                continue;
            }
            if (kit.getExcludedArenas().contains(arena.getName())) {
                continue;
            }
            if (kit.getArenaWhiteList().size() > 0 && !kit.getArenaWhiteList().contains(arena.getName())) {
                continue;
            }
            enabledArenas.add(arena);
        }
        if (enabledArenas.size() == 0) {
            return null;
        }
        return enabledArenas.get(ThreadLocalRandom.current().nextInt(enabledArenas.size()));
    }

    public void removeArenaMatchUUID(final StandaloneArena arena) {
        this.arenaMatchUUIDs.remove(arena);
    }

    public UUID getArenaMatchUUID(final StandaloneArena arena) {
        return this.arenaMatchUUIDs.get(arena);
    }

    public void setArenaMatchUUID(final StandaloneArena arena, final UUID matchUUID) {
        this.arenaMatchUUIDs.put(arena, matchUUID);
    }

    public Map<String, Arena> getArenas() {
        return this.arenas;
    }

    public Map<StandaloneArena, UUID> getArenaMatchUUIDs() {
        return this.arenaMatchUUIDs;
    }

    public int getGeneratingArenaRunnables() {
        return this.generatingArenaRunnables;
    }

    public void setGeneratingArenaRunnables(final int generatingArenaRunnables) {
        this.generatingArenaRunnables = generatingArenaRunnables;
    }
}
