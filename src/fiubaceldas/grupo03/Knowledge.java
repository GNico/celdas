package fiubaceldas.grupo03;

import java.io.*;
import java.util.HashMap;

/**
 * Created by root on 06/11/2016.
 */
public class Knowledge implements Serializable{

    private HashMap<String, QState> qtable;

    private void setQTable(HashMap<String, QState> qtable) {
        this.qtable = qtable;
    }

    public HashMap<String, QState> QTable() {
        return this.qtable;
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
