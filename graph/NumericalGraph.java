package sample.graph;

import javafx.scene.chart.XYChart;

public abstract class NumericalGraph extends Graph {
    String name;

    protected XYChart.Series<Double, Double> seriesErr;

    protected XYChart.Series<Double, Double> totalErr;

    protected double actSolution(Double c, Double x) {
        return Math.sqrt(2 + c * Math.exp(1/x));
    }

    public XYChart.Series<Double, Double> getSeriesErr() {
        return seriesErr;
    }

    public XYChart.Series<Double, Double> getTotalErr() {
        return totalErr;
    }

    protected Double findDer(Double x, Double y) {
        return (2-y*y) / (2*x*x*y);
    }

    public void findTotalErr(Double x0, Double y0, Double X, int N1, int N2) {
        totalErr = new XYChart.Series<>();
        totalErr.setName(name);
        for (int i = N1; i < N2; i++ ) {
            totalErr.getData().add(new XYChart.Data<>((double) i, findErr(x0, y0, X, i)));
        }
    }

    @Override
    protected void initialize (Double x0, Double y0) {
        seriesNeg = new XYChart.Series<>();
        seriesPos = new XYChart.Series<>();
        seriesPos.getData().add(new XYChart.Data<>(x0, y0));
        seriesNeg.getData().add(new XYChart.Data<>(x0, -y0));
    }

    public abstract Double findErr(Double x0, Double y0, Double X, int N);
}
