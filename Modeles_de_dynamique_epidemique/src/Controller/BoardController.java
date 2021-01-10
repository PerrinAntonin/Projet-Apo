package Controller;

import Model.Actor;
import Model.Board;
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

    public BoardController(Board board){
        this.board = board;
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
                this.dataset.get(j).add(index, arr[j]);
            }
            index++;
        }

    }

    public void reset(){
        board.move();
        board.infect();
    }

}
