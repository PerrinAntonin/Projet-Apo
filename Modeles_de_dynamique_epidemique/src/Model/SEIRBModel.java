package Model;

import java.util.*;

public class SEIRBModel implements Model{

    Map<String, Double> params = new HashMap<String, Double>();
    private Board board;
    private String[] states = {"Number of susceptible", "Number of exposed", "Number of infected", "Number of recovered"};

    public SEIRBModel(){
        this.params.put("beta",80/100.0);
        this.params.put("gamma",60/100.0);
        this.params.put("sigma",70/100.0);
        this.params.put("birthRate", 2/100.0);
    }

    public String[] getStates() { return states; }


    /**
     * @param board
     */
    public void setBoard(Board board){ this.board = board; }

    public void initActors(){ board.modifyActors(this.params); }

    /**
     * @param params
     */
    public void setModelParams(Map<String, Double> params ){ this.params=params; }

    public int[] numberOfPeople(){
        return new int[] { board.numberOfHealthy(), board.numberOfExposed(), board.numberOfSick(), board.numberOfCured()};
    }

    public int[] stepInfection(){
        board.move();
        this.infect();
        return numberOfPeople();
    };


    public void infect() {
        List<Set<Actor>> sets = board.find();
        for (Set<Actor> as : sets) {
            List<Actor> tmpActor = new ArrayList<>(as);
            for (Actor a : tmpActor) {
                if (giveBirth(a)) {
                    Actor newActor = board.actorBirth();
                    newActor.setParams(params);
                }
            }

            List<Actor> healthy = board.getHealthy(as);
            List<Actor> sick = board.getSick(as);
            List<Actor> exposed = board.getExposed(as);

            for (Actor aHealthy : healthy) {
                if (doExpose(aHealthy)) {
                    aHealthy.setState(Actor.State.EXPOSED);
                }
            }
            for (Actor aSick : sick) {
                for (Actor aExposed : exposed) {
                    if (doInfect(aSick)) {
                        aExposed.setState(Actor.State.SICK);
                    }
                }
                if (doCure(aSick)) {
                    aSick.setState(Actor.State.IMMUNE);
                }

            }
        }
    }


    public boolean doExpose(Actor a) {
        double sigma = getActorSigma(a);
        return Math.random() < sigma;
    }

    /**
     * @param a
     * @return
     */
    public boolean doInfect(Actor a){
        double beta = getActorBeta(a);
        return Math.random() < beta;
    }

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
    public boolean giveBirth(Actor a){
        double birthRate = getActorBirthRate(a);
        return Math.random() < birthRate;
    }

    /**
     * @param a
     * @return
     */
    public double getActorBirthRate(Actor a) {
        return a.getParams().get("birthRate");
    }

    /**
     * @param a
     * @return
     */
    public double getActorSigma(Actor a) {
        return a.getParams().get("sigma");
    }

    /**
     * @param a
     * @return
     */
    public double getActorGamma(Actor a){ return a.getParams().get("gamma"); }

    /**
     * @param a
     * @return
     */
    public double getActorBeta(Actor a){ return a.getParams().get("beta"); }

    /**
     * @param board
     */
    public void addBoard(Board board){ this.board = board; }

    public Map<String, Double> getParams(){return this.params;}
}
