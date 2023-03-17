package me.diffusehyperion.untitledduelsplugin;

import org.bukkit.entity.Player;

import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;

public class PreConditions {

    public static boolean isOwner(String arenaName, Player player) {
        return Objects.equals(data.getString("arenas." + arenaName + ".owner"), player.getDisplayName());
    }

    public static boolean isArena(String arenaName) {
        return data.contains("arenas." + arenaName);
    }

    public static boolean isPlayer(String playerName) {
        return data.contains("players." + playerName);
    }
}