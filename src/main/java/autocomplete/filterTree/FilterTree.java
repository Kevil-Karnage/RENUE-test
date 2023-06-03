package autocomplete.filterTree;

import autocomplete.filterTree.exceptions.FiltrationException;
import lombok.Getter;
import lombok.Setter;


/**
 * Дерево фильтров
 */
public class FilterTree {
    FilterNode head;

    /**
     * Узел дерева фильтров.
     * Содержит либо фильтр, либо действие между ними
     */
    @Getter
    @Setter
    class FilterNode {
        FilterNode left;
        FilterNode right;
        FilterNodeAction action;

        Filter filter;

        /**
         * проверка массива строк на корректность дереву, начинающемуся из этого узла
         * @param stringArr проверяемый массив строк
         * @return true - соответствует, false - нет
         */
        public boolean isCorrect(String[] stringArr) {
            // если отсутствуют оба потомка, то проверяем фильтр
            if (left == null || right == null) {
                return this.filter.isCorrect(stringArr);
            }

            // иначе возвращает соответствие массива выражению из его потомков
            return (this.action == FilterNodeAction.AND && (left.isCorrect(stringArr) && right.isCorrect(stringArr))) ||
                   (this.action == FilterNodeAction.OR  && (left.isCorrect(stringArr) || right.isCorrect(stringArr)));
        }
    }

    /**
     * построение дерева фильтров из строки
     * @param baseString базовая строка для построения
     * @return FilterTree Дерево фильтров
     * @throws FiltrationException если фильтр некорректен и из него нельзя построить дерево
     */
    public FilterTree buildFromString(String baseString) throws FiltrationException {
        head = searchOR(baseString);
        return this;
    }

    /**
     * Поиск выражений ИЛИ (OR, ||) в базовой строке
     * @param base базова строка
     * @return узел дерева
     * @throws FiltrationException если фильтр некорректен и из него нельзя построить дерево
     */
    private FilterNode searchOR(String base) throws FiltrationException {
        FilterNode node = new FilterNode();
        boolean isFound = false;
        int i = 0;
        // ищем ||
        while (i < base.length() - 1) {
            char symbol = base.charAt(i);
            // если натыкаемся на скобку - пропускаем всё её содержимое
            if (symbol == '(') {
                i += searchEndBracket(base.substring(i + 1));

            // если нашли знак '||' , значит нашли действие ИЛИ
            } else if (symbol == '|' && base.charAt(i + 1) == '|') {
                // разделяем строку на левый узел (всё что слева от '||') и правый (всё что справа от '||')
                String leftString = base.substring(0, i);
                String rightString = base.substring(i + 2);

                // сохраняем действие узла
                node.action = FilterNodeAction.OR;
                // и получаем потомков текущего узла из левой и правой строки соответственно
                node.left = searchOR(leftString);
                node.right = searchOR(rightString);

                // отмечаем что нашли действие и останавливаемся
                isFound = true;
                break;
            } else {
                i++;
            }
        }

        // если не нашли действие ИЛИ, то ищем действие И (AND, &)
        if (!isFound) {
            node = searchAND(base);
        }

        // возвращаем полученный узел
        return node;
    }

    /**
     * Поиск выражений И (AND, &) в базовой строке
     * @param base базовая строка
     * @return узел дерева
     * @throws FiltrationException если фильтр некорректен и из него нельзя построить дерево
     */
    private FilterNode searchAND(String base) throws FiltrationException {
        FilterNode node = new FilterNode();
        boolean isFound = false;
        int i = 0;
        // ищем &
        while (i < base.length() - 1) {
            char symbol = base.charAt(i);
            // если натыкаемся на скобку - пропускаем всё её содержимое
            if (symbol == '(') {
                i = searchEndBracket(base.substring(i + 1));

            // если нашли знак '&' , значит нашли действие И
            } else if (symbol == '&') {
                // разделяем строку на левый узел (всё что слева от '||') и правый (всё что справа от '||')
                String leftString = base.substring(0, i);
                String rightString = base.substring(i + 1);

                // сохраняем действие узла
                node.action = FilterNodeAction.AND;
                // и получаем потомков текущего узла из левой и правой строки соответственно
                node.left = searchAND(leftString);
                node.right = searchAND(rightString);

                // отмечаем что нашли действие и останавливаемся
                isFound = true;
                break;
            } else {
                i++;
            }
        }

        // если действие не нашли, то ищем скобки и разбираем их содержимое (если нашли)
        if (!isFound) {
            node = searchBrackets(base);
        }

        // возвращаем полученный узел
        return node;
    }

    /**
     * Поиск выражений в скобках в базовой строке
     * @param base базовая строка
     * @return узел дерева
     * @throws FiltrationException если фильтр некорректен и из него нельзя построить дерево
     */
    private FilterNode searchBrackets(String base) throws FiltrationException {
        FilterNode node = new FilterNode();
        // если базовая строка пустая, то сохраняем узел как пустой фильтр
        if (base.length() == 0) {
            node.filter = Filtration.parseFilter(base);

        // если в строке первым и последним символами являются соответствующие скобки -
        // значит до этого мы его игнорировали, разбираем внутрискобочное выражение с нуля (поиска ИЛИ)
        } else if (base.charAt(0) == '(' && base.charAt(base.length() - 1) == ')') {
            node = searchOR(base.substring(1, base.length() - 1));

        // иначе разбираем базовое выражение как фильтр
        } else {
            node.filter = Filtration.parseFilter(base);
        }

        // возвращаем созданный узел
        return node;
    }

    /**
     * Поиск окончания текущего внутрискобочного выражения
     * @param baseString базовая строка
     * @return индекс закрывающей скобки, соответствующей текущей открытой
     * @throws FiltrationException если нарушена скобочная структура
     */
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

    /**
     * Проверка строкового массива на соответствие дереву фильтров
     * @param stringArr строковый массив
     * @return true - соответствует, false - не соответствует
     */
    public boolean isCorrect(String[] stringArr) {
        return head.isCorrect(stringArr);
    }
}
