package sk.uniba.fmph.dcs;

import interfaces.*;

import java.util.ArrayList;

public final class Game implements GameInterface {
    private final BagInterface bag;
    private final TableAreaInterface tableArea;
    private final GameObserverInterface gameObserver;
    private final ArrayList<BoardInterface> boards;
    private int playerId;
    private int nextStartingPlayer;
    String gameState = "null";

    public Game(BagInterface bag, TableAreaInterface tableArea, GameObserverInterface gameObserver, ArrayList<BoardInterface> board){
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
    public boolean take(int playerId, int sourceId, int idx, int destinationIdx) {
        if(this.playerId != playerId) return false;
        ArrayList<Tile> tiles;
        try {
            tiles = tableArea.take(sourceId, idx);
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }

        if(tiles.contains(Tile.STARTING_PLAYER)) nextStartingPlayer = playerId;

        boards.get(playerId).put(destinationIdx, tiles);
        this.playerId = (this.playerId+1)%boards.size();
        if(!tableArea.isRoundEnd()) return true;

        FinishRoundResult f = FinishRoundResult.NORMAL;
        for (BoardInterface board : boards) if(board.finishRound() == FinishRoundResult.GAME_FINISHED) f = FinishRoundResult.GAME_FINISHED;

        if(f == FinishRoundResult.GAME_FINISHED) {
            for(BoardInterface board : boards) board.endGame();
            gameObserver.notifyEverybody("Game ended;");
            gameState = "ended";
        }
        else {
            gameObserver.notifyEverybody("New round started.");
            tableArea.startNewRound();
            this.playerId = nextStartingPlayer;
        }
        return true;
    }

    public int getCurrentPlayerId() {return playerId;}
    public int getNextStartingPlayer() {return nextStartingPlayer;}
    public String state() {return gameState;}
}