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
    private Double alpha = 0.15d;
    private Double gamma = 1.0d;

    private boolean isNewState(String state) {
        return !QTable().containsKey(state);
    }

    public Types.ACTIONS getActionFor(String currentState) {

        Types.ACTIONS nextAction = getQStateFor(currentState).getOptimalAction();
        this.prevState = currentState;
        this.prevAction = nextAction;
        return nextAction;
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
            if(qsPrev != null) {
                Double nextMax = this.getQStateFor(state).getMaxActionValue();
                qsPrev.update(this.prevAction, nextMax, reward, this.alpha, this.gamma);
            }
        }
    }

    public void load(String filename) {
        try {
            FileInputStream fIn = new FileInputStream(filename);
            ObjectInputStream out = new ObjectInputStream(fIn);
            Knowledge k = (Knowledge)out.readObject();
            this.loadFrom(k);
        }catch(Exception e) {
            System.err.println("Error loading serialized knowledge from "+filename+": "+e.getMessage());
        }
    }

    private void setQTable(HashMap<String, QState> qtable) {
        this.qtable = qtable;
    }

    private HashMap<String, QState> QTable() {
        return this.qtable;
    }

    private void loadFrom(Knowledge k) {
        setQTable(k.QTable());
    }

    public void store(String filename)  {
        try {
            FileOutputStream fOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fOut);
            out.writeObject(this);
            out.flush();
        }catch(Exception e) {
            System.err.println("Error storing serialized knowledge to "+filename+": "+e.getMessage());
        }
    }

}
