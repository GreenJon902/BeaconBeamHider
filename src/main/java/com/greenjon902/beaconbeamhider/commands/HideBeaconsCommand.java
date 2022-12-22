package com.greenjon902.beaconbeamhider.commands;

import com.greenjon902.beaconbeamhider.BeaconBeamHider;
import com.greenjon902.beaconbeamhider.Checker;
import com.greenjon902.beaconbeamhider.Config;
import com.greenjon902.beaconbeamhider.PlayerExcludeList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HideBeaconsCommand implements CommandExecutor {
    BeaconBeamHider beaconBeamHider;
    Config config;
    PlayerExcludeList playerExcludeList;

    public HideBeaconsCommand(BeaconBeamHider beaconBeamHider, Config config, PlayerExcludeList playerExcludeList) {
        this.beaconBeamHider = beaconBeamHider;
        this.config = config;
        this.playerExcludeList = playerExcludeList;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can hide beacons!");
        }

        Player player = (Player) sender;


        Chunk chunk = player.getChunk();
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
            for (int y = minHeight; y <= maxHeight; y++) {
                for (int z = 0; z < 16; z++) {

                    Block block = chunk.getBlock(x, y, z);
                    int X = block.getX(); // World coordinates
                    int Y = block.getY();
                    int Z = block.getZ();
                    if (block.getType() == Material.BEACON) {
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

        player.sendMessage(Component.text("Beacons in your current chunk have been hidden!"));

        return true;
    }
}
