# MergeSort

## Сортировка слиянием нескольких файлов ##
Программа для сортировки слиянием больших файлов, не помещающихся целиком в оперативную память.


***Параметры программы задаются при запуске через аргументы командной строки, по порядку:***

1. режим сортировки (-a или -d), необязательный, по умолчанию сортировка по возрастанию;
2. тип данных (-s или -i), обязательный;
3. имя выходного файла, обязательное;
4. остальные параметры – имена входных файлов, не менее одного.

***Примеры запуска из командной строки для Windows:***

java -jar MergeSort.jar -i -a out.txt in.txt (для целых чисел по возрастанию)

java -jar MergeSort.jar -s out.txt in1.txt in2.txt in3.txt (для строк по возрастанию)

java -jar MergeSort.jar -d -s out.txt in1.txt in2.txt (для строк по убыванию)

---
***Алгоритм работы:***
1. Считывание данных аргументов командной строки
2. Проверка входных файлов. Происходит считывание строк из каждого файла в буфер, количество определяет константа
   SIZE_BUFFER_FILE в классе FilesHandler, по умолчанию читает 10_000 строк. Проверяется порядок сортировки 
   (по возрастанию или убыванию), все ли данные идут в правильном порядке. Если нарушен порядок сортировки, производится
   быстрая сортировка. Если необходимый порядок сортировки не совпадает с порядком в файле, идёт необходимое преобразование.
   Если все данные правильно упорядочены, дополнительных преобразований не производится.
3. Если количество строк в файле больше SIZE_BUFFER_FILE, файл разбивается на временные меньших размеров.
   В них записываются данные из буфера.
4. Повторяются шаги 2, 3 до тех пор, пока не обработаются все входные файлы.
5. Производится сортировка слиянием временных файлов
6. Удаляется директория временных файлов.
---
***Особенности программы:***
1. Алгоритм работает с большими файлами, не помещающимися в оперативную память.
2. Программа может распознать порядок сортировки во входных файлах и определить, идут ли данные в правильном порядке.
   В случае необходимости, меняет порядок сортировки и/или производит сортировку.
3. Для целочисленных значений предусмотрена возможность обработки некорректных значений:

   Некорректное значение в файле in1.txt: "345"
   
   Преобразованное значение: 345
   
   Некорректное значение в файле in2.txt: tres
   
   Преобразование не удалось
   
   Некорректное значение в файле in3.txt: 888.5554
   
   Преобразованное значение: 888
