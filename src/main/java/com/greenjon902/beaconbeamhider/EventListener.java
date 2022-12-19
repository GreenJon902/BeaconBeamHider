package com.greenjon902.beaconbeamhider;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class EventListener implements Listener {
    Config config;
    PlayerExcludeList playerExcludeList;

    public EventListener(Config config, PlayerExcludeList playerExcludeList) {
        this.config = config;
        this.playerExcludeList = playerExcludeList;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChunkSend(PlayerChunkLoadEvent event) {
        Player player = event.getPlayer();

        if (!playerExcludeList.isPlayerExcluded(player)) {
            Chunk chunk = event.getChunk();
            World world = chunk.getWorld();

            int minHeight = config.min_beacon_y;
            if (minHeight < chunk.getWorld().getMinHeight()) {
                minHeight = chunk.getWorld().getMinHeight();
            }
            int maxHeight = config.max_beacon_y;
            if (maxHeight > chunk.getWorld().getMaxHeight()) {
                maxHeight = chunk.getWorld().getMaxHeight();
            }

            for (int x = 0; x < 16; x++) {
                for (int y = minHeight; y < maxHeight; y++) {
                    for (int z = 0; z < 16; z++) {

                        Block block = chunk.getBlock(x, y, z);
                        int X = block.getX(); // World coordinates
                        int Y = block.getY();
                        int Z = block.getZ();

                        if (Checker.check_beacon_at(X, Y, Z, world, config)) {
                            player.sendBlockChange(
                                    block.getLocation().add(0, -1, 0),
                                    config.getCorrectBlockReplacement(world.getBlockAt(X, Y - 1, Z).getType())
                                            .createBlockData());
                        }
                    }
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockChange(BlockBreakEvent event) {
        onBlockChange(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockChange(BlockPistonExtendEvent event) {
        onBlockChange(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockChange(BlockPistonRetractEvent event) {
        onBlockChange(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockChange(BlockPhysicsEvent event) {
        onBlockChange(event.getBlock());
    }

    /**
     * Checks if needed, then sets the block under any touching beacon to be what it should be to make sure it is
     * correct.
     */
    public void onBlockChange(Block block) {
        World world = block.getWorld();

        // Break beacon
        if (block.getType() == Material.BEACON) {
            Location location = block.getLocation().add(0, -1, 0);
            BlockData blockData = world.getBlockData(location);

            for (Player player : world.getPlayers()) {
                player.sendBlockChange(location, blockData);
            }
        }

        // Break block under beacon
        if (world.getBlockAt(block.getLocation().add(0, +2, 0)).getType() == Material.BEACON) {
            Location location = block.getLocation().add(0, +1, 0);
            BlockData blockData = world.getBlockData(location);

            for (Player player : world.getPlayers()) {
                player.sendBlockChange(location, blockData);
            }
        }

        // Break part of first beacon pyramid
        if (Checker.check_block_type(block.getType(), config)) {
            for (int rx = -1; rx <= 1; rx++) {
                for (int rz = -1; rz <= 1; rz++) {
                    if (!(rx == 0 && rz == 0)) {
                        Location location1 = block.getLocation().add(rx, 1, rz);
                        Location location2 = block.getLocation().add(rx, 0, rz);
                        if (world.getBlockAt(location1).getType() == Material.BEACON) {
                            for (Player player : world.getPlayers()) {
                                player.sendBlockChange(location2, world.getBlockData(location2));
                            }
                        }
                    }
                }
            }
        }
    }


}
