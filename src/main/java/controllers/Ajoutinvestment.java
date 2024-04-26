package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Ajoutinvestment {
    @FXML
    private TextField textage;

    @FXML
    private TextField textnom;

    @FXML
    private TextField textprenom;


    @FXML
    public void ajoutPersonneAction() {
        textage.setText("");
        textnom.setText("");
        textprenom.setText("");
    }


    public void load_add(ActionEvent actionEvent) throws IOException {
try{
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/formulaireAddInvest.fxml"));
        Parent root = loader.load();

        // Optionally get the controller if you need to pass data to it
        formulaireAddInvest controller = loader.getController();

        // Set up the stage and scene
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("New Window");
        stage.show();

        // Optionally close the current window if this is a full switch
        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}
