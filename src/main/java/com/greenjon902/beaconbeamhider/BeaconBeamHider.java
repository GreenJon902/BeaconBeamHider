package com.greenjon902.beaconbeamhider;

import com.greenjon902.beaconbeamhider.commands.HideBeaconsCommand;
import com.greenjon902.beaconbeamhider.commands.ReloadCommand;
import com.greenjon902.beaconbeamhider.commands.ShowBeaconsCommand;
import com.greenjon902.beaconbeamhider.commands.ToggleBeaconHidingCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class BeaconBeamHider extends JavaPlugin {
    private PlayerExcludeList playerExcludeList;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            Config config = new Config();
            config.loadFrom(new File(getDataFolder(), "config.yml"));

            playerExcludeList = new PlayerExcludeList();
            playerExcludeList.loadFrom(new File(getDataFolder(), "playerExcludeList.txt"));

            getServer().getPluginManager().registerEvents(new EventListener(config, playerExcludeList), this);
            this.getCommand("showbeacons").setExecutor(new ShowBeaconsCommand(this, config, playerExcludeList));
            this.getCommand("hidebeacons").setExecutor(new HideBeaconsCommand(this, config, playerExcludeList));
            this.getCommand("togglebeaconhiding").setExecutor(new ToggleBeaconHidingCommand(this, config, playerExcludeList));
            this.getCommand("reload").setExecutor(new ReloadCommand(this, config, playerExcludeList));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            playerExcludeList.saveTo(new File(getDataFolder(), "playerExcludeList.txt"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
