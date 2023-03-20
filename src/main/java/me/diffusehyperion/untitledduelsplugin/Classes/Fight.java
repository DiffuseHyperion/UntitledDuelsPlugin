package me.diffusehyperion.untitledduelsplugin.Classes;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.UUID;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.copyWorld;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.plugin;
import static me.diffusehyperion.untitledduelsplugin.Utilities.DuelsPlayerListener.duelsPlayerMap;

public class Fight implements Listener {
    private final Player player1;
    private final Player player2;

    private Location originalPlayer1Loc;
    private Location originalPlayer2Loc;
    private Inventory originalPlayer1Inv;
    private Inventory originalPlayer2Inv;

    private GameMode originalPlayer1GM;
    private GameMode originalPlayer2GM;
    private double originalPlayer1HP;
    private double originalPlayer2HP;
    private float originalPlayer1SAT;
    private float originalPlayer2SAT;

    private Player winner;

    private final Arena arena;

    private final Kit kit;

    private boolean started;

    private World copiedWorld;

    public Fight(Player player1, Player player2, Arena arena, Kit kit) {
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
        this.kit = kit;
        this.started = false;
    }

    public void startFight() {
        copiedWorld = copyWorld(arena.getWorld(), String.valueOf(UUID.randomUUID()));

        Location tpLoc1 = arena.getSpawn1();
        tpLoc1.setWorld(copiedWorld);
        Location tpLoc2 = arena.getSpawn2();
        tpLoc2.setWorld(copiedWorld);

        originalPlayer1Loc = player1.getLocation();
        originalPlayer2Loc = player2.getLocation();
        player1.teleport(tpLoc1);
        player2.teleport(tpLoc2);

        originalPlayer1Inv = player1.getInventory();
        originalPlayer2Inv = player2.getInventory();
        player1.getInventory().setContents(kit.getKit().getContents());
        player2.getInventory().setContents(kit.getKit().getContents());

        originalPlayer1GM = player1.getGameMode();
        originalPlayer2GM = player2.getGameMode();
        player1.setGameMode(GameMode.SURVIVAL);
        player2.setGameMode(GameMode.SURVIVAL);

        originalPlayer1HP = player1.getHealth();
        originalPlayer2HP = player2.getHealth();
        player1.setHealth(20);
        player2.setHealth(20);

        originalPlayer1SAT = player1.getSaturation();
        originalPlayer2SAT = player2.getSaturation();
        player1.setSaturation(5);
        player2.setSaturation(5);

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // OMEGALUL
                    player1.sendTitle("3", null);
                    player2.sendTitle("3", null);
                    Thread.sleep(1000);
                    player1.sendTitle("2", null);
                    player2.sendTitle("2", null);
                    Thread.sleep(1000);
                    player1.sendTitle("1", null);
                    player2.sendTitle("1", null);
                    Thread.sleep(1000);
                    player1.sendTitle("START!", null, 20, 20, 20);
                    player2.sendTitle("START!", null, 20, 20, 20);

                    started = true;

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if ((e.getPlayer().equals(player1) || e.getPlayer().equals(player2)) && !started) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (started) {
            if (e.getEntity().equals(player2)) {
                // player 1 won
                winner = player1;
                Bukkit.broadcastMessage(ChatColor.YELLOW + player1.getDisplayName() + " won against " + player2.getDisplayName() + " with " + player1.getHealth() + " HP left!");
                duelsPlayerMap.get(player1).incrementWins();
                duelsPlayerMap.get(player2).incrementLoses();
            } else if (e.getEntity().equals(player1)) {
                // player 2 won
                winner = player2;
                Bukkit.broadcastMessage(ChatColor.YELLOW + player2.getDisplayName() + " won against " + player1.getDisplayName() + " with " + player2.getHealth() + " HP left!");
                duelsPlayerMap.get(player2).incrementWins();
                duelsPlayerMap.get(player1).incrementLoses();
            } else {
                return;
            }

            duelsPlayerMap.get(player1).saveStats();
            duelsPlayerMap.get(player2).saveStats();
            player1.teleport(originalPlayer1Loc);
            player2.teleport(originalPlayer2Loc);
            player1.getInventory().setContents(originalPlayer1Inv.getContents());
            player2.getInventory().setContents(originalPlayer2Inv.getContents());

            player1.setGameMode(originalPlayer1GM);
            player2.setGameMode(originalPlayer2GM);
            player1.setHealth(originalPlayer1HP);
            player2.setHealth(originalPlayer2HP);
            player1.setSaturation(originalPlayer1SAT);
            player2.setSaturation(originalPlayer2SAT);

            Bukkit.unloadWorld(copiedWorld, false);
            try {
                FileUtils.deleteDirectory(copiedWorld.getWorldFolder());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            HandlerList.unregisterAll(this);
        }
    }
}
