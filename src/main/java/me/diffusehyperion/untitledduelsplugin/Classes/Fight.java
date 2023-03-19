package me.diffusehyperion.untitledduelsplugin.Classes;

import org.bukkit.entity.Player;

public class Fight {
    private final Player player1;
    private final Player player2;

    private final Arena arena;

    private final Kit kit;

    public Fight(Player player1, Player player2, Arena arena, Kit kit) {
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
        this.kit = kit;
    }

    public void startFight() {
        player1.teleport(arena.getSpawn1());
        player2.teleport(arena.getSpawn2());

        player1.getInventory().setContents(kit.getKit().getContents());
        player2.getInventory().setContents(kit.getKit().getContents());
    }
}
