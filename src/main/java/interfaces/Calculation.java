package interfaces;

import sk.uniba.fmph.dcs.Tile;

import java.util.List;
import java.util.Optional;

public interface Calculation {
    int calculate(List<List<Optional<Tile>>> wall);
}
