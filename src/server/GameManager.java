package server;

import java.io.IOException;
import java.util.*;
import model.*;

public class GameManager {
    private GameState state = new GameState();
    private Map<String, ClientHandler> clients = new HashMap<>();
    private List<BotPlayer> bots = new ArrayList<>();

    public synchronized void addPlayer(Player player, ClientHandler handler) throws IOException {
        state.addPlayer(player);
        clients.put(player.getName(), handler);
        broadcast(new Message("INFO", player.getName() + " joined!", "SERVER"));
        sendUserListToAll();
    }

    public synchronized void removeClient(String playerName) {
        clients.remove(playerName);
        state.getPlayers().remove(playerName);
        System.out.println("[SERVER] Removed player: " + playerName);
        try {
            sendUserListToAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addBot(BotPlayer bot) {
        Player p = new Player(bot.getName());
        state.addPlayer(p);
        bots.add(bot);
        System.out.println("[SERVER] Added bot: " + bot.getName());
    }

    public synchronized void startGame() throws IOException {
        broadcast(new Message("INFO", "Game Started with " + state.getPlayers().size() + " players!", "SERVER"));
        sendUserListToAll();
        updateAll();
        nextTurn();
    }

    public synchronized void handleMessage(Message msg, Player p) throws IOException {
        String type = msg.getType();

        if (type.equals(Message.ROLL) && p.getName().equals(state.getCurrentTurn())) {
            int dice = (int) (Math.random() * 6 + 1);
            broadcast(new Message("INFO", p.getName() + " rolled " + dice + " 🎲", "SERVER"));
            movePlayer(p.getName(), dice);
        } else if (type.equals(Message.PUBLIC_CHAT)) {
            broadcastChat(msg);
        } else if (type.equals(Message.PRIVATE_CHAT)) {
            sendPrivateChat(msg);
        }
    }

    public synchronized void botMove(String botName, int dice) throws IOException {
        if (botName.equals(state.getCurrentTurn())) {
            movePlayer(botName, dice);
        }
    }

    private void movePlayer(String name, int dice) throws IOException {
        boolean rolledSix = state.movePlayer(name, dice);
        broadcast(new Message("MOVE", name + " moved to position " + state.getPlayers().get(name).getPosition(), "SERVER"));
        updateAll();
        if (!rolledSix) nextTurn();
    }

    private void nextTurn() throws IOException {
        String current = state.getCurrentTurn();
        broadcast(new Message("INFO", "It's " + current + "'s turn!", "SERVER"));
        if (clients.containsKey(current)) {
            clients.get(current).sendMessage(new Message("YOUR_TURN", "", "SERVER"));
        }
    }

    public synchronized boolean isBotTurn(String name) {
        return name.equals(state.getCurrentTurn());
    }

    private void updateAll() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (var entry : state.getPlayers().entrySet()) {
            sb.append(entry.getKey()).append("=")
              .append(entry.getValue().getPosition()).append(",");
        }
        for (ClientHandler ch : clients.values()) {
            ch.sendMessage(new Message("STATE", sb.toString(), "SERVER"));
        }
    }

    public synchronized void broadcast(Message msg) {
        List<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, ClientHandler> entry : clients.entrySet()) {
            try {
                entry.getValue().sendMessage(msg);
            } catch (Exception e) {
                toRemove.add(entry.getKey());
            }
        }
        for (String name : toRemove) {
            removeClient(name);
        }
    }

    private void broadcastChat(Message chatMsg) {
        System.out.println("[CHAT] Public from " + chatMsg.getPlayerName() + ": " + chatMsg.getContent());
        for (ClientHandler ch : clients.values()) {
            try {
                ch.sendMessage(chatMsg);
            } catch (Exception e) {
                System.err.println("[CHAT] Failed to send to client: " + e.getMessage());
            }
        }
    }

    private void sendPrivateChat(Message chatMsg) {
        String recipient = chatMsg.getRecipient();
        String sender = chatMsg.getPlayerName();
        System.out.println("[CHAT] Private from " + sender + " to " + recipient + ": " + chatMsg.getContent());
        ClientHandler recipientHandler = clients.get(recipient);
        if (recipientHandler != null) {
            try {
                recipientHandler.sendMessage(chatMsg);
            } catch (Exception e) {
                System.err.println("[CHAT] Failed to send to recipient: " + e.getMessage());
            }
        }
        ClientHandler senderHandler = clients.get(sender);
        if (senderHandler != null) {
            try {
                senderHandler.sendMessage(chatMsg);
            } catch (Exception e) {
                System.err.println("[CHAT] Failed to send to sender: " + e.getMessage());
            }
        }
    }

    private void sendUserListToAll() throws IOException {
        String[] userNames = clients.keySet().toArray(new String[0]);
        Message userListMsg = new Message(Message.USER_LIST, "", "SERVER");
        userListMsg.setUserList(userNames);
        for (ClientHandler ch : clients.values()) {
            try {
                ch.sendMessage(userListMsg);
            } catch (Exception e) {
                System.err.println("[SERVER] Failed to send user list: " + e.getMessage());
            }
        }
    }
}
