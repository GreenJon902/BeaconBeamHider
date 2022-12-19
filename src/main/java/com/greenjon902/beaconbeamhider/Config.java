package com.greenjon902.beaconbeamhider;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

public class Config {
    public int min_beacon_y = -70;
    public int max_beacon_y = 320;

    public boolean replace_iron = true;
    public boolean replace_gold = true;
    public boolean replace_emerald = true;
    public boolean replace_diamond = true;
    public boolean replace_netherite = true;

    public Material iron_replacement = Material.WHITE_CONCRETE;
    public Material gold_replacement = Material.YELLOW_CONCRETE;
    public Material emerald_replacement = Material.LIME_CONCRETE;
    public Material diamond_replacement = Material.LIGHT_BLUE_CONCRETE;
    public Material netherite_replacement = Material.GRAY_CONCRETE;

    /**
     * Load config from a file.
     */
    public void loadFrom(File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();

            URL inputUrl = getClass().getResource("/default_config.yml");
            FileUtils.copyURLToFile(inputUrl, file);
        }

        InputStream inputStream = Files.newInputStream(file.toPath());

        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);

        min_beacon_y = (int) data.getOrDefault("min_beacon_y", -64);
        max_beacon_y = (int) data.getOrDefault("max_beacon_y", 320);
        replace_iron = (boolean) data.getOrDefault("replace_iron", true);
        replace_gold = (boolean) data.getOrDefault("replace_gold", true);
        replace_emerald = (boolean) data.getOrDefault("replace_emerald", true);
        replace_diamond = (boolean) data.getOrDefault("replace_diamond", true);
        replace_netherite = (boolean) data.getOrDefault("replace_netherite", true);
        iron_replacement = Material.getMaterial((String) data.getOrDefault("iron_replacement", "WHITE_CONCRETE"));
        gold_replacement = Material.getMaterial((String) data.getOrDefault("gold_replacement", "YELLOW_CONCRETE"));
        emerald_replacement = Material.getMaterial((String) data.getOrDefault("emerald_replacement", "LIME_CONCRETE"));
        diamond_replacement = Material.getMaterial((String) data.getOrDefault("diamond_replacement", "LIGHT_BLUE_CONCRETE"));
        netherite_replacement = Material.getMaterial((String) data.getOrDefault("netherite_replacement", "GRAY_CONCRETE"));
    }


    public Material getCorrectBlockReplacement(Material from) {
        switch (from) {
            case IRON_BLOCK: return iron_replacement;
            case GOLD_BLOCK: return gold_replacement;
            case EMERALD_BLOCK: return emerald_replacement;
            case DIAMOND_BLOCK: return diamond_replacement;
            case NETHERITE_BLOCK: return netherite_replacement;
        }

        BeaconBeamHider beaconBeamHider = (BeaconBeamHider) Bukkit.getPluginManager().getPlugin("BeaconBeamHider");
        beaconBeamHider.getLogger().warning("Tried to replace " + from);
        return from;
    }
}
