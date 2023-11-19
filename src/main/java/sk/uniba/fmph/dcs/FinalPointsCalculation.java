package sk.uniba.fmph.dcs;

import java.util.Optional;

public class FinalPointsCalculation {
    private final Vertical vertical;
    private final Horizontal horizontal;
    private final Color color;

    public FinalPointsCalculation(Vertical vertical, Horizontal horizontal, Color color) {
        this.vertical = vertical;
        this.horizontal = horizontal;
        this.color = color;
    }

    public int calculatePoints(Optional<Tile>[][] wall) {
        return vertical.calculate(wall) + horizontal.calculate(wall) + color.calculate(wall);
    }
}
