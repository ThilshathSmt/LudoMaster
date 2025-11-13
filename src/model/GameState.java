package model;

import java.io.Serializable;
import java.util.*;

public class GameState implements Serializable {
    private Map<String, Player> players = new LinkedHashMap<>();
    private String currentTurn;
    private static final int BOARD_SIZE = 52;

    public void addPlayer(Player p) {
        players.put(p.getName(), p);
        if (currentTurn == null) currentTurn = p.getName();
    }

    public synchronized boolean movePlayer(String playerName, int dice) {
        Player player = players.get(playerName);
        if (player == null) return false;

        int newPos = (player.getPosition() + dice) % BOARD_SIZE;
        player.setPosition(newPos);

        if (dice != 6) nextTurn();
        return dice == 6;
    }

    private void nextTurn() {
        List<String> keys = new ArrayList<>(players.keySet());
        int i = keys.indexOf(currentTurn);
        currentTurn = keys.get((i + 1) % keys.size());
    }

    public Map<String, Player> getPlayers() { return players; }
    public String getCurrentTurn() { return currentTurn; }
}
