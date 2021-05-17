package boardgame.model;

/**
 * Class representing the stones on the game board.
 */
public class Stone {

    private StoneType type;
    private Position position;

    /**
     * Creates a {@code Stone} object that is initialized by it's {@code StoneType} and {@code Position}.
     *
     * @param type the type of the {@code Stone}
     * @param position the position of the {@code Stone}
     */
    public Stone(StoneType type, Position position) {
        this.type = type;
        this.position = position;
    }

    public StoneType getType() {
        return type;
    }

    public void setType(StoneType type) {
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return type.toString() + " " + position.toString();
    }
}