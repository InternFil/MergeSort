package MergeSort;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для обработки аргументов командной строки
 */
public class CommandLineArgumentsHandler {

    private TypeOrder requiredOrder = TypeOrder.ASCENDING;
    private TypeInputFiles typeInputFiles;
    private Path outputFile;
    private List<Path> inputFiles = new ArrayList<>();

    /**
     * Метод для обработки аргументов командной строки
     *
     * @param args аргументы командной строки
     */
    public void processArguments(String[] args) {
        int idx = 0;

        if (args.length < 3) {
            System.out.println("Недостаточное число аргументов командной строки, попробуйте снова");
            System.exit(0);
        }
        if (args[idx].equals("-a")) {
            idx++;
        } else if (args[idx].equals("-d")) {
            requiredOrder = TypeOrder.DESCENDING;
            idx++;
        }

        if (args[idx].equals("-i")) {
            typeInputFiles = TypeInputFiles.INTEGER;
            idx++;
        } else if (args[idx].equals("-s")) {
            typeInputFiles = TypeInputFiles.STRING;
            idx++;
        } else {
            System.out.println("Неизвестный тип аргумента, выберите -i для целых чисел или -s для строковых данных");
            System.exit(0);
        }

        if (args[idx].matches(".+\\.txt")) {
            outputFile = Paths.get(args[idx]);
            idx++;
        } else {
            System.out.println("Неправильно указаны типы файлов. Необходимо расширение .txt");
            System.exit(0);
        }
        while (idx < args.length) {
            if (args[idx].matches(".+\\.txt")) {
                inputFiles.add(Paths.get(args[idx]));
                idx++;
            } else {
                System.out.println("Неправильно указаны типы файлов. Необходимо расширение .txt");
                System.exit(0);
            }
        }
    }

    /**Возвращает требуемый порядок сортировки
     * @return TypeOrder - тип сортировки
     */
    public TypeOrder getRequiredOrder() {
        return requiredOrder;
    }

    /**
     * Возвращает тип входных файлов
     * @return TypeInputFiles - тип входных файлов
     */
    public TypeInputFiles getTypeInputFiles() {
        return typeInputFiles;
    }

    /**
     * Возвращает название результирующего файла
     * @return Path - файл для записи
     */
    public Path getOutputFile() {
        return outputFile;
    }

    /**
     * Возвращает список входных файлов для сортировки
     * @return List<Path> - список входных файлов
     */
    public List<Path> getInputFiles() {
        return inputFiles;
    }
}
