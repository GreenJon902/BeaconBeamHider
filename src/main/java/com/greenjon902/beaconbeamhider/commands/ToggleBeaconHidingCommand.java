package com.greenjon902.beaconbeamhider.commands;

import com.greenjon902.beaconbeamhider.BeaconBeamHider;
import com.greenjon902.beaconbeamhider.Config;
import com.greenjon902.beaconbeamhider.PlayerExcludeList;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleBeaconHidingCommand implements CommandExecutor {
    BeaconBeamHider beaconBeamHider;
    Config config;
    PlayerExcludeList playerExcludeList;

    public ToggleBeaconHidingCommand(BeaconBeamHider beaconBeamHider, Config config, PlayerExcludeList playerExcludeList) {
        this.beaconBeamHider = beaconBeamHider;
        this.config = config;
        this.playerExcludeList = playerExcludeList;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can show beacons!");
        }

        Player player = (Player) sender;

        if (playerExcludeList.isPlayerExcluded(player)) {
            playerExcludeList.un_exclude(player);
            sender.sendMessage(Component.text("You have turned on beacons hiding! You may need to re-log to see the full effects."));
        } else {
            playerExcludeList.exclude(player);
            sender.sendMessage(Component.text("You have turned off beacons hiding! You may need to re-log to see the full effects."));
        }

        return true;
    }
}
