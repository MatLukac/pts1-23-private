package interfaces;

import sk.uniba.fmph.dcs.Points;
import sk.uniba.fmph.dcs.Tile;
import sk.uniba.fmph.dcs.WallLine;

import java.util.List;
import java.util.Optional;

public interface WallLineInterface {
    boolean canPutTile(Tile tile);

    List<Optional<Tile>> getTiles();

    Points putTile(Tile tile);

    void setLineUp(WallLine lineUp);

    void setLineDown(WallLine lineUp);

    String state();
}
