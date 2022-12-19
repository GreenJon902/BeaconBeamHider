package com.greenjon902.beaconbeamhider;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

/**
 * If the player is excluded then beacons are not hidden for them.
 */
public class PlayerExcludeList {
    private ArrayList<UUID> excluded = new ArrayList<>();

    public boolean isPlayerExcluded(OfflinePlayer offlinePlayer) {
        return excluded.contains(offlinePlayer.getUniqueId());
    }

    /**
     * Load list from a file.
     */
    public void loadFrom(File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        excluded.clear();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            excluded.add(UUID.fromString(scanner.nextLine()));
        }
    }

    public void saveTo(File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        for (UUID uuid : excluded) {
            fileWriter.write(uuid.toString());
            fileWriter.write("\n");
        }
        fileWriter.close();
    }

    public void exclude(Player player) {
        if (!excluded.contains(player.getUniqueId())) {
            excluded.add(player.getUniqueId());
        }
    }

    public void un_exclude(Player player) {
        excluded.remove(player.getUniqueId());
    }
}
