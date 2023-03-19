package me.diffusehyperion.untitledduelsplugin.Classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.plugin;

public class Fight implements Listener {
    private final Player player1;
    private final Player player2;

    private Location originalPlayer1Loc;
    private Location originalPlayer2Loc;
    private Inventory originalPlayer1Inv;
    private Inventory originalPlayer2Inv;

    private Player winner;

    private final Arena arena;

    private final Kit kit;

    private boolean started;

    public Fight(Player player1, Player player2, Arena arena, Kit kit) {
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
        this.kit = kit;
        this.started = false;
    }

    public void startFight() {
        originalPlayer1Loc = player1.getLocation();
        player1.teleport(arena.getSpawn1());
        originalPlayer2Loc = player2.getLocation();
        player2.teleport(arena.getSpawn2());

        originalPlayer1Inv = player1.getInventory();
        player1.getInventory().setContents(kit.getKit().getContents());
        originalPlayer2Inv = player2.getInventory();
        player2.getInventory().setContents(kit.getKit().getContents());

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
                Bukkit.broadcastMessage(player1.getDisplayName() + " won against " + player2.getDisplayName() + "!");
                ((DuelsPlayer) player1).incrementWins();
                ((DuelsPlayer) player2).incrementLoses();
            } else if (e.getEntity().equals(player1)) {
                // player 2 won
                winner = player2;
                Bukkit.broadcastMessage(player2.getDisplayName() + " won against " + player1.getDisplayName() + "!");
                ((DuelsPlayer) player2).incrementWins();
                ((DuelsPlayer) player1).incrementLoses();
            } else {
                return;
            }

            player1.teleport(originalPlayer1Loc);
            player2.teleport(originalPlayer2Loc);
            player1.getInventory().setContents(originalPlayer1Inv.getContents());
            player2.getInventory().setContents(originalPlayer2Inv.getContents());
        }
    }
}
