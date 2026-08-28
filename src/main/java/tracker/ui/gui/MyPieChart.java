package tracker.ui.gui;

import javafx.scene.chart.PieChart;
import tracker.model.Type;
import tracker.service.TransactionManager;

public class MyPieChart extends PieChart {

    PieChart.Data einnahmeData;
    PieChart.Data ausgabenData;

    public MyPieChart(TransactionManager manager) {

        einnahmeData = new PieChart.Data("Einnahmen", manager.getTotal(Type.EINNAHME));
        ausgabenData = new PieChart.Data("Ausgaben", manager.getTotal(Type.AUSGABE));
        this.getData().addAll(einnahmeData, ausgabenData);
        this.setTitle("Finanz-Übersicht");
        this.setVisible(true);
    }

    public void updateChart(TransactionManager manager) {
        einnahmeData.setPieValue(manager.getTotal(Type.EINNAHME));
        ausgabenData.setPieValue(manager.getTotal(Type.AUSGABE));
    }
}
