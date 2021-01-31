package Model;

import java.util.HashMap;
import java.util.Map;


public class Actor {
    public enum State {
        SICK, HEALTHY, IMMUNE, EXPOSED, DEAD
    }

    private Map<String, Double> params = new HashMap<>();
    private Map<String, Double> defaultParams;

    private State state;
    private int row;
    private int col;

    public Actor(State state, int row, int col) {
        this.state = state;
        this.row = row;
        this.col = col;
    }

    /**
     * @param params
     */
    public void setParams(Map<String, Double> params){
        if(this.defaultParams == null){
            this.defaultParams= new HashMap<>();
            for (Map.Entry<String, Double> entry : params.entrySet()) {
                this.defaultParams.put(entry.getKey(),entry.getValue());
            }
        }

        for (Map.Entry<String, Double> entry : params.entrySet()) {
            this.params.put(entry.getKey(),entry.getValue());
        }

    }

    public Map<String, Double> getParams(){return params; }
    public Map<String, Double> getDefaultParams(){return defaultParams; }

    public State getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

    public int getRow() {
        return row;
    }

    /**
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    /**
     * @param col
     */
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
