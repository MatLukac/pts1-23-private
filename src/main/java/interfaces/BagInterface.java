package interfaces;
import sk.uniba.fmph.dcs.Tile;

import java.util.ArrayList;
public interface BagInterface {
    ArrayList<Tile> take(int count);
    String state();
}
