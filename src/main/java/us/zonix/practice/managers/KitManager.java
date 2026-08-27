package us.zonix.practice.managers;

import java.util.Collection;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;
import dev.cobblesword.nachospigot.knockback.KnockbackProfile;
import com.windpvp.windspigot.knockback.KnockbackConfig;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import us.zonix.practice.file.ConfigFile;
import java.util.List;
import us.zonix.practice.kit.Kit;
import java.util.Map;
import us.zonix.practice.Practice;

public class KitManager
{
    private final Practice plugin;
    private final Map<String, Kit> kits;
    private final List<String> rankedKits;
    private final ConfigFile kitsFile;
    
    public KitManager() {
        this.plugin = Practice.getInstance();
        this.kits = new HashMap<String, Kit>();
        this.rankedKits = new ArrayList<String>();
        this.kitsFile = new ConfigFile(this.plugin, "kits");
        this.loadKits();
        this.kits.entrySet().stream().filter(kit -> kit.getValue().isEnabled()).filter(kit -> kit.getValue().isRanked()).forEach(kit -> this.rankedKits.add(kit.getKey()));
    }
    
    private void loadKits() {
        final ConfigurationSection kitSection = this.kitsFile.getConfiguration().getConfigurationSection("kits");
        if (kitSection == null) {
            return;
        }
        kitSection.getKeys(false).forEach(name -> {
            final ItemStack[] contents = ((List<ItemStack>)kitSection.get(name + ".contents")).toArray(new ItemStack[0]);
            final ItemStack[] armor = ((List<ItemStack>)kitSection.get(name + ".armor")).toArray(new ItemStack[0]);
            final ItemStack[] kitEditContents = ((List<ItemStack>)kitSection.get(name + ".kitEditContents")).toArray(new ItemStack[0]);
            final List<String> excludedArenas = kitSection.getStringList(name + ".excludedArenas");
            final List<String> arenaWhiteList = kitSection.getStringList(name + ".arenaWhitelist");
            final ItemStack icon = (ItemStack)kitSection.get(name + ".icon");
            final boolean enabled = kitSection.getBoolean(name + ".enabled");
            final boolean ranked = kitSection.getBoolean(name + ".ranked");
            final boolean combo = kitSection.getBoolean(name + ".combo");
            final boolean sumo = kitSection.getBoolean(name + ".sumo");
            final boolean build = kitSection.getBoolean(name + ".build");
            final boolean spleef = kitSection.getBoolean(name + ".spleef");
            final boolean parkour = kitSection.getBoolean(name + ".parkour");
            final boolean hcteams = kitSection.getBoolean(name + ".hcteams");
            final boolean bestOfThree = kitSection.getBoolean(name + ".bestOfThree");
            final boolean premium = kitSection.getBoolean(name + ".premium");
            final int priority = kitSection.getInt(name + ".priority");
            final Optional<KnockbackProfile> knockback = Optional.ofNullable(KnockbackConfig.getKbProfileByName(kitSection.getString(name + ".knockback")));
            final Kit kit = new Kit(name, contents, armor, kitEditContents, icon, excludedArenas, arenaWhiteList, enabled, ranked, combo, sumo, build, spleef, parkour, hcteams, premium, bestOfThree, priority, knockback.orElseGet(KnockbackConfig::getCurrentKb));
            this.kits.put(name, kit);
        });
    }
    
    public void saveKits() {
        final FileConfiguration fileConfig = (FileConfiguration)this.kitsFile.getConfiguration();
        fileConfig.set("kits", (Object)null);
        final FileConfiguration fileConfiguration;
        this.kits.forEach((kitName, kit) -> {
            if (kit.getIcon() != null && kit.getContents() != null && kit.getArmor() != null) {
                fileConfig.set("kits." + kitName + ".contents", (Object)kit.getContents());
                fileConfig.set("kits." + kitName + ".armor", (Object)kit.getArmor());
                fileConfig.set("kits." + kitName + ".kitEditContents", (Object)kit.getKitEditContents());
                fileConfig.set("kits." + kitName + ".icon", (Object)kit.getIcon());
                fileConfig.set("kits." + kitName + ".excludedArenas", (Object)kit.getExcludedArenas());
                fileConfig.set("kits." + kitName + ".arenaWhitelist", (Object)kit.getArenaWhiteList());
                fileConfig.set("kits." + kitName + ".enabled", (Object)kit.isEnabled());
                fileConfig.set("kits." + kitName + ".ranked", (Object)kit.isRanked());
                fileConfig.set("kits." + kitName + ".combo", (Object)kit.isCombo());
                fileConfig.set("kits." + kitName + ".sumo", (Object)kit.isSumo());
                fileConfig.set("kits." + kitName + ".build", (Object)kit.isBuild());
                fileConfig.set("kits." + kitName + ".spleef", (Object)kit.isSpleef());
                fileConfig.set("kits." + kitName + ".parkour", (Object)kit.isParkour());
                fileConfig.set("kits." + kitName + ".hcteams", (Object)kit.isHcteams());
                fileConfig.set("kits." + kitName + ".bestOfThree", (Object)kit.isBestOfThree());
                fileConfig.set("kits." + kitName + ".premium", (Object)kit.isPremium());
                fileConfig.set("kits." + kitName + ".priority", (Object)kit.getPriority());
                fileConfig.set("kits." + kitName + ".knockback", (Object)kit.getKnockbackProfile().getName());
            }
            return;
        });
        this.kitsFile.save();
    }
    
    public void deleteKit(final String name) {
        this.kits.remove(name);
    }
    
    public void createKit(final String name) {
        this.kits.put(name, new Kit(name));
    }
    
    public Collection<Kit> getKits() {
        return this.kits.values();
    }
    
    public Kit getKit(final String name) {
        return this.kits.get(name);
    }
    
    public List<String> getRankedKits() {
        return this.rankedKits;
    }
}
