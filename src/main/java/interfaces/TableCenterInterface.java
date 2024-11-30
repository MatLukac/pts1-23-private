package interfaces;

import records.AddTableCenterResult;
import records.StartNewRoundTableCenterResult;
import records.TakeTableCenterResult;
import sk.uniba.fmph.dcs.*;

import java.util.List;

public interface TableCenterInterface {
    TakeTableCenterResult take(int idx);

    boolean isEmpty();

    StartNewRoundTableCenterResult startNewRound();

    AddTableCenterResult add(List<Tile> tiles);

    String state();
}
