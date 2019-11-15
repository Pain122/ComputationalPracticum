package sample.graph;

import javafx.scene.chart.XYChart;

public class RungeKutta extends NumericalGraph {

    public RungeKutta() {
        name = "Runge-Kutta";
        mode = false;
        seriesPos = new XYChart.Series<>();
        seriesNeg = new XYChart.Series<>();
        seriesErr = new XYChart.Series<>();
    }

    @Override
    public Double findErr(Double x0, Double y0, Double X, int N) {
        double eMax = 0.0;
        seriesErr = new XYChart.Series<>(); //Re-initialize because there may be stored previous evaluations
        seriesErr.setName("Runge-kutta");
        double h = (X - x0)/N;
        Double c = findC(x0, y0);
        Double x = x0; //used to store x value at i th iteration
        double y; //used to store y value at i th iteration
        for (int i = 0; i < N; i++) {
            y = actSolution(c, x);
            double k1 = findDer(x, y);
            double k2 = findDer(x + h / 2, y + h * k1 / 2);
            double k3 = findDer(x + h / 2, y + h * k2 / 2);
            x = x + h;
            double k4 = findDer(x, y + h * k3);
            y = actSolution(c, x) - y - (k1 + 2*k2 + 2*k3 + k4)*h/6;
            seriesErr.getData().add(new XYChart.Data<>(x, y));
            if (eMax < Math.abs(y)) {
                eMax = Math.abs(y);
            }
        }
        return eMax;
    }

    @Override
    public void solve(Double x0, Double y0, Double X, int N) {
        // y(x+1) = y(x) = (k1 + 2k2 + 2k3 + k4) * h/6
        initialize(x0, y0);
        seriesPos.setName("Runge-Kutta");
        Double h = (X - x0) / N;
        Double x = x0;
        Double y = y0;
        for (int i = 1; i <= N; i++) {
            double k1 = findDer(x, y);
            double k2 = findDer(x + h / 2, y + h * k1 / 2);
            double k3 = findDer(x + h / 2, y + h * k2 / 2);
            double k4 = findDer(x + h, y + h * k3);
            x = x + h;
            y = y + (k1 + 2*k2 + 2*k3 + k4)*h/6;
            seriesPos.getData().add(new XYChart.Data<>(x, y));
            seriesNeg.getData().add(new XYChart.Data<>(x, -y));
        }
    }
}
