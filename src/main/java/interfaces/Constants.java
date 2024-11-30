package interfaces;

import sk.uniba.fmph.dcs.Tile;

import java.util.List;

public interface Constants {
    int NUM_OF_TILES_OF_EACH_COLOR = 20;
    List<Tile> DIFFTILES = List.of(Tile.RED, Tile.GREEN, Tile.YELLOW, Tile.BLUE, Tile.BLACK);
    int MAX_NUMBER_OF_TILES = 4;
}
