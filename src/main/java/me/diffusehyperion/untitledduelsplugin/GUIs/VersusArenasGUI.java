package me.diffusehyperion.untitledduelsplugin.GUIs;

import me.diffusehyperion.untitledduelsplugin.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.plugin;

public class VersusArenasGUI implements Listener {
    private final Inventory inv;
    private final VersusGUI gui;

    public VersusArenasGUI(VersusGUI gui) {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 9, "Select Arena");
        this.gui = gui;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        // Put the items into the inventory
        initializeItems();
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        inv.clear();

        for (String arenaName : Objects.requireNonNull(data.getConfigurationSection("arenas")).getKeys(false)) {
            String owner = data.getString("arenas." + arenaName + ".owner");
            inv.addItem(createSkullItem(arenaName, owner, "Created by "+ owner));
        }
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createSkullItem(final String name, final String owner, final String... lore) {
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        final SkullMeta meta = (SkullMeta) item.getItemMeta();

        // Set the name of the item
        assert meta != null;
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));
        meta.setOwnerProfile(Bukkit.getServer().createPlayerProfile(owner));
        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        initializeItems();
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
        String arenaName = Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName();
        try {
            gui.setArena(new Arena(arenaName));
            gui.initializeItems();
            p.closeInventory();
            gui.openInventory(p);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}
