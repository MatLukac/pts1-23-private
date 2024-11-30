package records;

import interfaces.FactoryInterface;
import interfaces.TableCenterInterface;
import sk.uniba.fmph.dcs.Tile;

import java.util.List;

public record TakeFactoryResult(List<Tile> tiles, FactoryInterface factory, TableCenterInterface tableCenter) {
}
