package sk.uniba.fmph.dcs;

import interfaces.Calculation;

import java.util.List;
import java.util.Optional;

public final class Color implements Calculation {
    private static final int NUM_OF_COLORS = 5;
    private static final int NUM_OF_TILES_OF_THE_SAME_COLOR = 5;
    private static final int POINTS_FOR_COLOR = 10;

    @Override
    public int calculate(final List<List<Optional<Tile>>> wall) {
        int totalPoints = 0;
        for (int colorIndex = 0; colorIndex < NUM_OF_COLORS; colorIndex++) {
            if (isColorComplete(wall, colorIndex)) {
                totalPoints += POINTS_FOR_COLOR;
            }
        }
        return totalPoints;
    }

    private boolean isColorComplete(final List<List<Optional<Tile>>> wall, final int colorIndex) {
        int count = 0;
        for (int i = 0; i < NUM_OF_TILES_OF_THE_SAME_COLOR; i++) {
            int colI = (colorIndex + i) % NUM_OF_TILES_OF_THE_SAME_COLOR;
            if (wall.get(i).get(colI).isPresent()) {
                count++;
            } else {
                return false;
            }
        }
        return count == NUM_OF_TILES_OF_THE_SAME_COLOR;
    }
}
