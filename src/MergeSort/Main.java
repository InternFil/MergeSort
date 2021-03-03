package MergeSort;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        new Main().start(args);
    }

    private void start(String[] args) {
        CommandLineArgumentsHandler argHandler = new CommandLineArgumentsHandler();
        argHandler.processArguments(args);
        TypeOrder requiredOrder = argHandler.getRequiredOrder();
        TypeInputFiles fileType = argHandler.getTypeInputFiles();
        List<Path> inputFiles = argHandler.getInputFiles();
        Path outputFile = argHandler.getOutputFile();

        FilesHandler filesHandler = new FilesHandler(inputFiles, requiredOrder, fileType);
        filesHandler.handle();
        List<Path> tmpFiles = filesHandler.getTmpFiles();

        FileMerger fileMerger = new FileMerger(tmpFiles, outputFile, requiredOrder, fileType);
        try {
            fileMerger.perform();
            filesHandler.deleteTmpDir();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Слияние файлов выполнено успешно");
    }
}
