package boardgame.model;

import java.util.Objects;

public record Position(int row, int col) {

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}