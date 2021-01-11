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
import java.util.Set;

public class BoardController {
    private Board board;
    private List<XYSeries> dataset = new ArrayList<>();
    private int index;

    private int xMax= 20;
    private int yMax= 20;
    private double beta = 60/100.0;
    private double gamma = 80/100.0;
    private int nPop = 1000;
    private int nPopSick = 10;


    public BoardController(){
        this.board = new Board(xMax, yMax, beta, gamma, nPop, nPopSick);
        dataset.add(new XYSeries("Number of susceptible"));
        dataset.add(new XYSeries("Number of infected"));
        dataset.add(new XYSeries("Number of recovered"));
        index = 0;
    }

    public void reset(int xMax, int yMax, int nPop, int nPopSick,double beta,double gamma){
        board.init(xMax, yMax, beta, gamma, nPop,nPopSick);
        this.dataset = new ArrayList<>();
        dataset.add(new XYSeries("Number of susceptible"));
        dataset.add(new XYSeries("Number of infected"));
        dataset.add(new XYSeries("Number of recovered"));
        index = 0;
    }

    public XYSeriesCollection getDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for(XYSeries serie : this.dataset) {
            dataset.addSeries(serie);
        }
        return dataset;
    }

    public void iterate(int nb){
        for(int i=0; i<nb; i++){
            board.move();
            board.infect();
            int arr[] = board.numberTotal();
            for(int j=0;j< arr.length;j++){
                this.dataset.get(j);
                this.dataset.get(j).add(index, arr[j]);
            }
            index++;
        }
    }

    public void changeParams(int xMax,int yMax,int nPop,int nPopSick,double beta,double gamma){
        board.modifyActors(gamma,beta);
        board.modifyBoard(xMax,yMax,nPop);
        // ADD nPopSick
        //board.init(xMax, yMax, beta,gamma, nPop);
    }

    public int[] getBoardParams(){
        return new int[] {xMax, yMax, nPop, nPopSick};
    }
    public double[] getModelParams(){
        return new double[] {beta, gamma};
    }
//    public
//    beta, gamma,
}
