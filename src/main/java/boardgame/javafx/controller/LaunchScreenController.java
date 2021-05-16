package boardgame.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class LaunchScreenController {

    @FXML
    private TextField firstPlayerLabel;

    @FXML
    private TextField secondPlayerLabel;

    @FXML
    private Label rulesLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize(){
        rulesLabel.setVisible(false);
    }

    @FXML
    private void onStartButtonClick(ActionEvent event) throws IOException {
        if (firstPlayerLabel.getText().isEmpty() || secondPlayerLabel.getText().isEmpty()) {
            errorLabel.setText("Add meg a játékosok neveit!");
        } else if (firstPlayerLabel.getText().equals(secondPlayerLabel.getText())) {
            errorLabel.setText("A két játékos neve ne legyen megegyező!");
        } else {
            Logger.info("Names entered: {}, {}", firstPlayerLabel.getText(), secondPlayerLabel.getText());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/board.fxml"));
            Parent root = fxmlLoader.load();
            GameController controller = fxmlLoader.<GameController>getController();
            controller.setPlayerNames(firstPlayerLabel.getText(), secondPlayerLabel.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML
    private void onResultsButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/results.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onRulesButtonClick() {
        errorLabel.setText("");

        if(! rulesLabel.isVisible() ) {
            rulesLabel.setVisible(true);
        } else {
            rulesLabel.setVisible(false);
        }
    }
}