package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class Board {
    private final Floor floor;
    private final Points points;
    private List<PatternLineInterface> patternLines;
    private List<WallLineInterface> wallLines;
    private final FinalPointsCalculationInterface finalPointsCalculation = new FinalPointsCalculationInterface() {
        @Override
        public Points getPoints(List<Optional<Tile>> wall) {
            return null;
        }
    };

    public Board(Floor floor, Points points, List<PatternLineInterface> patternLines, List<WallLineInterface> wallLines) {

        this.floor = floor;
        this.points = points;
        this.patternLines = patternLines;
        this.wallLines = wallLines;
    }

    public void put(int destinationIndex, List<Tile> tiles) {

        if (destinationIndex == -1) {

            floor.put(tiles);

            return;
        }

        if (tiles.contains(Tile.STARTING_PLAYER)) {

            tiles.remove(Tile.STARTING_PLAYER);
            floor.put(Collections.singleton(Tile.STARTING_PLAYER));
        }

        patternLines.get(destinationIndex).put(tiles);
    }

    public FinishRoundResult finishRound() {

        for (PatternLineInterface patternLine : patternLines) {

            points.add(patternLine.finishRound());
        }

        points.add(floor.finishRound());

        return GameFinished.gameFinished(wallLines);
    }

    public void endGame() {

        Tile[][] wall = wallLines.stream()
                .map(WallLineInterface::getTiles)
                .toArray(Tile[][]::new);

        Points finalPoints = finalPointsCalculation.getPoints(wall);

        points.add(finalPoints);
    }

    public String state() {
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

}