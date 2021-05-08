package boardgame.model;


public class Stone {

    private StoneType type;
    private Position position;

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