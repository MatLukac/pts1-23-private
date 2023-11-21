package sk.uniba.fmph.dcs;

import java.util.Optional;

public class FinalPointsCalculation {
    private static Vertical vertical = new Vertical();
    private static Horizontal horizontal = new Horizontal();
    private static Color color = new Color();


    public static Points getPoints(Optional<Tile>[][] wall) {
        return vertical.calculate(wall) + horizontal.calculate(wall) + color.calculate(wall);
    }
}
