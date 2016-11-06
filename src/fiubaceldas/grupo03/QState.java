package fiubaceldas.grupo03;

import ontology.Types;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 06/11/2016.
 */
public class QState implements Serializable {

    public static final double DEFAULT_ACTION_VALUE = 10.0;

    private HashMap<Types.ACTIONS, Double> actionValues = new HashMap<>();

    public QState(Collection<Types.ACTIONS> possibleActions) {
        for(Types.ACTIONS action : possibleActions) {
            // Init with default
            setActionValue(action, DEFAULT_ACTION_VALUE);
        }
        setActionValue(Types.ACTIONS.ACTION_NIL, DEFAULT_ACTION_VALUE);
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

    public Types.ACTIONS getOptimalAction() {
        if(actionValues.isEmpty()) {
            System.err.println("No actions for this state");
            return Types.ACTIONS.ACTION_NIL;
        }else{
            Types.ACTIONS maxAction = null;
            Double maxActionValue = null;
            for (Map.Entry<Types.ACTIONS, Double> entry : actionValues.entrySet()) {
                if(maxActionValue == null || entry.getValue() > maxActionValue) {
                    maxActionValue = entry.getValue();
                    maxAction = entry.getKey();
                }
            }
            return maxAction;
        }
    }

    private Double getActionValue(Types.ACTIONS action) {
        Double actionValue =  actionValues.get(action);
        if(actionValue == null) {
            actionValue = 0.0d;
            System.err.println("Requesting missing action: "+action);
            actionValues.put(action, actionValue);
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
