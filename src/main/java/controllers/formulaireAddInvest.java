package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Investment;
import service.iService;
import service.investService;

import java.sql.SQLException;

public class formulaireAddInvest {
    @FXML
    private TextArea description;

    @FXML
    private TextField name_inv;
    private iService iService = new investService();

    @FXML
    void save(ActionEvent event)  throws SQLException {
        if (name_inv.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez saisir un nom de investment.");
            alert.showAndWait();
            return;
        }

        // Assume name_inv and description are TextField components from JavaFX
        String nameInvText = name_inv.getText();
        String description = this.description.getText();

        // It is unclear why the first parameter for Investment is null. You may need to verify this.
        Investment inv = new Investment("akjdakjd", this.name_inv.getText(), this.description.getText());

        // Check if nameInvText and description are neither null nor empty
        if (nameInvText != null && !nameInvText.isEmpty() && description != null && !description.isEmpty()) {
            this.iService.create(inv);
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Success in adding new investement");
            alert.show();
        }
    }

}
