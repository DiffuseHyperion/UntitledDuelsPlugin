package me.diffusehyperion.untitledduelsplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;

public class Arena {

    public static List<Arena> arenaList = new ArrayList<>();

    private final String name;
    private final String owner;
    private final World world;
    private final Location spawn1;
    private final Location spawn2;

    public Arena(String name, String owner, World world, Location spawn1, Location spawn2) throws Exception {
        this.name = name;
        if (!Objects.equals(spawn1.getWorld(), world)) {
            throw new Exception(name + "'s spawn 1 has a different world than the arena world!");
        }
        if (!Objects.equals(spawn2.getWorld(), world)) {
            throw new Exception(name + "'s spawn 2 has a different world than the arena world!");
        }
        this.owner = owner;
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

        this.world = Bukkit.getWorld(name);
        /*
        if (Objects.isNull(world)) {
            throw new Exception("Could not find an world named " + name + " in the server's root directory!");
        }
         */
        this.owner = data.getString(dataName + ".owner");
        this.spawn1 = new Location(world,
                data.getInt(dataName + ".spawn1.x"),
                data.getInt(dataName + ".spawn1.y"),
                data.getInt(dataName + ".spawn1.z"),
                data.getInt(dataName + ".spawn1.pitch"),
                data.getInt(dataName + ".spawn1.yaw"));
        this.spawn2 = new Location(world,
                data.getInt(dataName + ".spawn2.x"),
                data.getInt(dataName + ".spawn2.y"),
                data.getInt(dataName + ".spawn2.z"),
                data.getInt(dataName + ".spawn2.pitch"),
                data.getInt(dataName + ".spawn2.yaw"));
        // i know there is data.getLocation but this is cleaner in data.yml lol
    }

    public static void initList() {
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

    public String getOwner() {
        return owner;
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
