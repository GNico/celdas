package fiubaceldas.grupo03;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 12/11/2016.
 */
public class KnowledgeDB {

    static KnowledgeDB instance;
    HashMap<String, Knowledge> cache = new HashMap<>();

    private KnowledgeDB() { }

    static public KnowledgeDB instance() {
        if(instance == null) {
             instance = new KnowledgeDB();
        }
        return instance;
    }

    Knowledge load(String filename) {
        Knowledge k;
        k = loadFromCache(filename);
        if(k == null) {
//            System.err.println("Cache miss for "+filename);
            k = loadFromFile(filename);
        }
        return k;
    }

    private Knowledge loadFromCache(String filename) {
        return cache.get(filename);
    }

    private Knowledge loadFromFile(String filename) {
        try {
            FileInputStream fIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fIn);
            Knowledge fromFile = (Knowledge)in.readObject();
            in.close();
            fIn.close();
            Knowledge k = new Knowledge();
            k.loadFrom(fromFile);
            return k;
        }catch(Exception e) {
            System.err.println("Error loading serialized knowledge from "+filename+": "+e.getMessage());
        }
        return null;
    }

    public void store(String filename, Knowledge k)  {
//        System.err.println("Storing in cache for "+filename);
        cache.put(filename, k);
    }

    public void toDisk() {
        // Dumps cache to disk
        for(Map.Entry<String, Knowledge> entry : cache.entrySet()) {
            String filename = entry.getKey();
            Knowledge k = entry.getValue();
            System.out.println("Storing to disk for "+filename+" - #States: " + k.numStates());
            storeToDisk(filename, k);
        }
    }

    public void storeToDisk(String filename, Knowledge k)  {
        try {
            FileOutputStream fOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fOut);
            out.writeObject(k);
            out.close();
            fOut.close();
        }catch(Exception e) {
            System.err.println("Error storing serialized knowledge to "+filename+": "+e.getMessage());
        }
    }

}
