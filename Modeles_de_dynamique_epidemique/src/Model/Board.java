package Model;

import Model.Actor;
import Model.Actor.State;

import java.util.*;

public class Board {
    private int maxRow;
    private int maxCol;
    private int minRow = 0;
    private int minCol = 0;

    private Double movable = 0.8;
    private Double vacination = 0.;

    private Random rand = new Random();
    public List<Actor> actors = new ArrayList<>();
    private List<Set<Actor>> sets = new ArrayList<>();


    public Board(int maxRow, int maxCol, int nPop, int nPopSick) {
        init(maxRow, maxCol, nPop, nPopSick);
    }

    /**
     * @param maxRow
     * @param maxCol
     * @param nActor
     * @param nPopSick
     */
    public void init(int maxRow, int maxCol, int nActor, int nPopSick) {
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        this.clearActors();
        for (int i = 0; i < nPopSick; i++) {
            addActor(gerRandomActor(Actor.State.SICK, maxRow, maxCol));
        }
        if (nActor > nPopSick) {
            for (int i = 0; i < nActor - nPopSick; i++) {
                addActor(gerRandomActor(Actor.State.HEALTHY, maxRow, maxCol));
            }
        }
    }

    public void clearActors() {
        actors = new ArrayList<>();
        sets = new ArrayList<>();
    }

    public void modifyActors(Map<String, Double> params) {
        for (Actor a : actors) {
            a.setParams(params);
        }
    }

    public List<Set<Actor>> find() {
        for (int row = 0; row <= maxRow; row++) {
            for (int col = 0; col <= maxCol; col++) {
                Set<Actor> actorSet = new HashSet<>();
                for (Actor a : actors) {
                    if (a.getRow() == row && a.getCol() == col) {
                        actorSet.add(a);
                    }
                }
                sets.add(actorSet);
            }
        }
        return sets;
    }

    public void move() {
        for (Actor a : actors) {
            step(a);
        }
    }

    public void setConfinement(Double value) {
        this.movable = value;
    }

    /**
     * @param a
     */
    public void step(Actor a) {
        // vacine une certaine partie de la population
        if (this.vacination > 0.) {
            if (a.getState() != State.IMMUNE) {
                if (Math.random() < this.vacination) {
                    a.setState(State.IMMUNE);
                }
            }
        }

        // limite le nombre de personnes qui vont se deplacer
        if (Math.random() < this.movable) {
            return;
        } else {
            int dir = rand.nextInt(4);
            switch (dir) {
                case 0: // move down
                    if (a.getRow() < maxRow && a.getRow() >= minRow) {
                        a.setRow(a.getRow() + 1);
                    } else if (a.getRow() == maxRow) {
                        a.setRow(minRow);
                    }
                    break;
                case 1: // move up
                    if (a.getRow() <= maxRow && a.getRow() > minRow) {
                        a.setRow(a.getRow() - 1);
                    } else if (a.getRow() == minRow) {
                        a.setRow(maxRow);
                    }
                    break;
                case 2: // move left
                    if (a.getCol() <= maxCol && a.getCol() > minCol) {
                        a.setCol(a.getCol() - 1);
                    } else if (a.getCol() == minCol) {
                        a.setCol(maxCol);
                    }
                    break;
                case 3: // move right
                    if (a.getCol() < maxCol && a.getCol() >= minCol) {
                        a.setCol(a.getCol() + 1);
                    } else if (a.getCol() == maxCol) {
                        a.setCol(minCol);
                    }
                    break;
            }
        }
    }

    /**
     * @param state
     * @param maxX
     * @param maxY
     * @return
     */
    public Actor gerRandomActor(Actor.State state, int maxX, int maxY) {
        Random r = new Random();
        return new Actor(state, r.nextInt(maxX), r.nextInt(maxY));
    }

    /**
     * @param actor
     */
    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public boolean isFinished() {
        for (Actor a : actors) {
            if (a.getState() == State.SICK) {
                return false;
            }
        }
        return true;
    }

    public Actor actorBirth() {
        Actor a = gerRandomActor(State.HEALTHY, this.maxRow, this.maxCol);
        addActor(a);
        return a;
    }

    public int numberOfSick() {
        int sum = 0;
        for (Actor a : actors) {
            if (a.getState() == State.SICK) {
                sum++;
            }
        }
        return sum;
    }

    public int numberOfExposed() {
        int sum = 0;
        for (Actor a : actors) {
            if (a.getState() == State.EXPOSED) {
                sum++;
            }
        }
        return sum;
    }

    /**
     * @param as
     * @return
     */
    public List<Actor> getExposed(Set<Actor> as) {
        List<Actor> tmp = new ArrayList<>();
        for (Actor a : as) {
            if (a.getState() == Actor.State.EXPOSED) {
                tmp.add(a);
            }
        }
        return tmp;
    }

    public int getMaxCol() {
        return maxCol;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public void setMask() {
        for (Actor a : actors) {
            if (Math.random() < .6) {
                Map<String, Double> defaultParams = a.getDefaultParams();
                Double beta = defaultParams.get("beta");
                Map<String, Double> params = a.getParams();
                params.put("beta", beta * .6);
                a.setParams(params);
            }
        }
    }

    public void setQuarantaine() {
        for (Actor a : actors) {
            if (a.getState() == State.SICK) {
                Map<String, Double> params = a.getParams();
                params.put("beta", 0.);
                a.setParams(params);
            }
        }
    }

    public void setVacination(Double vacination) {
        this.vacination = vacination;
    }

    public void resetParams() {
        for (Actor a : actors) {
            Map<String, Double> params = a.getDefaultParams();
            a.setParams(params);
        }
    }

    /**
     * @param as
     * @return
     */
    public List<Actor> getHealthy(Set<Actor> as) {
        List<Actor> tmp = new ArrayList<>();
        for (Actor a : as) {
            if (a.getState() == Actor.State.HEALTHY) {
                tmp.add(a);
            }
        }
        return tmp;
    }

    /**
     * @param as
     * @return
     */
    public List<Actor> getSick(Set<Actor> as) {
        List<Actor> tmp = new ArrayList<>();
        for (Actor a : as) {
            if (a.getState() == Actor.State.SICK) {
                tmp.add(a);
            }
        }
        return tmp;
    }

    public int numberOfHealthy() {
        int sum = 0;
        for (Actor a : actors) {
            if (a.getState() == State.HEALTHY) {
                sum++;
            }
        }
        return sum;
    }

    public int numberOfCured() {
        int sum = 0;
        for (Actor a : actors) {
            if (a.getState() == State.IMMUNE) {
                sum++;
            }
        }
        return sum;
    }

}
