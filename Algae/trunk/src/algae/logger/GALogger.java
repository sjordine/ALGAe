/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.logger;

import algae.chromossome.Chromossome;
import algae.engine.configuration.Configuration;
import algae.engine.configuration.Property;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 *
 * @author sergio
 */
public class GALogger {




    private enum LogState {
        NOT_OPEN,
        TEST_SET_STARTED,
        TEST_RUN_STARTED,
        TEST_GENERATION_STARTED,
        TEST_GENERATION_END,
        TEST_RUN_END,
        TEST_SET_END
    }
    
    private static GALogger _instance = null;
    private PrintStream logStream = null;
    private LogState state = LogState.NOT_OPEN;

    private GALogger() {
    }

    public static GALogger getInstance() {
        if (_instance == null) {
            _instance = new GALogger();
        }
        return _instance;
    }

    public void startTestSet(String filePath, String testSetId) throws FileNotFoundException {
        if (logStream != null) {
            logStream.close();
        }
        FileOutputStream fileStream = new FileOutputStream(filePath + "/" + testSetId + ".txt");
        logStream = new PrintStream(fileStream);
        logStream.println("<?xml version=\"1.0\"?>");        
        logStream.println("<testSet id=\"" + testSetId + "\">");
        logConfiguration();
        state = LogState.TEST_SET_STARTED;
    }

    public void endTestSet() throws Exception {
        if (state != LogState.TEST_SET_STARTED && state != LogState.TEST_RUN_END) {
            throw new Exception("Invalid state to end test set!");
        } else {
            if (logStream != null) {
                logStream.println("</testSet>");
                logStream.close();
                state = LogState.NOT_OPEN;
            }
            logStream = null;
        }
    }

    private void logConfiguration() {
         logStream.println("<configuration>");
         Configuration configuration = Configuration.getInstance();
         List<Property> properties = configuration.getProperties();
         for (Property property:properties){
             logProperty(property);
         }
         logStream.println("</configuration>");
    }

    private void logProperty(Property property) {
        logStream.println("<property name=\"" + property.getPropertyName() + "\">");
        logStream.println(property.getPropertyValue());
        logStream.println("</property>");
    }


    public void startTestRun(int runId) throws Exception{
         if (state != LogState.TEST_SET_STARTED && state != LogState.TEST_RUN_END) {
            throw new Exception("Invalid state to start test run!");
        } else {
            if (logStream != null) {
                logStream.println("<testRun id=\""+runId+"\">");
                state = LogState.TEST_RUN_STARTED;
            }            
        }
    }

    public void endTestRun() throws Exception{
         if (state != LogState.TEST_RUN_STARTED && state!=LogState.TEST_GENERATION_END) {
            throw new Exception("Invalid state to end test run!");
        } else {
            if (logStream != null) {
                logStream.println("</testRun>");
                state = LogState.TEST_RUN_END;
            }
            
        }
    }

    public void startGeneration(int generationNo) throws Exception{
        if (state != LogState.TEST_RUN_STARTED && state != LogState.TEST_GENERATION_END) {
            throw new Exception("Invalid state to end test set!");
        } else {
            if (logStream != null) {
                logStream.println("<generation number=\""+generationNo+"\">");
                state = LogState.TEST_GENERATION_STARTED;
            }

        }
    }

    public void endGeneration() throws Exception{
        if (state != LogState.TEST_GENERATION_STARTED) {
            throw new Exception("Invalid state to end test run!");
        } else {
            if (logStream != null) {
                logStream.println("</generation>");
                state = LogState.TEST_GENERATION_END;
            }
        }
    }

    public void preCutPopulation(List<Chromossome> population) throws Exception{
        if (state != LogState.TEST_GENERATION_STARTED){
            throw new Exception("Invalid state to gather population data (pre cut)!");
        } else {
            if (logStream != null) {
                logStream.println("<preCut>");
                if (population!=null){
                    PopulationLogger.logPopulation(logStream, population);
                }
                logStream.println("</preCut>");
            }
        }
    }

     public void postCutPopulation(List<Chromossome> population) throws Exception{
        if (state != LogState.TEST_GENERATION_STARTED){
            throw new Exception("Invalid state to gather population data (post cut)");
        } else {
            if (logStream != null) {
                logStream.println("<postCut>");
                PopulationLogger.logPopulation(logStream, population);
                logStream.println("</postCut>");
            }
        }
    }

}
