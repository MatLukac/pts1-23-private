package records;

import interfaces.BagInterface;
import interfaces.FactoryInterface;
import interfaces.UsedTilesInterface;

public record StartNewRoundFactoryResult(FactoryInterface factory, BagInterface bag,
                                         UsedTilesInterface usedTiles) {
}
