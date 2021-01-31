package Model;

import java.util.HashMap;
import java.util.Map;

public class BasicModel {
    protected String[] states = {"Number of susceptible", "Number of infected", "Number of recovered"};
    protected Map<String, Double> params = new HashMap<String, Double>();
    protected Board board;

    public String[] getStates() {
        return states;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void initActors() {
        board.modifyActors(this.params);
    }

    public void setModelParams(Map<String, Double> params) {
        this.params = params;
    }

    public Map<String, Double> getParams() {
        return this.params;
    }

    /**
     * @param a
     * @return
     */
    public boolean doInfect(Actor a) {
        double beta = getActorBeta(a);
        return Math.random() < beta;
    }

    /**
     * @param a
     * @return
     */
    public double getActorBeta(Actor a){ return a.getParams().get("beta"); }

    /**
     * @param a
     * @return
     */
    public boolean doCure(Actor a){
        double gamma = getActorGamma(a);
        return Math.random() < gamma;
    }

    /**
     * @param a
     * @return
     */
    public double getActorGamma(Actor a) {
        return a.getParams().get("gamma");
    }

}
