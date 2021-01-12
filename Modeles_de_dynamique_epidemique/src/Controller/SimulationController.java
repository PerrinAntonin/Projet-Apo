package Controller;

import Model.Actor;
import Model.Board;
import Model.Model;
import Model.SIRModel;
import Vue.App;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimulationController {
    private Board board;
    private Model model;
    private List<XYSeries> dataset = new ArrayList<>();
    private int index;

    private int xMax= 20;
    private int yMax= 20;
    private double beta = 60/100.0;
    private double gamma = 80/100.0;
    private int nPop = 1000;
    private int nPopSick = 10;


    public SimulationController(){
        index = 0;
        this.board = new Board(xMax, yMax, nPop, nPopSick);

        this.model = new SIRModel(beta, gamma, this.board);
        this.initDataset();
    }
    public void initDataset(){
        this.dataset.clear();
        String[] states = this.model.getStates();
        for (String state:states) {
            this.dataset.add(new XYSeries(state));
        }
    }

    public void changeModel(Model model){
        this.model = model;
        board.numberOfCured();
        this.model.setBoard(board);
        this.model.initActors();
        this.initDataset();
    }

    public void reset(int xMax, int yMax, int nPop, int nPopSick){
        this.board.init(xMax, yMax, nPop, nPopSick);
        this.model.initActors();
        this.dataset = new ArrayList<>();
        for (String StateName: model.getStates()) {
            this.dataset.add(new XYSeries(StateName));
        }
        index = 0;
    }

    public void iterate(int nb){
        for(int i=0; i<nb; i++){
            int[] result = model.stepInfection();
            for(int j=0;j< result.length;j++){
                this.dataset.get(j).add(index, result[j]);
            }
//            this.dataset.get(3).add(index, 100);
            index++;
        }
    }

    public void changeParams(int xMax, int yMax, int nPop, int nPopSick, Map<String, Double> paramsActor){
        board.modifyActors(paramsActor);
        board.modifyBoard(xMax,yMax,nPop);
        model.setModelParams(paramsActor);
        // TODO ADD nPopSick
    }

    public int[] getBoardParams(){
        return new int[] {xMax, yMax, nPop, nPopSick};
    }
    public Map<String, Double> getModelParams(){return model.getParams();}

    public XYSeriesCollection getDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for(XYSeries serie : this.dataset) {
            dataset.addSeries(serie);
        }
        return dataset;
    }
}
