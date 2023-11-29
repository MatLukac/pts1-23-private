package sk.uniba.fmph.dcs;

import interfaces.BagInterface;
import interfaces.BoardInterface;
import interfaces.GameInterface;
import interfaces.GameObserverInterface;
import interfaces.TableAreaInterface;

import java.util.ArrayList;

public final class Game implements GameInterface {
    private final BagInterface bag;
    private final TableAreaInterface tableArea;
    private final GameObserverInterface gameObserver;
    private final ArrayList<BoardInterface> boards;
    private int playerId;
    private int nextStartingPlayer;
    private String gameState;

    public Game(final BagInterface bag, final TableAreaInterface tableArea, final GameObserverInterface gameObserver, final ArrayList<BoardInterface> board) {
        this.bag = bag;
        this.tableArea = tableArea;
        this.gameObserver = gameObserver;
        this.boards = board;
        playerId = 0;
        nextStartingPlayer = 0;
        gameState = "alive";
        gameObserver.notifyEverybody("Game started.");
    }

    @Override
    public boolean take(final int playerId, final int sourceId, final int idx, final int destinationIdx) {
        if (this.playerId != playerId) {
            return false;
        }
        ArrayList<Tile> tiles;
        try {
            tiles = tableArea.take(sourceId, idx);
        } catch (IllegalArgumentException e) {
            return false;
        }

        if (tiles.contains(Tile.STARTING_PLAYER)) {
            nextStartingPlayer = playerId;
        }

        boards.get(playerId).put(destinationIdx, tiles);
        this.playerId = (this.playerId + 1) % boards.size();
        if (!tableArea.isRoundEnd()) {
            return true;
        }

        FinishRoundResult f = FinishRoundResult.NORMAL;
        for (BoardInterface board : boards) {
            if (board.finishRound() == FinishRoundResult.GAME_FINISHED) {
                f = FinishRoundResult.GAME_FINISHED;
            }
        }

        if (f == FinishRoundResult.GAME_FINISHED) {
            for (BoardInterface board : boards) {
                board.endGame();
            }
            gameObserver.notifyEverybody("Game ended.");
            gameState = "ended";
        } else {
            gameObserver.notifyEverybody("New round started.");
            tableArea.startNewRound();
            this.playerId = nextStartingPlayer;
        }
        return true;
    }

    public int getCurrentPlayerId() {
        return playerId;
    }

    public int getNextStartingPlayer() {
        return nextStartingPlayer;
    }

    public String state() {
        return gameState;
    }
}
