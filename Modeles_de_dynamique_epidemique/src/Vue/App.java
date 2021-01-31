package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.UIManager;

import Controller.CSVController;
import Controller.SimulationController;
import Model.SEIRBModel;
import Model.SEIRModel;
import Model.SIRModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

public class App extends JFrame {
    private SimulationController simulationController;

    private JPanel panelMain;
    private JPanel graphPanel;
    private JPanel SettingsPanel;
    private JPanel newSettingValueP;
    private JPanel newSettingLabelP;

    private JLabel ConsoleTest;
    private JButton resetModelButton;
    private JButton increm;

    private JComboBox SIRModelCB;

    private JSpinner nPopS;
    private JSpinner nPopSickS;
    private JSpinner yMaxSizeS;
    private JSpinner xMaxSizeS;
    private JLabel politiqueL;
    private JComboBox politiqueCB;
    private JButton downloadBtn;
    private Map<String, JSlider> params = new HashMap<>();

    public static void main(String[] args) {

//        try {
//            // Use the system look and feel for the swing application
//            String className = UIManager.getSystemLookAndFeelClassName();
//            UIManager.setLookAndFeel(className);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        App view = new App();
    }

    public App() {
        this.simulationController = new SimulationController();

        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setTitle("SIR Model");
        this.setVisible(true);
        setParamsFrame(simulationController.getBoardParams());

        JFreeChart chart = ChartFactory.createXYLineChart("SIR Model", null, null, null, PlotOrientation.VERTICAL, true, true, true);
        ChartPanel test = new ChartPanel(chart);

        graphPanel.removeAll();
        graphPanel.add(test, BorderLayout.CENTER);
        graphPanel.revalidate();


        ChangeListener listenerModelParams = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                Map<String, Double> modelParams = new HashMap<String, Double>();

                for (String labelName : params.keySet()) {
                    if (labelName.equals("birthRate") || labelName.equals("deathRate")) {
                        modelParams.put(labelName, params.get(labelName).getValue() / 1000.);
                    } else {
                        modelParams.put(labelName, params.get(labelName).getValue() / 100.);
                    }

                }

                simulationController.changeModelParams(modelParams);
            }
        };

        SIRModelCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = SIRModelCB.getSelectedItem().toString();
                switch (name) {
                    case "SIR":
                        newSettingLabelP.removeAll();
                        newSettingValueP.removeAll();
                        params.clear();

                        SIRModel sirmodel = new SIRModel();
                        Map<String, Double> paramsMapSir = sirmodel.getParams();
                        for (String labelName : paramsMapSir.keySet()) {
                            addSetting(labelName, listenerModelParams, paramsMapSir.get(labelName));
                        }

                        simulationController.changeModel(sirmodel);
                        graphPanel.revalidate();
                        break;

                    case "SEIR":
                        newSettingLabelP.removeAll();
                        newSettingValueP.removeAll();
                        params.clear();

                        SEIRModel seirmodel = new SEIRModel();
                        Map<String, Double> paramsMapSeir = seirmodel.getParams();
                        for (String labelName : paramsMapSeir.keySet()) {
                            addSetting(labelName, listenerModelParams, paramsMapSeir.get(labelName));
                        }
                        simulationController.changeModel(seirmodel);
                        graphPanel.revalidate();
                        break;

                    case "SIR with birth":
                        newSettingLabelP.removeAll();
                        newSettingValueP.removeAll();
                        params.clear();
                        SEIRBModel seirbmodel = new SEIRBModel();
                        Map<String, Double> paramsMapSeirb = seirbmodel.getParams();
                        for (String labelName : paramsMapSeirb.keySet()) {
                            addSetting(labelName, listenerModelParams, paramsMapSeirb.get(labelName));
                        }
                        simulationController.changeModel(seirbmodel);
                        graphPanel.revalidate();
                        break;
                    default:
                        break;
                }
            }
        });

        SIRModelCB.setSelectedItem("SIR");


        resetModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                politiqueCB.setSelectedItem("Aucune");
                // Regenerate Chart
                JFreeChart chart = ChartFactory.createXYLineChart(null, null, null, null, PlotOrientation.VERTICAL, true, true, true);
                ChartPanel newchartpanel = new ChartPanel(chart);
                graphPanel.removeAll();
                graphPanel.add(newchartpanel, BorderLayout.CENTER);
                graphPanel.revalidate();

                int xMax = (Integer) xMaxSizeS.getValue();
                int yMax = (Integer) yMaxSizeS.getValue();
                int nPop = (Integer) nPopS.getValue();
                int nPopSick = (Integer) nPopSickS.getValue();
                simulationController.reset(xMax, yMax, nPop, nPopSick);
            }
        });

        increm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulationController.iterate(5);
                updateChart();
            }
        });

        politiqueCB.addItem("Aucune");
        politiqueCB.addItem("Confinement");
        politiqueCB.addItem("Port du masque");
        politiqueCB.addItem("Quarantaine");
        politiqueCB.addItem("Vaccination");

        politiqueCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String petName = (String) politiqueCB.getSelectedItem();
                switch (Objects.requireNonNull(petName)) {
                    case "Confinement" -> simulationController.setPolitic(SimulationController.Politic.CONFINEMENT);
                    case "Port du masque" -> simulationController.setPolitic(SimulationController.Politic.PORTDUMASQUE);
                    case "Quarantaine" -> simulationController.setPolitic(SimulationController.Politic.QUARANTAINE);
                    case "Vaccination" -> simulationController.setPolitic(SimulationController.Politic.VACCINATION);
                    default -> simulationController.setPolitic(SimulationController.Politic.AUCUNE);
                }
            }
        });

        downloadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CSVController.donwloadData("test.csv",simulationController.getDataset());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        });

    }

    public void addSetting(String labelName, ChangeListener listener, double value) {
        JLabel lab = new JLabel(labelName);
        lab.setAlignmentX(Component.RIGHT_ALIGNMENT);
        newSettingLabelP.setLayout(new BoxLayout(newSettingLabelP, BoxLayout.Y_AXIS));
        newSettingLabelP.add(lab, BorderLayout.EAST);
        JSlider slider = new JSlider();
        if (labelName.equals("birthRate") || labelName.equals("deathRate")) {
            slider.setValue((int) (value * 1000));
            int test = (int) (value * 1000);
        } else {
            slider.setValue((int) (value * 100));
        }
        if (labelName.equals("birthRate")) {
            int test = (int) (value * 1000);
            ConsoleTest.setText(String.valueOf(test));
        }
        slider.addChangeListener(listener);
        params.put(labelName, slider);
        newSettingValueP.setLayout(new BoxLayout(newSettingValueP, BoxLayout.Y_AXIS));
        newSettingValueP.add(slider);
    }

    private void updateChart() {
        JFreeChart chart = ChartFactory.createXYLineChart("Model", null, null, simulationController.getDataset(), PlotOrientation.VERTICAL, true, true, true);

        graphPanel.removeAll();
        graphPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        graphPanel.revalidate();
    }

    public void setParamsFrame(int[] mapParams) {
        nPopSickS.setValue(mapParams[3]);
        nPopS.setValue(mapParams[2]);
        xMaxSizeS.setValue(mapParams[0]);
        yMaxSizeS.setValue(mapParams[1]);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        String params[] = {"SIR", "SEIR", "SIR with birth"};

        this.SIRModelCB = new JComboBox(params);
    }
}
