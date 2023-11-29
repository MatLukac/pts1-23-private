package sk.uniba.fmph.dcs;

import interfaces.BoardInterface;
import interfaces.FinalPointsCalculationInterface;
import interfaces.GameFinishedInterface;
import interfaces.PatternLineInterface;
import interfaces.WallLineInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Board implements BoardInterface {
    private final Floor floor;
    private final ArrayList<Points> points;
    private final ArrayList<PatternLineInterface> patternLines;
    private final ArrayList<WallLineInterface> wallLines;
    private final FinalPointsCalculationInterface finalPointsCalculation;
    private final GameFinishedInterface gameFinished;

    public Board(final Floor floor, final ArrayList<Points> points, final ArrayList<PatternLineInterface> patternLines, final ArrayList<WallLineInterface> wallLines, final FinalPointsCalculationInterface finalPointsCalculation, final GameFinishedInterface gameFinished) {
        this.finalPointsCalculation = finalPointsCalculation;
        this.gameFinished = gameFinished;
        this.floor = floor;
        this.points = points;
        this.patternLines = patternLines;
        this.wallLines = wallLines;
    }

    @Override
    public void put(final int destinationIndex, final ArrayList<Tile> tiles) {
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

    @Override
    public FinishRoundResult finishRound() {
        for (PatternLineInterface patternLine : patternLines) {
            points.add(patternLine.finishRound());
        }
        points.add(floor.finishRound());
        List<List<Optional<Tile>>> wallTiles = wallLines.stream()
                .map(WallLineInterface::getTiles) // Convert each WallLineInterface to List<Optional<Tile>>
                .collect(Collectors.toList());

        FinishRoundResult result = gameFinished.gameFinished(wallTiles);
        if (result == FinishRoundResult.GAME_FINISHED) {
            endGame();
        }
        return result;
    }

    @Override
    public void endGame() {
        List<List<Optional<Tile>>> wallTiles = wallLines.stream()
                .map(WallLineInterface::getTiles) // Convert each WallLineInterface to List<Optional<Tile>>
                .collect(Collectors.toList());
        Points finalPoints = finalPointsCalculation.getPoints(wallTiles);
        // Add the final points to the points object
        points.add(finalPoints);
    }

    @Override
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
        stateBuilder.append(Points.sum(points).toString()).append("\n");
        //System.out.println(stateBuilder.toString());
        return stateBuilder.toString();
    }

    public Points getPoints() {
        return Points.sum(points);
    }
}
