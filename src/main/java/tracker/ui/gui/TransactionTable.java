package tracker.ui.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tracker.model.Transaction;
import tracker.model.Type;

public class TransactionTable extends TableView<Transaction> {

    public TransactionTable() {
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Transaction, String> descCol = new TableColumn<>("Beschreibung");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Transaction, Type> typeCol = new TableColumn<>("Typ");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, Double> sumCol = new TableColumn<>("Betrag (€)");
        sumCol.setCellValueFactory(new PropertyValueFactory<>("sum"));

        this.getColumns().addAll(descCol, typeCol, sumCol);

    }
}