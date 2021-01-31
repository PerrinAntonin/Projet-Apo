package Model;

import java.util.List;
import java.util.Set;

public class SIRModel extends BasicModel implements Model {

    public SIRModel(){
        this.params.put("beta",80/100.0);
        this.params.put("gamma",60/100.0);
    }

    public int[] numberOfPeople(){
        return new int[] { board.numberOfHealthy(), board.numberOfSick(), board.numberOfCured()};
    }

    public int[] stepInfection(){
        board.move();
        this.infect();
        return numberOfPeople();
    }

    public void infect() {
        List<Set<Actor>> sets = board.find();
        for (Set<Actor> as : sets) {
            List<Actor> healthy = board.getHealthy(as);
            List<Actor> sick = board.getSick(as);
            for (Actor aSick : sick) {
                for (Actor aHealthy : healthy) {
                    if (doInfect(aSick)) {
                        aHealthy.setState(Actor.State.SICK);
                    }
                }
                if (doCure(aSick)) {
                    aSick.setState(Actor.State.IMMUNE);
                }
            }
        }
    }
}
