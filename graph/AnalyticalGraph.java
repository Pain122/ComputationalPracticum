package sample.graph;

import javafx.scene.chart.XYChart;

public class AnalyticalGraph extends Graph {
    private XYChart.Series<Double, Double> seriesPosAsymptote;
    private XYChart.Series<Double, Double> seriesNegAsymptote;

    public AnalyticalGraph() {
        mode = false;
        seriesPos = new XYChart.Series<>();
        seriesNeg = new XYChart.Series<>();
    }

    @Override
    public void solve(Double x0, Double y0, Double X, int n) { //I do not use the argument "n" because we need the analytical graph be as precise as it is possible
        initialize(x0, y0);
        seriesPos.setName("Analytical Graph");
        int N = 3000;
        double h = (X-x0) / N;
        double c = findC(x0, y0);

        Double x = x0;
        Double y;

        if ((x0 < 0) && (X > 0)) { //we have an asymptote in x = 0, so we need to calculate (x0, -0.1) to not get to infinity
            seriesPosAsymptote = new XYChart.Series<>();
            seriesNegAsymptote = new XYChart.Series<>();
            while (x < -0.1) { // stop at point x < -0.1 to not show big values of y. It's because it would scale program to big y axis and smaller x axis, which would make plotting pointless
                x = x + h;
                y = Math.sqrt(2 + c * Math.exp(1 / x));
                seriesPos.getData().add(new XYChart.Data<>(x, y));
                seriesNeg.getData().add(new XYChart.Data<>(x, -y));
            }
            x = 0.7 - h; //used to not show big values of y
            while (x < X) {
                x = x + h;
                y = Math.sqrt(2 + c * Math.exp(1 / x));
                seriesPosAsymptote.getData().add(new XYChart.Data<>(x, y));
                seriesNegAsymptote.getData().add(new XYChart.Data<>(x, -y));
            }
            y = Math.sqrt(2 + c * Math.exp(1 / X));
            seriesPosAsymptote.getData().add(new XYChart.Data<>(X, y));
            seriesNegAsymptote.getData().add(new XYChart.Data<>(X, -y));
        } else { //if we do not have asymptote There is no problem in calculating the graph
            for (int i = 0; i <= N; i++) {
                x = x0 + h * i;
                y = Math.sqrt(2 + c * Math.exp(1 / x));
                seriesPos.getData().add(new XYChart.Data<>(x, y));
                seriesNeg.getData().add(new XYChart.Data<>(x, -y));
            }
        }
    }

    @Override
    protected void initialize(Double x0, Double y0) {
        seriesNeg = new XYChart.Series<>();
        seriesPos = new XYChart.Series<>();
        if (y0 > 0) {
           seriesPos.getData().add(new XYChart.Data<>(x0, y0));
           seriesNeg.getData().add(new XYChart.Data<>(x0, -y0));
        } else {
           seriesPos.getData().add(new XYChart.Data<>(x0, -y0));
           seriesNeg.getData().add(new XYChart.Data<>(x0, y0));
        }
    }


    public XYChart.Series<Double, Double> getSeriesPosAsymptote() {
        return seriesPosAsymptote;
    }

    public XYChart.Series<Double, Double> getSeriesNegAsymptote() {
        return seriesNegAsymptote;
    }
}
