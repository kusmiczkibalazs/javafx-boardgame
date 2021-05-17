package boardgame.model;

import java.util.*;

/**
 * Class representing the state of the game board.
 */
public class GameModel {

    /**
     * Enum representing the two players of the game.
     */
    public enum Player {
        FIRST_PLAYER, SECOND_PLAYER;

        /**
         * Returns which {@code Player} comes next.
         *
         * @return the {@code Player} that is the next to take turns
         */
        public Player alterPlayer() {
            return switch(this) {
                case FIRST_PLAYER -> SECOND_PLAYER;
                case SECOND_PLAYER -> FIRST_PLAYER;
            };
        }
    }

    /**
     * The length and the height of the game board.
     */
    public static int BOARD_SIZE = 4;
    /**
     * The number of stones on the game board.
     */
    public static int STONE_COUNT = BOARD_SIZE * BOARD_SIZE;
    private Stone[] stones;
    private Player currentPlayer;

    /**
     * Creates a {@code GameModel} object representing the initial state of the puzzle.
     */
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

    /**
     * Creates a {@code GameModel} object that is initialized with the specified array.
     *
     * @param stones an array of 16 {@link Stone} objects
     */
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

    /**
     * Checks whether a given {@code Position} is present on the game board.
     *
     * @param position the position on the game board to check
     * @return {@code true} if the {@code Position} is on the game board and {@code false} if it is not
     */
    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_SIZE
                && 0 <= position.col() && position.col() < BOARD_SIZE;
    }

    /**
     * Returns the {@code StoneType} of the given {@code Position}.
     *
     * @param position the {@code Position} objects to check
     * @return the type of the {@code Stone}
     */
    public StoneType getStoneType(Position position) {
        return stones[getStoneNumber(position).getAsInt()].getType();
    }

    /**
     * Returns the {@code Position} of the {@code Stone} referenced by it's index in the array.
     *
     * @param stoneNumber the index of the {@code Stone} in the array
     * @return the {@code Position} of the stone on the game board
     */
    public Position getStonePosition(int stoneNumber) {
        return stones[stoneNumber].getPosition();
    }

    /**
     * Returns the index of the {@code Stone} in the array referenced by it's {@code Position}.
     * Although it returns an {@code OptionalInt}, it will never be empty, because the removed {@code Stone} objects
     * are not deleted from the array.
     *
     * @param position the {@code Position} of the stone on the game board
     * @return the index of the {@code Stone} in the array
     */
    public OptionalInt getStoneNumber(Position position) {
        for (int i = 0; i < stones.length; i++) {
            if (stones[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Return the {@code Player} that has to take turns.
     *
     * @return the current {@code Player}
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets a {@code Stone}'s {@code StoneType} to {@code SELECTED_STONE}.
     * It only changes the {@code StoneType} if it's type is {@code DESELECTED_STONE}.
     *
     * @param position the {@code Position} of the stone on the game board to select
     */
    public void selectStone(Position position) {
        var stone = stones[getStoneNumber(position).getAsInt()];
        if (stone.getType() == StoneType.DESELECTED_STONE)
            stone.setType(StoneType.SELECTED_STONE);
    }

    /**
     * Sets a {@code Stone}'s {@code StoneType} to {@code DESELECTED_STONE}.
     * It only changes the {@code StoneType} if it's type is {@code SELECTED_STONE}.
     *
     * @param position the {@code Position} of the stone on the game board to deselect
     */
    public void deselectStone(Position position) {
            var stone = stones[getStoneNumber(position).getAsInt()];
            if (stone.getType() == StoneType.SELECTED_STONE)
                stone.setType(StoneType.DESELECTED_STONE);
    }

    /**
     * Sets a list of {@code Stone}'s {@code StoneType} to {@code REMOVED_STONE}.
     * It only changes the {@code StoneType} if it's type is {@code SELECTED_STONE} and it is legal to remove them.
     *
     * @param selectedPositions a {@code List} of {@code Position} objects
     */
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

    /**
     * Checks whether a given list of {@code Position} of {@code Stone} objects are removable.
     *
     * @param selectedPositions a {@code List} of {@code Position} objects to check
     * @return {@code true} if the stones are removable and {@code false} if they are not
     */
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

    /**
     * Returns whether the game is finished.
     *
     * @return {@code true} if the game is finished and {@code false} if it is not
     */
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