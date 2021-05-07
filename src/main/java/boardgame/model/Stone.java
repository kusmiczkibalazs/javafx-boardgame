package boardgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Stone {

    private ObjectProperty<StoneType> type = new SimpleObjectProperty<>();
    private ObjectProperty<Position> position = new SimpleObjectProperty<Position>();

    public Stone(StoneType type, Position position) {
        this.type.set(type);
        this.position.set(position);
    }

    public StoneType getType() {
        return type.get();
    }

    public void setType(StoneType type) {
        this.type.set(type);
    }

    public Position getPosition() {
        return position.get();
    }

    public ObjectProperty<StoneType> stoneTypeProperty() {
        return type;
    }

    @Override
    public String toString() {
        return type.get().toString() + " " + position.get().toString();
    }
}