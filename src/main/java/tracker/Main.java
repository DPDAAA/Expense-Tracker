package tracker;
import javafx.application.Application;
import tracker.ui.console.TransactionUserInput;
import tracker.ui.gui.TrackerApp;

public class Main {

    public static void main(String[] args) {    

        if (args.length > 0 && args[0].equals("--console")) {
            System.out.println("Starte Konsolen-Modus...");
            TransactionUserInput consoleApp = new TransactionUserInput(); 
            consoleApp.startConsoleMenu();
  
        } else {
            System.out.println("Starte grafische Benutzeroberfläche...");
            Application.launch(TrackerApp.class, args);
        }
    }
}
