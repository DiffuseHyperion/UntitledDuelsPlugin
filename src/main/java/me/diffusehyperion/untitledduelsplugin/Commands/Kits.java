package me.diffusehyperion.untitledduelsplugin.Commands;

import me.diffusehyperion.untitledduelsplugin.Classes.Kit;
import me.diffusehyperion.untitledduelsplugin.Utilities.PreConditions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.saveData;

public class Kits implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!PreConditions.isPlayerType(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + "You can only run this command as a player!");
            return true;
        }

        Player p = (Player) commandSender;
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "/kits (create/delete/list)");
            return true;
        }

        switch (args[0]) {
            case "create": {
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "/kits create (name)");
                    return true;
                }
                String kitName = args[1];
                if (PreConditions.isKit(kitName)) {
                    p.sendMessage(ChatColor.RED + "There is already a kit called " + kitName + "!");
                    return true;
                }
                String dataName = "kits." + kitName;
                Inventory kit = p.getInventory();
                // 0 - 8 is hotbar, 9 - 35 is inv, 36 - 39 is armor, 40 is offhand
                for (int i = 0; i < 41; i++) {
                    ItemStack item = kit.getItem(i);
                    if (Objects.isNull(item)) {
                        continue;
                    }
                    data.set(dataName + "." + i, item);
                }
                data.set(dataName + ".owner", p.getDisplayName());
                saveData();
                try {
                    Kit.kitList.add(new Kit(kitName));
                } catch (Exception ignored) {}
                p.sendMessage(ChatColor.GREEN + "Done!");
                break;
            }
            case "delete": {
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "/kits delete (name)");
                    return true;
                }
                String kitName = args[1];
                if (!PreConditions.isKit(kitName)) {
                    p.sendMessage(ChatColor.RED + "There is no kit called " + kitName + "!");
                    return true;
                }
                String dataName = "kits." + kitName;
                data.set(dataName, null);
                saveData();
                p.sendMessage(ChatColor.GREEN + "Done!");
                break;
            }
            case "list": {
                p.sendMessage(ChatColor.GREEN + "Available kits:");
                for (String kitName : Objects.requireNonNull(data.getConfigurationSection("kits")).getKeys(false)) {
                    p.sendMessage(kitName);
                }
                break;
            }
            default: {
                p.sendMessage(ChatColor.RED + "/kits (create/delete/list)");
                break;
            }
        }
        return true;
    }
}
