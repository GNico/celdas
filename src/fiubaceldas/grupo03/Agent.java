package fiubaceldas.grupo03;

import core.game.Game;
import core.game.StateObservation;
import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import juanma.Perception;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.Direction;
import tools.ElapsedCpuTimer;
import tools.Utils;

public class Agent extends AbstractMultiPlayer
{
	int id; //this player's ID

	/**
	 * Public constructor with state observation and time due.
	 * @param so state observation of the current game.
	 * @param elapsedTimer Timer for the controller creation.
	 * @param playerID ID if this agent
	 */
	public Agent(StateObservationMulti so, ElapsedCpuTimer elapsedTimer, int playerID)
	{
		id = playerID;
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
		Types.ACTIONS action = Types.ACTIONS.ACTION_DOWN;

		return action;
	}

	public void result(StateObservation stateObservation, ElapsedCpuTimer elapsedCpuTimer)
	{
		//System.out.println("Thanks for playing! " + stateObservation.isAvatarAlive());
	}
}
