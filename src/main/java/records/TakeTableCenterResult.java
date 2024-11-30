package records;

import interfaces.TableCenterInterface;
import sk.uniba.fmph.dcs.Tile;

import java.util.List;

public record TakeTableCenterResult(List<Tile> tiles, TableCenterInterface tableCenter, boolean isFisrt) {
}
