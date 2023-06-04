package me.diffusehyperion.untitledduelsplugin.Utilities;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;

public class PreConditions {

    public static boolean isAllowed(String arenaName, Player player) {
        return data.getStringList("arenas." + arenaName + ".allowedPlayers").contains(player.getDisplayName());
    }

    public static boolean isArena(String arenaName) {
        return data.contains("arenas." + arenaName);
    }

    public static boolean isPlayer(String playerName) {
        return data.contains("players." + playerName);
    }

    public static boolean isPlayerType(CommandSender sender) {
        return sender instanceof Player;
    }

    public static boolean isKit(String kitName) {
        return data.contains("kits." + kitName);
    }
}
