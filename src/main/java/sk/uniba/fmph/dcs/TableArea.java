package sk.uniba.fmph.dcs;

import interfaces.TableAreaInterface;
import interfaces.TileSource;

import java.util.ArrayList;

public final class TableArea implements TableAreaInterface {
    private ArrayList<TileSource> tileSources;

    public TableArea(final ArrayList<TileSource> tileSources) {
        this.tileSources = tileSources;
    }

    @Override
    public ArrayList<Tile> take(final int sourceIdx, final int idx) {
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
