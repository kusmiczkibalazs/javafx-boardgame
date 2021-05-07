package boardgame.model;

import javafx.beans.property.ObjectProperty;

import java.util.*;

public class BoardGameModel {

    public static int BOARD_SIZE = 4;
    private Stone[] stones;

    public BoardGameModel() {
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
    }

    public BoardGameModel(Stone... stones) {
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

    public ObjectProperty<StoneType> stoneTypeProperty(int stoneNumber) {
        return stones[stoneNumber].stoneTypeProperty();
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

    public void selectStone(Position position) {
        var stone = stones[getStoneNumber(position).getAsInt()];
        stone.setType(StoneType.SELECTED_STONE);
    }

    public void deselectStone(Position position) {
        var stone = stones[getStoneNumber(position).getAsInt()];
        stone.setType(StoneType.DESELECTED_STONE);
    }

    public void removeStone(Position position) {
        var stone = stones[getStoneNumber(position).getAsInt()];
        stone.setType(StoneType.EMPTY);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        for (var stone : stones) {
            joiner.add(stone.toString());
        }
        return joiner.toString();
    }

    public static void main(String[] args) {
        var model = new BoardGameModel();
        System.out.println(model);
    }
}