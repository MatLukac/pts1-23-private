package sk.uniba.fmph.dcs;

import interfaces.Calculation;

import java.util.List;
import java.util.Optional;

public final class Vertical implements Calculation {
    private static final int POINTS_FOR_COLUMN = 7;

    @Override
    public int calculate(final List<List<Optional<Tile>>> wall) {
        int totalPoints = 0;
        for (int col = 0; col < wall.size(); col++) {
            if (isColumnComplete(wall, col)) {
                totalPoints += POINTS_FOR_COLUMN;
            }
        }
        return totalPoints;
    }

    public boolean isColumnComplete(final List<List<Optional<Tile>>> wall, final int colIndex) {
        for (List<Optional<Tile>> row : wall) {
            if (!row.get(colIndex).isPresent()) {
                return false;
            }
        }
        return true;
    }
}
