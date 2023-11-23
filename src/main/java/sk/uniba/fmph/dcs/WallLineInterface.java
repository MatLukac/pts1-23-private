package sk.uniba.fmph.dcs;
import java.util.*;
public interface WallLineInterface {
    boolean canPutTile(Tile tile);
    ArrayList<Optional<Tile>> getTiles();
    Points putTile(Tile tile);
    String state();
}
