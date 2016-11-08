package fiubaceldas.grupo03;

import ontology.Types;

import java.io.Serializable;
import java.util.*;

/**
 * Created by root on 06/11/2016.
 */
public class QState implements Serializable {

    public static final double DEFAULT_ACTION_VALUE = 100.0;

    private HashMap<Types.ACTIONS, Double> actionValues = new HashMap<>();

    public QState(Collection<Types.ACTIONS> possibleActions) {
        for(Types.ACTIONS action : possibleActions) {
            // Init with default
            setActionValue(action, DEFAULT_ACTION_VALUE);
        }
        setActionValue(Types.ACTIONS.ACTION_NIL, -50.0);
    }

    public int numActions() {
        return actionValues.size();
    }

    public Double getMaxActionValue() {
        Double maxActionValue = null;
        for (Double entry : actionValues.values()) {
            if(maxActionValue == null || entry > maxActionValue) {
                maxActionValue = entry;
            }
        }
        return maxActionValue;
    }

    public Types.ACTIONS getArgMaxActionValue(boolean canBeNil) {
        if(actionValues.isEmpty()) {
            throw new RuntimeException("No actions for this state");
        }
        Types.ACTIONS maxAction = null;
        Double maxActionValue = null;
        for (Map.Entry<Types.ACTIONS, Double> entry : actionValues.entrySet()) {
            if(!canBeNil && entry.getKey() == Types.ACTIONS.ACTION_NIL) continue;
            //System.out.println("Action: "+entry.getKey()+"\t"+"Value: "+entry.getValue());
            if(maxActionValue == null || entry.getValue() > maxActionValue) {
                maxActionValue = entry.getValue();
                maxAction = entry.getKey();
            }
        }
        if(maxAction == null) {
            throw new IllegalStateException("Can't be nil but only action is nil");
        }
        return maxAction;
    }

    private Types.ACTIONS getRandomAction() {
        Set<Types.ACTIONS> actions = actionValues.keySet();
        int size = actions.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for(Types.ACTIONS act : actions) {
            if (i == item) {
                return act;
            }
            i = i + 1;
        }
        return Types.ACTIONS.ACTION_NIL;
    }

    private Double getActionValue(Types.ACTIONS action) {
        Double actionValue = actionValues.get(action);
        if(actionValue == null) {
            System.err.println("Requesting missing action: "+action);
            actionValues.put(action, DEFAULT_ACTION_VALUE);
            actionValue = actionValues.get(action);
        }
        return actionValue;
    }

    private void setActionValue(Types.ACTIONS action, Double value) {
        actionValues.put(action, value);
    }

    public void update(Types.ACTIONS action, Double nextStateMax, Double reward, Double alpha, Double gamma) {
        Double oldActionValue = getActionValue(action);
        Double delta = reward + (gamma * nextStateMax) - oldActionValue;
        setActionValue(action, (oldActionValue + oldActionValue * delta * alpha));
    }

}
