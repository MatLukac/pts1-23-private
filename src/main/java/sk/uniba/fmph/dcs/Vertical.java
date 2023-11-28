package sk.uniba.fmph.dcs;

import interfaces.Calculation;

import java.util.List;
import java.util.Optional;

public final class Vertical implements Calculation {


    public int calculatePoints(final List<List<Optional<Tile>>> wall) {
        return calculate(wall);
    }
    @Override
    public int calculate(final List<List<Optional<Tile>>> wall) {
        int totalPoints = 0;
        for (int col = 0; col < wall.size(); col++) {
            if (isColumnComplete(wall, col)) {
                totalPoints += 7;
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
