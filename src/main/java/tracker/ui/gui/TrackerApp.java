package tracker.ui.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TrackerApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        Label titleLabel = new Label("Neue Ausgabe hinzufügen:");
        TextField amountInput = new TextField();
        amountInput.setPromptText("Betrag eingeben (z.B. 10.50)");
        
        Button saveButton = new Button("Speichern");

        saveButton.setOnAction(event -> {
            String inputText = amountInput.getText();
            System.out.println("Gespeichert: " + inputText);
            

            
            amountInput.clear(); 
        });

        
        VBox root = new VBox(10); 
        root.setPadding(new Insets(20)); 
        
        
        root.getChildren().addAll(titleLabel, amountInput, saveButton);

        
        Scene scene = new Scene(root, 400, 300);
        
        primaryStage.setTitle("Expense Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}