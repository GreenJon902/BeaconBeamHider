package com.greenjon902.beaconbeamhider;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Checker {
    /**
     * Checks whether a valid beacon exists at the given coordinates, it checks for the beacon, and the first pyramid
     * blocks (the 3x3 grid) and if they are to be removed.
     */
    public static boolean check_beacon_at(int x, int y, int z, World world, Config config) {
        Block block = world.getBlockAt(x, y, z);
        if (block.getType() == Material.BEACON) {
            if (check_block_type(world.getBlockAt(x - 1, y - 1, z - 1).getType(), config) &&
                check_block_type(world.getBlockAt(x - 1, y - 1, z + 0).getType(), config) &&
                check_block_type(world.getBlockAt(x - 1, y - 1, z + 1).getType(), config) &&
                check_block_type(world.getBlockAt(x + 0, y - 1, z - 1).getType(), config) &&
                check_block_type(world.getBlockAt(x + 0, y - 1, z + 0).getType(), config) &&
                check_block_type(world.getBlockAt(x + 0, y - 1, z + 1).getType(), config) &&
                check_block_type(world.getBlockAt(x + 1, y - 1, z - 1).getType(), config) &&
                check_block_type(world.getBlockAt(x + 1, y - 1, z + 0).getType(), config) &&
                check_block_type(world.getBlockAt(x + 1, y - 1, z + 1).getType(), config)) {

                if (world.getBlockAt(x, y - 2, z).getType() != Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a material is to be hidden based on config.
     */
    public static boolean check_block_type(Material material, Config config) {
        if (config.replace_iron && material == Material.IRON_BLOCK) {
            return true;
        } else if (config.replace_gold && material == Material.GOLD_BLOCK) {
            return true;
        } else if (config.replace_emerald && material == Material.EMERALD_BLOCK) {
            return true;
        } else if (config.replace_diamond && material == Material.DIAMOND_BLOCK) {
            return true;
        } else if (config.replace_netherite && material == Material.NETHERITE_BLOCK) {
            return true;
        }
        return false;
    }
}
