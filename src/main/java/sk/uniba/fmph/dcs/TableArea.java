package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public class TableArea {
    ArrayList<TileSource> tileSources;

    public TableArea(ArrayList<TileSource> tileSources) {
        this.tileSources = tileSources;
    }

    ArrayList<Tile> take(int sourceIdx, int idx) {
        return tileSources.get(sourceIdx).take(idx);
    }

    boolean isRoundEnd() {
        for (TileSource tileSource : tileSources) {
            if (!tileSource.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    void startNewRound() {
        for (TileSource tileSource : tileSources) {
            tileSource.startNewRound();
        }
    }

    public String State() {
        String toReturn = "";
        for (final TileSource tileSource : tileSources) {
            toReturn += tileSource.toString();
        }
        return toReturn;
    }
}
