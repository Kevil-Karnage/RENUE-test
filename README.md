
# Библиотека Autocomplete вводимого текста
#### Тестовое задание стажировки Java-разработчиков для компании Renue

---

## Используемые решения
 версия java: Amazon Coretto version 11.0.6

---

## Алгоритм использования приложения

1) в корневой папке проекта (RENUE-test) находим файл "airports-search-rozhnov.jar"
2) Запускаем его с помощью команды в консоли:


    java -jar airports-search-rozhnov.jar  

3) На экране должна появиться надпись:


    Введите фильтр:

4) Вводим в консоль фильтр формата:

    
    column[1]>10 & column[5]=’GKA’ || column[<номер колонки с 1>]<операция сравнения>...

5) Жмём *Enter* и ждём надписи:


    Введите начало названия аэропорта:

6) Вводим начало названия аэропорта, жмём *Enter* и ждём вывода результатов поиска в формате:


    <название найденного аэропорта 1>[<Список данных найденного аэропорта 1>]    
    <название найденного аэропорта 2>[<Список данных найденного аэропорта 2>]    
    ...  
    <название найденного аэропорта n>[<Список данных найденного аэропорта n>]  
    Количество найденных строк: n; Время, затраченное на поиск: m мс  

7) При желании повторяем действие №6, вписав другое значение
8) Для завершения работы программы необходимо ввести команду *!quit*

---

## Нефункциональные требования к заданию

1. Перечитывать все строки файла при каждом поиске нельзя. В том числе читать только определенную колонку у каждой строки.
2. Создавать новые файлы или редактировать текущий нельзя.
   В том числе использовать СУБД.
3. Хранить весь файл в памяти нельзя.
   Не только в качестве массива байт, но и в структуре, которая так или иначе содержит все
   данные из файла.
4. Для корректной работы программе требуется не более 7 МБ памяти.
   Все запуски java –jar должны выполняться с jvm флагом -Xmx7m.
5. Скорость поиска должна быть максимально высокой с учетом требований выше.
   В качестве ориентира можно взять число из скриншота выше: на поиск по «Bo», который
   выдает 68 строк, требуется 25 мс, поиск по «Bower», который выдает 1 строку без
   фильтров 5 мс.
6. Сложность поиска меньше чем O(n), где n число строк файла.
7. Должны соблюдаться принципы ООП и SOLID.
8. Ошибочные и краевые ситуации должны быть корректно обработаны.
9. Использовать готовые библиотеки для парсинга CSV формата нельзя.
10. Решенное тестовое задание код в публичном репозитории на GitHub. По готовности
    ссылку на репозиторий отправить в чат в Telegram контакту, от которого было получено
    задание.

---

## Вопросы, непокрытые постановкой задачи


Данный пункт существует благодаря тому что в конце файла описания задания было следующее:


 > В случае, если возникает вопрос, который не покрывает данная постановка задачи,
    кандидат должен сам выбрать любое его решение, не противоречащее постановке.
    В readme должно быть отражен вопрос и принятое решение.

- #### Реализация пунктов 1-3 из нефункциональных требований:

  После получения фильтра происходит построчное чтение всего файла. 
  
  Каждая строка сразу же после прочтения проверяется на соответствие фильтру и, в случае корректности сохраняется в список

  Полученный список сортируется по колонке имени аэропорта, и только после этого запрашивается начало названия аэропорта.

  Соответственно, поиск ведётся по отсортированному раннее списку

- #### Реализация пункта 5:

  > В качестве ориентира можно взять число из скриншота выше:  
    на поиск по «Bo», который выдает 68 строк, требуется 25 мс,   
    поиск по «Bower», который выдает 1 строку без фильтров 5 мс.

  Поиск по "Bo" осуществляется за 2мс (учитывается только время самого поиска, не учитывая вывод результатов на экран)  
  Поиск по "Bower" также осуществляется за 1-2мс (учитывается только время самого поиска, не учитывая вывод результатов на экран)
    

- #### Реализация пункта 6: 


    Сложность поиска равна O(2*log(n))

  В отсортированном при фильтрации массиве ищется первый подходящий элемент и последний  
  Список отсортирован, поэтому все элементы между первым и последним подходящими также соответствуют искомым  
  Фактически, к моменту нахождения первой и последней подходящих строк поиск окончен  
  поиск осуществляется бинарным способом (его сложность O(log(n))), а т.к. ищется 2 элемента,  
  то сложность данного алгоритма будет O(2 log(n))


- #### Реализация пункта 10:

  [GitHub репозиторий](https://github.com/Kevil-Karnage/RENUE-test)

