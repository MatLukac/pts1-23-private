package sk.uniba.fmph.dcs;

import interfaces.FinalPointsCalculationInterface;
import interfaces.WallLineInterface;
import interfaces.FinalPointsCalculationInterface;
import interfaces.GameFinishedInterface;
import interfaces.PatternLineInterface;
import interfaces.WallLineInterface;

import java.util.*;
import java.util.stream.Collectors;

public class Board {
    private final Floor floor;
    private final Points points;
    private final List<PatternLineInterface> patternLines;
    private final List<WallLineInterface> wallLines;

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

            floor.put(Collections.singleton(Tile.STARTING_PLAYER));
            List<Tile> help = new ArrayList<>();
            for (Tile tile : tiles){
                if (tile != Tile.STARTING_PLAYER){
                    help.add(tile);
                }
            }
            tiles = help;
        }

        patternLines.get(destinationIndex).put(tiles);
    }

    public FinishRoundResult finishRound() {

        for (PatternLineInterface patternLine : patternLines) {

            points.add(patternLine.finishRound());
        }

        points.add(floor.finishRound());

        List<List<Optional<Tile>>> wallTiles = wallLines.stream()
                .map(WallLineInterface::getTiles) // Convert each WallLineInterface to List<Optional<Tile>>
                .collect(Collectors.toList());

        FinishRoundResult result = GameFinishedInterface.gameFinished(wallTiles);
        if (result == FinishRoundResult.GAME_FINISHED) endGame();
        return result;
    }

    public void endGame() {

        List<List<Optional<Tile>>> wallTiles = wallLines.stream()
                .map(WallLineInterface::getTiles) // Convert each WallLineInterface to List<Optional<Tile>>
                .collect(Collectors.toList());

        Points finalPoints = FinalPointsCalculationInterface.getPoints(wallTiles);

        // Add the final points to the points object
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

    public Points getPoints(){
        return this.points;
    }
}