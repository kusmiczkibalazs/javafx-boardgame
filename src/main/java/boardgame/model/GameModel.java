package boardgame.model;


import java.util.*;

public class GameModel {

    public enum Player {
        FIRST_PLAYER, SECOND_PLAYER;

        public Player alterPlayer() {
            return switch(this) {
                case FIRST_PLAYER -> SECOND_PLAYER;
                case SECOND_PLAYER -> FIRST_PLAYER;
            };
        }
    }

    public static int BOARD_SIZE = 4;
    private Stone[] stones;
    private Player currentPlayer;

    public GameModel() {
        this(new Stone(StoneType.DESELECTED_STONE, new Position(0, 0)),
             new Stone(StoneType.DESELECTED_STONE, new Position(0, 1)),
             new Stone(StoneType.DESELECTED_STONE, new Position(0, 2)),
             new Stone(StoneType.DESELECTED_STONE, new Position(0, 3)),
             new Stone(StoneType.DESELECTED_STONE, new Position(1, 0)),
             new Stone(StoneType.DESELECTED_STONE, new Position(1, 1)),
             new Stone(StoneType.DESELECTED_STONE, new Position(1, 2)),
             new Stone(StoneType.DESELECTED_STONE, new Position(1, 3)),
             new Stone(StoneType.DESELECTED_STONE, new Position(2, 0)),
             new Stone(StoneType.DESELECTED_STONE, new Position(2, 1)),
             new Stone(StoneType.DESELECTED_STONE, new Position(2, 2)),
             new Stone(StoneType.DESELECTED_STONE, new Position(2, 3)),
             new Stone(StoneType.DESELECTED_STONE, new Position(3, 0)),
             new Stone(StoneType.DESELECTED_STONE, new Position(3, 1)),
             new Stone(StoneType.DESELECTED_STONE, new Position(3, 2)),
             new Stone(StoneType.DESELECTED_STONE, new Position(3, 3)) );

        currentPlayer = Player.FIRST_PLAYER;
    }

    public GameModel(Stone... stones) {
        checkStones(stones);
        this.stones = stones.clone();
    }

    private void checkStones(Stone[] stones) {
        var seen = new HashSet<Position>();
        for (var stone : stones) {
            if (!isOnBoard(stone.getPosition()) || seen.contains(stone.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(stone.getPosition());
        }
    }

    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_SIZE
                && 0 <= position.col() && position.col() < BOARD_SIZE;
    }

    public int getStoneCount() {
        return stones.length;
    }

    public StoneType getStoneType(Position position) {
        return stones[getStoneNumber(position).getAsInt()].getType();
    }

    public Position getStonePosition(int stoneNumber) {
        return stones[stoneNumber].getPosition();
    }

    public OptionalInt getStoneNumber(Position position) {
        for (int i = 0; i < stones.length; i++) {
            if (stones[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void selectStone(Position position) {
        var stone = stones[getStoneNumber(position).getAsInt()];
        if (stone.getType() == StoneType.DESELECTED_STONE)
            stone.setType(StoneType.SELECTED_STONE);
    }

    public void deselectStone(Position position) {
            var stone = stones[getStoneNumber(position).getAsInt()];
            if (stone.getType() == StoneType.SELECTED_STONE)
                stone.setType(StoneType.DESELECTED_STONE);
    }

    public void removeStones(List<Position> selectedPositions) {
        if (isRemovableSelection(selectedPositions)) {
            for (var position : selectedPositions) {
                var stone = stones[getStoneNumber(position).getAsInt()];
                if (stone.getType() == StoneType.SELECTED_STONE)
                    stone.setType(StoneType.REMOVED_STONE);
            }

            currentPlayer = currentPlayer.alterPlayer();
        }
    }

    public boolean isRemovableSelection(List<Position> selectedPositions) {
        if (selectedPositions.size() == 0)
            return false;
        else if (selectedPositions.size() == 1)
            return true;

        var rows = new ArrayList<Integer>();
        var cols = new ArrayList<Integer>();
        for (var position : selectedPositions) {
            rows.add(position.row());
            cols.add(position.col());
        }

        if (verifyAllEqual(rows)) {
            Collections.sort(cols);
            for (int i = 0; i < cols.size() - 1; i++) {
                if (cols.get(i) + 1 != cols.get(i + 1)) {
                    if (getStoneType(new Position(rows.get(0), cols.get(i) + 1) ) == StoneType.REMOVED_STONE) {
                        return false;
                    }
                }
            }
            return true;

        } else if (verifyAllEqual(cols)) {
            Collections.sort(rows);
            for (int i = 0; i < rows.size() - 1; i++) {
                if (rows.get(i) + 1 != rows.get(i + 1)) {
                    if (getStoneType(new Position(rows.get(i) + 1, cols.get(0)) ) == StoneType.REMOVED_STONE) {
                        return false;
                    }
                }
            }
            return true;
        }

        return false;
    }

    private boolean verifyAllEqual(List<Integer> list) {
        return new HashSet<Integer>(list).size() <= 1;
    }

    public boolean isEnd() {
        for (var stone : stones) {
            if (stone.getType() != StoneType.REMOVED_STONE) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        for (var stone : stones) {
            joiner.add(stone.toString());
        }
        return joiner.toString();
    }
}