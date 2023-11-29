package sk.uniba.fmph.dcs;

import interfaces.WallLineInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class WallLine implements WallLineInterface {
    private ArrayList<Tile> tileTypes;
    private WallLine lineUp;
    private WallLine lineDown;

    private boolean[] occupiedTiles;


    public WallLine(final List<Tile> tileTypes, final WallLine lineUp, final WallLine lineDown) {
        this.tileTypes = new ArrayList<>(tileTypes);
        this.lineUp = lineUp;
        this.lineDown = lineDown;

        this.occupiedTiles = new boolean[tileTypes.size()];
    }

    public void setLineUp(final WallLine lineUp) {
        this.lineUp = lineUp;
    }

    public void setLineDown(final WallLine lineDown) {
        this.lineDown = lineDown;
    }

    @Override
    public boolean canPutTile(final Tile tile) {
        if (tileTypes.contains(tile) && !occupiedTiles[tileTypes.indexOf(tile)]) {
            return true;
        }
        return false;
    }

    @Override
    public List<Optional<Tile>> getTiles() {
        ArrayList<Optional<Tile>> tiles = new ArrayList<>();
        for (int i = 0; i < tileTypes.size(); i++) {
            if (occupiedTiles[i]) {
                tiles.add(Optional.ofNullable(this.tileTypes.get(i)));
            } else {
                Tile t = null;
                tiles.add(Optional.ofNullable(t));
            }
        }
        return tiles;
    }

    @Override
    public Points putTile(final Tile tile) {
        if (canPutTile(tile)) {
            int idx = tileTypes.indexOf(tile);
            this.occupiedTiles[idx] = true;

            int points = 0;
            if ((1 + idx < tileTypes.size() && occupiedTiles[1 + idx]) || (idx - 1 >= 0 && occupiedTiles[idx - 1])) {
                points = 1;
                int offset = 1;
                while (offset + idx < tileTypes.size()) {
                    if (occupiedTiles[offset + idx]) {
                        points++;
                        offset++;
                    } else {
                        break;
                    }
                }

                offset = 1;
                while (idx - offset >= 0) {
                    if (occupiedTiles[idx - offset]) {
                        points++;
                        offset++;
                    } else {
                        break;
                    }
                }
            }


            WallLine current = this;
            if ((current.lineUp != null && !current.lineUp.getTiles().get(idx).isEmpty()) || (current.lineDown != null && !current.lineDown.getTiles().get(idx).isEmpty())) {
                points++;
                while (current.lineUp != null) {
                    if (!current.lineUp.getTiles().get(idx).isEmpty()) {
                        points++;
                        current = current.lineUp;
                    } else {
                        break;
                    }
                }

                current = this;
                //if (current.lineDown != null && !current.lineDown.getTiles().get(idx).isEmpty()) points++;
                while (current.lineDown != null) {
                    if (!current.lineDown.getTiles().get(idx).isEmpty()) {
                        points++;
                        current = current.lineDown;
                    } else {
                        break;
                    }
                }
            }

            if (points == 0) return new Points(1);
            return new Points(points);

        }
        return new Points(0);
    }

    @Override
    public String state() {
        String toReturn = "";
        for (int i = 0; i < tileTypes.size(); i++) {
            if (occupiedTiles[i]) {
                toReturn += tileTypes.get(i).toString();
            } else {
                toReturn += "_";
            }
        }
        return toReturn;
    }
}
