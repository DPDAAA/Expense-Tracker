package tracker.ui.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tracker.model.Type;
import tracker.service.TransactionManager;

public class TrackerApp extends Application {

    private TransactionManager manager = new TransactionManager();

    @Override
    public void start(Stage primaryStage) {
        // --- HAUPTMENÜ (Main Window) ---
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Mitte: Hier bauen wir später die Tabelle (TableView) ein
        Label tablePlaceholder = new Label("Hier erscheint bald deine Ausgaben-Übersicht.");
        tablePlaceholder.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
        root.setCenter(tablePlaceholder);

        // Unten: Aktionsleiste (HBox ordnet Buttons nebeneinander an)
        HBox actionBar = new HBox(15);
        actionBar.setAlignment(Pos.CENTER);
        actionBar.setPadding(new Insets(20, 0, 0, 0));

        Button addButton = new Button("+ Neue Transaktion");
        addButton.setStyle("-fx-base: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;"); // Grüner Button

        Button refreshButton = new Button("Aktualisieren");

        actionBar.getChildren().addAll(addButton, refreshButton);
        root.setBottom(actionBar);

        // Event: Klick öffnet das Pop-up
        addButton.setOnAction(e -> openAddTransactionWindow());

        // Szene laden
        Scene scene = new Scene(root, 600, 400);
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
                Type type = Type.valueOf(typeBox.getValue()); // String -> Enum
                double sum = Double.parseDouble(amountField.getText().replace(",", "."));

                System.out.println("Erfolgreich gespeichert!");
                popupStage.close();
                manager.addTransaction(desc, type, sum);
                manager.saveTransactions();
            } catch (NumberFormatException ex) {
                System.out.println("Fehler: Bitte eine gültige Zahl für den Betrag eingeben!");
            } catch (IllegalArgumentException ex) {
                System.out.println("Fehler beim Erstellen der Transaktion.");
            }

            System.out.println("Test - Speichere: " + descField.getText() + " | " + amountField.getText());
            popupStage.close();
        });

        Scene popupScene = new Scene(grid, 350, 250);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}