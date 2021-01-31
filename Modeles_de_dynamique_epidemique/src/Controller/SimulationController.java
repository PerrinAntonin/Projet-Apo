package Controller;

import Model.Board;
import Model.Model;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SimulationController {
    public enum Politic {
        AUCUNE, CONFINEMENT, PORTDUMASQUE, QUARANTAINE, VACCINATION
    }

    private Board board;
    private Model model;
    private List<XYSeries> dataset = new ArrayList<>();
    private int index = 0;

    private int xMax = 25;
    private int yMax = 25;
    private int nPop = 10000;
    private int nPopSick = 100;
    private Politic politic = Politic.AUCUNE;

    public void setPolitic(Politic politic) {
        this.politic = politic;
        switch (politic) {
            case CONFINEMENT:
                resetPolitic();
                board.setConfinement(0.3);
                break;
            case PORTDUMASQUE:
                resetPolitic();
                board.setMask();
                break;
            case QUARANTAINE:
                resetPolitic();
                board.setQuarantaine();
                break;
            case VACCINATION:
                resetPolitic();
                board.setVacination(.05);
                break;
            default:
                // Reset Model params
                resetPolitic();
        }
    }
    private void resetPolitic(){
        board.resetParams();
        board.setConfinement(.8);
    }

    public SimulationController() {
        this.board = new Board(xMax, yMax, nPop, nPopSick);
    }

    public void initDataset() {
        this.dataset.clear();
        String[] states = this.model.getStates();
        for (String state : states) {
            this.dataset.add(new XYSeries(state));
        }
    }

    /**
     * @param model
     */
    public void changeModel(Model model) {
        this.model = model;
        this.model.setBoard(board);
        this.model.initActors();
        this.initDataset();
    }

    /**
     * @param xMax
     * @param yMax
     * @param nPop
     * @param nPopSick
     */
    public void reset(int xMax, int yMax, int nPop, int nPopSick) {
        this.board.init(xMax, yMax, nPop, nPopSick);
        this.model.initActors();
        this.dataset = new ArrayList<>();
        for (String StateName : model.getStates()) {
            this.dataset.add(new XYSeries(StateName));
        }

        index = 0;
        int[] initNBPeople = model.numberOfPeople();
        for (int j = 0; j < initNBPeople.length; j++) {
            this.dataset.get(j).add(index, initNBPeople[j]);
        }
    }

    /**
     * @param nb
     */
    public void iterate(int nb) {
        for (int i = 0; i < nb; i++) {
            int[] result = model.stepInfection();
            for (int j = 0; j < result.length; j++) {
                this.dataset.get(j).add(index + 1, result[j]);
            }
            index++;
        }
    }

    /**
     * @param paramsActor
     */
    public void changeModelParams(Map<String, Double> paramsActor) {
        board.modifyActors(paramsActor);
        model.setModelParams(paramsActor);
    }

    public int[] getBoardParams() {
        return new int[]{xMax, yMax, nPop, nPopSick};
    }

    public XYSeriesCollection getDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (XYSeries serie : this.dataset) {
            dataset.addSeries(serie);
        }
        return dataset;
    }
}
