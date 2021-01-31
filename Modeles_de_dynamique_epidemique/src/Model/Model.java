package Model;

import java.util.Map;

public interface Model {

    int[] numberOfPeople();
    int[] stepInfection();
    void infect();
    String[] getStates();
    void setBoard(Board board);
    void initActors();
    void setModelParams(Map<String, Double> params);
    Map<String, Double> getParams();
}
