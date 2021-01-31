package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SEIRBModel extends BasicModel implements Model {

    public SEIRBModel() {
        this.states = new String[]{"Number of susceptible", "Number of exposed", "Number of infected", "Number of recovered"};

        this.params.put("beta", 80 / 100.0);
        this.params.put("gamma", 60 / 100.0);
        this.params.put("sigma", 70 / 100.0);
        this.params.put("birthRate", 5 / 1000.0);
        this.params.put("deathRate", 5 / 1000.0);
    }

    public int[] numberOfPeople() {
        return new int[]{board.numberOfHealthy(), board.numberOfExposed(), board.numberOfSick(), board.numberOfCured()};
    }

    public int[] stepInfection() {
        board.move();
        this.infect();
        return numberOfPeople();
    }

    public void infect() {
        List<Set<Actor>> sets = board.find();
        for (Set<Actor> as : sets) {
            List<Actor> tmpActor = new ArrayList<>(as);
            for (Actor a : tmpActor) {
                if (giveDeath(a) && a.getState() != Actor.State.DEAD) {
                    a.setState(Actor.State.DEAD);
                }
                if (giveBirth(a) && a.getState() != Actor.State.DEAD) {
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

    public boolean giveBirth(Actor a) {
        double birthRate = getActorBirthRate(a);
        return Math.random() < birthRate;
    }

    public boolean giveDeath(Actor a) {
        double deadRate = getActorDeadRate(a);
        return Math.random() < deadRate;
    }

    public double getActorBirthRate(Actor a) {
        return a.getParams().get("birthRate");
    }

    public double getActorDeadRate(Actor a) {
        return a.getParams().get("deathRate");
    }

    public double getActorSigma(Actor a) {
        return a.getParams().get("sigma");
    }

}
