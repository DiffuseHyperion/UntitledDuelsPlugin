package me.diffusehyperion.untitledduelsplugin.Classes;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.config;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.plugin;

public class LobbyListener implements Listener {
    public static Team noCollisionTeam = null;

    public LobbyListener() {
        if (config.getBoolean("lobby.forcetp")) {
            if (Objects.isNull(new me.diffusehyperion.untitledduelsplugin.Classes.Location("lobby").toLocation())) {
                Bukkit.getLogger().severe("'lobby.forcetp' was set to true in config.yml, but no lobby was set up!");
            }
        }
        if (config.getBoolean("lobby.no_collision")) {
            Scoreboard sb = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
            noCollisionTeam = sb.registerNewTeam("nocollision");
            noCollisionTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            noCollisionTeam.setCanSeeFriendlyInvisibles(false);
            noCollisionTeam.setAllowFriendlyFire(true);
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        if (config.getBoolean("lobby.freezecam")) {
            Bukkit.getServer().getPluginManager().registerEvents(new LobbyFreezeCamListener(), plugin);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (config.getBoolean("lobby.forcetp")) {
            org.bukkit.Location lobby = new me.diffusehyperion.untitledduelsplugin.Classes.Location("lobby").toLocation();
            if (Objects.isNull(lobby)) {
                Bukkit.getLogger().severe("'lobby.forcetp' was set to true in config.yml, but no lobby was set up!");
            } else {
                e.getPlayer().teleport(lobby);
            }
        }
        if (config.getBoolean("lobby.blindness")) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, true, false));
        }
        if (config.getBoolean("lobby.invisibility")) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, false));
        }
        if (config.getBoolean("lobby.no_collision")) {
            noCollisionTeam.addEntry(e.getPlayer().getDisplayName());
        }
    }

    public static class LobbyFreezeCamListener implements Listener {
        @EventHandler
        public void onPlayerMove(PlayerMoveEvent e) {
            org.bukkit.Location lobby = new me.diffusehyperion.untitledduelsplugin.Classes.Location("lobby").toLocation();
            if (Objects.isNull(lobby)) {
                Bukkit.getLogger().severe("'lobby.freezecam' was set to true in config.yml, but no lobby was set up!");
            } else {
                e.getPlayer().teleport(lobby);
            }
        }
    }
}
