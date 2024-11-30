package sk.uniba.fmph.dcs;

import interfaces.*;
import records.StartNewRoundFactoryResult;
import records.TakeBagResult;
import records.TakeFactoryResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Factory implements FactoryInterface, Constants {
    private final List<Tile> tiles;

    public Factory() {
        tiles = new ArrayList<>();
    }

    public Factory(List<Tile> tiles) {
        this.tiles = List.copyOf(tiles);
    }

    private static List<Integer> generateIndexes(int index, Tile idx, List<Tile> tiles) {

        return index >= tiles.size()
                ? List.of()
                : idx == tiles.get(index)
                ? Stream.concat(List.of(index).stream(), generateIndexes(index, idx, Stream.concat(tiles.subList(0, index).stream(), tiles.subList(index + 1, tiles.size()).stream()).toList()).stream()).collect(Collectors.toList())
                : generateIndexes(index + 1, idx, tiles);
    }

    private static List<Tile> takeFromFactory(List<Integer> indexes, int count, List<Tile> tiles) {

        return count >= indexes.size()
                ? List.of()
                : Stream.concat(
                List.of(tiles.get(indexes.get(count))).stream(),
                takeFromFactory(
                        indexes,
                        count + 1,
                        Stream.concat(tiles.subList(0, indexes.get(count)).stream(), tiles.subList(indexes.get(count) + 1, tiles.size()).stream()).collect(Collectors.toList())
                ).stream()
        ).collect(Collectors.toList());

    }

    private static List<Tile> goesToTableCenter(List<Integer> indexes, int count, List<Tile> tiles) {

        return count >= indexes.size()
                ? Collections.unmodifiableList(tiles)
                : goesToTableCenter(
                indexes,
                count + 1,
                Stream.concat(tiles.subList(0, indexes.get(count)).stream(), tiles.subList(indexes.get(count) + 1, tiles.size()).stream()).collect(Collectors.toList())
        );
    }

    @Override
    public TakeFactoryResult take(final int idx, TableCenterInterface tableCenter) {

        if (idx < 0 || idx >= tiles.size()) {
            throw new IllegalArgumentException("index not in tiles[]");
        }

        List<Integer> indexes = generateIndexes(0, tiles.get(idx), tiles);

        return new TakeFactoryResult(takeFromFactory(indexes, 0, tiles), new Factory(), tableCenter.add(goesToTableCenter(indexes, 0, tiles)).tableCenter());
    }

    @Override
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    @Override
    public StartNewRoundFactoryResult startNewRound(BagInterface bag, UsedTilesInterface usedTiles) {
        TakeBagResult takeBagResult = bag.take(MAX_NUMBER_OF_TILES, usedTiles);
        return new StartNewRoundFactoryResult(new Factory(takeBagResult.tiles()), takeBagResult.bag(), takeBagResult.usedTiles());
    }

    @Override
    public String state() {
        return tiles.stream()
                .map(Tile::toString)
                .collect(Collectors.joining(""));
    }
}
