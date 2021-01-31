package Model;

import java.util.List;
import java.util.Set;

public class SEIRModel extends BasicModel implements Model {

    public SEIRModel() {
        this.states = new String[]{"Number of susceptible", "Number of exposed", "Number of infected", "Number of recovered"};
        this.params.put("sigma", 70 / 100.0);
        this.params.put("beta", 80 / 100.0);
        this.params.put("gamma", 60 / 100.0);
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
    public double getActorSigma(Actor a) {
        return a.getParams().get("sigma");
    }
}
