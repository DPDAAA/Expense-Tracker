package tracker.ui.console;

import java.util.InputMismatchException;
import java.util.Scanner;

import tracker.model.TransactionContainer;
import tracker.model.Type;
import tracker.service.TransactionManager;
import tracker.storage.TransactionFileStorage;

public class TransactionUserInput {

    Scanner scanner = new Scanner(System.in);

    public void closeScanner() {
        this.scanner.close();
    }

    public char inputSwitch() {

        while (true) {

            System.out.println("Eingabe:");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                System.out.println("---Bitte Eingabe!---");
                continue;
            }
            return input.charAt(0);
        }
    }

    public String inputDescription() {

        System.out.print("---Beschreibung---: ");
        return scanner.nextLine();

    }

    public Type inputType() {

        Type type = null;
        while (type == null) {
            System.out.print("---Typ (AUSGABE/EINNAHME)---: ");
            String typeInput = scanner.nextLine().toUpperCase().trim();

            try {
                type = Type.valueOf(typeInput);
            } catch (IllegalArgumentException e) {
                System.err.println("Ungültiger Typ! Bitte entweder 'Ausgabe' oder 'Eingabe'");
            }
        }
        return type;
    }

    public double inputSum() {
        System.out.print("---Summe---: ");

        while (true) {
            try {
                double sum = scanner.nextDouble();
                scanner.nextLine(); // Ist hier um beim Double das x\n "\n" zu löschen, sonst Probleme beim
                                    // nächsten
                                    // Durchgang im Puffer (Pufferfehler)

                if (sum <= 0) {
                    System.err.println("Die Summe darf nicht negativ sein oder Nulls ein!");
                    System.out.print("---Summe---: ");

                    continue;
                }

                return sum;
            } catch (InputMismatchException e) {
                System.err.println("Nur double-Werte als Summe!");
                scanner.nextLine(); // ungültige Eingabe entfernen
                System.out.print("---Summe---: ");

            }

        }

    }

    public int inputTransactionId() {
        System.out.print("Welche Transaktion wollen sie löschen?\nID: ");

        int kern = -1;
        while (kern == -1) {
            try {
                int kern1 = Integer.parseInt(scanner.nextLine());
                if (kern1 >= 0) {
                    kern = kern1;
                } else {
                    System.out.println("Bitte eine positive ganze Zahl eingeben:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Bitte eine positive ganze Zahl eingeben:");

            }
        }

        return kern;
    }

    public String inputConfirmation() {

        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("j") || input.equals("n")) {
                return input;
            }

            System.out.print("Bitte 'j' oder 'n' eingeben: ");
        }
    }

    public void startConsoleMenu() {

        TransactionContainer Container = TransactionFileStorage.loadTransactions("Speicher.txt");
        TransactionManager manager = new TransactionManager();
        TransactionUserInput user = new TransactionUserInput();

        System.out.println("---g: für Ausgabe des Verlaufs---");
        System.out.println("---h: für die Hinzugabe einer Transaktion---");
        System.out.println("---j: für das Löschen einer Transaktion---");
        System.out.println("---q: Speichern der Transaktionen---");
        System.out.println("---k: Beenden des Programms---");
        boolean modified = false;

        while (true) {

            switch (user.inputSwitch()) {

                case 'g':
                    manager.TransactionHistory(Container);
                    break;
                case 'h':
                    manager.addTransaction(Container, user.inputDescription(), user.inputType(), user.inputSum());
                    System.out.println("---Transaktion erfolgreich hinzugefügt!");
                    modified = false;
                    break;
                case 'j':

                    int id = user.inputTransactionId();
                    boolean deleted = manager.deleteTransaction(Container, id);
                    if (deleted) {
                        System.out.println("Transaktion mit ID: " + id + " wurde gelöscht!");
                        modified = false;
                    } else {
                        System.out.println("Keine Transaktion mit dieser ID gefunden.");
                    }
                    break;

                case 'q':
                    manager.saveTransactions(Container, "Speicher.txt");
                    modified = true;
                    break;

                case 'k':
                    if (!modified) {
                        System.out.println("Sie haben noch nicht gespeichert!");
                        System.out.print("Möchten Sie wirklich ohne zu speichern beenden? (j/n): ");

                        String confirmation = user.inputConfirmation();

                        if (confirmation.equals("n")) {
                            System.out.println("---Zurück zum Programm---");
                            break;
                        }
                    }

                    user.closeScanner();
                    System.out.println("---Beendet!---");
                    return;

                default:
                    System.out.println("---Bitte Anweisungen befolgen!---");

            }

        }
    }
}