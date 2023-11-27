package interfaces;

import sk.uniba.fmph.dcs.Tile;

import java.util.ArrayList;

public interface TableAreaInterface {
    ArrayList<Tile> take(int sourceIdx, int idx);
    boolean isRoundEnd();
    void startNewRound();
    String state();
}
