package fiubaceldas.grupo03;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import juanma.Perception;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractMultiPlayer
{
	private int playerId; //this player's ID
	private Knowledge knowledge;
	private String serializationFilename;
	private static Double WIN_REWARD = 10.0;
	private static Double LOSE_REWARD = -10.0;

	/**
	 * Public constructor with state observation and time due.
	 * @param so state observation of the current game.
	 * @param elapsedTimer Timer for the controller creation.
	 * @param playerID ID if this agent
	 */
	public Agent(StateObservationMulti so, ElapsedCpuTimer elapsedTimer, int playerID)
	{
		playerId = playerID;
		serializationFilename = "data/knowledge_" + playerId + ".ser";
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

		// Gamescore reward
		Double gameScore = stateObs.getGameScore(0);

		// Default reward
		Double reward = -0.1 + gameScore;

		knowledge.sampleState(stateHash, reward, stateObs.getAvailableActions(playerId));

		Types.ACTIONS action = knowledge.getActionFor(stateHash);

//		System.out.println("Player "+playerId+ " action: "+action);

		return action;
	}

	public void resultMulti(StateObservationMulti stateObs, ElapsedCpuTimer elapsedCpuTimer) {
		String stateHash = new Perception(stateObs).toString();

		if (stateObs.getGameWinner() == Types.WINNER.NO_WINNER || stateObs.getGameWinner() == Types.WINNER.PLAYER_LOSES){
			System.out.println("No winner");
			knowledge.sampleState(stateHash, LOSE_REWARD, stateObs.getAvailableActions(playerId));
		}else {
			System.out.println("Winner");
			knowledge.sampleState(stateHash, WIN_REWARD, stateObs.getAvailableActions(playerId));
		}

//		System.out.println("Storing knowledge to file: "+serializationFilename);
		System.out.println("Num states: "+knowledge.numStates());
		knowledge.store(serializationFilename);
	}
}