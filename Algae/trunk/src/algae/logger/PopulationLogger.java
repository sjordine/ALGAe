/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.logger;

import algae.chromossome.Chromossome;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author sergio
 */
public class PopulationLogger {

    private static class OperationData {

        double max;
        double min;
        double sum;
        int numberOfElements;

        public OperationData() {
            max = Double.MIN_VALUE;
            min = Double.MAX_VALUE;
            sum = 0;
            numberOfElements = 0;
        }
    }

    public static void logPopulation(PrintStream logStream, List<Chromossome> population) {
        Map<String, OperationData> populationMap = new HashMap<String, OperationData>();

        if (logStream != null && population != null) {
            for (Chromossome c : population) {
                if (c != null) {
                    String origin = c.getOrigin();
                    Double score = c.getScore();
                    OperationData data = populationMap.get(origin);
                    if (data == null) {
                        data = new OperationData();
                        data.max = score;
                        data.min = score;
                        data.sum = score;
                        data.numberOfElements = 1;
                        populationMap.put(origin, data);
                    } else {
                        data.sum += c.getScore();
                        data.numberOfElements++;
                        if (score > data.max) {
                            data.max = score;
                        }
                        if (score < data.min) {
                            data.min = score;
                        }
                    }
                }
            }
        }

        //log data
        for (Entry<String, OperationData> entry : populationMap.entrySet()) {
            logStream.println("<operation id=\"" + entry.getKey() + "\">");
            OperationData data = entry.getValue();
            logStream.println("<population value=\"" + data.numberOfElements + "\"/>");
            logStream.println("<max value=\"" + data.max + "\"/>");
            logStream.println("<min value=\"" + data.min + "\"/>");
            logStream.println("<rate value=\"" + (data.sum / data.numberOfElements) + "\"/>");
            logStream.println("</operation>");
        }
    }
}
