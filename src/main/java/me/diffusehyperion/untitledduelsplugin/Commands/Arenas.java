package me.diffusehyperion.untitledduelsplugin.Commands;

import me.diffusehyperion.untitledduelsplugin.Classes.Arena;
import me.diffusehyperion.untitledduelsplugin.Utilities.PreConditions;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.Classes.Location.setLocation;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.*;

public class Arenas implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!PreConditions.isPlayerType(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + "You can only run this command as a player!");
            return true;
        }
        Player p = (Player) commandSender;

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "/arena (create/join/set/add)");
            return true;
        }
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
                List<String> allowedPlayers = new ArrayList<>();
                allowedPlayers.add(p.getDisplayName());
                data.set(dataName + ".allowedPlayers", allowedPlayers);
                setLocation(dataName + ".spawn1", world.getSpawnLocation());
                setLocation(dataName + ".spawn2", world.getSpawnLocation());
                saveData();

                p.sendMessage(ChatColor.GREEN + "Done! Rejoin the world with /arena join " + arenaName);
                return true;
            }
            case "join": {
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "/arena join (name)");
                    return true;
                }
                String arenaName = args[1];

                if (!PreConditions.isArena(arenaName)) {
                    p.sendMessage(ChatColor.RED + "There is no arena called " + arenaName + "!");
                    return true;
                }

                if (!PreConditions.isAllowed(arenaName, p)) {
                    p.sendMessage(ChatColor.RED + "You dont have permissions to join this arena!");
                    return true;
                }
                World world = Bukkit.getWorld(arenaName);
                if (Objects.isNull(world)) {
                    p.sendMessage(ChatColor.RED + "Could not find world named " + arenaName + "!");
                    p.sendMessage(ChatColor.RED + "This is a bug, please report it to the creator!");
                    return true;
                }
                p.teleport(new Location(world, 0.5, 1, 0.5));
                p.setGameMode(GameMode.CREATIVE);
                p.sendMessage(ChatColor.GREEN + "Done!");
                return true;
            }
            case "set": {
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
                String dataName = "arenas." + arenaName;
                if (!PreConditions.isArena(arenaName)) {
                    p.sendMessage(ChatColor.RED + "You are not in an arena world!");
                    return true;
                }
                if (!PreConditions.isAllowed(arenaName, p)) {
                    p.sendMessage(ChatColor.RED + "You dont have permissions to join this arena!");
                    return true;
                }

                String spawnName;
                if (spawn == 1) {
                    spawnName = dataName + ".spawn1";
                } else {
                    spawnName = dataName + ".spawn2";
                }
                setLocation(spawnName, p.getLocation());
                // reconstruct a location when it needs to be used

                saveData();
                try {
                    Arena.arenaList.add(new Arena(arenaName));
                } catch (Exception ignored) {}
                p.sendMessage(ChatColor.GREEN + "Spawn point " + spawn + " has been set!");
                return true;
            }
            case "add": {
                if (args.length != 3) {
                    p.sendMessage(ChatColor.RED + "/arena add (arena name) (player name)");
                    return true;
                }
                String arenaName = args[1];
                String playerName = args[2];

                if (!PreConditions.isArena(arenaName)) {
                    p.sendMessage(ChatColor.RED + "There is no arena called " + arenaName + "!");
                    return true;
                }

                if (!PreConditions.isAllowed(arenaName, p)) {
                    p.sendMessage(ChatColor.RED + "You don't have permissions to invite players to this arena!");
                    return true;
                }
                String dataName = "arenas." + arenaName + ".allowedPlayers";
                List<String> allowedPlayers = data.getStringList(dataName);
                allowedPlayers.add(playerName);
                data.set(dataName, allowedPlayers);
                saveData();

                p.sendMessage(ChatColor.GREEN + "Invited " + playerName + " to arena " + arenaName + "!");
                return true;
            }

            default: {
                p.sendMessage(ChatColor.RED + "/arena (create/join/set/add)");
                return true;
            }
        }
    }
}
