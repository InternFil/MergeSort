package MergeSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для сортировки файлов методом слияния. Сортирует типы String и int в порядке возрастания и убывания
 */
public class FileMerger {

    private TypeInputFiles filesType;
    private TypeOrder requiredOrder;
    private Path outputFile;
    private List<Path> inputFiles;
    private int numberOfFiles;

    /**
     * Конструктор класса
     * @param inputFiles List<Path> список входных файлов
     * @param outputFile Path - файл для записи отсортированных данных
     * @param requiredOrder TypeOrder - требуемый тип сортировки
     * @param filesType TypeInputFiles - тип входных файлов
     */
    FileMerger(List<Path> inputFiles, Path outputFile, TypeOrder requiredOrder, TypeInputFiles filesType) {
        this.filesType = filesType;
        this.requiredOrder = requiredOrder;
        this.outputFile = outputFile;
        this.inputFiles = inputFiles;
        this.numberOfFiles = inputFiles.size();
    }

    /**
     * Метод для старта процесса слияния
     * @throws IOException если возникло непредвиденное исключение
     */
    public void perform() throws IOException {
        switch (filesType) {
            case STRING:
                mergeString();
                break;
            case INTEGER:
                mergeInt();
                break;
        }
    }
//сортировка строк
    private void mergeString() throws IOException {
        String minVal;
        int minFile;
        //список для хранения значений по одному из каждого файлов
        List<String> firstValues = new ArrayList<>(numberOfFiles);
        //список для хранения BufferedReader для всех файлов
        List<BufferedReader> readers = new ArrayList<>(numberOfFiles);
        //BufferedWriter для записи
        BufferedWriter writer = Files.newBufferedWriter(outputFile);

        //связываем файлы с потоками ввода и считываем первые значения из каждого файла
        for (int i = 0; i < numberOfFiles; i++) {
            readers.add(Files.newBufferedReader(inputFiles.get(i)));
            String s = readers.get(i).readLine();
            if (s != null) firstValues.add(s);
            else firstValues.add(String.valueOf(Integer.MAX_VALUE));             //подумать над этим
        }

        //пока есть значения в списке, выполняем цикл
        while (firstValues.size() != 0) {
            minVal = firstValues.get(0);
            minFile = 0;
            String s;
            switch (requiredOrder) {
                case ASCENDING:
                    //ищем минимальное значение в списке значений
                    for (int i = 0; i < firstValues.size(); i++) {
                        if (minVal.compareTo(firstValues.get(i)) > 0) {
                            minVal = firstValues.get(i);
                            minFile = i;
                        }
                    }
                    //запись в файл
                    writer.write(firstValues.get(minFile));
                    writer.newLine();
                    //читаем в файле с минимальным значением новую строку
                    s = readers.get(minFile).readLine();
                    if (s != null) firstValues.set(minFile, s);
                    //если строка null, удаляем элемент из списка значений и из списка BufferedReader
                    else {
                        firstValues.remove(minFile);
                        readers.get(minFile).close();
                        readers.remove(minFile);
                    }
                    break;
                case DESCENDING:
                    //ищем максимальное значение в списке значений
                    for (int i = 0; i < firstValues.size(); i++) {
                        if (minVal.compareTo(firstValues.get(i)) < 0) {
                            minVal = firstValues.get(i);
                            minFile = i;
                        }
                    }
                    writer.write(firstValues.get(minFile));
                    writer.newLine();
                    s = readers.get(minFile).readLine();
                    if (s != null) firstValues.set(minFile, s);
                    else {
                        firstValues.remove(minFile);
                        readers.get(minFile).close();
                        readers.remove(minFile);
                    }
                    break;
            }
        }
        writer.close();
    }
//сортировка int
    private void mergeInt() throws IOException {
        int minVal;
        int minFile;
        List<Integer> firstValues = new ArrayList<>(numberOfFiles);
        List<BufferedReader> readers = new ArrayList<>(numberOfFiles);
        BufferedWriter writer = Files.newBufferedWriter(outputFile);

        for (int i = 0; i < numberOfFiles; i++) {
            readers.add(Files.newBufferedReader(inputFiles.get(i)));
            String s = readers.get(i).readLine();
            if (s != null) {
                int n = Integer.parseInt(s);
                firstValues.add(n);
            } else firstValues.add(Integer.MAX_VALUE);
        }

        while (firstValues.size() != 0) {
            minVal = firstValues.get(0);
            minFile = 0;
            String s;
            int num;
            switch (requiredOrder) {
                case ASCENDING:
                    for (int i = 0; i < firstValues.size(); i++) {
                        if (minVal > firstValues.get(i)) {
                            minVal = firstValues.get(i);
                            minFile = i;
                        }
                    }
                    num = firstValues.get(minFile);
                    writer.write(Integer.toString(num));
                    writer.newLine();
                    s = readers.get(minFile).readLine();
                    if (s != null) {
                        int n = Integer.parseInt(s);
                        firstValues.set(minFile, n);
                    } else {
                        firstValues.remove(minFile);
                        readers.get(minFile).close();
                        readers.remove(minFile);
                    }
                    break;
                case DESCENDING:
                    for (int i = 0; i < firstValues.size(); i++) {
                        if (minVal < firstValues.get(i)) {
                            minVal = firstValues.get(i);
                            minFile = i;
                        }
                    }
                    num = firstValues.get(minFile);
                    writer.write(Integer.toString(num));
                    writer.newLine();
                    s = readers.get(minFile).readLine();
                    if (s != null) {
                        int n = Integer.parseInt(s);
                        firstValues.set(minFile, n);
                    } else {
                        firstValues.remove(minFile);
                        readers.get(minFile).close();
                        readers.remove(minFile);
                    }
                    break;
            }
        }
        writer.close();
    }
}
