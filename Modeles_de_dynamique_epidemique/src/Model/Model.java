package Model;

import java.util.Map;

public interface Model {
    int[] stepInfection();
    Map<String, Double> getParams();
    void initActors();
    void setModelParams(Map<String, Double> params);
    void setBoard(Board board);
    String[] getStates();
    int[] numberOfPeople();
}
