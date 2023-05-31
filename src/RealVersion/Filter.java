package RealVersion;

public class Filter{

    int column;
    char action;
    String value;



    public Filter(int column, char action, String value) {
        this.column = column;
        this.action = action;
        this.value = value;
    }

    public boolean isCorrect(String[] arr) {
        return action == '=' && arr[column].equals(value) ||
                (action == '>' && Integer.parseInt(arr[column]) > Integer.parseInt(value)) ||
                (action == '<' && Integer.parseInt(arr[column]) < Integer.parseInt(value));
    }
}
