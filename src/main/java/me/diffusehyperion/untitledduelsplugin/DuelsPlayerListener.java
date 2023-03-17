package me.diffusehyperion.untitledduelsplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.saveData;

public class DuelsPlayerListener implements Listener {

    public static HashMap<Player, DuelsPlayer> duelsPlayerMap = new HashMap<>();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String pname = p.getDisplayName();
        String dataPlayer = "players." + pname;
        DuelsPlayer dp;
        try {
            dp = new DuelsPlayer(pname);
        } catch (Exception ex) {
            dp = new DuelsPlayer(p);
            data.createSection(dataPlayer);
            data.set(dataPlayer + ".wins", 0);
            data.set(dataPlayer + ".loses", 0);
            saveData();
        }
        duelsPlayerMap.put(p, dp);
    }
}
