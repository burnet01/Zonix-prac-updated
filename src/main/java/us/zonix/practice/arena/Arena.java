package us.zonix.practice.arena;

import org.bukkit.Location;
import us.zonix.practice.CustomLocation;
import java.util.List;

public class Arena
{
    private final String name;
    private List<StandaloneArena> standaloneArenas;
    private List<StandaloneArena> availableArenas;
    private CustomLocation a;
    private CustomLocation b;
    private CustomLocation min;
    private CustomLocation max;
    private boolean enabled;
    
    public Arena(final String name, final List<StandaloneArena> standaloneArenas, final List<StandaloneArena> availableArenas, final CustomLocation a, final CustomLocation b, final CustomLocation min, final CustomLocation max, final boolean enabled) {
        this.name = name;
        this.standaloneArenas = standaloneArenas;
        this.availableArenas = availableArenas;
        this.a = a;
        this.b = b;
        this.min = min;
        this.max = max;
        this.enabled = enabled;
        this.loadChunks();
    }
    
    private void loadChunks() {
        this.standaloneArenas.forEach(standaloneArena -> {
            final Location locA = standaloneArena.getA().toBukkitLocation();
            final Location locB = standaloneArena.getB().toBukkitLocation();
            if (!locA.isChunkLoaded()) {
                locA.getChunk().load();
            }
            if (!locB.isChunkLoaded()) {
                locB.getChunk().load();
            }
        });
        this.availableArenas.forEach(standaloneArena -> {
            final Location locA = standaloneArena.getA().toBukkitLocation();
            final Location locB = standaloneArena.getB().toBukkitLocation();
            if (!locA.isChunkLoaded()) {
                locA.getChunk().load();
            }
            if (!locB.isChunkLoaded()) {
                locB.getChunk().load();
            }
        });
        final Location locA3 = this.a.toBukkitLocation();
        final Location locB3 = this.b.toBukkitLocation();
        if (!locA3.isChunkLoaded()) {
            locA3.getChunk().load();
        }
        if (!locB3.isChunkLoaded()) {
            locB3.getChunk().load();
        }
    }
    
    public StandaloneArena getAvailableArena() {
        final StandaloneArena arena = this.availableArenas.get(0);
        this.availableArenas.remove(0);
        return arena;
    }
    
    public void addStandaloneArena(final StandaloneArena arena) {
        this.standaloneArenas.add(arena);
    }
    
    public void addAvailableArena(final StandaloneArena arena) {
        this.availableArenas.add(arena);
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<StandaloneArena> getStandaloneArenas() {
        return this.standaloneArenas;
    }
    
    public List<StandaloneArena> getAvailableArenas() {
        return this.availableArenas;
    }
    
    public CustomLocation getA() {
        return this.a;
    }
    
    public CustomLocation getB() {
        return this.b;
    }
    
    public CustomLocation getMin() {
        return this.min;
    }
    
    public CustomLocation getMax() {
        return this.max;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setStandaloneArenas(final List<StandaloneArena> standaloneArenas) {
        this.standaloneArenas = standaloneArenas;
    }
    
    public void setAvailableArenas(final List<StandaloneArena> availableArenas) {
        this.availableArenas = availableArenas;
    }
    
    public void setA(final CustomLocation a) {
        this.a = a;
    }
    
    public void setB(final CustomLocation b) {
        this.b = b;
    }
    
    public void setMin(final CustomLocation min) {
        this.min = min;
    }
    
    public void setMax(final CustomLocation max) {
        this.max = max;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public Arena(final String name) {
        this.name = name;
    }
}
