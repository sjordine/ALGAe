/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.engine.configuration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.util.StringTokenizer;


/**
 *
 * @author sergio
 */
public class Configuration {

    private static Configuration instance = new Configuration();
    private List<Property> properties;

    private Configuration(){
        properties = new ArrayList<Property>();        
    }

    /**
     * @return the instance
     */
    public static Configuration getInstance() {
        return instance;
    }

     public void configure(String configurationFile) throws FileNotFoundException, IOException {

        FileInputStream fstream = new FileInputStream(configurationFile);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));

        String line = null;
        
        getProperties().clear();

        while ((line = stream.readLine()) != null) {
            //Split line
            StringTokenizer tokens = new StringTokenizer(line, ":");
            if (tokens.countTokens() == 2){
                
                    String key = tokens.nextToken();
                    String value = tokens.nextToken();
                    Property property = new Property(key, value);
                    getProperties().add(property);
            } else {
                //TODO: ignore line or throw exception
            }
         }
    }

    /**
     * @return the properties
     */
    public List<Property> getProperties() {
        return properties;
    }
}
