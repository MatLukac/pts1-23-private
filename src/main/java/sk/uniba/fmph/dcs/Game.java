package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public class Game implements GameInterface{
    private final BagInterface bag;
    private final TableAreaInterface tableArea;
    private final ObserverInterface gameObserver;
    private final ArrayList<BoardInterface> boards;
    private int playerId;

    public Game(BagInterface bag, TableAreaInterface tableArea, ObserverInterface gameObserver, ArrayList<BoardInterface> board){
        this.bag = bag;
        this.tableArea = tableArea;
        this.gameObserver = gameObserver;
        this.boards = board;
        playerId = 0;
    }

    @Override
    public boolean take(int playerId, int sourceId, int idx, int destinationIdx) {
        if(this.playerId != playerId) return false;
        ArrayList<Tile> tiles = tableArea.take(sourceId, idx);
        if(tiles == null) return false;
        boards.get(playerId).put(destinationIdx, tiles);

        if(!tableArea.isRoundEnd()) return true;

        FinishRoundResult f = FinishRoundResult.NORMAL;
        for (BoardInterface board : boards) if(board.finishRound() == FinishRoundResult.GAME_FINISHED) f = FinishRoundResult.GAME_FINISHED;

        if(f == FinishRoundResult.GAME_FINISHED) {
            for(BoardInterface board : boards) board.endGame();
        }
        else tableArea.startNewRound();

        return true;
    }
}