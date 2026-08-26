package tracker.ui.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tracker.model.Transaction;
import tracker.model.Type;
import tracker.service.TransactionManager;

public class TrackerApp extends Application {

    private TransactionManager manager = new TransactionManager();

    // Hilfsfunktion
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) {
        // --- HAUPTMENÜ (Main Window) ---
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        TableView<Transaction> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Transaction, String> descCol = new TableColumn<>("Beschreibung");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Transaction, Type> typeCol = new TableColumn<>("Typ");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, Double> sumCol = new TableColumn<>("Betrag (€)");
        sumCol.setCellValueFactory(new PropertyValueFactory<>("sum"));

        table.getColumns().addAll(descCol, typeCol, sumCol);

        if (manager.getContainer() != null) {
            table.getItems().addAll(manager.getContainer());
        }

        Label tableTitle = new Label("Transaktionshistorie");
        tableTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        VBox history = new VBox(10, tableTitle, table);
        history.setAlignment(Pos.CENTER_LEFT);
        root.setCenter(history);

        HBox actionBar = new HBox(15);
        actionBar.setAlignment(Pos.CENTER);
        actionBar.setPadding(new Insets(20, 0, 0, 0));

        Button addButton = new Button("+ Neue Transaktion");
        addButton.setStyle("-fx-base: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;"); // Grüner Button

        Button refreshButton = new Button("Aktualisieren");
        refreshButton.setStyle("-fx-base: #2900e2; -fx-text-fill: white;");
        Button deleteButton = new Button("Löschen");
        deleteButton.setStyle("-fx-base: #f44336; -fx-text-fill: white;");
        actionBar.getChildren().addAll(addButton, deleteButton ,refreshButton);
        root.setBottom(actionBar);

        refreshButton.setOnAction(e -> {
            if (manager.getContainer() != null) {
                table.getItems().clear();
                table.getItems().addAll(manager.getContainer());
            }

        });

        deleteButton.setOnAction(e -> {
            
            Transaction selectedTransaction = table.getSelectionModel().getSelectedItem();

            if (selectedTransaction == null) {
                
                showErrorAlert("Keine Auswahl", "Nichts ausgewählt",
                        "Bitte klicke zuerst auf eine Transaktion in der Tabelle.");
                return;
            }

            
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Transaktion löschen");
            confirmAlert.setHeaderText("Bist du sicher?");
            confirmAlert.setContentText("Möchtest du '" + selectedTransaction.getDescription() + "' wirklich löschen?");

            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {

                 
                    manager.deleteTransaction(selectedTransaction.getId());
                    manager.saveTransactions();

                    
                    table.getItems().remove(selectedTransaction);
                }
            });
        });
        // Event: Klick öffnet das Pop-up
        addButton.setOnAction(e -> openAddTransactionWindow());

        // Szene laden
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Expense Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- POP-UP FENSTER ---
    private void openAddTransactionWindow() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Transaktion hinzufügen");

        // Blockiert das Hauptfenster, bis das Pop-up geschlossen ist
        popupStage.initModality(Modality.APPLICATION_MODAL);

        // GridPane ordnet Elemente perfekt in einem Raster (Spalten/Zeilen) an
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10); // Horizontaler Abstand
        grid.setVgap(15); // Vertikaler Abstand

        // Zeile 1: Beschreibung
        Label descLabel = new Label("Beschreibung:");
        TextField descField = new TextField();
        descField.setPromptText("z. B. Supermarkt");

        // Zeile 2: Typ (Dropdown-Menü verhindert falsche Eingaben!)
        Label typeLabel = new Label("Typ:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("AUSGABE", "EINNAHME");
        typeBox.setValue("AUSGABE"); // Standardwert

        // Zeile 3: Betrag
        Label amountLabel = new Label("Betrag (€):");
        TextField amountField = new TextField();
        amountField.setPromptText("0.00");

        // Ins Grid einfügen (Element, Spalte, Zeile)
        grid.add(descLabel, 0, 0);
        grid.add(descField, 1, 0);
        grid.add(typeLabel, 0, 1);
        grid.add(typeBox, 1, 1);
        grid.add(amountLabel, 0, 2);
        grid.add(amountField, 1, 2);

        // Zeile 4: Buttons
        Button saveButton = new Button("Speichern");
        saveButton.setStyle("-fx-base: #2196F3; -fx-text-fill: white;"); // Blauer Button
        Button cancelButton = new Button("Abbrechen");

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, 3); // Spalte 1, Zeile 3

        // Events für Pop-up
        cancelButton.setOnAction(e -> popupStage.close());

        saveButton.setOnAction(e -> {
            try {
                String desc = descField.getText();
                Type type = Type.valueOf(typeBox.getValue());
                double sum = Double.parseDouble(amountField.getText().replace(",", "."));

                if (sum < 0) {
                    showErrorAlert("Ungültiger Wert", "Negativer Betrag nicht erlaubt",
                            "Bitte geben Sie einen positiven Betrag für die Transaktion ein.");
                    return;
                }
                popupStage.close();
                manager.addTransaction(desc, type, sum);
                manager.saveTransactions();
            } catch (NumberFormatException ex) {

                showErrorAlert("Eingabefehler", "Ungültiges Format",
                        "Bitte eine gültige Zahl für den Betrag eingeben!");
            } catch (IllegalArgumentException ex) {
                showErrorAlert("Fehler", "Transaktion fehlgeschlagen", "Bitte überprüfen Sie Ihre Auswahl.");
            }

            System.out.println("Test - Speichere: " + descField.getText() + " | " + amountField.getText());
            System.out.println("Erfolgreich gespeichert!");

            popupStage.close();
        });

        Scene popupScene = new Scene(grid, 350, 250);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}