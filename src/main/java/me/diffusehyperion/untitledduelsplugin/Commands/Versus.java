package me.diffusehyperion.untitledduelsplugin.Commands;

import me.diffusehyperion.untitledduelsplugin.Classes.Arena;
import me.diffusehyperion.untitledduelsplugin.Classes.DuelsPlayer;
import me.diffusehyperion.untitledduelsplugin.Classes.Fight;
import me.diffusehyperion.untitledduelsplugin.GUIs.VersusGUI;
import me.diffusehyperion.untitledduelsplugin.Classes.Kit;
import me.diffusehyperion.untitledduelsplugin.Utilities.PreConditions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.Utilities.DuelsPlayerListener.duelsPlayerMap;

// used for command '/1v1"
// if used as /1v1 (Player) (Arena) (Kit), directly send request

public class Versus implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!PreConditions.isPlayerType(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + "You can only run this command as a player!");
            return true;
        }

        Player p = (Player) commandSender;
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "/1v1 (player)");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (Objects.isNull(targetPlayer)) {
            commandSender.sendMessage(ChatColor.RED + args[0] + " is not online!");
            return true;
        }

        if (targetPlayer.equals(p)) {
            p.sendMessage(ChatColor.RED + "You cannot fight yourself!");
            return true;
        }

        if (Arena.arenaList.isEmpty()) {
            p.sendMessage(ChatColor.RED + "There are no available arena to fight in!");
            p.sendMessage(ChatColor.RED + "Contact the server owner.");
            return true;
        }

        if (Kit.kitList.isEmpty()) {
            p.sendMessage(ChatColor.RED + "There are no available kits to use!");
            p.sendMessage(ChatColor.RED + "Contact the server owner.");
            return true;
        }

        DuelsPlayer targetDuelsPlayer = duelsPlayerMap.get(targetPlayer);
        if (Objects.isNull(targetDuelsPlayer)) {
            p.sendMessage(ChatColor.RED + "Unable to find DuelsPlayer in duelsPlayerMap!");
            p.sendMessage(ChatColor.RED + "This is a bug, please report it to the creator!");
            return true;
        }


        if (Objects.nonNull(duelsPlayerMap.get(p).getFightingPlayer())) {
            p.sendMessage(ChatColor.RED + "You are already in a fight!");
            return true;
        }


        if (Objects.equals(targetDuelsPlayer.getFightingPlayer(), p)) {
            // accpeted fight
            duelsPlayerMap.get(p).setFightingPlayer(targetPlayer);
            try {
                new Fight(p, targetPlayer, new Arena(args[1]), new Kit(args[2])).startFight();
            } catch (Exception ignored) {}
        } else {
            if (Objects.equals(args[0], "deny")) {
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "/1v1 deny (player)");
                    return true;
                }
                String tpName = args[1];
                Player tp = Bukkit.getPlayer(tpName);
                if (Objects.isNull(tp)) {
                    p.sendMessage(ChatColor.RED + tpName + " is not online!");
                    return true;
                }
                DuelsPlayer duelsTargetPlayer = duelsPlayerMap.get(tp);
                if (duelsTargetPlayer.getFightingPlayer() != p) {
                    p.sendMessage(ChatColor.RED + "There was no duel request from " + tpName + "!");
                    return true;
                }
                duelsTargetPlayer.setFightingPlayer(null);
                tp.sendMessage(ChatColor.RED + p.getDisplayName() + " rejected your duel request...");
            }
            new VersusGUI(targetPlayer).openInventory(p);
        }
        return true;
    }
}
