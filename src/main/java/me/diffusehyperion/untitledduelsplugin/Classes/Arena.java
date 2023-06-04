package me.diffusehyperion.untitledduelsplugin.Classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.createVoidWorld;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;

public class Arena {

    public static List<Arena> arenaList = new ArrayList<>();
    private final String name;
    private List<String> allowedPlayers = new ArrayList<>();
    private final World world;
    private Location spawn1;
    private Location spawn2;

    public Arena(String name, List<String> allowedPlayers, World world, Location spawn1, Location spawn2) throws Exception {
        this.name = name;
        if (!Objects.equals(spawn1.getWorld(), world)) {
            throw new Exception(name + "'s spawn 1 has a different world than the arena world!");
        }
        if (!Objects.equals(spawn2.getWorld(), world)) {
            throw new Exception(name + "'s spawn 2 has a different world than the arena world!");
        }
        this.allowedPlayers = allowedPlayers;
        this.world = world;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
    }

    public Arena(String name) throws Exception {
        this.name = name;
        String dataName = "arenas." + name;
        if (!data.contains(dataName)) {
            throw new Exception("Could not find an arena named " + name + " in data.yml!");
        }

        this.world = createVoidWorld(name);

        this.allowedPlayers = data.getStringList(dataName + ".allowedPlayers");
        try {
            this.spawn1 = new me.diffusehyperion.untitledduelsplugin.Classes.Location(dataName + ".spawn1").toLocation();
            this.spawn2 = new me.diffusehyperion.untitledduelsplugin.Classes.Location(dataName + ".spawn2").toLocation();
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Arena " + name + " doesn't have their spawns set! Skipping it for now.");
        }
        // i know there is data.getLocation but this is cleaner in data.yml lol
    }

    public static void initList() {
        if (Objects.isNull(data.getConfigurationSection("arenas"))) {
            return;
        }
        for (String arenaName : Objects.requireNonNull(data.getConfigurationSection("arenas")).getKeys(false)) {
            try {
                arenaList.add(new Arena(arenaName));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getAllowedPlayers() {
        return allowedPlayers;
    }

    public World getWorld() {
        return world;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }
}
