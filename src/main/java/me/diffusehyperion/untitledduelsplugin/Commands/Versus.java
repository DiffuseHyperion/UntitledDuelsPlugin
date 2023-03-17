package me.diffusehyperion.untitledduelsplugin.Commands;

import me.diffusehyperion.untitledduelsplugin.DuelsPlayer;
import me.diffusehyperion.untitledduelsplugin.PreConditions;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.DuelsPlayerListener.duelsPlayerMap;

// used for command '/1v1"

public class Versus implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!PreConditions.isPlayerType(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + "You can only run this command as a player!");
            return true;
        }

        Player p = (Player) commandSender;
        if (args.length != 1) {
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

        DuelsPlayer targetDuelsPlayer = duelsPlayerMap.get(targetPlayer);
        if (Objects.isNull(targetDuelsPlayer)) {
            p.sendMessage(ChatColor.RED + "Unable to find DuelsPlayer in duelsPlayerMap!");
            p.sendMessage(ChatColor.RED + "This is a bug, please report it to the creator!");
            return true;
        }

        if (Objects.equals(targetDuelsPlayer.getFightingPlayer(), p)) {
            // accpeted fight
            Bukkit.broadcastMessage("fighting rn lmao");
        } else {
            // sending fight request
            DuelsPlayer senderDuelsPlayer = duelsPlayerMap.get(p);
            senderDuelsPlayer.setFightingPlayer(targetPlayer);
            p.sendMessage(ChatColor.GREEN + "Sending duel request to " + targetPlayer.getDisplayName() + "!");

            targetPlayer.spigot().sendMessage(
                    new ComponentBuilder(p.getDisplayName() + " wants to 1v1 you!").color(net.md_5.bungee.api.ChatColor.RED).create());
            targetPlayer.spigot().sendMessage(
                    new ComponentBuilder("Accept?       ").color(net.md_5.bungee.api.ChatColor.DARK_RED)
                            .append("[YES]").color(net.md_5.bungee.api.ChatColor.GREEN)
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/1v1 " + p.getDisplayName()))
                            .append("   ")
                            .append("[NO]").color(net.md_5.bungee.api.ChatColor.RED)
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kill DiffuseHyperion"))
                            .create());
        }
        return true;
    }
}
