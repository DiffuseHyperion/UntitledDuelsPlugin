package me.diffusehyperion.untitledduelsplugin.Commands;

import me.diffusehyperion.untitledduelsplugin.PreConditions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;

public class Stats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 2) {
            commandSender.sendMessage(ChatColor.RED + "/stats (wins/loses) (player)");
            return true;
        }

        String targetStat = args[0];
        String targetPlayer = args[1];
        if (Objects.equals(targetStat, "wins")) {
            if (!PreConditions.isPlayer(targetPlayer))  {
                commandSender.sendMessage(ChatColor.RED + "This player has not joined the server before!");
                return true;
            }
            commandSender.sendMessage(ChatColor.GOLD + targetPlayer + " has " + data.getInt("players." + targetPlayer + ".wins") + " wins."); // didnt use duelsplayer here cuz annoying to integrate it lol
            return true;
        } else if (Objects.equals(targetStat, "loses")) {
            if (!PreConditions.isPlayer(targetPlayer)) {
                commandSender.sendMessage(ChatColor.RED + "This player has not joined the server before!");
                return true;
            }
            commandSender.sendMessage(ChatColor.GOLD + targetPlayer + " has " + data.getInt("players." + targetPlayer + ".loses") + " loses.");
        } else {
            commandSender.sendMessage(ChatColor.RED + "/stats (wins/loses) (player)");
            return true;
        }
        return true;
    }
}
