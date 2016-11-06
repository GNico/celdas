package fiubaceldas.grupo03;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import juanma.Perception;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractMultiPlayer
{
	private int id; //this player's ID
	private Knowledge knowledge;
	private String serializationFilename;

	/**
	 * Public constructor with state observation and time due.
	 * @param so state observation of the current game.
	 * @param elapsedTimer Timer for the controller creation.
	 * @param playerID ID if this agent
	 */
	public Agent(StateObservationMulti so, ElapsedCpuTimer elapsedTimer, int playerID)
	{
		id = playerID;
		serializationFilename = "data/knowledge_" + Integer.toString(id) + ".ser";
		knowledge = new Knowledge();
		knowledge.load(serializationFilename);

	}


	/**
	 * Picks an action. This function is called every game step to request an
	 * action from the player.
	 * @param stateObs Observation of the current state.
	 * @param elapsedTimer Timer when the action returned is due.
	 * @return An action for the current state
	 */
	public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer)
	{
		String stateHash = new Perception(stateObs).toString();
		Types.ACTIONS action = ACTIONS.ACTION_NIL;
		return action;
	}

	public void resultMulti(StateObservationMulti stateObservation, ElapsedCpuTimer elapsedCpuTimer)
	{
		System.out.println("Storing knowledge to file: "+serializationFilename);
		knowledge.store(serializationFilename);
	}
}