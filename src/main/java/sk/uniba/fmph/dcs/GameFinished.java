package sk.uniba.fmph.dcs;

import interfaces.GameFinishedInterface;

import java.util.List;
import java.util.Optional;

public final class GameFinished implements GameFinishedInterface {
    public FinishRoundResult gameFinished(final List<List<Optional<Tile>>> wall) {
        Horizontal horizontal = new Horizontal();
        if (horizontal.calculatePoints(wall) >= 2) {
            return FinishRoundResult.GAME_FINISHED;
        }
        return FinishRoundResult.NORMAL;
    }
}
