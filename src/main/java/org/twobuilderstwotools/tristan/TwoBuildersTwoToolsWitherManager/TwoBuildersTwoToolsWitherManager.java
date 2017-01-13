package org.twobuilderstwotools.tristan.TwoBuildersTwoToolsWitherManager;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import java.nio.file.Files;

public class TwoBuildersTwoToolsWitherManager extends JavaPlugin implements Listener{

    File configFile;
    FileConfiguration config;

    @Override
    public void onEnable() {

        configFile = new File(getDataFolder(), "config.yml");

        try {
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                Files.copy(getResource("config.yml"), configFile.toPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        config = new YamlConfiguration();
        
        try {
            config.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        if (e.getEntity().getType() == EntityType.WITHER) {
            Location loc = e.getLocation();
            if (!witherSpawningAllowed(e.getLocation().getX(), e.getLocation().getZ())) {
                e.setCancelled(true);
            }
        }
    }

    public boolean witherSpawningAllowed(double LocX, double LocZ) {
        int radius = config.getInt("protected-radius");
        if (LocX < 0){
            //Negative x
            if (LocZ < 0) {
                if (LocX > radius && LocZ > radius) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (LocX > radius && LocZ < radius) {
                    return false;
                } else {
                    return true;
                }
            }
        } else if (LocZ < 0) {
            //Negative z
            if (LocX < radius && LocZ > radius) {
                return false;
            } else{
                return true;
            }
        } else if (LocX > -1 && LocZ > -1) {
            //Both positive
            if (LocX < radius && LocZ < radius) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
