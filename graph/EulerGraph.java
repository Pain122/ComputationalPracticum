package sample.graph;

import javafx.scene.chart.XYChart;

public class EulerGraph extends NumericalGraph {

    public EulerGraph() {
        name = "Euler";
        mode = false;
        seriesPos = new XYChart.Series<>();
        seriesNeg = new XYChart.Series<>();
        seriesErr = new XYChart.Series<>();
    }

    @Override
    public Double findErr(Double x0, Double y0, Double X, int N) {
        double eMax = 0.0;
        seriesErr = new XYChart.Series<>(); //Re-initialize because there may be stored previous evaluations
        seriesErr.setName("Euler Method");
        double h = (X - x0)/N;
        Double c = findC(x0, y0);
        Double x = x0; //used to store x value at i th iteration
        double y; //used to store y value at i th iteration
        for (int i = 0; i < N; i++) {
            y = actSolution(c,x);
            x = x + h;
            y = actSolution(c, x) - y - h * (findDer(x - h, y));
            seriesErr.getData().add(new XYChart.Data<>(x, y));
            if (eMax < Math.abs(y)) {
                eMax = Math.abs(y);
            }
        }
        return eMax;
    }

    @Override
    public void solve(Double x0, Double y0, Double X, int N) {
        initialize(x0, y0);
        seriesPos.setName("Euler Method");
        Double h = (X - x0) / N;
        Double x = x0;
        Double y = y0;
        for (int i = 1; i <= N; i++) {
            y = y + h * findDer(x, y);
            x = x + h;
            seriesPos.getData().add(new XYChart.Data<>(x, y));
            seriesNeg.getData().add(new XYChart.Data<>(x, -y));
        }
    }
}
