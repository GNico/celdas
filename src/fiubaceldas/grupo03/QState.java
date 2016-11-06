package fiubaceldas.grupo03;

import ontology.Types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 06/11/2016.
 */
public class QState {
    private HashMap<Types.ACTIONS, Float> actionValues = new HashMap<>();

    public QState(Collection<Types.ACTIONS> possibleActions) {
        for(Types.ACTIONS action : possibleActions) {
            // Init with default
            setActionValue(action, 0.0f);
        }
    }

    public int numActions() {
        return actionValues.size();
    }

    public Float getMaxActionValue() {
        Float maxActionValue = null;
        for (Float entry : actionValues.values()) {
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
            Float maxActionValue = null;
            for (Map.Entry<Types.ACTIONS, Float> entry : actionValues.entrySet()) {
                if(maxActionValue == null || entry.getValue() > maxActionValue) {
                    maxActionValue = entry.getValue();
                    maxAction = entry.getKey();
                }
            }
            return maxAction;
        }
    }

    private Float getActionValue(Types.ACTIONS action) {
        Float actionValue =  actionValues.get(action);
        if(actionValue == null) {
            actionValue = 0.0f;
            System.err.println("Requesting missing action: "+action);
            actionValues.put(action, actionValue);
        }
        return actionValue;
    }

    private void setActionValue(Types.ACTIONS action, Float value) {
        actionValues.put(action, value);
    }

}
