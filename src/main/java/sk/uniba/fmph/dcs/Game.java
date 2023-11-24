package sk.uniba.fmph.dcs;

import sun.jvm.hotspot.utilities.Observer;

import javax.swing.text.TabExpander;

public class Game {
    private final BagInterface bag;
    private final TableAreaInterface tableArea;
    private final ObserverInterface gameObserver;

    public Game(BagInterface bag, TableAreaInterface tableArea, ObserverInterface gameObserver){
        this.bag = bag;
        this.tableArea = tableArea;
        this.gameObserver = gameObserver;
    }
}
