package RealVersion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {

    private static String csvSplitBy = ",";

    public static List<Airport> readAndFilter(String fileName, List<ConjunctionFilter> filters) {
        String line;
        List<Airport> airports = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            while ((line = br.readLine()) != null) {
                String[] lineArray = line.split(csvSplitBy);

                if (isCorrect(filters, lineArray))
                    airports.add(new Airport(lineArray[1], lineArray));            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return airports;
    }

    private static boolean isCorrect(List<ConjunctionFilter> filters, String[] lineArr) {
        for (ConjunctionFilter f : filters) {
            if (!f.isCorrect(lineArr)) {
                return false;
            }
        }

        return true;
    }

}
