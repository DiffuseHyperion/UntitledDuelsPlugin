package me.diffusehyperion.untitledduelsplugin;

import me.diffusehyperion.untitledduelsplugin.Classes.Arena;
import me.diffusehyperion.untitledduelsplugin.Classes.Kit;
import me.diffusehyperion.untitledduelsplugin.Commands.*;
import me.diffusehyperion.untitledduelsplugin.Utilities.DuelsPlayerListener;
import me.diffusehyperion.untitledduelsplugin.Utilities.EmptyChunkGenerator;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.Classes.LobbyListener.noCollisionTeam;

public final class UntitledDuelsPlugin extends JavaPlugin implements Listener {

    public static Plugin plugin;
    public static FileConfiguration data;
    public static File dataFile;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        initConfig();
        Arena.initList();
        Kit.initList();
        Objects.requireNonNull(getCommand("arenas")).setExecutor(new Arenas());
        Objects.requireNonNull(getCommand("stats")).setExecutor(new Stats());
        Objects.requireNonNull(getCommand("1v1")).setExecutor(new Versus());
        Objects.requireNonNull(getCommand("kits")).setExecutor(new Kits());
        Objects.requireNonNull(getCommand("lobby")).setExecutor(new Lobby());
        getServer().getPluginManager().registerEvents(new DuelsPlayerListener(), this);
    }

    @Override
    public void onDisable() {
        if (Objects.nonNull(noCollisionTeam)) {
            noCollisionTeam.unregister();
        }
    }

    private void initConfig() {
        plugin = this;
        this.saveDefaultConfig();
        config = this.getConfig();
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
        File worldDir = world.getWorldFolder();
        try {
            FileUtils.copyDirectory(worldDir, new File(worldDir.getParent(), name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File uid = new File(worldDir.getParent() + File.separator + name + File.separator + "uid.dat");
        uid.delete();

        WorldCreator wc = new WorldCreator(name);

        return wc.createWorld();
    }
}
