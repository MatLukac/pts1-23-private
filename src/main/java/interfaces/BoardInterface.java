package interfaces;

import sk.uniba.fmph.dcs.FinishRoundResult;
import sk.uniba.fmph.dcs.Tile;

import java.util.ArrayList;

public interface BoardInterface {
    void put(int destinationIdx, ArrayList<Tile> tiles);
    FinishRoundResult finishRound();
    void endGame();
    String state();
}
