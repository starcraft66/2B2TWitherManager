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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TwoBuildersTwoToolsWitherManager extends JavaPlugin implements Listener{

    File configFile;
    FileConfiguration config;

    @Override
    public void onEnable() {

        configFile = new File(getDataFolder(), "config.yml");

        try {
            firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
        config = new YamlConfiguration();
        loadYamls();

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

    private void firstRun() throws Exception {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            copy(getResource("config.yml"), configFile);
        }
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte [1024];
            int len;
            while ((len = in.read(buf))>0) {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadYamls() {
        try {
            config.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
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
