package fiubaceldas.grupo03;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ontology.Types;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 12/11/2016.
 */
public class KnowledgeDB {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
            String jsonString = new String(Files.readAllBytes(Paths.get(filename)));
            Map<String, QState> json = gson.fromJson(jsonString, new TypeToken<Map<String, QState>>(){}.getType());
            HashMap<String, QState> table = new HashMap<String, QState>(json);
            Knowledge k = new Knowledge();
            k.setQTable(table);
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
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            String text = gson.toJson(k.QTable());
            out.write(text);
            out.close();
        }catch(Exception e) {
            System.err.println("Error storing serialized knowledge to "+filename+": "+e.getMessage());
        }
    }

}
