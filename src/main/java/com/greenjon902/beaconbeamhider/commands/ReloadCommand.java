package com.greenjon902.beaconbeamhider.commands;

import com.greenjon902.beaconbeamhider.BeaconBeamHider;
import com.greenjon902.beaconbeamhider.Checker;
import com.greenjon902.beaconbeamhider.Config;
import com.greenjon902.beaconbeamhider.PlayerExcludeList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReloadCommand implements CommandExecutor {
    BeaconBeamHider beaconBeamHider;
    Config config;
    PlayerExcludeList playerExcludeList;

    public ReloadCommand(BeaconBeamHider beaconBeamHider, Config config, PlayerExcludeList playerExcludeList) {
        this.beaconBeamHider = beaconBeamHider;
        this.config = config;
        this.playerExcludeList = playerExcludeList;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("beaconbeamhider.reload")) {
            sender.sendMessage(Component.text("You do not have permission to reload this plugin!")
                    .color(TextColor.color(0xFF0000)));
            return true;
        }

        sender.sendMessage(Component.text("Reloading..."));

        try {
            config.loadFrom(new File(beaconBeamHider.getDataFolder(), "config.yml"));

            playerExcludeList.saveTo(new File(beaconBeamHider.getDataFolder(), "playerExcludeList.txt"));
            playerExcludeList.loadFrom(new File(beaconBeamHider.getDataFolder(), "playerExcludeList.txt"));
        } catch (IOException e) {
            sender.sendMessage(Component.text("An error occurred while reloading this plugin!")
                    .color(TextColor.color(0xFF0000)));
            throw new RuntimeException(e);
        }

        return true;
    }
}
