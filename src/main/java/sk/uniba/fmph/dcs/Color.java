package sk.uniba.fmph.dcs;

import java.util.Optional;

public class Color implements Calculation{
    @Override
    public int calculate(Optional<Tile>[][] wall){
        int totalPoints = 0;

        for (int i = 0; i < 5; i++) {
            int oneColor = 0;

            for (int j = 0; j < 5; j++) {
                int rowI = j;
                int colI = (i + j) % 5;

                if (wall[rowI][colI].isPresent()) {
                    oneColor++;
                } else {
                    oneColor = 0;
                }
            }
            if (oneColor == 5) {
                totalPoints += 10;
            }
        }
        return totalPoints;
    }
}