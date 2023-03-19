package me.diffusehyperion.untitledduelsplugin.Classes;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;

public class Kit {

    public static List<Kit> kitList = new ArrayList<>();

    private final String name;
    private final String owner;

    private final Inventory kit;

    public Kit(String name, String owner, Inventory kit){
        this.name = name;
        this.owner = owner;
        this.kit = kit;
    }

    public Kit(String name) throws Exception {
        this.name = name;
        String dataName = "kits." + name;
        if (!data.contains(dataName)) {
            throw new Exception("Could not find an arena named " + name + " in data.yml!");
        }

        this.owner = data.getString(dataName + ".owner");

        this.kit = Bukkit.createInventory(null, InventoryType.PLAYER, name);
        for (String index : Objects.requireNonNull(data.getConfigurationSection(dataName)).getKeys(false)) {
            if (Objects.equals(index, "owner")) { // lmao ok
                continue;
            }
            kit.setItem(Integer.parseInt(index), data.getItemStack(dataName + "." + index));
        }
    }

    public static void initList() {
        if (Objects.isNull(data.getConfigurationSection("kits"))) {
            return;
        }
        for (String kitName : Objects.requireNonNull(data.getConfigurationSection("kits")).getKeys(false)) {
            try {
                kitList.add(new Kit(kitName));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public Inventory getKit() {
        return kit;
    }
}
