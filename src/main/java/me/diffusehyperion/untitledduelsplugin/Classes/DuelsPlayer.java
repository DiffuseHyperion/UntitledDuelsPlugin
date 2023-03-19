package me.diffusehyperion.untitledduelsplugin.Classes;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;
import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.saveData;

public class DuelsPlayer {
    private int wins;
    private int loses;

    /**
     * The player this player is or will be fighting, if there is any.
     */
    private Player fightingPlayer;

    private final OfflinePlayer player;

    public DuelsPlayer(OfflinePlayer player) {
        this.player = player;
        this.wins = 0;
        this.loses = 0;
    }

    public DuelsPlayer(OfflinePlayer player, int wins, int loses) {
        this.player = player;
        this.wins = wins;
        this.loses = loses;
    }

    public DuelsPlayer(String playerName) throws Exception {
        String dataPlayer = "players." + playerName;
        if (!data.contains(dataPlayer)) {
            throw new Exception("Could not find " + playerName + " in data.yml!");
        }
        player = Bukkit.getOfflinePlayer(playerName); // dont really care if player names are not unique lol
        wins = data.getInt(dataPlayer + ".wins");
        loses = data.getInt(dataPlayer + ".loses");
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public Player getFightingPlayer() {
        return fightingPlayer;
    }

    public void setFightingPlayer(Player player) {
        this.fightingPlayer = player;
    }

    /**
     * Saves wins and loses to data.yml.
     */
    public void saveStats() {
        data.set("players." + player.getName() + ".wins", wins);
        data.set("players." + player.getName() + ".loses", loses);
        saveData();
    }


}
