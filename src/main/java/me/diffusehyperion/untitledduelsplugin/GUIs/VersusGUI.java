package me.diffusehyperion.untitledduelsplugin.GUIs;

import me.diffusehyperion.untitledduelsplugin.Classes.Arena;
import me.diffusehyperion.untitledduelsplugin.Classes.DuelsPlayer;
import me.diffusehyperion.untitledduelsplugin.Classes.Kit;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static me.diffusehyperion.untitledduelsplugin.Utilities.DuelsPlayerListener.duelsPlayerMap;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.plugin;

public class VersusGUI implements Listener {
    protected final Inventory inv;
    protected final Player duelingPlayer;

    protected Arena arena;
    protected Kit kit;

    public VersusArenasGUI versusArenasGUI = new VersusArenasGUI(this);
    public VersusKitsGUI versusKitsGUI = new VersusKitsGUI(this);

    public VersusGUI(Player duelingPlayer) {
        this.duelingPlayer = duelingPlayer;
        this.arena = null;
        this.kit = null;
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 27, "Dueling " + duelingPlayer.getDisplayName());

        // Put the items into the inventory
        initializeItems();
    }

    public VersusGUI(Player duelingPlayer, Arena arena) {
        this.duelingPlayer = duelingPlayer;
        this.arena = arena;
        this.kit = null;
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 27, "Dueling " + duelingPlayer.getDisplayName());
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        // Put the items into the inventory
        initializeItems();
    }

    public VersusGUI(Player duelingPlayer, Kit kit) {
        this.duelingPlayer = duelingPlayer;
        this.arena = null;
        this.kit = kit;
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 27, "Dueling " + duelingPlayer.getDisplayName());
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        // Put the items into the inventory
        initializeItems();
    }

    public VersusGUI(Player duelingPlayer, Arena arena, Kit kit) {
        this.duelingPlayer = duelingPlayer;
        this.arena = arena;
        this.kit = kit;
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 27, "Dueling " + duelingPlayer.getDisplayName());
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        // Put the items into the inventory
        initializeItems();
    }




    // You can call this whenever you want to put the items in
    public void initializeItems() {
        for (int i = 0; i < 27; i++) {
            switch (i) {
                case 11: {
                    if (Objects.isNull(kit)) {
                        inv.setItem(i, createGuiItem(Material.DIAMOND_SWORD, "Select kit", "Selected kit: Random"));
                    } else {
                        inv.setItem(i, createGuiItem(Material.DIAMOND_SWORD, "Select kit", "Selected kit: " + kit.getName()));
                    }
                    break;
                }
                case 13: {
                    inv.setItem(i, createGuiItem(Material.PAPER, "Send Request", "Send a 1v1 request to " + duelingPlayer.getDisplayName() + "!"));
                    break;
                }
                case 15: {
                    if (Objects.isNull(arena)) {
                        inv.setItem(i, createGuiItem(Material.GRASS_BLOCK, "Select arena", "Selected arena: Random"));
                    } else {
                        inv.setItem(i, createGuiItem(Material.GRASS_BLOCK, "Select arena", "Selected arena: " + arena.getName()));
                    }
                    break;
                }
                default: {
                    inv.setItem(i, createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "));
                    break;
                }
            }
        }
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        assert meta != null;
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        ent.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        switch (e.getRawSlot()) {
            case 11: {
                versusKitsGUI.openInventory(p);
                break;
            }
            case 13: {
                Arena arena;
                Kit kit;
                // sending fight request
                if (Objects.isNull(this.arena)) {
                    arena = Arena.arenaList.get(new Random().nextInt(Arena.arenaList.size()));
                } else {
                    arena = this.arena;
                }
                if (Objects.isNull(this.kit)) {
                    kit = Kit.kitList.get((new Random().nextInt(Kit.kitList.size())));
                } else {
                    kit = this.kit;
                }
                DuelsPlayer senderDuelsPlayer = duelsPlayerMap.get(p);
                senderDuelsPlayer.setFightingPlayer(duelingPlayer);
                p.sendMessage(ChatColor.GREEN + "Sending duel request to " + duelingPlayer.getDisplayName() + "!");
                p.closeInventory();

                duelingPlayer.spigot().sendMessage(
                        new ComponentBuilder(p.getDisplayName() + " wants to 1v1 you!").color(net.md_5.bungee.api.ChatColor.RED).create());
                duelingPlayer.spigot().sendMessage(
                        new ComponentBuilder("Accept?       ").color(net.md_5.bungee.api.ChatColor.DARK_RED)
                                .append("[YES]").color(net.md_5.bungee.api.ChatColor.GREEN)
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/1v1 " + p.getDisplayName() + " " + arena.getName() + " " + kit.getName()))
                                .append("   ")
                                .append("[NO]").color(net.md_5.bungee.api.ChatColor.RED)
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kill DiffuseHyperion"))
                                .create());

                HandlerList.unregisterAll(this);
                HandlerList.unregisterAll(versusArenasGUI);
                HandlerList.unregisterAll(versusKitsGUI);
                break;
            }
            case 15: {
                versusArenasGUI.openInventory(p);
                break;
            }
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryLeave(InventoryCloseEvent e) {
        if (e.getInventory().equals(inv)) {
            HandlerList.unregisterAll(this);
        }
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }
}