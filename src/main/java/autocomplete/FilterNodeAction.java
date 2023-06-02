package autocomplete;

public enum FilterNodeAction {
    AND("&"),
    OR("||");

    final String symbol;

    FilterNodeAction(String symbol) {
        this.symbol = symbol;
    }

    public static FilterAction getCorrectAction(String symbol) throws FilterNodeActionException {
        for (FilterAction fa : FilterAction.values()) {
            if (fa.action.equals(symbol)) {
                return fa;
            }
        }

        throw new FilterNodeActionException("Некорректное действие");
    }


}

class FilterNodeActionException extends FiltrationException {
    public FilterNodeActionException(String message) {
        super(message);
    }


}
