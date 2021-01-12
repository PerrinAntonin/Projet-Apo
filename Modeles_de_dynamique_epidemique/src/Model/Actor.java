package Model;

import java.util.HashMap;
import java.util.Map;


public class Actor {
    public enum State {
        SICK, HEALTHY, IMMUNE, EXPOSED
    }

    private Map<String, Double> params = new HashMap<>();

    private State state;
    private int row;
    private int col;

    public Actor(State state, int row, int col) {
        this.state = state;
        this.row = row;
        this.col = col;
    }

    public void setParams(Map<String, Double> params){
        this.params = params;
    }

    public void modParams(String key,double value){
        this.params.put(key,value);
    }

    public Map<String, Double> getParams(){return params; }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }


    @Override
    public String toString() {
        return "Actor{" +
                "state=" + state +
                ", row=" + row +
                ", col=" + col +
                '}';
    }
}
