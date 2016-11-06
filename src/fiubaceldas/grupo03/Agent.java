package fiubaceldas.grupo03;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import juanma.Perception;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

import java.util.ArrayList;

public class Agent extends AbstractMultiPlayer
{
	private int playerId; //this player's ID
	private Knowledge knowledge;
	private String serializationFilename;
	private static Double WIN_REWARD = 100.0;
	private static Double LOSE_REWARD = -100.0;
	private String prevBoxesHash = null;
	private int movesWithoutBoxesChange = 0;

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
		Perception perception = new Perception(stateObs);
		String stateHash = perception.gridHash();

		// Gamescore reward
		Double gameScore = stateObs.getGameScore(0);

		// Default reward
		Double reward = (-0.1 * movesWithoutBoxesChange) + gameScore;

		// If boxes were moved, increase reward
		String boxesHash = perception.boxesHash();
		if(!boxesHash.equals(prevBoxesHash)) {
			if(prevBoxesHash != null) {
				reward += 100.0;
				System.out.println("Boxes moved. Moves without boxes change: "+movesWithoutBoxesChange);
			}
			prevBoxesHash = boxesHash;
			movesWithoutBoxesChange = 0;
		}else{
			movesWithoutBoxesChange++;
		}

		Types.ACTIONS action;
		knowledge.sampleState(stateHash, reward, getUsefulActions(stateObs));
//		System.out.println("Evaluating actions for player "+idToString(playerId)+":\n"+stateHash);
		action = knowledge.getActionFor(stateHash);


//		System.out.println("Player "+playerId+ " action: "+action);

		return action;
	}

	private String idToString(int playerId) {
		return (playerId == 0 ? "A" :" B");
	}

	private ArrayList<Types.ACTIONS> getUsefulActions(StateObservationMulti stateObs) {
		ArrayList<Types.ACTIONS> availableActions = stateObs.getAvailableActions(playerId);
		Vector2d currentPos = stateObs.getAvatarPosition(playerId);
		ArrayList<Types.ACTIONS> usefulActions = new ArrayList<>();
		for(Types.ACTIONS action : availableActions) {
			// Skip nil action
			if(action == Types.ACTIONS.ACTION_NIL) continue;
			// Sheningans para que avance el estado considerando que el otro jugador no hace nada
			Types.ACTIONS[] acts = new Types.ACTIONS[2];
			acts[playerId] = action;
			acts[oppositePlayerId()] = Types.ACTIONS.ACTION_NIL;
			StateObservationMulti stCopy = stateObs.copy();
			stCopy.advance(acts);
			Vector2d nextPos = stCopy.getAvatarPosition(playerId);
			if(!nextPos.equals(currentPos)) {
				usefulActions.add(action);
			}
		}
		return usefulActions;
	}

	private int oppositePlayerId() {
		return playerId == 0 ? 1 : 0;
	}

	public void resultMulti(StateObservationMulti stateObs, ElapsedCpuTimer elapsedCpuTimer) {
		String stateHash = new Perception(stateObs).toString();
//		System.out.println("Event history size: "+stateObs.getEventsHistory().size());

		if (stateObs.getGameWinner() == Types.WINNER.NO_WINNER || stateObs.getGameWinner() == Types.WINNER.PLAYER_LOSES){
//			System.out.println("No winner");
			knowledge.sampleState(stateHash, LOSE_REWARD, getUsefulActions(stateObs));
		}else {
//			System.out.println("Winner");
			knowledge.sampleState(stateHash, WIN_REWARD, getUsefulActions(stateObs));
		}

		Double gameScore = stateObs.getGameScore(0);
		if(gameScore > 0.0) {
			System.out.println("Non zero score: "+gameScore);
		}

//		System.out.println("Storing knowledge to file: "+serializationFilename);
		System.out.println("Num states: "+knowledge.numStates());
		knowledge.store(serializationFilename);
	}
}