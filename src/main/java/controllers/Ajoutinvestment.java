package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Investment;
import service.investService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Ajoutinvestment {

    @FXML
    private AnchorPane mainId;

    @FXML
    private TextArea descTA;

    @FXML
    private TableColumn<Investment, String> descriptionCol;

    @FXML
    private TableColumn<Investment, String> imgCol;

    @FXML
    private TableView<Investment> tablev;


    @FXML
    private TableColumn<Investment, String> investissementCol;

    @FXML
    private TextField nomTF;

    @FXML
    private ImageView imgId;

    @FXML
    private TextField searchBtn;

    private Image photo;
    private String imagePath;

    private ObservableList<Investment> masterData = FXCollections.observableArrayList();


    @FXML
    void ajoutBtn(ActionEvent event) {
        if (nomTF.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez saisir un nom investissement.");
            alert.showAndWait();
            return;
        }

        investService is = new investService();
        Investment investment = new Investment();
        investment.setImage(imagePath);
        investment.setNom_invest(nomTF.getText());
        investment.setDescription_invest(descTA.getText());
        try {
            is.create(investment);
           updateTable();
            clearForm();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("success");
            alert.setContentText("investissement ajoutee");
            alert.showAndWait();
        }catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    private void updateTable(){
        try{
            investService is = new investService();

            List<Investment>investmentList=is.read();
            ObservableList<Investment>observableList=FXCollections.observableList(investmentList);
            tablev.setItems(observableList);
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void clearBtn(ActionEvent event) {
        clearForm();
    }

    private void clearForm(){
        nomTF.setText("");
        descTA.setText("");
        imgId.setImage(null);
        imagePath=null;
    }

    @FXML
    void detailsBtn(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/investmentDetails.fxml"));
        try {
            nomTF.getScene().setRoot(loader.load());
        }catch (IOException e){
            System.err.println(e.getMessage());
        }



    }

    @FXML
    void editBtn(ActionEvent event) {
        investService is = new investService();

        Investment selectedInvestment = tablev.getSelectionModel().getSelectedItem();
        if (selectedInvestment == null) {
            showAlert("Error", "No investment selected!", Alert.AlertType.ERROR);
            return;
        }

        selectedInvestment.setNom_invest(nomTF.getText());
        selectedInvestment.setDescription_invest(descTA.getText());
        selectedInvestment.setImage(imagePath); // Ensure the imagePath is updated in imgImpoBtn

        try {
            is.update(selectedInvestment);
            updateTable();
            clearForm();
            showAlert("Success", "Investment updated successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error", "Problem updating investment: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    void imgImpoBtn(ActionEvent event) {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File","*png","*jpg"));
        File file = openFile.showOpenDialog(mainId.getScene().getWindow());
        if(file != null){
            imagePath = file.toURI().toString();
            imgId.setImage(new Image(imagePath));
        }


    }

    @FXML
    void suppBtn(ActionEvent event) {
        Investment selectedInvestment = tablev.getSelectionModel().getSelectedItem();
        if (selectedInvestment==null){
            showAlert("erreur","no investissemnet selected",Alert.AlertType.ERROR);
            return;
        }
        try {
            investService is = new investService();
            is.delete(selectedInvestment.getId());
            updateTable();
            showAlert("success","investissement deleted successfully",Alert.AlertType.ERROR);


        }catch (SQLException e){
            showAlert("erreur","problem deleting invest: "+ e.getMessage(),Alert.AlertType.ERROR);
        }
    }

    @FXML
    void initialize() {
        setupImageColumn();
        loadData();
        investService is = new investService();
        //FilteredList<Investment> filteredData = null;
        try {
            updateTable();
            List<Investment> investmentList = is.read();
            masterData.setAll(investmentList);
            FilteredList<Investment> filteredData = new FilteredList<>(masterData, p -> true);

            SortedList<Investment> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tablev.comparatorProperty());

            tablev.setItems(sortedData);

            ObservableList<Investment> observableList = FXCollections.observableList(investmentList);
          //  tablev.setItems(observableList);
            imgCol.setCellValueFactory(new PropertyValueFactory<>("image"));
            investissementCol.setCellValueFactory(new PropertyValueFactory<>("nom_invest"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description_invest"));
            tablev.setOnMouseClicked(event -> onTableClick());

            searchBtn.textProperty().addListener((observable, oldValue, newValue) -> {

                filteredData.setPredicate(investment -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;  // Show all data when filter text is empty.
                    }

                    return investment.getNom_invest().toLowerCase().contains(newValue.toLowerCase());
                });
            });

        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }


    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void onTableClick() {
        Investment selectedInvestment = tablev.getSelectionModel().getSelectedItem();
        if (selectedInvestment != null) {
            nomTF.setText(selectedInvestment.getNom_invest());
            descTA.setText(selectedInvestment.getDescription_invest());
            imgId.setImage(new Image(selectedInvestment.getImage()));
            imagePath = selectedInvestment.getImage(); // Assuming image paths are used
        }
    }



    private void setupImageColumn() {
        imgCol.setCellValueFactory(new PropertyValueFactory<>("image"));  // Assuming 'image' holds the path
        imgCol.setCellFactory(new Callback<TableColumn<Investment, String>, TableCell<Investment, String>>() {
            @Override
            public TableCell<Investment, String> call(TableColumn<Investment, String> param) {
                return new TableCell<Investment, String>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String imagePath, boolean empty) {
                        super.updateItem(imagePath, empty);
                        if (empty || imagePath == null || imagePath.isEmpty()) {
                            setGraphic(null);
                        } else {
                            imageView.setImage(new Image(imagePath));
                            imageView.setFitHeight(50); // Adjust size as needed
                            imageView.setFitWidth(50);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
    }

    private void loadData() {
        investService is = new investService();
        try {
            List<Investment> investmentList = is.read();
            ObservableList<Investment> observableList = FXCollections.observableList(investmentList);
            tablev.setItems(observableList);
        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }



}
