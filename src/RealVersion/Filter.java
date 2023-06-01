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
}
