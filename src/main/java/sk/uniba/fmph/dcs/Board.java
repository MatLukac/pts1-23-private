package sk.uniba.fmph.dcs;

import sk.uniba.fmph.dcs.interfaces.PatternLineInterface;
import sk.uniba.fmph.dcs.interfaces.WallLineInterface;

import java.util.*;

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

        Optional<Tile>[][] wallTiles = new Optional[wallLines.size()][];
        for (int i = 0; i < wallLines.size(); i++) {
            wallTiles[i] = wallLines.get(i).getTiles().toArray(new Optional[0]);
        }

        return GameFinished.gameFinished(wallTiles);
    }

    public void endGame() {

        Tile[][] wallTiles = wallLines.stream()
                .map(WallLineInterface::getTiles)
                .toArray(Tile[][]::new);

        // Convert it into an Optional<Tile>[][] array
        List<Optional[]> list = new ArrayList<>();
        for (Tile[] row : wallTiles) {
            Optional[] optionals = Arrays.stream(row)
                    .map(Optional::ofNullable)
                    .toArray(Optional[]::new);
            list.add(optionals);
        }
        Optional<Tile>[][] optionalWall = list.toArray(new Optional[0][]);

        // Now you can use this array with getPoints
        Points finalPoints = FinalPointsCalculation.getPoints(optionalWall);

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