package fiubaceldas.grupo03;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import juanma.Perception;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Agent extends AbstractMultiPlayer
{
	private int playerId; //this player's ID
	private Knowledge knowledge;
	private String serializationFilename;
	private static Double WIN_REWARD = 1000.0;
	private static Double LOSE_REWARD = -1000.0;
	private boolean stillAlive = true;
	private Set<String> visitedStates = new HashSet<>();

	/**
	 * Public constructor with state observation and time due.
	 * @param so state observation of the current game.
	 * @param elapsedTimer Timer for the controller creation.
	 * @param playerID ID if this agent
	 */
	public Agent(StateObservationMulti so, ElapsedCpuTimer elapsedTimer, int playerID)
	{
		playerId = playerID;
		serializationFilename = "data/knowledge_" + idToString(playerId) + ".json";
		knowledge = KnowledgeDB.instance().load(serializationFilename);
		if(knowledge == null) {
			knowledge = new Knowledge();
		}
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
		Types.ACTIONS action = Types.ACTIONS.ACTION_NIL;
		if(stillAlive) {
			Perception perception = new Perception(stateObs);
			String stateHash = perception.gridHash();
			try {
				// Check if we have visited the same state twice in this game
//				if(stateObs.getAvatarLastAction(playerId) != Types.ACTIONS.ACTION_NIL) {
					if (visitedStates.contains(stateHash)) {
						throw new IllegalStateException("Visited same state twice. States count: " + visitedStates.size());
					} else {
						visitedStates.add(stateHash);
					}
//				}
				Double reward = -1.0;
				knowledge.sampleState(stateHash, reward, realAvailableActions(stateObs));

//				System.out.println("Evaluating actions for player "+idToString(playerId)+":\n"+stateHash);
				action = knowledge.getActionFor(stateHash, stateObs.getAvatarLastAction(oppositePlayerId()));
//				System.out.println("Took action:  "+action);
			}catch(IllegalStateException e) {
				// Pretend the game has ended in a loss
//				System.out.println("Deadlock: "+e.getMessage());
				gameLostUpdate(stateObs, stateHash, 0.0d);
				stillAlive = false;
			}
			//System.out.println("Player "+idToString(playerId)+" took action "+action+"\n");


//		System.out.println("Player "+playerId+ " action: "+action);
		}else{
			//System.out.println("Forced deadlock, waiting for timeout");
		}
		try {
			Thread.sleep(250);
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// Nothing
		}
		return action;
	}

	private String idToString(int playerId) {
		return (playerId == 0 ? "A" : "B");
	}

	private ArrayList<Types.ACTIONS> realAvailableActions(StateObservationMulti stateObs) {
		Types.ACTIONS allMovementActions[] = new Types.ACTIONS[]{
				Types.ACTIONS.ACTION_DOWN,Types.ACTIONS.ACTION_UP,
				Types.ACTIONS.ACTION_LEFT, Types.ACTIONS.ACTION_RIGHT};
		Vector2d currentPos = stateObs.getAvatarPosition(playerId);
		ArrayList<Types.ACTIONS> realActions = new ArrayList<>();
		for(Types.ACTIONS action : allMovementActions) {
			// Shenaningans para que avance el estado considerando que el otro jugador no hace nada
			Types.ACTIONS[] acts = new Types.ACTIONS[2];
			acts[playerId] = action;
			acts[oppositePlayerId()] = Types.ACTIONS.ACTION_NIL;
			StateObservationMulti stCopy = stateObs.copy();
			stCopy.advance(acts);
			Vector2d nextPos = stCopy.getAvatarPosition(playerId);
			if(!nextPos.equals(currentPos)) {
				realActions.add(action);
			}
		}
		//String stateHash = new Perception(stateObs).gridHash();
		//System.out.println("Evaluating useful actions for player "+idToString(playerId)+":\n"+stateHash);
		//System.out.println("Available actions: "+availableActions);
		//System.out.println("Useful actions: "+usefulActions);
		return realActions;
	}

	private int oppositePlayerId() {
		return playerId == 0 ? 1 : 0;
	}

	public void resultMulti(StateObservationMulti stateObs, ElapsedCpuTimer elapsedCpuTimer) {
		Double gameScore = stateObs.getGameScore(0);
		if(stillAlive) {
			String stateHash = new Perception(stateObs).toString();
			// System.out.println("Event history size: "+stateObs.getEventsHistory().size());

			if (stateObs.getGameWinner() == Types.WINNER.NO_WINNER || stateObs.getGameWinner() == Types.WINNER.PLAYER_LOSES) {
//				System.out.println("No winner");
				gameLostUpdate(stateObs, stateHash, gameScore);
			} else {
				System.out.println("Winner");
				knowledge.sampleState(stateHash, WIN_REWARD, realAvailableActions(stateObs));
			}
		}

//		System.out.println("Storing knowledge to file: "+serializationFilename);
//		System.out.println("Num states: "+knowledge.numStates());
		KnowledgeDB.instance().store(serializationFilename, knowledge);
//		System.out.println("------------------------------------------ Score: "+gameScore);
	}

	private void gameLostUpdate(StateObservationMulti stateObs, String stateHash, Double score) {
		knowledge.sampleState(stateHash, LOSE_REWARD + (WIN_REWARD * 0.25d * score), realAvailableActions(stateObs));
	}
}