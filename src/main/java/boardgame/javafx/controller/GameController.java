package boardgame.javafx.controller;

import boardgame.model.GameModel;
import boardgame.model.Position;
import boardgame.results.GameResult;
import boardgame.results.GameResultHandle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    private GameModel model = new GameModel();
    private List<Position> selectedPositions = new ArrayList<>();
    private String firstPlayerName;
    private String secondPlayerName;
    private final Color DESELECTED_COLOR = Color.DARKGRAY;
    private final Color SELECTED_COLOR = Color.BLACK;

    public void setPlayerNames (String firstPlayerName, String secondPlayerName) {
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
    }

    @FXML
    private GridPane board;

    @FXML
    private Label errorLabel;

    @FXML
    private Label currentPlayerLabel;

    @FXML
    private void initialize() {
        createBoard();
        createStones();
        errorLabel.setVisible(false);
        Platform.runLater(
                () -> currentPlayerLabel.setText(firstPlayerName + " következik")
        );
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
        for (int i = 0; i < GameModel.STONE_COUNT; i++) {
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
        errorLabel.setVisible(false);
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);

        try {
            var stone = (Ellipse) square.getChildren().get(0);
            handleClickOnSquare(position, stone);
            Logger.debug("Click on stone at ({}, {})", row, col);
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
    private void onRemoveButtonClick() throws IOException {
        model.removeStones(selectedPositions);

        if (model.isRemovableSelection(selectedPositions)) {
            for (var position : selectedPositions)
                getSquare(position).getChildren().clear();

            if (model.isEnd()) {
                switch (model.getCurrentPlayer()) {
                    case FIRST_PLAYER -> endGame(firstPlayerName);
                    case SECOND_PLAYER -> endGame(secondPlayerName);
                }
                Logger.info("Game finished! The winner is {}", model.getCurrentPlayer());
            }
            Logger.debug("Removed stones from these positions: {}, the next player is {}", selectedPositions.toString(), model.getCurrentPlayer());

        } else {
            errorLabel.setVisible(true);
            for (var position : selectedPositions) {
                model.deselectStone(position);
                var stone = (Ellipse) getSquare(position).getChildren().get(0);
                stone.setFill(DESELECTED_COLOR);
            }
            Logger.debug("Illegal step at these positions: {}", selectedPositions.toString());
        }
        selectedPositions.clear();

        switch (model.getCurrentPlayer()) {
            case FIRST_PLAYER -> currentPlayerLabel.setText(firstPlayerName + " következik");
            case SECOND_PLAYER -> currentPlayerLabel.setText(secondPlayerName + " következik");
        }
    }

    private void endGame (String winnerName) throws IOException {
        GameResultHandle.insertIntoResultTable(new GameResult(firstPlayerName, secondPlayerName, winnerName, LocalDateTime.now()));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ending.fxml"));
        Parent root = fxmlLoader.load();
        EndingController controller = fxmlLoader.<EndingController>getController();
        controller.setWinnerLabel(winnerName);
        Stage stage = (Stage) ((Node) board).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onExitButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/launch.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        Logger.info("Ongoing game has been terminated");
    }

}
