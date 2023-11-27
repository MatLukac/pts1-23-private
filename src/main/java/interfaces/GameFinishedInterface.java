package interfaces;

import sk.uniba.fmph.dcs.FinishRoundResult;
import sk.uniba.fmph.dcs.Tile;

import java.util.List;
import java.util.Optional;

public interface GameFinishedInterface {
    static FinishRoundResult gameFinished(List<List<Optional<Tile>>> wall) {
        return null;
    }

}
