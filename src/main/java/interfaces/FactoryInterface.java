package interfaces;

import records.StartNewRoundFactoryResult;
import records.TakeFactoryResult;

public interface FactoryInterface {
    TakeFactoryResult take(int idx, TableCenterInterface tableCenter);

    boolean isEmpty();

    StartNewRoundFactoryResult startNewRound(BagInterface bag, UsedTilesInterface usedTiles);

    String state();
}
