package sk.uniba.fmph.dcs;

import interfaces.*;
import records.TakeBagResult;
import records.TakeUsedTilesResult;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class Bag implements BagInterface, Constants {

    private final List<Tile> tiles;
    private final RandomGeneratorInterface randomGenerator;

    //hopefully streams are part of functional prorgaming
    //this would be just simple recursion that will generate DIFFTILES.size()*NUM_OF_TILES_OF_EACH_COLOR
    private final Function<Integer, Function<Integer, List<Tile>>> gen =
            collor -> count ->
                    count < NUM_OF_TILES_OF_EACH_COLOR && collor < DIFFTILES.size()
                            ? Stream.concat(List.of(DIFFTILES.get(collor)).stream(), this.gen.apply(collor).apply(count + 1).stream()).collect(Collectors.toList())
                            : collor < DIFFTILES.size()
                            ? this.gen.apply(collor + 1).apply(0)
                            : List.of();

    //alternative method for state() with Function<> if streams are not part of functional programing
    //private final Function<Integer, String> rec = index -> index == tiles.size() ? "" : tiles.get(index).toString() + this.rec.apply(index + 1);

    public Bag() {
        tiles = new ArrayList<>();
        randomGenerator = new RealRandom();
    }

    public Bag(RandomGeneratorInterface randomGenerator) {
        this.randomGenerator = randomGenerator;
        tiles = new ArrayList<>(gen.apply(0).apply(0));
    }

    public Bag(RandomGeneratorInterface randomGenerator, List<Tile> tiles) {
        this.randomGenerator = randomGenerator;
        this.tiles = tiles;
    }

    private static List<Integer> generateIndexes(RandomGeneratorInterface randomGenerator, int count, int bound, int pick) {
        return count == 0
                ? List.of(pick)
                : Stream.concat(
                List.of(pick).stream(),
                generateIndexes(randomGenerator, count - 1, bound - 1, randomGenerator.nextInt(bound)).stream()
        ).collect(Collectors.toList());
    }

    private static List<Tile> takeFromBag(RandomGeneratorInterface randomGenerator, List<Tile> tiles, int count, List<Integer> index) {
      
        return count >= index.size()
                ? List.of()
                : Stream.concat(
                List.of(tiles.get(index.get(count))).stream(),
                takeFromBag(
                        randomGenerator,
                        Stream.concat(tiles.subList(0, index.get(count)).stream(), tiles.subList(index.get(count) + 1, tiles.size()).stream()).collect(Collectors.toList()),
                        count + 1,
                        index).stream()
        ).collect(Collectors.toList());

    }

    private static List<Tile> staysInBag(RandomGeneratorInterface randomGenerator, List<Tile> tiles, int count, List<Integer> index) {

        return count >= index.size()
                ? Collections.unmodifiableList(tiles)
                : staysInBag(
                randomGenerator,
                Stream.concat(tiles.subList(0, index.get(count)).stream(), tiles.subList(index.get(count) + 1, tiles.size()).stream()).collect(Collectors.toList()),
                count + 1,
                index);

    }


    @Override
    public TakeBagResult take(final int count, UsedTilesInterface usedTiles) {
        ArrayList<Tile> toReturn = new ArrayList<>();
        if (tiles.size() < count) {

            toReturn.addAll(tiles);
            TakeUsedTilesResult takeUsedTilesResult = usedTiles.take();

            if (takeUsedTilesResult.tiles().size() < count - toReturn.size())
                throw new IndexOutOfBoundsException();  //not enough tiles for request


            TakeBagResult takeBagResult = new Bag(this.randomGenerator, new ArrayList<>(takeUsedTilesResult.tiles())).take(count - toReturn.size(), takeUsedTilesResult.usedTiles());

            return new TakeBagResult(Stream.concat(toReturn.stream(), takeBagResult.tiles().stream()).toList(), takeBagResult.bag(), takeBagResult.usedTiles());


        }

        List<Integer> index = generateIndexes(randomGenerator, count - 1, tiles.size() - 1, randomGenerator.nextInt(tiles.size()));

        return new TakeBagResult(takeFromBag(randomGenerator, tiles, 0, index), new Bag(randomGenerator, staysInBag(randomGenerator, tiles, 0, index)), usedTiles);
    }


    @Override
    public String state() {
        return tiles.stream()
                .map(Tile::toString)
                .collect(Collectors.joining(""));

        //return rec.apply(0);
    }
}
