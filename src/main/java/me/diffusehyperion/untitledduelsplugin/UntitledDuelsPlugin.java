package me.diffusehyperion.untitledduelsplugin;

import me.diffusehyperion.untitledduelsplugin.Classes.Arena;
import me.diffusehyperion.untitledduelsplugin.Classes.Kit;
import me.diffusehyperion.untitledduelsplugin.Commands.Arenas;
import me.diffusehyperion.untitledduelsplugin.Commands.Kits;
import me.diffusehyperion.untitledduelsplugin.Commands.Stats;
import me.diffusehyperion.untitledduelsplugin.Commands.Versus;
import me.diffusehyperion.untitledduelsplugin.Utilities.DuelsPlayerListener;
import me.diffusehyperion.untitledduelsplugin.Utilities.EmptyChunkGenerator;
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
import java.util.Objects;

public final class UntitledDuelsPlugin extends JavaPlugin {

    public static Plugin plugin;
    public static FileConfiguration data;
    public static File dataFile;

    @Override
    public void onEnable() {
        initConfig();
        Arena.initList();
        Kit.initList();
        Objects.requireNonNull(getCommand("arenas")).setExecutor(new Arenas());
        Objects.requireNonNull(getCommand("stats")).setExecutor(new Stats());
        Objects.requireNonNull(getCommand("1v1")).setExecutor(new Versus());
        Objects.requireNonNull(getCommand("kits")).setExecutor(new Kits());
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
