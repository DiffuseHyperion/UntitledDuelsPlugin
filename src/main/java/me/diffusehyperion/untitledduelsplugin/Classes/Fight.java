package me.diffusehyperion.untitledduelsplugin.Classes;

import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.*;
import static me.diffusehyperion.untitledduelsplugin.Utilities.DuelsPlayerListener.duelsPlayerMap;

public class Fight implements Listener {
    private final Player player1;
    private final Player player2;

    private Location originalPlayer1Loc;
    private Location originalPlayer2Loc;
    // private Inventory originalPlayer1Inv;
    // private Inventory originalPlayer2Inv;

    private GameMode originalPlayer1GM;
    private GameMode originalPlayer2GM;
    private double originalPlayer1HP;
    private double originalPlayer2HP;

    private int originalPlayer1Hunger;
    private int originalPlayer2Hunger;
    private float originalPlayer1SAT;
    private float originalPlayer2SAT;

    private Player winner;

    private final Arena arena;

    private final Kit kit;

    private boolean started;

    private World copiedWorld;

    private final Location lobby;

    public Fight(Player player1, Player player2, Arena arena, Kit kit) {
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
        this.kit = kit;
        this.started = false;

        lobby = new me.diffusehyperion.untitledduelsplugin.Classes.Location("lobby").toLocation();
    }

    public void startFight() {
        copiedWorld = copyWorld(arena.getWorld(), String.valueOf(UUID.randomUUID()));

        Location tpLoc1 = arena.getSpawn1();
        Location tpLoc2 = arena.getSpawn2();
        tpLoc1.setWorld(copiedWorld);
        tpLoc2.setWorld(copiedWorld);
        originalPlayer1Loc = player1.getLocation();
        originalPlayer2Loc = player2.getLocation();
        // originalPlayer1Inv = player1.getInventory();
        // originalPlayer2Inv = player2.getInventory();
        originalPlayer1GM = player1.getGameMode();
        originalPlayer2GM = player2.getGameMode();
        originalPlayer1HP = player1.getHealth();
        originalPlayer2HP = player2.getHealth();
        originalPlayer1Hunger = player1.getFoodLevel();
        originalPlayer2Hunger = player2.getFoodLevel();
        originalPlayer1SAT = player1.getSaturation();
        originalPlayer2SAT = player2.getSaturation();

        player1.teleport(tpLoc1);
        player2.teleport(tpLoc2);

        player1.getInventory().setContents(kit.getKit().getContents());
        player2.getInventory().setContents(kit.getKit().getContents());

        player1.setGameMode(GameMode.SURVIVAL);
        player2.setGameMode(GameMode.SURVIVAL);

        player1.setHealth(20);
        player2.setHealth(20);

        player1.setFoodLevel(20);
        player2.setFoodLevel(20);

        player1.setSaturation(5);
        player2.setSaturation(5);

        duelsPlayerMap.get(player1).isFighting = true;
        duelsPlayerMap.get(player2).isFighting = true;

        if (config.getBoolean("lobby.blindness")) {
            player1.removePotionEffect(PotionEffectType.BLINDNESS);
            player2.removePotionEffect(PotionEffectType.BLINDNESS);
        }
        if (config.getBoolean("lobby.invisibility")) {
            player1.removePotionEffect(PotionEffectType.INVISIBILITY);
            player2.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

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
            if (!(e.getEntity().equals(player1) || e.getEntity().equals(player2))) {
                return;
            }

            DuelsPlayer duelsPlayer1 = duelsPlayerMap.get(player1);
            DuelsPlayer duelsPlayer2 = duelsPlayerMap.get(player2);
            assert player1 != null;
            assert player2 != null;
            player1.spigot().respawn();
            player2.spigot().respawn();

            duelsPlayer1.saveStats();
            duelsPlayer2.saveStats();

            duelsPlayer1.setFightingPlayer(null);
            duelsPlayer2.setFightingPlayer(null);

            duelsPlayer1.isFighting = false;
            duelsPlayer2.isFighting = false;

            if (Objects.nonNull(lobby)) {
                player1.teleport(lobby);
                player2.teleport(lobby);
            } else {
                player1.teleport(originalPlayer1Loc);
                player2.teleport(originalPlayer2Loc);
            }

            player1.getInventory().clear();
            player2.getInventory().clear();

            player1.setGameMode(originalPlayer1GM);
            player2.setGameMode(originalPlayer2GM);

            player1.setHealth(originalPlayer1HP);
            player2.setHealth(originalPlayer2HP);

            player1.setFoodLevel(originalPlayer1Hunger);
            player2.setFoodLevel(originalPlayer2Hunger);

            player1.setSaturation(originalPlayer1SAT);
            player2.setSaturation(originalPlayer2SAT);

            if (config.getBoolean("lobby.blindness")) {
                player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, true, false));
                player2.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, true, false));
            }
            if (config.getBoolean("lobby.invisibility")) {
                player1.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, false));
                player2.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, true, false));
            }

            if (e.getEntity().equals(player2)) {
                // player 1 won
                winner = player1;
                Bukkit.broadcastMessage(ChatColor.YELLOW + player1.getDisplayName() + " won against " + player2.getDisplayName() + " with " + player1.getHealth() + " HP left!");
                duelsPlayer1.incrementWins();
                duelsPlayer2.incrementLoses();
            } else if (e.getEntity().equals(player1)) {
                // player 2 won
                winner = player2;
                Bukkit.broadcastMessage(ChatColor.YELLOW + player2.getDisplayName() + " won against " + player1.getDisplayName() + " with " + player2.getHealth() + " HP left!");
                duelsPlayer2.incrementWins();
                duelsPlayer1.incrementLoses();
            }
            try {
                FileUtils.deleteDirectory(copiedWorld.getWorldFolder());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Bukkit.unloadWorld(copiedWorld, false);
            HandlerList.unregisterAll(this);
        }
    }
}
