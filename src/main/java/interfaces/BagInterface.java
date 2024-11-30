package interfaces;

import records.TakeBagResult;

public interface BagInterface {

    TakeBagResult take(final int count, UsedTilesInterface usedTiles);

    String state();


}
