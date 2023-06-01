package RealVersion;

import java.util.*;

public class Filtration {

    static List<ConjunctionFilter> filters = new ArrayList<>();
    static char lastFilterAction;


    public static List<Airport> filterAndSort(String filter, String fileName) throws Exception {
        filterDescription(filter, 0);
        filters.removeIf(x -> x.size() == 0);
        sortFilters();

        List<Airport> airports = CSVReader.readAndFilter(fileName, filters);
        airports.sort(Airport::compareTo);
        return airports;
    }

    private static void filterDescription(String filter, int priority) throws Exception {
        StringBuilder s = new StringBuilder();

        int i = 0;
        while (i != filter.length()) {
            char symbol = filter.charAt(i);
            if (symbol == '(') {
                lastFilterAction = '(';
                filterDescription(
                        String.copyValueOf(filter.toCharArray(), i + 1, filter.length() - (i + 1)),
                        priority + 1);
                i += filter.length() - (i + 1);
            } else if (symbol == ')') {
                addNewConjunctionFilter(s, priority);
                s = new StringBuilder();
                lastFilterAction = ')';

                filterDescription(
                        String.copyValueOf(filter.toCharArray(), i + 1, filter.length() - (i + 1)),
                        priority - 1);
                i += filter.length() - (i + 1);
            } else if (symbol == '&') {
                addFilter(s, priority);
                s = new StringBuilder();
                lastFilterAction = '&';

            } else if (symbol == '|' && filter.charAt(i + 1) == '|') {
                addFilter(s, priority);
                s = new StringBuilder();
                lastFilterAction = '|';

                i++; // т.к. знак "или" обозначается 2мя знаками
            } else {
                s.append(symbol);
            }
            i++;
        }
        addFilter(s, priority);
    }

    private static void addFilter(StringBuilder s, int priority) throws Exception {
        if (lastFilterAction != '&') {
            addNewConjunctionFilter(priority);
        }

        if (!s.toString().equals("")) {
            if (filters.size() == 0)
                addNewConjunctionFilter(priority);

            filters.get(filters.size() - 1).addFilter(parseFilter(s.toString()));
        }
    }

    private static void addNewConjunctionFilter(int priority) {
        ConjunctionFilter filter = new ConjunctionFilter(priority);
        filters.add(filter);
    }

    private static void addNewConjunctionFilter(StringBuilder s, int priority) throws Exception {
        addNewConjunctionFilter(priority);
        addFilter(s, priority);
    }

    private static Filter parseFilter(String str) throws Exception {
        int column = Integer.parseInt("" + str.charAt(7)) - 1;
        char action;
        int actionIndex;
        if (str.contains("<")) {
            if (str.contains(">")) {
                action = '!';
                actionIndex = str.indexOf(">");
            } else {
                action = '<';
                actionIndex = str.indexOf("<");
            }
        } else if (str.contains(">")) {
            action = '>';
            actionIndex = str.indexOf(">");
        } else if (str.contains("=")) {
            action = '=';
            actionIndex = str.indexOf("=");
        } else {
            throw new Exception("Uncorrected filter: not found action");
        }

        String value = String.copyValueOf(str.toCharArray(), actionIndex + 1, str.length() - actionIndex - 1);
        value = value.replaceAll("\'", "\"");
        return new Filter(column, action, value);
    }

    private static void sortFilters () {
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < filters.size() - 1; i++) {
                if (filters.get(i).compareTo(filters.get(i + 1)) < 0) {
                    isSorted = false;

                    ConjunctionFilter buffer = filters.get(i);
                    filters.set(i, filters.get(i + 1));
                    filters.set(i + 1, buffer);
                }
            }
        }
    }
}
