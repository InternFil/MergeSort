package MergeSort;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для предварительной обработки входных файлов. Проверяет в каком порядке они упорядочены, неуждаются ли данные в
 * файлах в сортировке, разбивает входные файлы на временные меньших размеров.
 */
public class FilesHandler {

    private int countLines;
    private static int countTmpFiles;
    private final int SIZE_BUFFER_FILE = 10_000;
    private boolean isNeedSort;
    private final Path tmpDir = Paths.get("tmp");
    private List<Path> inputFiles;
    private TypeInputFiles fileType;
    private TypeOrder requiredOrder, orderFile;
    private List<Path> tmpFiles = new ArrayList<>();


    /**
     * Конструктор обработчика файлов
     *
     * @param inputFiles    список входных файлов
     * @param requiredOrder требуемый порядок сортировки
     * @param fileType      тип входных файлов
     */
    FilesHandler(List<Path> inputFiles, TypeOrder requiredOrder, TypeInputFiles fileType) {
        this.inputFiles = inputFiles;
        this.requiredOrder = requiredOrder;
        this.fileType = fileType;
    }

    /**
     * Метод, начинающий обработку входных файлов. Если в файле нарушен порядок сортировки или он требует
     * дополнительной сортировки, выполняются необходимые преобразования. В итоге формируются временные файлы
     * для сортировки слиянием
     */
    public void handle() {
        for (Path file : inputFiles) {
            if (!Files.exists(file)) {
                System.out.println("Файл " + file.toString() + " не найден");
                return;
            }
            countLines = 0;
            preprocessFile(fileType, file);
            switch (fileType) {
                case STRING:
                    splitFileString(file);
                    break;
                case INTEGER:
                    splitFileIntegers(file);
                    break;
            }
        }
    }

    /**
     * Удаляет временный каталог и файлы в нём
     *
     * @throws IOException если возникло непредвиденное исключение
     */
    public void deleteTmpDir() throws IOException {
        for (Path tmpFile : tmpFiles) {
            Files.delete(tmpFile);
        }
        Files.delete(tmpDir);
    }

    /**
     * Возвращает список временных файлов
     *
     * @return List<Path> временные файлы
     */
    public List<Path> getTmpFiles() {
        return tmpFiles;
    }

    //Деление входного файла строк на временные меньших размеров
    private void splitFileString(Path file) {
        int numberOfBufferFiles = (int) Math.ceil((double) countLines / SIZE_BUFFER_FILE);
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            //Создание списка временных файлов
            for (int i = 0; i < numberOfBufferFiles; i++) {
                ArrayList<String> listString = new ArrayList<>(SIZE_BUFFER_FILE);
                //считывание из входного файла строк
                for (int j = 0; j < Math.min(SIZE_BUFFER_FILE, countLines); j++) {
                    String line = reader.readLine();
                    if (line != null) {
                        listString.add(line);
                    } else break;
                }

                listString.trimToSize();
                //получение массива из списка
                String[] bufferString = new String[listString.size()];
                bufferString = listString.toArray(bufferString);
                //если необходимо, производится сортировка
                if (isNeedSort) {
                    bufferString = new QuickSort().sort(bufferString, orderFile);
                }
                //если порядок в файле совпадает с необходимым порядком, записывается временный файл
                if (requiredOrder == TypeOrder.ASCENDING && orderFile == TypeOrder.ASCENDING ||
                        requiredOrder == TypeOrder.DESCENDING && orderFile == TypeOrder.DESCENDING) {
                    writeTmpFile(bufferString);
                }
                //если порядок в файле не совпадает с необходимым порядком, производится преобразование
                //и запись во временный файл
                else if (requiredOrder == TypeOrder.ASCENDING && orderFile == TypeOrder.DESCENDING ||
                        requiredOrder == TypeOrder.DESCENDING && orderFile == TypeOrder.ASCENDING) {
                    for (int k = 0, m = bufferString.length - 1; k < bufferString.length / 2; k++, m--) {
                        String tmp = bufferString[k];
                        bufferString[k] = bufferString[m];
                        bufferString[m] = tmp;
                    }
                    writeTmpFile(bufferString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void splitFileIntegers(Path file) {
        int numberOfBufferFiles = (int) Math.ceil((double) countLines / SIZE_BUFFER_FILE);
        int val;
        try (BufferedReader reader = Files.newBufferedReader(file)) {

            for (int i = 0; i < numberOfBufferFiles; i++) {
                ArrayList<Integer> listInteger = new ArrayList<>(SIZE_BUFFER_FILE);

                for (int j = 0; j < Math.min(SIZE_BUFFER_FILE, countLines); j++) {
                    String line = reader.readLine();
                    if (line != null) {
                        try {
                            val = tryParseInt(line, file, true);
                            listInteger.add(val);
                        } catch (ParseIntException ex) {
                            System.out.println("Преобразование не удалось");
                        }
                    } else break;
                }

                listInteger.trimToSize();

                int[] bufferInt = listInteger.stream().mapToInt(Integer::intValue).toArray();

                if (isNeedSort) {
                    bufferInt = new QuickSort().sort(bufferInt, orderFile);
                }

                if (requiredOrder == TypeOrder.ASCENDING && orderFile == TypeOrder.ASCENDING ||
                        requiredOrder == TypeOrder.DESCENDING && orderFile == TypeOrder.DESCENDING) {
                    writeTmpFile(bufferInt);
                } else if (requiredOrder == TypeOrder.ASCENDING && orderFile == TypeOrder.DESCENDING ||
                        requiredOrder == TypeOrder.DESCENDING && orderFile == TypeOrder.ASCENDING) {
                    for (int k = 0, m = bufferInt.length - 1; k < bufferInt.length / 2; k++, m--) {
                        int tmp = bufferInt[k];
                        bufferInt[k] = bufferInt[m];
                        bufferInt[m] = tmp;
                    }
                    writeTmpFile(bufferInt);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTmpFile(String[] arr) throws IOException {
        Path tmpFile = Paths.get(".\\tmp\\tmp_" + countTmpFiles + ".txt");
        if (!Files.exists(tmpDir)) {
            Files.createDirectory(tmpDir);
        }
        Path p = Files.createFile(tmpFile);
        tmpFiles.add(p);
        countTmpFiles++;
        try (BufferedWriter writer = Files.newBufferedWriter(tmpFile)) {
            for (String cell : arr) {
                writer.write(cell);
                writer.newLine();
            }
        }
    }

    private void writeTmpFile(int[] arr) throws IOException {
        Path tmpFile = Paths.get(".\\tmp\\tmp_" + countTmpFiles + ".txt");
        if (!Files.exists(tmpDir)) {
            Files.createDirectory(tmpDir);
        }
        Path p = Files.createFile(tmpFile);
        tmpFiles.add(p);
        countTmpFiles++;
        try (BufferedWriter writer = Files.newBufferedWriter(tmpFile)) {
            for (int cell : arr) {
                writer.write(Integer.toString(cell));
                writer.newLine();
            }
        }
    }

    //Метод для предварительной обработки входного файла. Подсчитывает количество строк, проверяет порядок сортировки и
    //определяет нужна ли промежуточная сортировка
    private void preprocessFile(TypeInputFiles typeInputFiles, Path file) {
        try (BufferedReader firstReader = Files.newBufferedReader(file);
             BufferedReader secondReader = Files.newBufferedReader(file)) {

            String firstLine = "", secondLine = "", firstFileLine;
            boolean isPos = false, isNeg = false;
            int compareResult, firstVal, secondVal = Integer.MAX_VALUE, firstFileVal = Integer.MIN_VALUE;

            switch (typeInputFiles) {
                case STRING:
                    firstLine = firstReader.readLine();
                    firstFileLine = firstLine;
                    countLines++;
                    while ((firstLine = firstReader.readLine()) != null) {
                        secondLine = secondReader.readLine();
                        compareResult = firstLine.compareTo(secondLine);
                        if (compareResult < 0) isNeg = true;
                        else if (compareResult > 0) isPos = true;
                        countLines++;
                    }
                    int val = firstFileLine.compareTo(secondLine);
                    if (val < 0) orderFile = TypeOrder.ASCENDING;
                    else if (val > 0) orderFile = TypeOrder.DESCENDING;

                    if (isNeg && isPos) isNeedSort = true;
                    break;

                case INTEGER:
                    firstLine = firstReader.readLine();
                    firstFileLine = firstLine;
                    try {
                        firstFileVal = tryParseInt(firstFileLine, file, false);
                    } catch (ParseIntException e) {
                    }
                    countLines++;

                    while ((firstLine = firstReader.readLine()) != null) {
                        secondLine = secondReader.readLine();
                        try {
                            firstVal = tryParseInt(firstLine, file, false);
                            secondVal = tryParseInt(secondLine, file, false);
                            if (secondVal < firstVal) isNeg = true;
                            else if (secondVal > firstVal) isPos = true;
                            countLines++;
                        } catch (ParseIntException e) {
                        }
                    }

                    if (firstFileVal < secondVal) orderFile = TypeOrder.ASCENDING;
                    else if (firstFileVal > secondVal) orderFile = TypeOrder.DESCENDING;

                    if (isNeg && isPos) isNeedSort = true;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //преобразование строки в целочисленное значение
    private int tryParseInt(String line, Path file, boolean isNeedPrint) throws ParseIntException {
        int val;
        try {
            val = Integer.parseInt(line);
        } catch (NumberFormatException e1) {
            try {
                if (isNeedPrint) {
                    System.out.println("Некорректное значение в файле " + file + ": " + line);
                }
                val = Integer.parseInt(correctLine(line));
                if (isNeedPrint) {
                    System.out.println("Преобразованное значение: " + val);
                }
            } catch (NumberFormatException e2) {
                throw new ParseIntException();
            }
        }
        return val;
    }

    //попытка преобразования некорректного значения
    private String correctLine(String line) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(line);
        if (line.contains(".") || line.contains(",")) {
            matcher.find();
            return matcher.group();
        } else {
            String[] s = line.split("\\D");
            StringBuilder sb = new StringBuilder();
            for (String elem : s) {
                sb.append(elem);
            }
            return sb.toString();
        }
    }
}
