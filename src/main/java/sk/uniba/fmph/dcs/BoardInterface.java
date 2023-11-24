package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public interface BoardInterface {
    void put(int destinationIdx, ArrayList<Tile> tiles);
    FinishRoundResult finishRound();
    void endGame();
    String state();
}
