package boardgame.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LaunchScreenController {

    @FXML
    private Label rulesLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize(){
        rulesLabel.setVisible(false);
        errorLabel.setVisible(false);
    }

    @FXML
    private void onStartButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/board.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onHighscoresButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/highscores.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onRulesButtonClick() {
        if(! rulesLabel.isVisible() ) {
            rulesLabel.setVisible(true);
        } else {
            rulesLabel.setVisible(false);
        }
    }
}