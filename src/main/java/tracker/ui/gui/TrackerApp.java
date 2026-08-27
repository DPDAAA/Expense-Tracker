package tracker.ui.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tracker.model.Transaction;
import tracker.service.TransactionManager;

public class TrackerApp extends Application {

    
    private TransactionManager manager = new TransactionManager();
    private TransactionTable table = new TransactionTable();

    
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) {
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        
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
        addButton.setStyle("-fx-base: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button deleteButton = new Button("Löschen");
        deleteButton.setStyle("-fx-base: #f44336; -fx-text-fill: white;");

        Button refreshButton = new Button("Aktualisieren");
        refreshButton.setStyle("-fx-base: #2900e2; -fx-text-fill: white;");

        actionBar.getChildren().addAll(addButton, deleteButton, refreshButton);
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

        addButton.setOnAction(e -> {

            AddTransactionWindow addWindow = new AddTransactionWindow(manager);

            addWindow.showAndWait();

            
            if (manager.getContainer() != null) {
                table.getItems().clear();
                table.getItems().addAll(manager.getContainer());
            }
        });

        Scene scene = new Scene(root, 600, 400);

        try {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.out.println("Tipp: style.css wurde noch nicht im resources Ordner gefunden.");
        }

        primaryStage.setTitle("Expense Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}