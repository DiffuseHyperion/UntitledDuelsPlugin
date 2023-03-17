package me.diffusehyperion.untitledduelsplugin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.*;

public class Arena implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        Player p = (Player) commandSender;

        switch (args[0]) {
            case "create": {
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "/arena create (name)");
                    return true;
                }
                String arenaName = args[1];
                String dataName = "arenas." + arenaName;

                if (PreConditions.isArena(arenaName)) {
                    p.sendMessage(ChatColor.RED + "There is already an arena called " + arenaName + "!");
                    return true;
                }

                p.sendMessage(ChatColor.GREEN + "Please wait...");
                World world = createVoidWorld(arenaName); // cant run this async or it will throw an error :(((((
                p.teleport(new Location(world, 0.5, 1, 0.5));
                p.setGameMode(GameMode.CREATIVE);

                data.createSection(dataName);
                data.set(dataName + ".owner", p.getDisplayName());
                saveData();

                p.sendMessage(ChatColor.GREEN + "Done! Rejoin the world with /arena join " + arenaName);
                break;
            }
            case "join": {
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "/arena join (name)");
                    return true;
                }
                String arenaName = args[1];

                if (!PreConditions.isArena(arenaName)) {
                    p.sendMessage(ChatColor.RED + "There is no arena called " + arenaName + "!");
                }

                if (!PreConditions.isOwner(arenaName, p)) {
                    p.sendMessage(ChatColor.RED + "You dont have permissions to join this arena!");
                    return true;
                }
                World world = Bukkit.getWorld(arenaName);
                p.teleport(new Location(world, 0.5, 1, 0.5));
                p.setGameMode(GameMode.CREATIVE);
                p.sendMessage(ChatColor.GREEN + "Done!");
                break;
            }
            case "set":
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "/arena set (1/2)");
                    return true;
                }
                int spawn;
                // lul
                switch (args[1]) {
                    case "1":
                        spawn = 1;
                        break;
                    case "2":
                        spawn = 2;
                        break;
                    default:
                        p.sendMessage(ChatColor.RED + "/arena set (1/2)");
                        return true;
                }

                World playerWorld = p.getWorld();
                String arenaName = playerWorld.getName();
                String dataName = "areans." + arenaName;
                if (!PreConditions.isArena(arenaName)) {
                    p.sendMessage(ChatColor.RED + "You are not in an arena world!");
                    return true;
                }
                if (!PreConditions.isOwner(arenaName, p)) {
                    p.sendMessage(ChatColor.RED + "You dont have permissions to join this arena!");
                    return true;
                }

                data.set(dataName + "." + spawn, p.getLocation());
                saveData();
                p.sendMessage(ChatColor.GREEN + "Spawn point " + spawn + "has been set!");
        }
        return true;
    }
}
