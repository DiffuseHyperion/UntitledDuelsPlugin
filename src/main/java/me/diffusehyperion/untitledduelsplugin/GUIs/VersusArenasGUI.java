package me.diffusehyperion.untitledduelsplugin.GUIs;

import me.diffusehyperion.untitledduelsplugin.Classes.Arena;
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

public class VersusArenasGUI implements Listener {
    private final Inventory inv;
    private final VersusGUI gui;

    public VersusArenasGUI(VersusGUI gui) {
        inv = Bukkit.createInventory(null, 9, "Select Arena");
        this.gui = gui;

        initializeItems();
    }

    public void initializeItems() {
        inv.clear();

        for (String arenaName : Objects.requireNonNull(data.getConfigurationSection("arenas")).getKeys(false)) {
            String owner = data.getString("arenas." + arenaName + ".owner");
            inv.addItem(createSkullItem(arenaName, owner, "Created by "+ owner));
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

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

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
