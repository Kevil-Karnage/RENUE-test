package autocomplete.filterTree;

import autocomplete.filterTree.exceptions.FilterActionException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FilterAction {
    EQUALS("="),
    NOT_EQUALS("<>"),
    OVER(">"),
    LESS("<");

    final String action;

    public static FilterAction getCorrectAction(String symbol) throws FilterActionException {
        for (FilterAction fa : FilterAction.values()) {
            if (fa.action.equals(symbol)) {
                return fa;
            }
        }

        throw new FilterActionException("Некорректное действие");
    }
}

