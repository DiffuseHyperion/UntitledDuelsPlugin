package me.diffusehyperion.untitledduelsplugin.GUIs;

import me.diffusehyperion.untitledduelsplugin.Classes.Kit;
import org.bukkit.Bukkit;
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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.plugin;

public class VersusKitsGUI implements Listener {
    private final Inventory inv;
    private final VersusGUI gui;
    public VersusKitsGUI(VersusGUI gui) {
        inv = Bukkit.createInventory(null, 9, "Select Kit");
        this.gui = gui;

        // Put the items into the inventory
        initializeItems();
    }

    public void initializeItems() {
        inv.clear();

        for (String kitName : Objects.requireNonNull(data.getConfigurationSection("kits")).getKeys(false)) {
            String owner = data.getString("kits." + kitName + ".owner");
            inv.addItem(createSkullItem(kitName, owner, "Created by " + owner));
        }
    }

    protected ItemStack createSkullItem(final String name, final String owner, final String... lore) {
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        final SkullMeta meta = (SkullMeta) item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));
        meta.setOwnerProfile(Bukkit.getServer().createPlayerProfile(owner));
        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(final HumanEntity ent) {
        initializeItems();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        ent.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        String kitName = Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName();
        try {
            gui.setKit(new Kit(kitName));
            gui.initializeItems();
            p.closeInventory();
            gui.openInventory(p);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

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
}
