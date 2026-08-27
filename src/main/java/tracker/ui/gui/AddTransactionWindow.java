package tracker.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tracker.model.Type;
import tracker.service.TransactionManager;

public class AddTransactionWindow extends Stage {

    private TransactionManager manager;

    public AddTransactionWindow(TransactionManager manager) {
        this.manager = manager;

        this.setTitle("Transaktion hinzufügen");
        this.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(15);

        Label descLabel = new Label("Beschreibung:");
        TextField descField = new TextField();
        descField.setPromptText("z. B. Supermarkt");

        Label typeLabel = new Label("Typ:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("AUSGABE", "EINNAHME");
        typeBox.setValue("AUSGABE");

        Label amountLabel = new Label("Betrag (€):");
        TextField amountField = new TextField();
        amountField.setPromptText("0.00");

        grid.add(descLabel, 0, 0);
        grid.add(descField, 1, 0);
        grid.add(typeLabel, 0, 1);
        grid.add(typeBox, 1, 1);
        grid.add(amountLabel, 0, 2);
        grid.add(amountField, 1, 2);

        Button saveButton = new Button("Speichern");
        saveButton.setStyle("-fx-base: #2196F3; -fx-text-fill: white;");
        Button cancelButton = new Button("Abbrechen");

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, 3);

        
        cancelButton.setOnAction(e -> this.close());

        saveButton.setOnAction(e -> {
            try {
                String desc = descField.getText();
                Type type = Type.valueOf(typeBox.getValue());
                double sum = Double.parseDouble(amountField.getText().replace(",", "."));

                if (sum < 0) {
                    showErrorAlert("Ungültiger Wert", "Negativer Betrag nicht erlaubt",
                            "Bitte geben Sie einen positiven Betrag ein.");
                    return;
                }
                
                
                this.manager.addTransaction(desc, type, sum);
                this.manager.saveTransactions();
                
                this.close();
            } catch (NumberFormatException ex) {
                showErrorAlert("Eingabefehler", "Ungültiges Format", "Bitte eine gültige Zahl eingeben!");
            } catch (IllegalArgumentException ex) {
                showErrorAlert("Fehler", "Transaktion fehlgeschlagen", "Bitte überprüfen Sie Ihre Auswahl.");
            }
        });

        Scene scene = new Scene(grid, 350, 250);
        this.setScene(scene);
    }

    
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}