package me.diffusehyperion.untitledduelsplugin.Commands;

import me.diffusehyperion.untitledduelsplugin.Utilities.PreConditions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.saveData;

public class Lobby implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!PreConditions.isPlayerType(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + "You can only run this command as a player!");
            return true;
        }
        Player p = (Player) commandSender;

        if (args.length == 0) {
            Location lobby = data.getLocation("lobby");
            if (Objects.isNull(lobby)) {
                p.sendMessage(ChatColor.RED + "The lobby has not been setup yet! Do /lobby set to set the lobby.");
                return true;
            }
            p.teleport(lobby);
            p.sendMessage(ChatColor.GREEN + "Done!");
        } else {
            switch (args[0]) {
                case "set": {
                    data.set("lobby", p.getLocation());
                    saveData();
                    p.sendMessage(ChatColor.GREEN + "Done!");
                    break;
                }
                case "tp": {
                    Location lobby = data.getLocation("lobby");
                    if (Objects.isNull(lobby)) {
                        p.sendMessage(ChatColor.RED + "The lobby has not been setup yet! Do /lobby set to set the lobby.");
                        return true;
                    }
                    p.teleport(lobby);
                    p.sendMessage(ChatColor.GREEN + "Done!");
                    break;
                }
                default: {
                    p.sendMessage(ChatColor.RED + "/lobby (set/tp)");
                    break;
                }
            }
        }
        return true;
    }
}
