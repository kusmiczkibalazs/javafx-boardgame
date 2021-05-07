package boardgame;

import boardgame.model.BoardGameModel;
import boardgame.model.Position;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class BoardGameController {

    private BoardGameModel model = new BoardGameModel();
    private List<Position> selectedPositions = new ArrayList<>();

    @FXML
    private GridPane board;

    @FXML
    private void initialize() {
        createBoard();
        createStones();
    }

    private void createBoard() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                board.add(square, j, i);
            }
        }
    }

    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private void createStones() {
        for (int i = 0; i < model.getStoneCount(); i++) {
            var stone = new Ellipse(32, 47);
            stone.setFill(Color.DARKGRAY);
            getSquare(model.getStonePosition(i)).getChildren().add(stone);
        }
    }

    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.debug("Click on stone at ({}, {})", row, col);
        var stone = (Ellipse) square.getChildren().get(0);
        var position = new Position(row, col);
        handleClickOnSquare(position, stone);
    }

    private void handleClickOnSquare(Position position, Ellipse stone) {
        if (! selectedPositions.contains(position)) {
            selectedPositions.add(position);
            model.selectStone(position);
            stone.setFill(Color.BLACK);
            Logger.debug("Stone type changed to SELECTED_STONE at position {}", position);
        } else {
            selectedPositions.remove(position);
            model.deselectStone(position);
            stone.setFill(Color.DARKGRAY);
            Logger.debug("Stone type changed to DESELECTED_STONE at position {}", position);
        }

        //selectedPositions.stream().forEach(System.out::print);
    }

}
