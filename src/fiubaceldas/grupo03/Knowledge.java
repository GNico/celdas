package fiubaceldas.grupo03;

import ontology.Types;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by root on 06/11/2016.
 */
public class Knowledge implements Serializable{

    private HashMap<String, QState> qtable = new HashMap<>();
    private String prevState = null;
    private Types.ACTIONS prevAction = null;
    private Double alpha = 0.25d;
    private Double gamma = 0.95d;

    private boolean isNewState(String state) {
        return !QTable().containsKey(state);
    }

    public int numStates() {
        return qtable.size();
    }

    public QState getQStateFor(String state) {
        return QTable().get(state);
    }

    public void sampleState(String state, Double reward, Collection<Types.ACTIONS> possibleActions) {
        if(isNewState(state)) {
            QTable().put(state, new QState(possibleActions));
        }
        if(this.prevState != null && this.prevAction != null) {
            QState qsPrev = getQStateFor(this.prevState);
            if(qsPrev == null) throw new RuntimeException("Prev state has no QState");
            Double nextMax = this.getQStateFor(state).getMaxActionValue();
            qsPrev.update(this.prevAction, nextMax, reward, this.alpha, this.gamma);
        }
        this.prevState = state;
    }

    public Types.ACTIONS getActionFor(String currentState, Types.ACTIONS oppositeLastAction) {
        boolean canBeNil = oppositeLastAction != Types.ACTIONS.ACTION_NIL;
        Types.ACTIONS nextAction = getQStateFor(currentState).getArgMaxActionValue(canBeNil);
        this.prevAction = nextAction;
        return nextAction;
    }

    public void setQTable(HashMap<String, QState> qtable) {
        this.qtable = qtable;
    }

    public HashMap<String, QState> QTable() {
        return this.qtable;
    }

}
