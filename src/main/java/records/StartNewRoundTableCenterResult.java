package records;

import interfaces.BagInterface;
import interfaces.TableCenterInterface;
import interfaces.UsedTilesInterface;

public record StartNewRoundTableCenterResult(TableCenterInterface tableCenter, BagInterface bag,
                                             UsedTilesInterface usedTiles) {
}
