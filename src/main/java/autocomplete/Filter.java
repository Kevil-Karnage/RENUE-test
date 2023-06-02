package autocomplete;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для хранения фильтров к строкам
 */
public class Filter implements Comparable<Filter>{
    private final List<Integer> columns;
    private final List<FilterAction> actions;
    private final List<String> values;

    int priority;

    public Filter(int priority) {
        columns = new ArrayList<>();
        actions = new ArrayList<>();
        values = new ArrayList<>();

        this.priority = priority;
    }

    public Filter(int column, char action, String value) throws FilterActionException {
        (columns = new ArrayList<>()).add(column);
        (actions = new ArrayList<>()).add(FilterAction.getCorrectAction(action));
        (values = new ArrayList<>()).add(value);
    }

    /**
     * Получение количества фильтров для их конъюнкции
     * @return
     */
    public int size() {
        return columns.size();
    }


    /**
     * Добавление фильтра
     * @param column столбец
     * @param action действие (>, <, =, !)
     * @param value значение
     */
    public void addFilter(int column, char action, String value) throws FilterActionException {
        columns.add(column);
        actions.add(FilterAction.getCorrectAction(action));
        values.add(value);
    }

    /**
     * Добавление фильтра
     * @param f Filter
     */
    public void addFilter(Filter f) {
        columns.addAll(f.columns);
        actions.addAll(f.actions);
        values.addAll(f.values);
    }

    /**
     * Проверка массива строк на соответствие фильтру
     * @param arr
     * @return
     */
    public boolean isCorrect(String[] arr) {
        boolean isCorrect= true;
        for (int i = 0; i < columns.size(); i++) {
            isCorrect = isCorrect && isCorrectColumn(
                    arr[columns.get(i)],
                    actions.get(i),
                    values.get(i));
        }

        return isCorrect;
    }

    /**
     * Проверка столбца на соответствие фильтру
     * @param arrValue
     * @param action
     * @param value
     * @return
     */
    private boolean isCorrectColumn(String arrValue, FilterAction action, String value) {
        return action == FilterAction.EQUALS && arrValue.equals(value) ||
                (action == FilterAction.NOT_EQUALS && !arrValue.equals(value)) ||
                (action == FilterAction.OVER && Integer.parseInt(arrValue) > Integer.parseInt(value)) ||
                (action == FilterAction.LESS && Integer.parseInt(arrValue) < Integer.parseInt(value));
    }

    @Override
    public int compareTo(Filter o) {
        return this.priority - o.priority;
    }
}
