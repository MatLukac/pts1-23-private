package sk.uniba.fmph.dcs;

import java.util.Optional;

public class GameFinished {

    public static FinishRoundResult gameFinished(Optional<Tile>[][] wall){
        Horizontal horizontal = new Horizontal();
        if(horizontal.calculatePoints(wall) > 0){
            return FinishRoundResult.GAME_FINISHED;
        }
        return FinishRoundResult.NORMAL;
    }
}
