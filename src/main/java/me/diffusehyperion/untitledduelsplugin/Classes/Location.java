package me.diffusehyperion.untitledduelsplugin.Classes;

import org.bukkit.Bukkit;

import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;

public class Location {
    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float pitch;
    private final float yaw;

    public Location(String world, double x, double y, double z, float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Location(String dataName) {
        this.world = data.getString(dataName + ".world");
        this.x = data.getDouble(dataName + ".x");
        this.y = data.getDouble(dataName + ".y");
        this.z = data.getDouble(dataName + ".z");
        this.pitch = (float) data.getDouble(dataName + ".pitch");
        this.yaw = (float) data.getDouble(dataName + ".yaw");
    }


    public org.bukkit.Location toLocation() {
        return new org.bukkit.Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
    }

    public static void setLocation(String dataName, org.bukkit.Location loc) {
        data.set(dataName + ".world", Objects.requireNonNull(loc.getWorld()).getName());
        data.set(dataName + ".x", loc.getX());
        data.set(dataName + ".y", loc.getY());
        data.set(dataName + ".z", loc.getZ());
        data.set(dataName + ".pitch", loc.getPitch());
        data.set(dataName + ".yaw", loc.getYaw());

    }
}
    // made this because i couldnt load locations in data.yml if their worlds are not loaded, but i couldnt load the worlds without loading data.yml...


