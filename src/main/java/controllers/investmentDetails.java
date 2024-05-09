package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import okhttp3.*;
import org.json.JSONObject;


import java.io.IOException;

public class investmentDetails {

    @FXML
    private TextField resultDisplay;

    @FXML
    private TextField amountDollar;


    public AnchorPane root;
    @FXML
    private Text test;


    @FXML
    private LineChart<String, Number> returnChart;
    @FXML
    private TextField amountField;
    @FXML
    private BarChart<String, Number> investmentChart;

    @FXML
    public void initialize() {
        setupChart();

        XYChart.Series<String, Number> qatarSeries = new XYChart.Series<>();
        qatarSeries.setName("Qatar");
        qatarSeries.getData().add(new XYChart.Data<>("2020", 100));
        qatarSeries.getData().add(new XYChart.Data<>("2021", 120));
        qatarSeries.getData().add(new XYChart.Data<>("2022", 140));
        qatarSeries.getData().add(new XYChart.Data<>("2023", 160));

        XYChart.Series<String, Number> emiratesSeries = new XYChart.Series<>();
        emiratesSeries.setName("Emirates");
        emiratesSeries.getData().add(new XYChart.Data<>("2020", 200));
        emiratesSeries.getData().add(new XYChart.Data<>("2021", 220));
        emiratesSeries.getData().add(new XYChart.Data<>("2022", 250));
        emiratesSeries.getData().add(new XYChart.Data<>("2023", 300));

        XYChart.Series<String, Number> saudiSeries = new XYChart.Series<>();
        saudiSeries.setName("Saudi Arabia");
        saudiSeries.getData().add(new XYChart.Data<>("2020", 150));
        saudiSeries.getData().add(new XYChart.Data<>("2021", 180));
        saudiSeries.getData().add(new XYChart.Data<>("2022", 190));
        saudiSeries.getData().add(new XYChart.Data<>("2023", 210));

        investmentChart.getData().addAll(qatarSeries, emiratesSeries, saudiSeries);

    }

    private void setupChart() {
        returnChart.setTitle("Projected Investment Growth Over Months");
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Investment Value ($)");
    }

    @FXML
    private void calculateReturn(ActionEvent event) {
        String input = amountField.getText();
        try {
            double initialAmount = Double.parseDouble(input);
            double returnRate = 0.07; // 7% monthly return rate
            updateChart(initialAmount, returnRate, 12); // Calculate for 12 months
        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + input);
            // Optionally, show an error message on the GUI
        }
    }

    private void updateChart(double initialAmount, double monthlyRate, int months) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Investment Over Time");

        double currentAmount = initialAmount;
        for (int i = 1; i <= months; i++) {
            currentAmount *= (1 + monthlyRate); // Compound interest calculation
            series.getData().add(new XYChart.Data<>(Integer.toString(i), currentAmount));
        }

        returnChart.getData().clear();
        returnChart.getData().add(series);
    }


    @FXML
    void back(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ajoutinvestment.fxml"));
        try {
            test.getScene().setRoot(loader.load());
        }catch (IOException e){
            System.err.println(e.getMessage());
        }


    }

    @FXML
    void conversion(ActionEvent event) {

        String amountInDollars = amountDollar.getText();
        String apiUrl = "https://v6.exchangerate-api.com/v6/44a5a670a5601febb1c5c859/pair/USD/TND/" + amountInDollars;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                javafx.application.Platform.runLater(() -> resultDisplay.setText("Failed to fetch data"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    javafx.application.Platform.runLater(() -> resultDisplay.setText("Request failed"));
                    return;
                }

                String jsonData = response.body().string();
                // Assume the API returns the converted amount directly
                JSONObject jsonObject = new JSONObject(jsonData);
                double conversionResult = jsonObject.getDouble("conversion_result");

                javafx.application.Platform.runLater(() -> resultDisplay.setText(String.format("%.3f", conversionResult) + " TND"));
            }
        });

    }

}
