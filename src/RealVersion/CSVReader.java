package RealVersion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {

    static List<Airport> airports = new ArrayList<>();

    public static int readAndFilter(String fileName, List<ConjunctionFilter> filters) {
        int count = 0;

        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName));) {
            while ((line = br.readLine()) != null) {

                String[] lineArray = line.split(csvSplitBy);
                if (isCorrect(filters, lineArray)) {
                    addAirport(lineArray);
                    count++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
        return count;
    }

    private static boolean isCorrect(List<ConjunctionFilter> filters, String[] lineArr) {
        for (ConjunctionFilter f : filters) {
            if (!f.isCorrect(lineArr)) {
                return false;
            }
        }

        return true;
    }

    private static void addAirport(String[] arr) {
        airports.add(new Airport(arr[1], arr));
    }
}
