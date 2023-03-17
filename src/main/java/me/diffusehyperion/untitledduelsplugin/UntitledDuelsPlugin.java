package me.diffusehyperion.untitledduelsplugin;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class UntitledDuelsPlugin extends JavaPlugin {

    public static Plugin plugin;
    public static FileConfiguration data;
    public static File dataFile;

    @Override
    public void onEnable() {
        initConfig();
        getCommand("arena").setExecutor(new Arena());
        getServer().getPluginManager().registerEvents(new DuelsPlayerListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initConfig() {
        plugin = this;
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("data.yml", false);
        }
        data = new YamlConfiguration();
        try {
            data.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

    }

    public static void saveData() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static World createVoidWorld(String name) {
        WorldCreator wc = new WorldCreator(name);

        wc.generator(new EmptyChunkGenerator());

        World w = wc.createWorld();
        assert w != null;
        w.getBlockAt(0, 0, 0).setType(Material.BEDROCK);
        return w;
    }

    public static World copyWorld(World world, String name) {
        WorldCreator wc = new WorldCreator(name);

        wc.copy(world);

        return wc.createWorld();
    }
}
