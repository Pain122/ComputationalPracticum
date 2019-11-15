package sample.graph;

import javafx.scene.chart.XYChart;

public abstract class Graph {

    protected XYChart.Series<Double, Double> seriesPos; //used because graph is symmetric and Javafx does not support multiple values of y
    protected  XYChart.Series<Double, Double> seriesNeg;
    protected final int N = 3000;
    protected boolean mode;

    protected double findC(double x0, double y0) {
        return (y0 * y0 - 2) / Math.exp(1 / x0);
    }

    public XYChart.Series<Double, Double> getSeriesPos() {
        return seriesPos;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public XYChart.Series<Double, Double> getSeriesNeg() {
        return seriesNeg;
    }

    public abstract void solve(Double x0, Double y0, Double X, int N);

    protected abstract void initialize (Double x0, Double y0);
}
