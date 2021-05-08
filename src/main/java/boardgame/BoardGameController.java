package boardgame;

import boardgame.model.BoardGameModel;
import boardgame.model.CurrentPlayer;
import boardgame.model.Position;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private final Color DESELECTED_COLOR = Color.DARKGRAY;
    private final Color SELECTED_COLOR = Color.BLACK;
    public CurrentPlayer currentPlayer = new CurrentPlayer();

    @FXML
    private GridPane board;

    @FXML
    private Button removeButton;

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
            stone.setFill(DESELECTED_COLOR);
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
        var position = new Position(row, col);

        try {
            var stone = (Ellipse) square.getChildren().get(0);
            handleClickOnSquare(position, stone);
        } catch (IndexOutOfBoundsException e) {
            Logger.error("The stone has already been removed from position {}", position);
        }
    }

    private void handleClickOnSquare(Position position, Ellipse stone) {
        if (!selectedPositions.contains(position)) {
            selectedPositions.add(position);
            model.selectStone(position);
            stone.setFill(SELECTED_COLOR);
            Logger.debug("Stone type changed to SELECTED_STONE at position {}", position);
        } else {
            selectedPositions.remove(position);
            model.deselectStone(position);
            stone.setFill(DESELECTED_COLOR);
            Logger.debug("Stone type changed to DESELECTED_STONE at position {}", position);
        }

    }

    @FXML
    private void onRemoveButtonClick() {

        if (model.isRemovableSelection(selectedPositions)) {
            for (var position : selectedPositions) {
                model.removeStone(position);
                getSquare(position).getChildren().clear();
                Logger.debug("Removed stone from position {}", position);
            }
            currentPlayer.nextPlayer();

            if (model.isEnd()) {
                Logger.debug("Game finished! The winner is {}", currentPlayer.toString());
            }

        } else {
            Logger.debug("Impossible to remove these positions at once: {}", selectedPositions.toString());
            for (var position : selectedPositions) {
                model.deselectStone(position);
                var stone = (Ellipse) getSquare(position).getChildren().get(0);
                stone.setFill(DESELECTED_COLOR);
            }
        }

        selectedPositions.clear();
    }

}
