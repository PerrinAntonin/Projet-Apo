package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Controller.BoardController;
import Model.Board;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class App extends JFrame{
    private BoardController boardController;
    private JPanel panelMain;
    private JPanel graphPanel;
    private JPanel SettingsPanel;
    private JPanel NewSettingPanel;

    private JLabel ConsoleTest;
    private JButton resetModelButton;
    private JButton increm;

    private JComboBox SIRModelCB;

    private JSpinner nPopS;
    private JSpinner nPopSickS;
    private JSpinner yMaxSizeS;
    private JSpinner xMaxSizeS;
    private JSlider gammaS;
    private JSlider betaS;

    private JLabel gammaL;
    private JLabel betaL;
    private JLabel SIRModelL;

    public static void main(String[] args)   {
        App view = new App();
    }

    public App() {

        boardController = new BoardController();

        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setTitle("SIR Model");
        this.setVisible(true);
        setParamsFrame(boardController.getBoardParams()[0],boardController.getBoardParams()[1],boardController.getModelParams()[0],boardController.getModelParams()[1], boardController.getBoardParams()[2], boardController.getBoardParams()[3]);

        JFreeChart chart = ChartFactory.createXYLineChart("SIR Model", null, null, null, PlotOrientation.VERTICAL, true, true, true);
        ChartPanel test = new ChartPanel(chart);
        graphPanel.removeAll();
        graphPanel.add(test, BorderLayout.CENTER);
        graphPanel.revalidate();


        resetModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFreeChart chart = ChartFactory.createXYLineChart(null, null, null, null, PlotOrientation.VERTICAL, true, true, true);
                ChartPanel test = new ChartPanel(chart);
                graphPanel.removeAll();
                graphPanel.add(test, BorderLayout.CENTER);
                graphPanel.revalidate();
            }
        });

        increm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardController.iterate(10);
                updateChart();
//                JLabel lab1 = new JLabel("User Name", JLabel.RIGHT);
//                NewSettingPanel.setLayout(new FlowLayout());
//                NewSettingPanel.add(lab1 = new JLabel("add JLabel"));

            }
        });

        resetModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double gamma = gammaS.getValue()/100.0;
                double beta = betaS.getValue()/100.0;
                int xMax = (Integer) xMaxSizeS.getValue();
                int yMax = (Integer)yMaxSizeS.getValue();
                int nPop = (Integer)nPopS.getValue();
                int nPopSick = (Integer)nPopSickS.getValue();
                boardController.reset(xMax,yMax,nPop,nPopSick,beta,gamma);

            }
        });

        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ConsoleTest.setText("Value changed");
                double gamma = gammaS.getValue()/100.0;
                double beta = betaS.getValue()/100.0;
                int xMax = (Integer) xMaxSizeS.getValue();
                int yMax = (Integer)yMaxSizeS.getValue();
                int nPop = (Integer)nPopS.getValue();
                int nPopSick = (Integer)nPopSickS.getValue();
                boardController.changeParams(xMax,yMax,nPop,nPopSick,beta,gamma);
            }
        };

        nPopS.addChangeListener(listener);
        nPopSickS.addChangeListener(listener);
        yMaxSizeS.addChangeListener(listener);
        xMaxSizeS.addChangeListener(listener);
        gammaS.addChangeListener(listener);
        betaS.addChangeListener(listener);
    }

    private void updateChart() {
        JFreeChart chart = ChartFactory.createXYLineChart("SIR Model", null, null, boardController.getDataset(), PlotOrientation.VERTICAL, true, true, true);

        ChartPanel test = new ChartPanel(chart);
        graphPanel.removeAll();
        graphPanel.add(test, BorderLayout.CENTER);
        graphPanel.revalidate();

    }

    public void setParamsFrame(int xMax,int yMax,double beta,double gamma, int nPop, int nPopSick){
        nPopSickS.setValue(nPopSick);
        nPopS.setValue(nPop);
        gammaS.setValue((int)(gamma*100.0));
        betaS.setValue((int)(beta*100.0));
        xMaxSizeS.setValue(xMax);
        yMaxSizeS.setValue(yMax);
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
