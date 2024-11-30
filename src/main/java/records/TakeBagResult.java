package records;

import interfaces.BagInterface;
import interfaces.UsedTilesInterface;
import sk.uniba.fmph.dcs.Tile;

import java.util.List;

public record TakeBagResult(List<Tile> tiles, BagInterface bag, UsedTilesInterface usedTiles) {
}
