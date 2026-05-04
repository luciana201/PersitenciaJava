package pio.daw.ra8.proyecto.ui;

import pio.daw.ra8.proyecto.model.Individuo;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

import java.util.List;

public class GraficaDistribucion {

    public static void mostrar(List<Individuo> individuos) {
        double[] saldos = individuos.stream()
            .mapToDouble(Individuo::getSaldoActual)
            .toArray();

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Individuos", saldos, 10);

        JFreeChart chart = ChartFactory.createHistogram(
            "Distribucion de Riqueza - Mercado Libre",
            "Saldo (unidades monetarias)",
            "Numero de individuos",
            dataset
        );

        ChartFrame frame = new ChartFrame("Mercado Libre", chart);
        frame.pack();
        frame.setVisible(true);
    }
}