package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import sample.graph.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private int mode; //used to change graphs plotted
    private Double x0;
    private Double y0;
    private Double X;
    private Integer N;
    private Integer N2;
    private Graph[] graphs;

    @FXML
    CheckBox analytical;

    @FXML
    CheckBox euler;

    @FXML
    CheckBox improvedEuler;

    @FXML
    CheckBox rungeKutta;

    @FXML
    LineChart<Double, Double> chart;

    @FXML
    Button plotGraphs;

    @FXML
    Label sysStat;

    @FXML
    TextField inpX0;

    @FXML
    TextField inpY0;

    @FXML
    TextField inpX;

    @FXML
    TextField inpN;

    @FXML
    TextField inpN2;

    @FXML
    ComboBox<String> comboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBox.getItems().addAll("Solution graphs", "Error graphs", "Total Error graphs");
        sysStat.setDisable(true);
        mode = 0;
        chart.getXAxis().setLabel("X - AXIS");
        chart.getYAxis().setLabel("Y - AXIS");
        plotGraphs.setDisable(true);
        graphs = new Graph[4];
        graphs[0] = new AnalyticalGraph();
        graphs[0].setMode(false);
        graphs[1] = new EulerGraph();
        graphs[1].setMode(false);
        graphs[2] = new ImprovedEulerGraph();
        graphs[2].setMode(false);
        graphs[3] = new RungeKutta();
        graphs[3].setMode(false);
    }

    public boolean readInp() {
        //Initializes the values with the one given in textboxes
        sysStat.setText("");
        try {
                x0 = Double.parseDouble(inpX0.getText());
                y0 = Double.parseDouble(inpY0.getText());
                X = Double.parseDouble(inpX.getText());
                N = Integer.parseInt(inpN.getText());
                if (x0 > X) {
                    sysStat.setText("x0 > X");
                    return false;
                }
                if (mode == 3) { //we do not need this field if checkbox is not in mode 3
                    N2 = Integer.parseInt(inpN2.getText());
                }
                return true; //to say that reading was successful
            }
         catch (NumberFormatException | NullPointerException nfe) { //case if we have no input or not a number
            sysStat.setText("Wrong input");
            sysStat.setDisable(false);
            return false; //to say that reading was unsuccessful
        }
    }

    public void setMode(ActionEvent event) {
        //sets the mode of the graph which defines whether it should be plotted or not
        if (analytical.isSelected()) {
            graphs[0].setMode(true);
        } else {
            graphs[0].setMode(false);
        }

        if (euler.isSelected()) {
            graphs[1].setMode(true);
        } else {
            graphs[1].setMode(false);
        }

        if (improvedEuler.isSelected()) {
            graphs[2].setMode(true);
        } else {
            graphs[2].setMode(false);
        }

        if (rungeKutta.isSelected()) {
            graphs[3].setMode(true);
        } else {
            graphs[3].setMode(false);
        }
    }

    private void disableAll() {
        //Disable if we change the item in comboBox
        plotGraphs.setDisable(false);
        analytical.setSelected(false);
        euler.setSelected(false);
        improvedEuler.setSelected(false);
        rungeKutta.setSelected(false);
        for (int i = 0; i < 4; i++) {
            graphs[i].setMode(false);
        }
    }

    public void comboChanged(ActionEvent event) {
        disableAll();
        switch (comboBox.getValue()) {
            case "Solution graphs":
                mode = 1; //in this case we need all fields except from N2
                analytical.setDisable(false);
                inpN2.setDisable(true);
                inpN2.setText("");
                break;
            case "Error graphs":
                mode = 2; //in this case we need all fields except from N2 and we must not plot Analytical graph
                graphs[0].setMode(false);
                analytical.setDisable(true);
                inpN2.setDisable(true);
                inpN2.setText("");
                break;
            case "Total Error graphs":
                mode = 3; //in this case we need all fields and we must not plot Analytical graph
                graphs[0].setMode(false);
                analytical.setDisable(true);
                inpN2.setDisable(false);
        }
    }

    @FXML
    public void plotGraphs(ActionEvent event) {
        if (readInp()) {
            sysStat.setText("");
            chart.getData().clear();
            if (mode == 1) {
                for (int i = 0; i < 4; i++) {
                    if ((x0 < 0) && (X > 0) && (i == 0)) { //case when we have 4 pieces of graph (which is not supported properly in JavaFX)
                        sysStat.setText("Undef numeric methods");
                        if (graphs[i].isMode()) {
                            graphs[i].solve(x0, y0, X, N);
                            chart.getData().add(graphs[i].getSeriesPos());
                            chart.getData().add(graphs[i].getSeriesNeg());
                            chart.getData().add(((AnalyticalGraph) graphs[i]).getSeriesPosAsymptote());
                            chart.getData().add(((AnalyticalGraph) graphs[i]).getSeriesNegAsymptote());
                        }
                    } else { //normal behaviour of Analytical Solution
                        if (! ((x0 < 0) && (X > 0))) {  //if we have an asymptote then there is undefined behaviour of numerical methods, and they will not be printed, because it leads to mistakes
                            if (graphs[i].isMode()) {
                                graphs[i].solve(x0, y0, X, N);
                                chart.getData().add(graphs[i].getSeriesPos());
                                chart.getData().add(graphs[i].getSeriesNeg());
                            }
                        }
                    }
                }
            }
            if (mode == 2) { //plotting ErrorGraphs
                for (int i = 1; i < 4; i++) {
                    if (graphs[i].isMode()) {
                        ((NumericalGraph) graphs[i]).findErr(x0, y0, X, N); //will always work, since graphs[i] are numerical for 1 <= i <= 3
                        chart.getData().add(((NumericalGraph) graphs[i]).getSeriesErr());
                    }
                }
            }
            if (mode == 3) { //Plotting TotalErrors
                for (int i = 1; i < 4; i++) {
                    if (graphs[i].isMode()) {
                        ((NumericalGraph) graphs[i]).findTotalErr(x0, y0, X, N, N2); //will always work, since graphs[i] are numerical for 1 <= i <= 3
                        chart.getData().add(((NumericalGraph) graphs[i]).getTotalErr());
                    }
                }
            }
        }
    }
}
