package RealVersion;

import java.util.ArrayList;
import java.util.List;

public class ConjunctionFilter implements Comparable<ConjunctionFilter>{
    List<Integer> columns;
    List<Character> actions;
    List<String> values;

    int priority;

    public ConjunctionFilter(int priority) {
        columns = new ArrayList<>();
        actions = new ArrayList<>();
        values = new ArrayList<>();

        this.priority = priority;
    }

    public int size() {
        return columns.size();
    }


    public void addFilter(int column, char action, String value) {
        columns.add(column);
        actions.add(action);
        values.add(value);
    }

    public void addFilter(Filter f) {
        addFilter(f.column, f.action, f.value);
    }

    public boolean isCorrect(String[] arr) {
        boolean isCorrect= true;
        for (int i = 0; i < columns.size(); i++) {
            isCorrect = isCorrect && isCorrectColumn(arr[columns.get(i)], actions.get(i), values.get(i));
        }

        return isCorrect;
    }

    private boolean isCorrectColumn(String arrValue, char action, String value) {
        return action == '=' && arrValue.equals(value) ||
                (action == '!' && !arrValue.equals(value)) ||
                (action == '>' && Integer.parseInt(arrValue) > Integer.parseInt(value)) ||
                (action == '<' && Integer.parseInt(arrValue) < Integer.parseInt(value));
    }

    @Override
    public int compareTo(ConjunctionFilter o) {
        return this.priority - o.priority;
    }
}
