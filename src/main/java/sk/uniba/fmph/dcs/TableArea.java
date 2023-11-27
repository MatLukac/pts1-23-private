package sk.uniba.fmph.dcs;

import interfaces.TableAreaInterface;
import interfaces.TileSource;

import java.util.ArrayList;

public class TableArea implements TableAreaInterface {
    ArrayList<TileSource> tileSources;

    public TableArea(ArrayList<TileSource> tileSources) {
        this.tileSources = tileSources;
    }

    @Override
    public ArrayList<Tile> take(int sourceIdx, int idx) {
        return tileSources.get(sourceIdx).take(idx);
    }

    @Override
    public boolean isRoundEnd() {
        for (TileSource tileSource : tileSources) {
            if (!tileSource.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void startNewRound() {
        for (TileSource tileSource : tileSources) {
            tileSource.startNewRound();
        }
    }

    @Override
    public String state() {
        String toReturn = "";
        for (final TileSource tileSource : tileSources) {
            toReturn += tileSource.toString();
        }
        return toReturn;
    }
}
