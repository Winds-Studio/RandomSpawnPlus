package systems.kscott.randomspawnplus.spawn;

import org.bukkit.Material;

import java.util.Set;

public class SpawnData {

    private static Set<Material> unsafeBlocks;

    public static Set<Material> getUnsafeBlocks() {
        return unsafeBlocks;
    }

    public static void setUnsafeBlocks(Set<Material> blocks) {
        unsafeBlocks = blocks;
    }
}
