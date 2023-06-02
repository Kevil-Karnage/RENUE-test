package autocomplete;

import java.util.*;

public class Filtration {

    static List<Filter> filters = new ArrayList<>();
    static char lastFilterAction; // действие последнего найденного фильтра


    /**
     * получение отфильтрованных и отсортированных строк файла
     * @param filter фильтр в строковом формате
     * @param fileName название файла для чтения моделей
     * @return Список прочитанных и отсортированных моделей из файла
     * @throws FiltrationException при некорректном фильтре
     */
    public static List<CSVModel> getAndSortAirportsFromFile(String filter, String fileName) throws FiltrationException {
        describeFilters(filter, 0);
        filters.removeIf(x -> x.size() == 0);
        sortFilters();

        List<CSVModel> airports = CSVReader.readFromFileAndFilter(fileName, filters);
        airports.sort(new Comparator<CSVModel>() {
            @Override
            public int compare(CSVModel o1, CSVModel o2) {
                String s1 = o1.getName().toLowerCase(Locale.ROOT).replaceAll("\"", "");
                String s2 = o2.getName().toLowerCase(Locale.ROOT).replaceAll("\"", "");

                for (int i = 0; i < Integer.min(s1.length(), s2.length()); i++) {
                    char char1 = s1.charAt(i);
                    char char2 = s2.charAt(i);

                    if (char1 != char2)
                        return (int) char1 - (int) char2;
                }

                return 0;
            }
        });
        return airports;
    }

    /**
     * распознавание фильтров
     * @param fString выражение из фильтров в формате String
     * @param priority приоритет фильтра (запись в скобках)
     * @throws Exception
     */
    private static void describeFilters(String fString, int priority) throws FiltrationException {
        StringBuilder currentFilter = new StringBuilder();

        int i = 0;
        // разбираем строку
        while (i != fString.length()) {
            char symbol = fString.charAt(i);

            // очередность по приоритетам: (), &, ||
            if (symbol == '(') {
                // фильтры в скобках выше приоритетом, поэтому вызывается рекурсия с приоритетом на 1 выше текущего
                lastFilterAction = '(';
                describeFilters(
                        String.copyValueOf(fString.toCharArray(), i + 1, fString.length() - (i + 1)),
                        priority + 1);
                // переходим
                i += fString.length() - (i + 1);

            } else if (symbol == ')') {
                // после закрытой скобки приоритет уменьшается на 1
                addNewFilterAndFill(currentFilter, priority);
                currentFilter = new StringBuilder();
                lastFilterAction = ')';
                describeFilters(
                        String.copyValueOf(fString.toCharArray(), i + 1, fString.length() - (i + 1)),
                        priority - 1
                );

                i += fString.length() - (i + 1);

            } else if (symbol == '&') {
                addToFilter(currentFilter, priority);
                currentFilter = new StringBuilder();
                lastFilterAction = '&';

            } else if (symbol == '|' && fString.charAt(i + 1) == '|') {
                addToFilter(currentFilter, priority);
                currentFilter = new StringBuilder();
                lastFilterAction = '|';

                i++; // т.к. знак "или" обозначается 2мя знаками

            } else {
                currentFilter.append(symbol);

            }
            i++;

        }

        addToFilter(currentFilter, priority);
    }

    /**
     * добавление условия в фильтр
     * @param s
     * @param priority
     * @throws Exception
     */
    private static void addToFilter(StringBuilder s, int priority) throws FiltrationException {
        if (lastFilterAction != '&') {
            addNewFilter(priority);
        }

        if (!s.toString().equals("")) {
            if (filters.size() == 0)
                addNewFilter(priority);

            filters.get(filters.size() - 1).addFilter(parseFilter(s.toString()));
        }
    }

    /**
     * создание нового фильтра и добавление его в список
     * @param priority приоритет фильтра
     */
    private static void addNewFilter(int priority) {
        Filter filter = new Filter(priority);
        filters.add(filter);
    }

    /**
     * создание нового фильтра и добавление в него данных
     * @param s
     * @param priority
     * @throws Exception
     */
    private static void addNewFilterAndFill(StringBuilder s, int priority) throws FiltrationException {
        addNewFilter(priority);
        addToFilter(s, priority);
    }

    /**
     * перевод фильтра из String в Filter
     * @param str
     * @return
     * @throws Exception
     */
    private static Filter parseFilter(String str) throws FiltrationException {
        str = str.replaceAll(" ", "");
        if (str.length() < 11 && str.length() != 0) {
            throw new FiltrationException("Некорректный фильтр");
        }

        int column;
        try {
            String columnString = str.split("[\\[\\]]")[1];
            column = Integer.parseInt("" + columnString) - 1;
        } catch (NumberFormatException e) {
            throw new FiltrationException("Некорректный фильтр");
        }
        FilterAction action;
        int actionIndex;
        if (str.contains("<>")) {
            action = FilterAction.NOT_EQUALS;
            actionIndex = str.indexOf(">");

        } else if (str.contains("<")) {
            action = FilterAction.LESS;
            actionIndex = str.indexOf("<");

        } else if (str.contains(">")) {
            action = FilterAction.OVER;
            actionIndex = str.indexOf(">");

        } else if (str.contains("=")) {
            action = FilterAction.EQUALS;
            actionIndex = str.indexOf("=");

        } else {
            throw new FiltrationException("Некорректный фильтр: не найдено действие");
        }

        String value = String.copyValueOf(str.toCharArray(),
                actionIndex + 1, str.length() - actionIndex - 1);
        value = value.replaceAll("['’]", "\"");
        return new Filter(column, action, value);
    }

    /**
     * Сортировка фильтров (базовая сортировка пузырьком по значению приоритета фильтра)
     */
    private static void sortFilters () {
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < filters.size() - 1; i++) {
                if (filters.get(i).compareTo(filters.get(i + 1)) < 0) {
                    isSorted = false;

                    Filter buffer = filters.get(i);
                    filters.set(i, filters.get(i + 1));
                    filters.set(i + 1, buffer);
                }
            }
        }
    }
}

class FiltrationException extends Exception {
    public FiltrationException(String message) {
        super(message);
    }
}