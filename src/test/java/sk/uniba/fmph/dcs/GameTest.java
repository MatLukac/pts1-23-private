package sk.uniba.fmph.dcs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import interfaces.BagInterface;
import interfaces.BoardInterface;
import interfaces.GameObserverInterface;
import interfaces.TableAreaInterface;
import org.junit.Before;
import org.junit.Test;

class FakeBagGame implements BagInterface {
    private ArrayList<Tile> tiles;

    public FakeBagGame() {
        tiles = new ArrayList<>();
        tiles.addAll(List.of(Tile.RED, Tile.GREEN, Tile.BLUE, Tile.RED, Tile.GREEN, Tile.BLUE, Tile.GREEN, Tile.BLUE));
    }

    @Override
    public ArrayList<Tile> take(int count) {
        ArrayList<Tile> toReturn = new ArrayList<>();
        for (int i = 0; i < count; i++) toReturn.add(tiles.get(i));
        return toReturn;
    }

    @Override
    public String state() {
        return null;
    }
}

class FakeBoard implements BoardInterface {
    //very simplify version of the correct Board class, put automatically puts tile on WallLine
    public ArrayList<ArrayList<Boolean>> tiles;

    public FakeBoard() {
        tiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) tiles.add(new ArrayList(List.of(false, false, false, false, false)));
        tiles.add(new ArrayList(List.of(false, true, true, true, true)));
    }

    @Override
    public void put(int destinationIdx, ArrayList<Tile> tiles) {
        if (destinationIdx < 0 || destinationIdx >= this.tiles.size()) return;
        if (tiles.get(0).equals(Tile.RED))
            this.tiles.get(destinationIdx).set(0, true);
    }

    @Override
    public FinishRoundResult finishRound() {
        for (ArrayList<Boolean> a : tiles) {
            boolean end = true;
            for (boolean b : a) end = end && b;
            if (end) return FinishRoundResult.GAME_FINISHED;
        }
        return FinishRoundResult.NORMAL;
    }

    @Override
    public void endGame() {
        return;
    }

    @Override
    public String state() {
        return null;
    }
}

class FakeTableArea implements TableAreaInterface {
    ArrayList<List<Tile>> tiles;

    public FakeTableArea() {
        tiles = new ArrayList();
        tiles.add(List.of(Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.STARTING_PLAYER));
        tiles.add(List.of(Tile.GREEN, Tile.GREEN, Tile.GREEN));
    }

    @Override
    public ArrayList<Tile> take(int sourceIdx, int idx) {
        if (sourceIdx < 0 || sourceIdx >= tiles.size()) throw new IllegalArgumentException();
        if (idx < 0 || idx >= tiles.get(sourceIdx).size()) throw new IllegalArgumentException();

        List<Tile> t = tiles.get(sourceIdx);
        tiles.set(sourceIdx, new ArrayList());
        return new ArrayList<>(t);
    }

    @Override
    public boolean isRoundEnd() {
        for (List<Tile> t : tiles) if (t.size() != 0) return false;
        return true;
    }

    @Override
    public void startNewRound() {
        tiles = new ArrayList<>();
        tiles.add(List.of(Tile.STARTING_PLAYER));
        tiles.add(List.of(Tile.RED, Tile.RED, Tile.RED));
    }

    @Override
    public String state() {
        return null;
    }
}

class FakeGameObserver implements GameObserverInterface {
    @Override
    public void notifyEverybody(String state) {
        System.out.println(state);
    }
}

public class GameTest {
    private FakeBagGame bag;
    private ArrayList<BoardInterface> boards;
    private FakeTableArea tableArea;
    private FakeGameObserver gameObserver;
    private Game game;

    @Before
    public void setUp() {
        bag = new FakeBagGame();
        boards = new ArrayList<>(List.of(new FakeBoard(), new FakeBoard()));
        tableArea = new FakeTableArea();
        gameObserver = new FakeGameObserver();
        game = new Game(bag, tableArea, gameObserver, boards);
    }

    @Test
    public void gameTest() {
        assertEquals("At the beginning, starting player should be 0.)", 0, game.getCurrentPlayerId());
        assertEquals("Wrong player shoulf not be allowed to take from TableArea.", false, game.take(1, 0, 0, 0));

        assertEquals("Wrong tile source description or tile description in source should return false.", false, game.take(0, -1, 0, 0) || game.take(0, 0, -1, 0));
        assertEquals("After wrong sourceIdx or idx, game.playerId should not change.", 0, game.getCurrentPlayerId());
        assertEquals("game.take() with correct sourceIdx and idx, should return true.", true, game.take(0, 1, 0, 4));
        assertEquals("After successful game.take(), player should change to next player(1).", 1, game.getCurrentPlayerId());
        game.take(1, 0, 0, 4);
        assertEquals("After someone takes Tile.STARTING_PLAYER, he will become starting player for next round", 1, game.getNextStartingPlayer());
        assertEquals("After TableArea is empty, new round starts if no one finished row and starting player is nextStartingPlayer.", 1, game.getCurrentPlayerId());
        game.take(1, 0, 0, 4);
        assertEquals("Current player should be 0.", 0, game.getCurrentPlayerId());
        game.take(0, 1, 0, 4);
        assertEquals("After TableArea is empty, game ends if someone finished row.", "ended", game.state());
    }
}
