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

public class EndingController {

    @FXML
    private Label winnerLabel;

    @FXML
    public void setWinnerLabel(String winnerName) {
        this.winnerLabel.setText("A játékot " + winnerName + " nyerte!");
    }

    @FXML
    private void onBackButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/launch.fxml"));
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
}
