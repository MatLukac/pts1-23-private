package sk.uniba.fmph.dcs;

import interfaces.Calculation;

import java.util.List;
import java.util.Optional;

public final class Horizontal implements Calculation {
    public int calculatePoints(final List<List<Optional<Tile>>> wall) {
        return calculate(wall);
    }

    @Override
    public int calculate(final List<List<Optional<Tile>>> wall) {
        int totalPoints = 0;
        for (int row = 0; row < wall.size(); row++) {
            if (isRowComplete(wall, row)) {
                totalPoints += 2;
            }
        }
        return totalPoints;
    }

    public boolean isRowComplete(final List<List<Optional<Tile>>> wall, final int rowIndex) {
        for (Optional<Tile> tile : wall.get(rowIndex)) {
            if (!tile.isPresent()) {
                return false;
            }
        }
        return true;
    }
}
