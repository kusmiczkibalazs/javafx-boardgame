package boardgame.javafx.controller;

import boardgame.results.GameResult;
import boardgame.results.GameResultHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResultsController {

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn<GameResult, String> firstPlayer;

    @FXML
    private TableColumn<GameResult, String> secondPlayer;

    @FXML
    private TableColumn<GameResult, String> winner;

    @FXML
    private TableColumn<GameResult, LocalDateTime> gameDate;

    @FXML
    private void initialize () {
        firstPlayer.setCellValueFactory(new PropertyValueFactory<>("firstPlayer"));
        secondPlayer.setCellValueFactory(new PropertyValueFactory<>("secondPlayer"));
        winner.setCellValueFactory(new PropertyValueFactory<>("winner"));
        gameDate.setCellValueFactory(new PropertyValueFactory<>("gameDate"));

        gameDate.setCellFactory(column -> {
            TableCell<GameResult, LocalDateTime> cell = new TableCell<GameResult, LocalDateTime>() {
                private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty)
                        setText(null);
                    else
                        setText(item.format(formatter));
                }
            };
            return cell;
        });

        var gameResults = GameResultHandle.selectFromResultTable();
        ObservableList<GameResult> observableList = FXCollections.observableArrayList();
        observableList.addAll(gameResults);
        tableView.setItems(observableList);
    }

    @FXML
    private void onBackButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/launch.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
