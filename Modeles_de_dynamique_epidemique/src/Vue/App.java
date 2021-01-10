package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

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

        int xMax= 20;
        int yMax= 20;
        double beta = 60/100.0;
        double gamma = 80/100.0;
        int nPop = 1000;
        int nPopSick = 10;

        Board board = new Board(xMax, yMax, beta, gamma, nPop, nPopSick);
        boardController = new BoardController(board);

        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setTitle("SIR Model");
        this.setVisible(true);
        setParamsFrame(xMax,yMax,beta,gamma, nPop, nPopSick);

        JFreeChart chart = ChartFactory.createXYLineChart("SIR Model", null, null, null, PlotOrientation.VERTICAL, true, true, true);
        ChartPanel test = new ChartPanel(chart);
        graphPanel.removeAll();
        graphPanel.add(test, BorderLayout.CENTER);
        graphPanel.revalidate();

//        double gamma = gammaS.getValue()/100.0;
//        double beta = betaS.getValue()/100.0;
//        int xMax = (Integer) xMaxSizeS.getValue();
//        int yMax = (Integer)yMaxSizeS.getValue();
//        int nPop = (Integer)nPopS.getValue();
//        int nPopSick = (Integer)nPopSickS.getValue();
//        board = new Board(xMax, yMax, beta, gamma, nPop, nPopSick);

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
