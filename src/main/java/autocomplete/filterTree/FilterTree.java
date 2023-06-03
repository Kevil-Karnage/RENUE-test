package autocomplete.filterTree;

import autocomplete.filterTree.exceptions.FiltrationException;
import lombok.Getter;
import lombok.Setter;

public class FilterTree {
    FilterNode head;
    @Getter
    @Setter
    class FilterNode {
        FilterNode left;
        FilterNode right;
        FilterNodeAction action;

        Filter filter;

        public boolean isCorrect(String[] stringArr) {
            if (left == null || right == null) {
                return this.filter.isCorrect(stringArr);
            }


            return (this.action == FilterNodeAction.AND && (left.isCorrect(stringArr) && right.isCorrect(stringArr))) ||
                   (this.action == FilterNodeAction.OR  && (left.isCorrect(stringArr) || right.isCorrect(stringArr)));
        }
    }

    public FilterTree() {
    }

    public FilterTree buildFromString(String baseString) throws FiltrationException {
        head = searchOR(baseString);
        return this;
    }

    private FilterNode searchOR(String base) throws FiltrationException {
        FilterNode node = new FilterNode();
        boolean isFound = false;
        int i = 0;
        // ищем ||
        while (i < base.length() - 1) {
            char symbol = base.charAt(i);
            // если натыкаемся на скобку - пропускаем её всю
            if (symbol == '(') {
                i += searchEndBracket(base.substring(i + 1));
            } else if (symbol == '|' && base.charAt(i + 1) == '|') {
                // разделяем на левый узел и правый
                String leftString = base.substring(0, i);
                String rightString = base.substring(i + 2);

                node.left = searchOR(leftString);
                node.right = searchOR(rightString);
                node.action = FilterNodeAction.OR;

                isFound = true;
                break;
            } else {
                i++;
            }
        }

        if (!isFound) {
            node = searchAND(base);
        }

        return node;
    }

    private FilterNode searchAND(String base) throws FiltrationException {
        FilterNode node = new FilterNode();
        boolean isFound = false;
        int i = 0;
        // ищем &
        while (i < base.length() - 1) {
            char symbol = base.charAt(i);
            // если натыкаемся на скобку - пропускаем её всю
            if (symbol == '(') {
                i = searchEndBracket(base.substring(i + 1));
            } else if (symbol == '&') {
                // разделяем на левый узел и правый
                String leftString = base.substring(0, i);
                String rightString = base.substring(i + 1);

                node.left = searchAND(leftString);
                node.right = searchAND(rightString);
                node.action = FilterNodeAction.AND;

                isFound = true;
                break;
            } else {
                i++;
            }
        }

        if (!isFound) {
            node = searchBrackets(base);
        }

        return node;
    }

    private FilterNode searchBrackets(String base) throws FiltrationException {
        FilterNode node = new FilterNode();

        if (base.length() == 0) {
            node.filter = Filtration.parseFilter(base);
        } else if (base.charAt(0) == '(' && base.charAt(base.length() - 1) == ')') {
            node = searchOR(base.substring(1, base.length() - 1));
        } else {
            node.filter = Filtration.parseFilter(base);
        }

        return node;
    }

    private int searchEndBracket(String baseString) throws FiltrationException {
        int i = 0;
        while (i != baseString.length()) {

            if (baseString.charAt(i) == '(')
                i += searchEndBracket(baseString.substring(i + 1));
            else if (baseString.charAt(i) == ')')
                return i + 1;

            i++;

        }

        throw new FiltrationException("Некорректный фильтр: нарушена скобочная структура");
    }

    public boolean isCorrect(String[] stringArr) {
        boolean isCorrect = head.isCorrect(stringArr);
        return isCorrect;
    }
}
