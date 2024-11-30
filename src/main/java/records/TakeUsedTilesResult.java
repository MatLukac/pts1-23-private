package records;

import interfaces.UsedTilesInterface;
import sk.uniba.fmph.dcs.Tile;

import java.util.List;

public record TakeUsedTilesResult(List<Tile> tiles, UsedTilesInterface usedTiles) {
}
