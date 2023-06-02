package autocomplete;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * класс чтения .csv файлов
 */
public class CSVReader {

    private static String csvSplitBy = ",";

    public static List<Airport> readFromFileAndFilter(String fileName, List<Filter> filters) {
        String line;
        List<Airport> airports = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            while ((line = br.readLine()) != null) {
                String[] lineArray = line.split(csvSplitBy);

                if (isCorrect(filters, lineArray))
                    airports.add(new CSVModel(1, lineArray));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return airports;
    }

    private static boolean isCorrect(List<Filter> filters, String[] lineArr) {
        for (Filter f : filters) {
            if (!f.isCorrect(lineArr)) {
                return false;
            }
        }

        return true;
    }

}
