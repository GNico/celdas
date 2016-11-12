import java.util.Random;

import core.ArcadeMachine;

/**
 * Created with IntelliJ IDEA.
 * User: Raluca
 * Date: 12/04/16
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 * Modified: Juan Manuel Rodríguez, 75.68 Celdas, fi.uba.ar: http://materias.fi.uba.ar/7568/
 */
public class TestMultiPlayerCeldas
{

    public static void main(String[] args)
    {
        //Controladores disponibles de ejemplo:
        String doNothingController = "controllers.multiPlayer.doNothing.Agent";
        String randomController = "controllers.multiPlayer.sampleRandom.Agent";
        String oneStepController = "controllers.multiPlayer.sampleOneStepLookAhead.Agent";
        String sampleMCTSController = "controllers.multiPlayer.sampleMCTS.Agent";
        String sampleOLMCTSController = "controllers.multiPlayer.sampleOLMCTS.Agent";
        String sampleGAController = "controllers.multiPlayer.sampleGA.Agent";
        String humanController = "controllers.multiPlayer.human.Agent";
        String controllerPropio = "fiubaceldas.grupo03.Agent";

        //definir acá los controladores usados para el juego (se necesitan 2 controladores separados por un espacio)
//        String controllers = randomController + " " + randomController;
        String controllers = controllerPropio + " " + controllerPropio;
//        String controllers = humanController + " " + controllerPropio;
//        String controllers = humanController + " " + humanController;

        //ubicación de los juegos disponibles:
        String gamesPath = "examples/2player/";
        String gameName = "sokoban";
        
        
        //otros settings
        boolean visuals = true;
        int seed = new Random().nextInt();

        //Qué nivel se utiliza
        String game = gamesPath + gameName + ".txt";
        String level = gamesPath + gameName + "_lvl_ez.txt";

        //nombre de archivo en doned se guardaran las acciones ejecutadas. Si es null no guardará nada
        String recordActionsFile = null;//"actions_" + games[gameIdx] + "_lvl" + levelIdx + "_" + seed + ".txt";

        // 4. Las siguientes líneas permiten jugar un mismo juego en N niveles M veces
        int M = 5000;
//        ArcadeMachine.runGames(game, new String[]{level}, M, controllers, null);
        ArcadeMachine.runOneGame(game, level, visuals, controllers, recordActionsFile, seed, 0);


    }
}
