package interfaces;
import sk.uniba.fmph.dcs.Points;
import sk.uniba.fmph.dcs.Tile;

import java.util.*;
public interface WallLineInterface {
    boolean canPutTile(Tile tile);
    ArrayList<Optional<Tile>> getTiles();
    Points putTile(Tile tile);
    String state();
}
