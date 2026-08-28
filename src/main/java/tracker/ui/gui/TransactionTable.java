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
        descCol.setMinWidth(150);
        descCol.setPrefWidth(250);
        TableColumn<Transaction, Type> typeCol = new TableColumn<>("Typ");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setMinWidth(100);
        typeCol.setMaxWidth(150);

        TableColumn<Transaction, Double> sumCol = new TableColumn<>("Betrag (€)");
        sumCol.setCellValueFactory(new PropertyValueFactory<>("sum"));
        sumCol.setMinWidth(100);
        sumCol.setMaxWidth(150);

        this.getColumns().addAll(descCol, typeCol, sumCol);

    }
}