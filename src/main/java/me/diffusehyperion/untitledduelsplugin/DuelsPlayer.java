package me.diffusehyperion.untitledduelsplugin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import static me.diffusehyperion.untitledduelsplugin.UntitledDuelsPlugin.data;

public class DuelsPlayer {
    private int wins;
    private int loses;

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


}
