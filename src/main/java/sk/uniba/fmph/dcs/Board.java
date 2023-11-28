package sk.uniba.fmph.dcs;

import interfaces.FinalPointsCalculationInterface;
import interfaces.GameFinishedInterface;
import interfaces.PatternLineInterface;
import interfaces.WallLineInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Board {
    private final Floor floor;
    private final Points points;
    private final List<PatternLineInterface> patternLines;
    private final List<WallLineInterface> wallLines;

    public Board(final Floor floor, final Points points, final List<PatternLineInterface> patternLines, final List<WallLineInterface> wallLines) {

        this.floor = floor;
        this.points = points;
        this.patternLines = patternLines;
        this.wallLines = wallLines;
    }

    public final void put(final int destinationIndex, final List<Tile> tiles) {

        if (destinationIndex == -1) {

            floor.put(tiles);

            return;
        }


        List<Tile> toReturn = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile != Tile.STARTING_PLAYER) {
                toReturn.add(tile);
            } else {
                floor.put(Collections.singleton(Tile.STARTING_PLAYER));
            }
        }
        patternLines.get(destinationIndex).put(toReturn);
    }


    public final FinishRoundResult finishRound() {

        for (PatternLineInterface patternLine : patternLines) {

            points.add(patternLine.finishRound());
        }

        points.add(floor.finishRound());

        List<List<Optional<Tile>>> wallTiles = wallLines.stream()
                .map(WallLineInterface::getTiles) // Convert each WallLineInterface to List<Optional<Tile>>
                .collect(Collectors.toList());

        FinishRoundResult result = GameFinishedInterface.gameFinished(wallTiles);
        if (result == FinishRoundResult.GAME_FINISHED) {
            endGame();
        }
        return result;
    }

    public final void endGame() {

        List<List<Optional<Tile>>> wallTiles = wallLines.stream()
                .map(WallLineInterface::getTiles) // Convert each WallLineInterface to List<Optional<Tile>>
                .collect(Collectors.toList());

        Points finalPoints = FinalPointsCalculationInterface.getPoints(wallTiles);

        // Add the final points to the points object
        points.add(finalPoints);
    }

    public final String state() {
        StringBuilder stateBuilder = new StringBuilder();

        // Append state of each pattern line
        stateBuilder.append("Pattern Lines:\n");
        for (PatternLineInterface patternLine : patternLines) {
            stateBuilder.append(patternLine.state()).append("\n"); // Using state method of PatternLine
        }

        // Append state of each wall line
        stateBuilder.append("Wall Lines:\n");
        for (WallLineInterface wallLine : wallLines) {
            stateBuilder.append(wallLine.state()).append("\n");
        }

        // Append state of the floor
        stateBuilder.append("Floor:\n");
        stateBuilder.append(floor.state()).append("\n");

        // Append current points
        stateBuilder.append(points.toString()).append("\n");

        return stateBuilder.toString();
    }

    public final Points getPoints() {
        return this.points;
    }
}
