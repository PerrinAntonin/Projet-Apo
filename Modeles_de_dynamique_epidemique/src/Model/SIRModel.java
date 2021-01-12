package Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SIRModel implements Model {

    Map<String, Double> params = new HashMap<String, Double>();
    private Board board;

    private String[] states = {"Number of susceptible", "Number of infected", "Number of recovered"};

    public SIRModel(double beta, double gamma, Board board){
        this.params.put("beta",beta);
        this.params.put("gamma",gamma);
        this.board = board;
        this.initActors();
    }
    public SIRModel(){
        this.params.put("beta",80/100.0);
        this.params.put("gamma",60/100.0);
    }

    public SIRModel(double beta, double gamma){
        this.params.put("beta",beta);
        this.params.put("gamma",gamma);
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public void initActors(){
        board.modifyActors(this.params);
    }

    public void setModelParams(Map<String, Double> params ){
        this.params=params;
    }

    public int[] stepInfection(){
        board.move();
        this.infect();
        return new int[] { board.numberOfHealthy(), board.numberOfSick(), board.numberOfCured()};
    }

    public void infect() {
        List<Set<Actor>> sets = board.find();
        for (Set<Actor> as : sets) {
            List<Actor> healthy = board.getHealthy(as);
            List<Actor> sick = board.getSick(as);
            for (Actor a : sick) {
                if (doInfect(a)) {
                    setAll(healthy, Actor.State.SICK);
                }

                if (doCure(a)) {
                    a.setState(Actor.State.IMMUNE);
                }
            }
        }
    }

    public void setAll(List<Actor> as, Actor.State state) {
        for (Actor a : as) {
            a.setState(state);
        }
    }

    public boolean doInfect(Actor a){
        double beta = getActorBeta(a);
        return Math.random() < beta;
    }

    public boolean doCure(Actor a){
        double gamma = getActorGamma(a);
        return Math.random() < gamma;
    }

    public double getActorGamma(Actor a){
        return a.getParams().get("gamma");
    }

    public double getActorBeta(Actor a){
        return a.getParams().get("beta");
    }

    public String[] getStates() {
        return states;
    }

    public void addBoard(Board board){
        this.board = board;
    }

    public Map<String, Double> getParams(){return this.params;}
}
